package com.goexp.pipeline.core

import com.goexp.pipeline.core.MessageQueueProxy._

import java.util.concurrent.{ArrayBlockingQueue, TimeUnit}

private[pipeline]
class MessageQueueProxy[T <: Message](val capacity: Int) {
  private val msgQueue = new ArrayBlockingQueue[T](capacity)

  def offer(o: T): Boolean =
    msgQueue.offer(o, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT_UNIT)


  def offer(o: T, timeout: Long, unit: TimeUnit): Boolean =
    msgQueue.offer(o, timeout, unit)

  def poll(): T =
    msgQueue.poll(DEFAULT_TIMEOUT, DEFAULT_TIMEOUT_UNIT)

  def poll(timeout: Long, unit: TimeUnit): T =
    msgQueue.poll(timeout, unit)
}

private object MessageQueueProxy {
  private val DEFAULT_TIMEOUT = 60
  private val DEFAULT_TIMEOUT_UNIT = TimeUnit.MINUTES
}