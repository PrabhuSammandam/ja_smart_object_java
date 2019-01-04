/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.coapresrep;

/**
 *
 * @author psammand
 */
public class StringResPropVal extends ResPropValue<String>
{

  String _value;

  public StringResPropVal()
  {
    super(ResPropValue.TYPE_STRING);
  }

  public StringResPropVal(String value)
  {
    super(ResPropValue.TYPE_STRING);
    _value = value;
  }

  public String getValue()
  {
    return _value;
  }

  public void setValue(String _value)
  {
    this._value = _value;
  }
}
