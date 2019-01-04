/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack;

import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.stack.coap.CoapMsg;
import org.jinjuamla.stack.coap.CoapMsgToken;
import org.jinjuamla.stack.coap.CoapOptionSet;
import org.jinjuamla.stack.consts.CoapMsgConsts;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author psammand
 */
public class ClientInteraction extends Interaction
{

  CoapMsg _clientRequest = null;
  CoapMsg _clientResponse = null;
  IClientCallback _clientCallback = null;
  Endpoint _endpoint = null;
  CoapMsgToken _token = null;

  public ClientInteraction(CoapMsg coapMsg)
  {
    _clientRequest = coapMsg;
  }

  @Override
  public void addExchange(Exchange exchange)
  {
    if (exchange != null)
    {
      InteractionStore.Inst().addClientExchange(exchange);
      exchange.setInteraction(this);
      _noOfExchanges++;
    }
  }

  @Override
  public void deleteExchange(Exchange exchange)
  {
    if (exchange == null)
    {
      return;
    }

    if (_currentInExchange == exchange)
    {
      _currentInExchange = null;
    } else if (_currentOutExchange == exchange)
    {
      _currentOutExchange = null;
    }

    if (InteractionStore.Inst().deleteClientExchange(exchange))
    {
      _noOfExchanges--;
    }
  }

  public boolean isMsgMatched(CoapMsg coapMsg)
  {
    return _token.equals(coapMsg.get_token()) && _endpoint.equals(coapMsg.getEndpoint());
  }

  public Endpoint getEndpoint()
  {
    return _endpoint;
  }

  public void setEndpoint(Endpoint _endpoint)
  {
    this._endpoint = _endpoint;
  }

  public CoapMsgToken getToken()
  {
    return _token;
  }

  public void setToken(CoapMsgToken _token)
  {
    this._token = _token;
  }

  public IClientCallback getClientCallback()
  {
    return _clientCallback;
  }

  public void setClientCallback(IClientCallback _clientCallback)
  {
    this._clientCallback = _clientCallback;
  }

  public boolean hasCallback()
  {
    return _clientCallback != null;
  }

  public void sendRequest()
  {
    if (_clientRequest == null || !hasCallback())
    {
      return;
    }

    MessageIdProvider.assignMsgId(_clientRequest);
    TokenProvider.assignNextToken(_clientRequest);

    setToken(_clientRequest.get_token());
    setEndpoint(_clientRequest.getEndpoint());

    CoapMsg singleCoapReq = null;

    if ((_clientRequest.get_code() == CoapMsgConsts.MSG_CODE_PUT || _clientRequest.get_code() == CoapMsgConsts.MSG_CODE_POST)
            && _clientRequest.get_payload() != null && _clientRequest.get_payload().length > 512)
    {
      BlockTransferStatus reqBlockTransferStatus = createReqBlockTransferStatus(_clientRequest, false);
      singleCoapReq = getNextRequestBlock(_clientRequest, reqBlockTransferStatus);
    } else
    {
      try
      {
        singleCoapReq = (CoapMsg) _clientRequest.clone();
      } catch (CloneNotSupportedException ex)
      {
        Logger.getLogger(ClientInteraction.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    if (singleCoapReq != null)
    {
      sendSingleRequestMsg(singleCoapReq);
    }
  }

  public void receiveResponse(CoapMsg coapMsg) throws CloneNotSupportedException
  {
    deleteCurrentOutExchange();

    if (coapMsg.is_con() || coapMsg.is_non())
    {
      deleteCurrentInExchange();
      createIncomingExchange(coapMsg);

      if (coapMsg.is_con())
      {
        acknowledge();
      }
    }

    if (!coapMsg.hasBlockOption())
    {
      _clientResponse = (CoapMsg) coapMsg.clone();
      notifyResponse(_clientResponse, 0);
    } else
    {
      if (coapMsg.get_option_set().has_block1())
      {
        handleBlock1Transfer(coapMsg);
      }

      if (coapMsg.get_option_set().has_block2())
      {
        handleBlock2Transfer(coapMsg);
      }
    }
  }

  void notifyResponse(CoapMsg coapMsg, int status)
  {
    if (hasCallback())
    {
      _clientCallback.handleResponse(coapMsg, status);
    }
  }

  void handleBlock1Transfer(CoapMsg coapMsg) throws CloneNotSupportedException
  {
    BlockTransferStatus reqBlockTransferStatus = getReqBlockTransferStatus();

    if (reqBlockTransferStatus == null)
    {
      return;
    }

    int msgCode = coapMsg.get_code();

    switch (msgCode)
    {
      case CoapMsgConsts.MSG_CODE_CONTINUE_231:
      {
        int blockSize = reqBlockTransferStatus.getBlockSize();
        int newBlockSize = blockSize;

        if (coapMsg.get_option_set().get_block1().get_size() < blockSize)
        {
          newBlockSize = coapMsg.get_option_set().get_block1().get_size();
        }

        int nextBlockNo = reqBlockTransferStatus.getCurrentBlockNo() + blockSize / newBlockSize;
        reqBlockTransferStatus.setCurrentBlockNo(nextBlockNo);
        reqBlockTransferStatus.setBlockSize(newBlockSize);

        CoapMsg nextRequestBlock = getNextRequestBlock(_clientRequest, reqBlockTransferStatus);
        sendSingleRequestMsg(nextRequestBlock);
      }
      break;
      case CoapMsgConsts.MSG_CODE_REQ_ENTITY_INCOMPLETE_408:
      {
        deleteReqBlockTransferStatus();
        reqBlockTransferStatus = createReqBlockTransferStatus(_clientRequest, false);

        int new_block_size = coapMsg.get_option_set().has_block1() ? coapMsg.get_option_set().get_block1().get_size() : 256;
        reqBlockTransferStatus.setBlockSize(new_block_size);

        CoapMsg nextRequestBlock = getNextRequestBlock(_clientRequest, reqBlockTransferStatus);
        sendSingleRequestMsg(nextRequestBlock);
      }
      break;
      case CoapMsgConsts.MSG_CODE_REQ_ENTITY_TOO_LARGE_413:
      {
        CoapOptionSet option_set = coapMsg.get_option_set();

        if (option_set.has_block1())
        {
          short newBlockSize = option_set.get_block1().get_size();

          if (newBlockSize != reqBlockTransferStatus.getBlockSize())
          {
            reqBlockTransferStatus.setBlockSize(newBlockSize);
            reqBlockTransferStatus.setCurrentBlockNo(0);

            CoapMsg nextRequestBlock = getNextRequestBlock(_clientRequest, reqBlockTransferStatus);
            sendSingleRequestMsg(nextRequestBlock);
          }
        }
      }
      break;
      default:
      {
        if (!coapMsg.get_option_set().has_block2())
        {
          _clientResponse = (CoapMsg) coapMsg.clone();
          notifyResponse(_clientResponse, 0);
        }
      }
      break;
    }
  }

  void handleBlock2Transfer(CoapMsg coapMsg) throws CloneNotSupportedException
  {
    CoapOptionSet option_set = coapMsg.get_option_set();
    BlockTransferStatus resBlockTransferStatus = getResBlockTransferStatus();

    if (resBlockTransferStatus == null)
    {
      resBlockTransferStatus = createResBlockTransferStatus(coapMsg, true);
      resBlockTransferStatus.setBlockSize(option_set.get_block2().get_size());
    }

    if (option_set.get_block2().get_number() == resBlockTransferStatus.getNextBlockNo())
    {
      resBlockTransferStatus.addPayload(coapMsg.get_payload());

      if (option_set.get_block2().is_has_more())
      {
        resBlockTransferStatus.moveToNextBlock();
        CoapMsg newReqBlock = new CoapMsg();
        newReqBlock.set_type(_clientRequest.get_type());
        newReqBlock.set_code(_clientRequest.get_code());
        newReqBlock.set_token(_clientRequest.get_token());
        newReqBlock.set_option_set((CoapOptionSet) _clientRequest.get_option_set().clone());
        MessageIdProvider.assignMsgId(newReqBlock);
        newReqBlock.setEndpoint(getEndpoint());
        newReqBlock.get_option_set().set_block2((short) resBlockTransferStatus.getSzx(), false, resBlockTransferStatus.getNextBlockNo());
        Exchange currentOutgoingExchange = createOutgoingExchange(newReqBlock);
        currentOutgoingExchange.deliver();

      } else
      {
        _clientResponse = new CoapMsg();
        _clientResponse.set_id(coapMsg.get_id());
        _clientResponse.set_type(coapMsg.get_type());
        _clientResponse.set_code(coapMsg.get_code());
        _clientResponse.set_token(coapMsg.get_token());
        _clientResponse.setEndpoint(coapMsg.getEndpoint());
        _clientResponse.set_option_set((CoapOptionSet) coapMsg.get_option_set().clone());
        _clientResponse.set_payload(resBlockTransferStatus.getPayloadBuffer());
        deleteResBlockTransferStatus();
        notifyResponse(_clientResponse, 0);
      }
    }
  }

  CoapMsg getNextRequestBlock(CoapMsg clientRequest, BlockTransferStatus reqBlockTransferStatus)
  {
    int blockSize = reqBlockTransferStatus.getBlockSize();
    int currentBlockNo = reqBlockTransferStatus.getCurrentBlockNo();
    int from = currentBlockNo * blockSize;
    int to = Math.min((currentBlockNo + 1) * blockSize, clientRequest.get_payload() != null ? clientRequest.get_payload().length : 0);
    int length = to - from;
    byte[] newPayload = Arrays.copyOfRange(clientRequest.get_payload(), from, length);

    CoapMsg newReqBlock = new CoapMsg();
    newReqBlock.set_type(clientRequest.get_type());
    newReqBlock.set_code(clientRequest.get_code());

    try
    {
      newReqBlock.set_option_set((CoapOptionSet) clientRequest.get_option_set().clone());
    } catch (CloneNotSupportedException ex)
    {
      Logger.getLogger(ClientInteraction.class.getName()).log(Level.SEVERE, null, ex);
    }

    newReqBlock.set_payload(newPayload);
    newReqBlock.get_option_set().set_block1((short) reqBlockTransferStatus.getSzx(), to < clientRequest.get_payload().length, currentBlockNo);
    newReqBlock.get_option_set().set_size1(clientRequest.get_payload().length);

    return newReqBlock;
  }

  void sendSingleRequestMsg(CoapMsg requestMsg)
  {
    if (requestMsg.get_type() == CoapMsgConsts.COAP_MSG_TYPE_NONE)
    {
      requestMsg.set_type(CoapMsgConsts.COAP_MSG_TYPE_CON);
    }

    MessageIdProvider.assignMsgId(requestMsg);

    requestMsg.set_token(getToken());
    requestMsg.setEndpoint(getEndpoint());

    Exchange clientExchange = createOutgoingExchange(requestMsg);
    clientExchange.deliver();
  }

}
