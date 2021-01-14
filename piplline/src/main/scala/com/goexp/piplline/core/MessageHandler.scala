package com.goexp.piplline.core

private[piplline]
trait MessageHandler extends MessageDriven {
  def process(message: UserMessage): Unit
}