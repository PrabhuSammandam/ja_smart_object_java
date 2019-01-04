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
public class ObjectArrayResPropVal extends ResPropValue<ArrayList<ResRep>>
{
  ArrayList<ResRep> _value;
  
  public ObjectArrayResPropVal(ArrayList<ResRep> value)
  {
    super(ResPropValue.TYPE_OBJECT_ARRAY);
    _value = value;
  }

  public ArrayList<ResRep> getValue()
  {
    return _value;
  }

  public void setValue(ArrayList<ResRep> _value)
  {
    this._value = _value;
  }
  
}
