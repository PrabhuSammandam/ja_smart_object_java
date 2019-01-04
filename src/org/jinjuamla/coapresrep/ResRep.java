/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.coapresrep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author psammand
 */
public class ResRep {

    private final Map<String, ResPropValue> _maps;

    public Map<String, ResPropValue> getMaps() {
        return _maps;
    }

    public void add(String key, Boolean value) {
        add(key, new BoolResPropVal(value));
    }

    public void add(String key, Long value) {
        add(key, new IntegerResPropVal(value));
    }

    public void add(String key, Double value) {
        add(key, new NumberResPropVal(value));
    }

    public void add(String key, String value) {
        add(key, new StringResPropVal(value));
    }

    public void add(String key, ResRep value) {
        add(key, new ObjectResPropVal(value));
    }

    public void addBooleanArray(String key, ArrayList<Boolean> value) {
        add(key, new BoolArrayResPropVal(value));
    }

    public void addIntegerArray(String key, ArrayList<Long> value) {
        add(key, new IntegerArrayResPropVal(value));
    }

    public void addNumberArray(String key, ArrayList<Double> value) {
        add(key, new NumberArrayResPropVal(value));
    }

    public void addStringArray(String key, ArrayList<String> value) {
        add(key, new StringArrayResPropVal(value));
    }

    public void addObjectArray(String key, ArrayList<ResRep> value) {
        add(key, new ObjectArrayResPropVal(value));
    }

    public void add(String key, ResPropValue value) {
        if (!_maps.containsKey(key)) {
            _maps.put(key, value);
        }
    }

    public boolean hasProp(String prop) {
        return _maps.containsKey(prop);
    }

    public boolean getBool(String prop) {
        if (hasProp(prop)) {
            ResPropValue propValue = _maps.get(prop);

            if (propValue != null && propValue.getType() == ResPropValue.TYPE_BOOL) {
                BoolResPropVal boolResPropVal = (BoolResPropVal) propValue;
                return boolResPropVal.isValue();
            }
        }

        return false;
    }

    public ResRep() {
        this._maps = new HashMap<>();
    }

}
