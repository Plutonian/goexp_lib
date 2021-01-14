package com.goexp.piplline.core

private[piplline]
trait Message

private[piplline]
case class UserMessage(target: Class[_ <: MessageHandler], entity: Any) extends Message

private[piplline]
class SystemMessage extends Message

private[piplline]
case class Shutdown() extends SystemMessage