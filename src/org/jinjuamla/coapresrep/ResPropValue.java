/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.coapresrep;

/**
 *
 * @author psammand
 * @param <E>
 */
public class ResPropValue<E>
{

  public static final int TYPE_BOOL = 0;
  public static final int TYPE_INTEGER = 1;
  public static final int TYPE_NUMBER = 2;
  public static final int TYPE_STRING = 3;
  public static final int TYPE_OBJECT = 4;
  public static final int TYPE_BOOL_ARRAY = 5;
  public static final int TYPE_INTEGER_ARRAY = 6;
  public static final int TYPE_NUMBER_ARRAY = 7;
  public static final int TYPE_STRING_ARRAY = 8;
  public static final int TYPE_OBJECT_ARRAY = 9;
  public static final int TYPE_NONE = 10;

  public ResPropValue(int type)
  {
    this._type = type;
  }
  
  public int getType()
  {
    return _type;
  }

  public void setType(int _type)
  {
    this._type = _type;
  }

  private int _type;
}
