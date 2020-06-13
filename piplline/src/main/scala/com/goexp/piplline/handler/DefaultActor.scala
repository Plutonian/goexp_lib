package com.goexp.piplline.handler

import com.goexp.common.util.Logger
import com.goexp.piplline.core.{Message, MessageHandler}

abstract class DefaultActor extends MessageHandler with Actor with Logger {
  override def process(message: Message): Unit = {
    handle(message)
  }

  def handle(msg: Message): Unit = {
    val defaultCase: Rec = {
      case x =>
        logger.error(s"No catch case!! Case:$x")
    }

    val func = receive orElse defaultCase
    func(msg.entity)
  }
}
