/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.stack;

import org.jinjuamla.stack.coap.CoapBlockOption;

/**
 *
 * @author psammand
 */
public class BlockTransferStatus
{

  public int getBlockSize()
  {
    return _blockSize;
  }

  public void setBlockSize(int _blockSize)
  {
    this._blockSize = _blockSize;
  }

  public int getCurrentBlockNo()
  {
    return _currentBlockNo;
  }

  public void setCurrentBlockNo(int _currentBlockNo)
  {
    this._currentBlockNo = _currentBlockNo;
  }

  public int getNextBlockNo()
  {
    return _nextBlockNo;
  }

  public void setNextBlockNo(int _nextBlockNo)
  {
    this._nextBlockNo = _nextBlockNo;
  }

  public int getPayloadIndex()
  {
    return _payloadIndex;
  }
  
  public byte[] getPayloadBuffer()
  {
    return _payloadBuffer;
  }
  
  public int moveToNextBlock()
  {
    _currentBlockNo = _nextBlockNo;
    _nextBlockNo++;
    
    return _nextBlockNo;
  }

  public int getPayloadCapacity()
  {
    return _payloadBuffer == null ? 0 : _payloadBuffer.length;
  }

  public boolean addPayload(byte[] payload)
  {
    System.arraycopy(payload, 0, _payloadBuffer, _payloadIndex, payload.length);
    return true;
  }

  public boolean resizeBuffer(int initialCapacity)
  {
    clear();

    _payloadBuffer = new byte[initialCapacity];

    if (_payloadBuffer == null)
    {
      return false;
    }

    return true;
  }

  public void clear()
  {
    _payloadBuffer = null;
    _payloadIndex = 0;
    _blockSize = 0;
    _currentBlockNo = 0;
    _nextBlockNo = 0;
  }

  public int getSzx()
  {
    return CoapBlockOption.size_to_szx((short) _blockSize);
  }

  byte[] _payloadBuffer = null;
  int _payloadIndex = 0;
  int _blockSize = 0;
  int _currentBlockNo = 0;
  int _nextBlockNo = 0;
}
