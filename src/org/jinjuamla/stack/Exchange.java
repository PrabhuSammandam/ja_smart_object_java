package org.jinjuamla.stack;

import org.jinjuamla.stack.coap.CoapMsg;
import org.jinjuamla.stack.consts.CoapMsgConsts;

/**
 * Created by psammand on 2/19/2018.
 */
public class Exchange
{

  public static final short TYPE_LOCAL = 0;
  public static final short TYPE_REMOTE = 1;

  public Exchange(CoapMsg originatorMsg, short origin)
  {
    this._origin = origin;

    if (originatorMsg != null)
    {
      if (origin == TYPE_REMOTE)
      {
        _in_msg = originatorMsg;
      } else
      {
        _out_msg = originatorMsg;
      }
    }
  }

  public boolean isOriginLocal()
  {
    return _origin == TYPE_LOCAL;
  }

  public boolean isOriginRemote()
  {
    return _origin == TYPE_REMOTE;
  }

  public void acknowledge()
  {
    if (_in_msg != null && isOriginRemote() && _in_msg.is_con())
    {
      _out_msg = new CoapMsg();
      _out_msg.set_type(CoapMsgConsts.COAP_MSG_TYPE_ACK);
      _out_msg.set_id(_in_msg.get_id());
      _out_msg.setEndpoint(_in_msg.getEndpoint());
      deliver();
    }
  }

  public void reject()
  {
    if (_in_msg != null && isOriginRemote())
    {
      _out_msg = new CoapMsg();
      _out_msg.set_type(CoapMsgConsts.COAP_MSG_TYPE_RST);
      _out_msg.set_id(_in_msg.get_id());
      _out_msg.setEndpoint(_in_msg.getEndpoint());
      deliver();
    }
  }

  public void resendReply()
  {
    if (_in_msg != null && isOriginRemote() && _in_msg.is_con() && _out_msg != null)
    {
      deliver();
    }
  }

  public void replyPiggyback(CoapMsg response)
  {
    if (_in_msg != null)
    {
      response.set_id(_in_msg.get_id());
      _out_msg = response;
      deliver();
    }
  }

  public Interaction getInteraction()
  {
    return _interaction;
  }

  public void setInteraction(Interaction _interaction)
  {
    this._interaction = _interaction;
  }

  public boolean isConOutGoing()
  {
    return isOriginLocal() && _out_msg.is_con();
  }

  public CoapMsg getOriginatorMsg()
  {
    return isOriginLocal() ? _out_msg : _in_msg;
  }

  public void deliver()
  {
    if (_out_msg != null)
    {
        MessageSender.sendMessage(_out_msg);
    }
  }

  public boolean isMsgMatched(CoapMsg coapMsg)
  {
    CoapMsg exchangeMsg = isOriginLocal() ? _out_msg : _in_msg;

    return coapMsg != null
            && exchangeMsg != null
            && exchangeMsg.get_token().equals(coapMsg.get_token())
            && exchangeMsg.getEndpoint().equals(coapMsg.getEndpoint());
  }

  short _retransmissionCount = 0;
  long _timeoutInMicroSec = 0;
  short _origin = TYPE_LOCAL;
  CoapMsg _in_msg = null;
  CoapMsg _out_msg = null;
  Interaction _interaction = null;

}
