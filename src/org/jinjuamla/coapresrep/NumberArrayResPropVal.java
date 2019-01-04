/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.coapresrep;

import java.util.ArrayList;

/**
 *
 * @author psammand
 */
public class NumberArrayResPropVal extends ResPropValue<ArrayList<Double>>
{

  ArrayList<Double> _value;

  public NumberArrayResPropVal(ArrayList<Double> value)
  {
    super(ResPropValue.TYPE_NUMBER_ARRAY);
    _value = value;
  }

  public ArrayList<Double> getValue()
  {
    return _value;
  }

  public void setValue(ArrayList<Double> _value)
  {
    this._value = _value;
  }

}
