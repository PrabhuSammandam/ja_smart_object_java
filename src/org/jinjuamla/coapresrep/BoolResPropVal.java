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
public class BoolResPropVal extends ResPropValue<Boolean>
{

  boolean _value;

  public BoolResPropVal()
  {
    super(ResPropValue.TYPE_BOOL);
  }

  BoolResPropVal(boolean value)
  {
    super(ResPropValue.TYPE_BOOL);
    _value = value;
  }

  public boolean isValue()
  {
    return _value;
  }

  public void setValue(boolean _value)
  {
    this._value = _value;
  }
  
}
