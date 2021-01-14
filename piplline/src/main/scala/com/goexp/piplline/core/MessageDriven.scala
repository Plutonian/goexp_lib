package com.goexp.piplline.core

import scala.reflect.ClassTag

trait MessageDriven {

  var queue: MessageQueueProxy[Message] = _

  def send(mes: Message): Unit = queue.offer(mes)

  def sendTo(target: Class[_ <: MessageHandler], entity: Any): Unit = queue.offer(UserMessage(target, entity))

  def sendTo[TARGET <: MessageHandler](entity: Any)(implicit target: ClassTag[TARGET]): Unit = queue.offer(UserMessage(target.runtimeClass.asInstanceOf[Class[MessageHandler]], entity))

  def sendShutdownMessage() = queue.offer(Shutdown())

}
