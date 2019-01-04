package org.jinjuamla.stack.coap;

import java.util.Arrays;

/**
 * Created by psammand on 2/19/2018.
 */
public class CoapMsgToken implements Cloneable
{

  public void set_token(byte[] token)
  {
    this._token = token;
  }

  public byte[] get_token()
  {
    return this._token;
  }

  public byte get_length()
  {
    if (_token != null)
    {
      return (byte) _token.length;
    }
    return 0;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    CoapMsgToken that = (CoapMsgToken) o;

    return Arrays.equals(_token, that._token);
  }

  @Override
  public int hashCode()
  {
    return Arrays.hashCode(_token);
  }

  @Override
  protected Object clone() throws CloneNotSupportedException
  {
    CoapMsgToken newToken = (CoapMsgToken) super.clone();

    if (_token != null)
    {
      newToken._token = Arrays.copyOf(_token, _token.length);
    }

    return newToken;
  }

  byte[] _token = null;
}
