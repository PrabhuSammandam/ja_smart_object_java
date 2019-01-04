/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.cborresrep;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.builder.MapBuilder;
import co.nstant.in.cbor.model.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jinjuamla.coapresrep.*;
import org.jinjuamla.coapresrep.ObjectResPropVal;
import org.jinjuamla.coapresrep.ResPropValue;
import org.jinjuamla.coapresrep.ResRep;

/**
 *
 * @author psammand
 */
public class ResRepCborBuilder
{
  public byte[] encode(ResRep res_representation)
  {
    CborBuilder builder = new CborBuilder();
    Collection<ResPropValue> values = res_representation.getMaps().values();
    ResPropValue first_element = values.iterator().next();
    
    switch (first_element.getType())
    {
      case ResPropValue.TYPE_OBJECT:
      {
        ObjectResPropVal obj = (ObjectResPropVal) first_element;
        MapBuilder<CborBuilder> map_builder = builder.addMap();
        encode(obj.getValue(), map_builder);
      }
      break;
      case ResPropValue.TYPE_OBJECT_ARRAY:
      {
        ObjectArrayResPropVal obj_array = (ObjectArrayResPropVal) first_element;
        ArrayBuilder<CborBuilder> array_builder = builder.addArray();
        encode_array(obj_array.getValue(), array_builder);
      }
      break;
      default:
        return null;
    }
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    CborEncoder encoder = new CborEncoder(baos);
    
    try
    {
      List<DataItem> data_items = builder.build();
      
      data_items.forEach((t) ->
      {
        System.out.println(t);
      });
      encoder.encode(data_items);
    } catch (CborException ex)
    {
      Logger.getLogger(ResRepCborBuilder.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }
    
    return baos.toByteArray();
  }
  public void decode(byte[] encoded_data, ResRep representation)
  {
    ByteArrayInputStream bais = new ByteArrayInputStream(encoded_data);
    List<DataItem> dataItems = null;
    try
    {
      dataItems = new CborDecoder(bais).decode();
    } catch (CborException ex)
    {
      Logger.getLogger(ResRepCborBuilder.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    if (dataItems == null || dataItems.isEmpty() || dataItems.size() > 1)
    {
      return;
    }
    
    DataItem root_item = dataItems.get(0);
    
    if (root_item.getMajorType() == MajorType.ARRAY)
    {
      decode_object_array((co.nstant.in.cbor.model.Array) root_item, "", representation);
    } else if (root_item.getMajorType() == MajorType.MAP)
    {
      decode_object((co.nstant.in.cbor.model.Map) root_item, representation);
    }
  }

  private void encode_array(ArrayList<ResRep> object_array, ArrayBuilder<CborBuilder> array_builder)
  {
    object_array.forEach((obj) ->
    {
      CborBuilder cbor_builder = new CborBuilder();
      MapBuilder<CborBuilder> map_builder = cbor_builder.addMap();
      encode(obj, map_builder);
      array_builder.add(cbor_builder.build().get(0));
    });
  }

  private void encode(ResRep res_representation, MapBuilder<CborBuilder> map)
  {
    Map<String, ResPropValue> map_rep = res_representation.getMaps();
    Iterator<String> iterator = map_rep.keySet().iterator();

    while (iterator.hasNext())
    {
      String key = iterator.next();
      ResPropValue prop_value = map_rep.get(key);

      if (prop_value != null)
      {
        switch (prop_value.getType())
        {
          case ResPropValue.TYPE_BOOL:
          {
            map.put(key, ((BoolResPropVal) prop_value).isValue());
          }
          break;
          case ResPropValue.TYPE_INTEGER:
          {
            map.put(key, ((IntegerResPropVal) prop_value).getValue());
          }
          break;
          case ResPropValue.TYPE_NUMBER:
          {
            map.put(key, ((NumberResPropVal) prop_value).getValue());
          }
          break;
          case ResPropValue.TYPE_STRING:
          {
            map.put(key, ((StringResPropVal) prop_value).getValue());
          }
          break;
          case ResPropValue.TYPE_OBJECT:
          {
            ResRep object = ((ObjectResPropVal) prop_value).getValue();
            CborBuilder sub_map_builder = new CborBuilder();
            MapBuilder<CborBuilder> sub_map = sub_map_builder.addMap();

            encode(object, sub_map);
            map.put(new UnicodeString(key), sub_map_builder.build().get(0));
          }
          break;
          case ResPropValue.TYPE_BOOL_ARRAY:
          {
            ArrayBuilder<MapBuilder<CborBuilder>> bool_array_builder = map.putArray(key);
            ArrayList<Boolean> bool_array = ((BoolArrayResPropVal) prop_value).getValue();
            bool_array.forEach(bool_array_builder::add);
          }
          break;
          case ResPropValue.TYPE_INTEGER_ARRAY:
          {
            ArrayBuilder<MapBuilder<CborBuilder>> integer_array_builder = map.putArray(key);
            ArrayList<Long> bool_array = ((IntegerArrayResPropVal) prop_value).getValue();
            bool_array.forEach(integer_array_builder::add);
          }
          break;
          case ResPropValue.TYPE_NUMBER_ARRAY:
          {
            ArrayBuilder<MapBuilder<CborBuilder>> integer_array_builder = map.putArray(key);
            ArrayList<Double> bool_array = ((NumberArrayResPropVal) prop_value).getValue();
            bool_array.forEach(integer_array_builder::add);
          }
          break;
          case ResPropValue.TYPE_STRING_ARRAY:
          {
            ArrayBuilder<MapBuilder<CborBuilder>> integer_array_builder = map.putArray(key);
            ArrayList<String> bool_array = ((StringArrayResPropVal) prop_value).getValue();
            bool_array.forEach(integer_array_builder::add);
          }
          break;
          case ResPropValue.TYPE_OBJECT_ARRAY:
          {
            ArrayList<ResRep> object = ((ObjectArrayResPropVal) prop_value).getValue();
            CborBuilder sub_array_builder = new CborBuilder();
            ArrayBuilder<CborBuilder> sub_array = sub_array_builder.addArray();
            encode_array(object, sub_array);
            map.put(new UnicodeString(key), sub_array_builder.build().get(0));
          }
          break;
          default:
            break;
        }
      }
    }
  }


  private boolean decode_array(co.nstant.in.cbor.model.Array arrayItem, String keyString, ResRep representation)
  {
    List<DataItem> item_list = arrayItem.getDataItems();

    if (item_list == null || item_list.isEmpty())
    {
      return false;
    }
    DataItem firstItem = item_list.get(0);

    switch (firstItem.getMajorType())
    {
      case UNSIGNED_INTEGER:
      {
        ArrayList<Long> long_array = new ArrayList<>();

        arrayItem.getDataItems().forEach((t) ->
        {
          long_array.add(((UnsignedInteger) t).getValue().longValue());
        });
        representation.addIntegerArray(keyString, long_array);
      }
      break;
      case NEGATIVE_INTEGER:
      {
        ArrayList<Long> long_array = new ArrayList<>();

        arrayItem.getDataItems().forEach((t) ->
        {
          long_array.add(((NegativeInteger) t).getValue().longValue());
        });
        representation.addIntegerArray(keyString, long_array);
      }
      break;
      case UNICODE_STRING:
      {
        ArrayList<String> long_array = new ArrayList<>();

        arrayItem.getDataItems().forEach((t) ->
        {
          long_array.add(((UnicodeString) t).getString());
        });
        representation.addStringArray(keyString, long_array);
      }
      break;
      case MAP:
      {
        decode_object_array(arrayItem, keyString, representation);
      }
      break;
      case SPECIAL:
      {
        Special specialItem = (Special) firstItem;

        if (specialItem.getSpecialType() == SpecialType.SIMPLE_VALUE
                && ((((SimpleValue) specialItem).getSimpleValueType() == SimpleValueType.FALSE))
                || (((SimpleValue) specialItem).getSimpleValueType() == SimpleValueType.TRUE))
        {
          ArrayList<Boolean> long_array = new ArrayList<>();

          arrayItem.getDataItems().forEach((t) ->
          {
            SimpleValue simpleValue = (SimpleValue) t;
            long_array.add((simpleValue.getSimpleValueType() == SimpleValueType.TRUE));
          });
          representation.addBooleanArray(keyString, long_array);
        } else if (specialItem.getSpecialType() == SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT)
        {
          ArrayList<Double> long_array = new ArrayList<>();

          arrayItem.getDataItems().forEach((t) ->
          {
            long_array.add(((DoublePrecisionFloat) t).getValue());
          });
          representation.addNumberArray(keyString, long_array);
        } else if (specialItem.getSpecialType() == SpecialType.IEEE_754_HALF_PRECISION_FLOAT)
        {
          ArrayList<Double> long_array = new ArrayList<>();

          arrayItem.getDataItems().forEach((t) ->
          {
            long_array.add((double) ((HalfPrecisionFloat) t).getValue());
          });
          representation.addNumberArray(keyString, long_array);
        } else if (specialItem.getSpecialType() == SpecialType.IEEE_754_SINGLE_PRECISION_FLOAT)
        {
          ArrayList<Double> long_array = new ArrayList<>();

          arrayItem.getDataItems().forEach((t) ->
          {
            long_array.add((double) ((SinglePrecisionFloat) t).getValue());
          });
          representation.addNumberArray(keyString, long_array);
        }
      }
      break;

      default:
        break;
    }

    return true;
  }

  private boolean decode_object_array(co.nstant.in.cbor.model.Array arrayItem, String objectArrayKey, ResRep representation)
  {
    ArrayList<ResRep> object_array = new ArrayList<>();
    List<DataItem> items = arrayItem.getDataItems();

    for (DataItem t : items)
    {
      ResRep object = new ResRep();

      if (decode_object((co.nstant.in.cbor.model.Map) t, object) != true)
      {
        return false;
      }

      object_array.add(object);
    }

    representation.addObjectArray(objectArrayKey, object_array);

    return true;
  }

  private boolean decode_object(co.nstant.in.cbor.model.Map mapItem, ResRep representation)
  {
    for (DataItem item : mapItem.getKeys())
    {
      if (item == null || item.getMajorType() != MajorType.UNICODE_STRING || mapItem.get(item) == null)
      {
        continue;
      }

      String keyString = ((UnicodeString) item).getString();
      DataItem valueItem = mapItem.get(item);

      switch (valueItem.getMajorType())
      {
        case UNSIGNED_INTEGER:
        {
          representation.add(keyString, ((UnsignedInteger) valueItem).getValue().longValue());
        }
        break;
        case NEGATIVE_INTEGER:
        {
          representation.add(keyString, ((NegativeInteger) valueItem).getValue().longValue());
        }
        break;
        case UNICODE_STRING:
        {
          representation.add(keyString, ((UnicodeString) valueItem).getString());
        }
        break;
        case ARRAY:
        {
          decode_array((Array) valueItem, keyString, representation);
        }
        break;
        case MAP:
        {
          ResRep new_map = new ResRep();

          if (decode_object((co.nstant.in.cbor.model.Map) valueItem, new_map) != true)
          {
            return false;
          }
          representation.add(keyString, new_map);
        }
        break;
        case SPECIAL:
        {
          Special specialItem = (Special) valueItem;

          if (specialItem.getSpecialType() == SpecialType.SIMPLE_VALUE
                  && ((((SimpleValue) specialItem).getSimpleValueType() == SimpleValueType.FALSE))
                  || (((SimpleValue) specialItem).getSimpleValueType() == SimpleValueType.TRUE))
          {
            SimpleValue simpleValue = (SimpleValue) specialItem;
            representation.add(keyString, (simpleValue.getSimpleValueType() == SimpleValueType.TRUE));
          } else if (specialItem.getSpecialType() == SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT)
          {
            representation.add(keyString, ((DoublePrecisionFloat) specialItem).getValue());
          } else if (specialItem.getSpecialType() == SpecialType.IEEE_754_HALF_PRECISION_FLOAT)
          {
            representation.add(keyString, (double) ((HalfPrecisionFloat) specialItem).getValue());
          } else if (specialItem.getSpecialType() == SpecialType.IEEE_754_SINGLE_PRECISION_FLOAT)
          {
            representation.add(keyString, (double) ((SinglePrecisionFloat) specialItem).getValue());
          }
        }
        break;
      }
    }
    return true;
  }

}
