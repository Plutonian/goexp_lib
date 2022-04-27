package com.goexp.pipeline.handler

import com.goexp.pipeline.core.MessageHandler

import java.util.concurrent.{ExecutorService, Executors}

case class HandlerConfig(handler: MessageHandler, executor: ExecutorService) {

  def this(handler: MessageHandler, threadCount: Int) =
    this(handler, Executors.newFixedThreadPool(threadCount))

}