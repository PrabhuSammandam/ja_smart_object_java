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
public class IntegerArrayResPropVal extends ResPropValue<ArrayList<Long>>
{
   ArrayList<Long> _value;
 
  public IntegerArrayResPropVal(ArrayList<Long> value)
  {
    super(ResPropValue.TYPE_INTEGER_ARRAY);
    _value = value;
  }

  public ArrayList<Long> getValue()
  {
    return _value;
  }

  public void setValue(ArrayList<Long> _value)
  {
    this._value = _value;
  }
  
  
}
