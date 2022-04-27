package com.goexp.pipeline.core

private[pipeline]
trait Message

private[pipeline]
case class UserMessage(target: Class[_ <: MessageHandler], entity: Any) extends Message

private[pipeline]
class SystemMessage extends Message

private[pipeline]
case class Shutdown() extends SystemMessage