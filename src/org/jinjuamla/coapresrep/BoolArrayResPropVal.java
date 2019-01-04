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
public class BoolArrayResPropVal extends ResPropValue<ArrayList<Boolean>>
{
  ArrayList<Boolean> _value;
  
  public BoolArrayResPropVal(ArrayList<Boolean> value)
  {
    super(ResPropValue.TYPE_BOOL_ARRAY);
    _value = value;
  }

  public ArrayList<Boolean> getValue()
  {
    return _value;
  }

  public void setValue(ArrayList<Boolean> _value)
  {
    this._value = _value;
  }

}
