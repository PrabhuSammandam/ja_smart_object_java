package org.jinjuamla.stack.coap;

/**
 * Created by psammand on 2/19/2018.
 */
public class CoapBlockOption
{

  public CoapBlockOption()
  {
  }

  public CoapBlockOption(int _number, boolean _has_more, short _szx)
  {
    this._number = _number;
    this._has_more = _has_more;
    this._szx = _szx;
  }

  public void clear()
  {
    this._number = 0;
    this._has_more = false;
    this._szx = 0;
  }

  public void decode(byte[] buffer)
  {
    if (buffer == null || buffer.length == 0)
    {
      clear();
    } else
    {
      byte u8_lsb_byte = buffer[buffer.length - 1];
      _szx = (short) (u8_lsb_byte & 0x07);
      _has_more = (u8_lsb_byte >> 3 & 0x01) == 0x01;

      int temp_num = (u8_lsb_byte & 0xFF) >> 4;

      for (int i = 1; i < buffer.length; i++)
      {
        temp_num += (buffer[buffer.length - i - 1] & 0xff) << (i * 8 - 4);
      }

      _number = temp_num;
    }
  }

  public short encode(byte[] buffer)
  {
    short buf_len;
    short end = (short) (get_szx() | (is_has_more() ? 1 << 3 : 0));
    int num = get_number();

    if (num == 0 && !is_has_more() && get_szx() == 0)
    {
      buf_len = 0;
    } else if (num < 1 << 4)
    {
      buf_len = 1;
      buffer[0] = (byte) (num << 4 | end);
    } else if (num < 1 << 12)
    {
      buf_len = 2;
      buffer[0] = (byte) (num >> 4);
      buffer[1] = (byte) (num << 4 | end);
    } else
    {
      buf_len = 3;
      buffer[0] = (byte) (num >> 12);
      buffer[1] = (byte) (num >> 4);
      buffer[2] = (byte) (num << 4 | end);
    }
    return buf_len;
  }

  public short get_size()
  {
    return (short) (1 << (4 + get_szx()));
  }

  public void set_size(short size)
  {
    set_szx(size_to_szx(size));
  }

  public int get_number()
  {
    return _number;
  }

  public void set_number(int _number)
  {
    this._number = _number;
  }

  public boolean is_has_more()
  {
    return _has_more;
  }

  public void set_has_more(boolean _has_more)
  {
    this._has_more = _has_more;
  }

  public short get_szx()
  {
    return _szx;
  }

  public void set_szx(short _szx)
  {
    this._szx = _szx;
  }

  public static short size_to_szx(short size)
  {
    if (size <= 16)
    {
      return 0;
    }

    if (size >= 32 && size < 64)
    {
      return 1;
    }

    if (size >= 64 && size < 128)
    {
      return 2;
    }

    if (size >= 128 && size < 256)
    {
      return 3;
    }

    if (size >= 256 && size < 512)
    {
      return 4;
    }

    if (size >= 512 && size < 1024)
    {
      return 5;
    }

    return 6;
  }

  public static short szx_to_size(short szx)
  {
    if (szx <= 0)
    {
      return 16;
    }

    if (szx >= 6)
    {
      return 1024;
    }

    return (short) (1 << (szx + 4));
  }

  @Override
  protected Object clone() throws CloneNotSupportedException
  {
    CoapBlockOption newBlockOption = (CoapBlockOption) super.clone();

    newBlockOption._number = _number;
    newBlockOption._has_more = _has_more;
    newBlockOption._szx = _szx;

    return newBlockOption;
  }

  int _number = 0;
  boolean _has_more = false;
  short _szx = 0;
}
