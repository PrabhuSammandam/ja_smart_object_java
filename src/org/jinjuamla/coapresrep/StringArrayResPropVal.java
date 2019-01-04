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
public class StringArrayResPropVal extends ResPropValue<ArrayList<String>>
{
  ArrayList<String> _value;
  
  public StringArrayResPropVal(ArrayList<String> value)
  {
    super(ResPropValue.TYPE_STRING_ARRAY);
    _value = value;
  }

  public ArrayList<String> getValue()
  {
    return _value;
  }

  public void setValue(ArrayList<String> _value)
  {
    this._value = _value;
  }
  
}
