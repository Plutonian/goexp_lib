package com.goexp.common

import java.nio.channels.SeekableByteChannel
import java.nio.charset.Charset
import java.nio.{ByteBuffer, ByteOrder}
import scala.language.implicitConversions


object LimitData2ByteBuffer {
  implicit def change(item: LimitData) = {
    item.data

  }

  implicit def changeArray(item: LimitData) = {
    item.data.array()
  }
}


class LimitData(val size: () => Int) {
  lazy val data: ByteBuffer = ByteBuffer.allocate(size()).order(ByteOrder.LITTLE_ENDIAN)

  def load(buffer: ByteBuffer): Unit = {
    data.clear()
    buffer.get(data.array())
  }

  def load(channel: SeekableByteChannel): Unit = {
    data.clear()
    channel.read(data)
  }
}


case class ByteData() extends LimitData(() => 1) {
  def get() = {
    data.rewind()
    data.get(0)
  }

  override def toString: String = {
    get().toString
  }

  def hexString() = {
    s"${"0x%02x".formatted(get())}"
  }
}

case class ShortData() extends LimitData(() => 2) {
  def get() = {
    data.rewind()
    data.getShort
  }

  override def toString: String = {
    get().toString

  }

  def hexString() = {
    s"${"%04x".formatted(get())}"

  }
}

case class IntData() extends LimitData(() => 4) {
  def get() = {
    data.rewind()
    data.getInt
  }

  override def toString: String = {
    get().toString

  }

  def hexString() = {
    s"${"%08x".formatted(get())}"

  }
}

case class LongData() extends LimitData(() => 8) {
  def get() = {
    data.rewind()
    data.getLong
  }

  override def toString: String = {
    get().toString

  }

  def hexString() = {
    s"${"%016x".formatted(get())}"

  }
}

class ByteArrayData(length: () => Int) extends LimitData(length)

object ByteArrayData {
  def apply(length: => Int): ByteArrayData = new ByteArrayData(() => length)
}

class StringData(length: () => Int, charset: Charset) extends LimitData(length) {
  override def toString: String = {
    charset.decode(data.rewind()).toString
  }

  def fromCString(): String = {
    data.rewind()

    val endIndex = data.array().indexOf(0)

    if (endIndex != -1) {
      data.limit(endIndex)
    }

    charset.decode(data).toString

  }


}

object StringData {
  def apply(length: => Int, charset: Charset): StringData = new StringData(() => length, charset)
}