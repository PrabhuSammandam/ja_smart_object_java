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
public class ObjectResPropVal extends ResPropValue
{
  ResRep _value = null;

  public ObjectResPropVal()
  {
    super(ResPropValue.TYPE_OBJECT);
  }

  public ObjectResPropVal(ResRep value)
  {
    this();
    this._value = value;
  }

  public ResRep getValue()
  {
    return _value;
  }

  public void setValue(ResRep _value)
  {
    this._value = _value;
  }
}
