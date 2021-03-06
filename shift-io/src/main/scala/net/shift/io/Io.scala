package net.shift
package io

import java.io.InputStream

trait Closing {
  def close
}

trait ReadChannel extends Closing {
  def readBuffer(buf: Array[Byte]): Int
  def readBuffer(buf: Array[Byte], offset: Int, length: Int): Int
  def readInt: Int
  def readByte: Byte
  def readLong: Long
  def readFloat: Float
  def readDouble: Double
  def readShort: Short
  def readBoolean: Boolean
  def readChar: Char
}

trait WriteChannel extends Closing {
  def writeBuffer(buf: Array[Byte])
  def writeInt(v: Int)
  def writeByte(v: Byte)
  def writeLong(v: Long)
  def writeFloat(v: Float)
  def writeDouble(v: Double)
  def writeShort(v: Short)
  def writeBoolean(v: Boolean)
  def writeChar(v: Char)
  def bytesWritten: Long
}
