package org.jinjuamla.stack.coap;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by psammand on 2/19/2018.
 */

public class CoapOption implements Comparable<CoapOption> {
    int _number = 0;
    int _length = 0;
    byte[] _value = new byte[0];

    public CoapOption() {
    }

    public CoapOption(int _number) {
        this._number = _number;
    }

    public CoapOption(int _number, String value) {
        this._number = _number;
        setStringValue(value);
    }

    public CoapOption(int _number, int value) {
        this._number = _number;
        setIntegerValue(value);
    }

    public CoapOption(int number, byte[] opaque) {
        this._number = number;
        setValue(opaque);
    }

    public int getLength() {
        if (_value != null) {
            return _value.length;
        }
        return 0;
    }

    public int getNumber() {
        return _number;
    }

    public void setNumber(int number) {
        this._number = number;
    }

    public byte[] getValue() {
        return _value;
    }

    public String getStringValue() {
        return new String(_value, StandardCharsets.UTF_8);
    }

    public int getIntegerValue() {
        int ret = 0;
        for (int i = 0; i < _value.length; i++) {
            ret += (_value[_value.length - i - 1] & 0xFF) << (i * 8);
        }
        return ret;
    }

    /**
     * Gets the option value as long.
     *
     * @return the long value
     */
    public long getLongValue() {
        long ret = 0;
        for (int i = 0; i < _value.length; i++) {
            ret += (long) (_value[_value.length - i - 1] & 0xFF) << (i * 8);
        }
        return ret;
    }

    /**
     * Sets the option value.
     *
     * @param value the new value
     */
    public void setValue(byte[] value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this._value = value;
        this._length = _value.length;
    }

    /**
     * Sets the option value from a string.
     *
     * @param str the new option value as string
     */
    public final void setStringValue(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        _value = str.getBytes(StandardCharsets.UTF_8);
        _length = _value.length;
    }

    /**
     * Sets the option value from an integer.
     *
     * @param val the new option value as integer
     */
    public final void setIntegerValue(int val) {
        int length = 0;
        for (int i = 0; i < 4; i++) {
            if (val >= 1 << (i * 8) || val < 0) {
                length++;
            } else {
                break;
            }
        }
        _value = new byte[length];
        for (int i = 0; i < length; i++) {
            _value[length - i - 1] = (byte) (val >> i * 8);
        }
        
        _length = _value.length;
    }

    /**
     * Sets the option value from a long.
     *
     * @param val the new option value as long
     */
    public void setLongValue(long val) {
        int length = 0;
        for (int i = 0; i < 8; i++) {
            if (val >= 1L << (i * 8) || val < 0) {
                length++;
            } else {
                break;
            }
        }
        _value = new byte[length];
        for (int i = 0; i < length; i++) {
            _value[length - i - 1] = (byte) (val >> i * 8);
        }
    }

    /**
     * Checks if is this option is critical.
     *
     * @return true, if is critical
     */
    public boolean isCritical() {
        // Critical = (onum & 1);
        return (_number & 1) != 0;
    }

    /**
     * Checks if is this option is unsafe.
     *
     * @return true, if is unsafe
     */
    public boolean isUnSafe() {
        // UnSafe = (onum & 2);
        return (_number & 2) != 0;
    }

    /**
     * Checks if this option is a NoCacheKey.
     *
     * @return true, if is NoCacheKey
     */
    public boolean isNoCacheKey() {
        // NoCacheKey = ((onum & 0x1e) == 0x1c);
        return (_number & 0x1E) == 0x1C;
    }

    /* (non-Javadoc)
       * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CoapOption)) {
            return false;
        }

        CoapOption op = (CoapOption) o;
        return _number == op._number && Arrays.equals(_value, op._value);
    }

    /* (non-Javadoc)
       * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return _number * 31 + Arrays.hashCode(_value);
    }

    /* (non-Javadoc)
       * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
//		sb.append(OptionNumberRegistry.toString(number));
        sb.append(_number);
        sb.append(": ");
//		sb.append(toValueString());
        return sb.toString();
    }

    /**
     * Renders the option value as string.
     *
     * @return the option value as string
     */
//	public String toValueString() {
//		switch (OptionNumberRegistry.getFormatByNr(number)) {
//		case INTEGER:
//			if (number==OptionNumberRegistry.ACCEPT || number==OptionNumberRegistry.CONTENT_FORMAT) return "\""+MediaTypeRegistry.toString(getIntegerValue())+"\"";
//			else if (number==OptionNumberRegistry.BLOCK1 || number==OptionNumberRegistry.BLOCK2) return "\""+ new BlockOption(value) +"\"";
//			else return Integer.toString(getIntegerValue());
//		case STRING:
//			return "\""+this.getStringValue()+"\"";
//		default:
//			return toHexString(this.getValue());
//		}
//	}
  /*
     * Converts the specified byte array to a hexadecimal string.
	 *
	 * @param bytes the byte array
	 * @return the hexadecimal code string
   */
    private String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }


    @Override
    public int compareTo(CoapOption coapOption) {
        return _number - coapOption._number;
    }
}
