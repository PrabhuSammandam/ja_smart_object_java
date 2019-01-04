/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack;

import org.jinjuamla.stack.coap.CoapMsg;
import org.jinjuamla.stack.coap.CoapOptionSet;

/**
 *
 * @author psammand
 */
public abstract class Interaction
{
  
  public void acknowledge()
  {
    if(_currentInExchange != null)
    {
      _currentInExchange.acknowledge();
    }
  }

  public Exchange createOutgoingExchange(CoapMsg outgoingMsg)
  {
    _currentOutExchange = new Exchange(outgoingMsg, Exchange.TYPE_LOCAL);
    addExchange(_currentOutExchange);

    if (outgoingMsg.is_con())
    {
      // initiate retransmission
    }

    return _currentOutExchange;
  }

  public Exchange createIncomingExchange(CoapMsg incomingMsg)
  {
    _currentInExchange = new Exchange(incomingMsg, Exchange.TYPE_REMOTE);
    addExchange(_currentInExchange);
    return _currentInExchange;
  }

  public abstract void addExchange(Exchange exchange);

  public abstract void deleteExchange(Exchange exchange);

  public Exchange getCurrentInExchange()
  {
    return _currentInExchange;
  }

  public Exchange getCurrentOutExchange()
  {
    return _currentOutExchange;
  }

  public int getNoOfExchanges()
  {
    return _noOfExchanges;
  }

  public boolean hasExchanges()
  {
    return _noOfExchanges != 0;
  }

  public void deleteCurrentInExchange()
  {
    if (_currentInExchange != null)
    {
      deleteExchange(_currentInExchange);
      _currentInExchange = null;
    }
  }

  public void deleteCurrentOutExchange()
  {
    if (_currentOutExchange != null)
    {
      deleteExchange(_currentOutExchange);
      _currentOutExchange = null;
    }
  }

  public BlockTransferStatus getReqBlockTransferStatus()
  {
    return _reqBlockTransferStatus;
  }

  public BlockTransferStatus getResBlockTransferStatus()
  {
    return _resBlockTransferStatus;
  }

  public BlockTransferStatus createReqBlockTransferStatus(CoapMsg coapMsg, boolean isDownloading)
  {
    if (isDownloading)
    {
      CoapOptionSet option_set = coapMsg.get_option_set();
      int maxResourceBodySize = option_set.has_block1() && option_set.has_size1() ? option_set.get_size1() : 1024;

      _reqBlockTransferStatus = new BlockTransferStatus();
      _reqBlockTransferStatus.setBlockSize(option_set.get_block1().get_size());

      if (!_reqBlockTransferStatus.resizeBuffer(maxResourceBodySize))
      {
        _reqBlockTransferStatus = null;
      }
    } else
    {
      _reqBlockTransferStatus = new BlockTransferStatus();
      _reqBlockTransferStatus.setBlockSize(256);
    }

    return _reqBlockTransferStatus;
  }

  public void deleteReqBlockTransferStatus()
  {
    _reqBlockTransferStatus = null;
  }

  public BlockTransferStatus createResBlockTransferStatus(CoapMsg coapMsg, boolean isDownloading)
  {
    if (isDownloading)
    {
      CoapOptionSet option_set = coapMsg.get_option_set();
      int maxResourceBodySize = option_set.has_block2() && option_set.has_size2() ? option_set.get_size2() : 1024;

      _resBlockTransferStatus = new BlockTransferStatus();
      _resBlockTransferStatus.setBlockSize(256);

      if (!_resBlockTransferStatus.resizeBuffer(maxResourceBodySize))
      {
        _resBlockTransferStatus = null;
      }
    } else
    {
      _resBlockTransferStatus = new BlockTransferStatus();
      _resBlockTransferStatus.setBlockSize(256);
    }

    return _resBlockTransferStatus;

  }

  public void deleteResBlockTransferStatus()
  {
    _resBlockTransferStatus = null;
  }

  protected Exchange _currentInExchange = null;
  protected Exchange _currentOutExchange = null;
  protected int _noOfExchanges = 0;

  BlockTransferStatus _reqBlockTransferStatus = null;
  BlockTransferStatus _resBlockTransferStatus = null;
}
