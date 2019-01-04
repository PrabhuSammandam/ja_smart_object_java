package org.jinjuamla.stack.coap;

import org.jinjuamla.network.enums.ErrCode;

/**
 * Created by psammand on 2/19/2018.
 */
public class CoapMsgCodecException extends Exception
{
  ErrCode _errCode;

  public CoapMsgCodecException(ErrCode _errCode)
  {
    this._errCode = _errCode;
  }

  public ErrCode getErrCode()
  {
    return _errCode;
  }

  public void setErrCode(ErrCode _errCode)
  {
    this._errCode = _errCode;
  }

}
