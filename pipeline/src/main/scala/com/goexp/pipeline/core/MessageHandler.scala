package com.goexp.pipeline.core

private[pipeline]
trait MessageHandler extends MessageDriven {
  def process(message: UserMessage): Unit
}