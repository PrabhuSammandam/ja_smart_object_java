package org.jinjuamla.stack.coap;

import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.stack.consts.CoapMsgConsts;
import java.util.Arrays;

public class CoapMsg implements Cloneable
{

  public int get_version()
  {
    return _version;
  }

  public void set_version(int _version)
  {
    this._version = _version;
  }

  public int get_type()
  {
    return _type;
  }

  public void set_type(int _type)
  {
    this._type = _type;
  }

  public int get_id()
  {
    return _id;
  }

  public void set_id(int _id)
  {
    this._id = _id;
  }

  public int get_code()
  {
    return _code;
  }

  public void set_code(int _code)
  {
    this._code = _code;
  }

  public CoapMsgToken get_token()
  {
    return _token;
  }

  public void set_token(CoapMsgToken _token)
  {
    this._token = _token;
  }

  public byte[] get_payload()
  {
    return _payload;
  }

  public void set_payload(byte[] _payload)
  {
    this._payload = _payload;
  }

  public CoapOptionSet get_option_set()
  {
    return _option_set;
  }

  public void set_option_set(CoapOptionSet _option_set)
  {
    this._option_set = _option_set;
  }

  public boolean is_request()
  {
    return _code > CoapMsgConsts.MSG_CODE_EMPTY && _code <= CoapMsgConsts.MSG_CODE_DELETE;
  }

  public boolean is_empty_msg()
  {
    return _code == CoapMsgConsts.MSG_CODE_EMPTY;
  }

  public boolean is_con()
  {
    return _type == CoapMsgConsts.COAP_MSG_TYPE_CON;
  }

  public boolean is_non()
  {
    return _type == CoapMsgConsts.COAP_MSG_TYPE_NON;
  }

  public boolean is_ack()
  {
    return _type == CoapMsgConsts.COAP_MSG_TYPE_ACK;
  }

  public boolean is_rst()
  {
    return _type == CoapMsgConsts.COAP_MSG_TYPE_RST;
  }

  public boolean has_mid()
  {
    return _id != 0;
  }

  public boolean hasBlockOption()
  {
    return _option_set.has_block1() || _option_set.has_block2();
  }

  public Endpoint getEndpoint()
  {
    return _endpoint;
  }

  public void setEndpoint(Endpoint _endpoint)
  {
    this._endpoint = _endpoint;
  }

  @Override
  public Object clone() throws CloneNotSupportedException
  {
    CoapMsg newCoapMsg = (CoapMsg) super.clone();

    newCoapMsg._version = _version;
    newCoapMsg._type = _type;
    newCoapMsg._id = _id;
    newCoapMsg._code = _code;
    newCoapMsg._token = (CoapMsgToken) _token.clone();
    newCoapMsg._option_set = (CoapOptionSet) _option_set.clone();
    newCoapMsg._endpoint = (Endpoint) _endpoint.clone();

    if (_payload != null)
    {
      newCoapMsg._payload = Arrays.copyOf(_payload, _payload.length);
    }

    return newCoapMsg;
  }

  int _version = 1;
  int _type;
  int _id;
  int _code;
  byte[] _payload = null;
  CoapMsgToken _token = new CoapMsgToken();
  CoapOptionSet _option_set = new CoapOptionSet();
  Endpoint _endpoint = new Endpoint();
}
