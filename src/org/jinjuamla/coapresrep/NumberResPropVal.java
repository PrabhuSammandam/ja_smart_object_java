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
public class NumberResPropVal extends ResPropValue<Double>
{

  double _value;

  public NumberResPropVal()
  {
    super(ResPropValue.TYPE_NUMBER);
  }

  public NumberResPropVal(double value)
  {
    super(ResPropValue.TYPE_NUMBER);
    _value = value;
  }

  public double getValue()
  {
    return _value;
  }

  public void setValue(double _value)
  {
    this._value = _value;
  }
}
