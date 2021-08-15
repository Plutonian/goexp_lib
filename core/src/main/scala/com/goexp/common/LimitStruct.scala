package com.goexp.common

import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel

trait LimitStruct

class StructLoader(struct: LimitStruct) {
  val limitDataFields = struct.getClass.getDeclaredFields
    .filter { f =>
      f.setAccessible(true)
      classOf[LimitData].isAssignableFrom(f.getType)
    }


  def size() = {

    limitDataFields.to(LazyList)
      .map {
        _.get(struct).asInstanceOf[LimitData].size()
      }
      .sum

  }

  def loadFrom(buffer: ByteBuffer): Unit = {
    limitDataFields.foreach {
      _.get(struct).asInstanceOf[LimitData].load(buffer)
    }
  }

  def loadFrom(channel: SeekableByteChannel): Unit = {
    limitDataFields.foreach {
      _.get(struct).asInstanceOf[LimitData].load(channel)
    }
  }
}

object StructLoader {
  def apply(struct: LimitStruct): StructLoader = new StructLoader(struct)
}

