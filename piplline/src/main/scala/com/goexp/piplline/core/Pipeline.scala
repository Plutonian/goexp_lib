package com.goexp.piplline.core

import com.goexp.common.util.Logger
import com.goexp.piplline
import com.goexp.piplline.handler.HandlerConfig

import java.util.concurrent.{ExecutorService, Executors}
import scala.collection.mutable

class Pipeline extends MessageDriven with Logger {

  private val msgQueueProxy = new MessageQueueProxy[Message](1000)


  private val dispatchExecutor = Executors.newSingleThreadExecutor

  private val configs = mutable.Set[HandlerConfig]()


  def registry(config: HandlerConfig): Pipeline = {
    configs.add(config)
    this
  }


  private def registry(handler: MessageHandler, executor: ExecutorService): Pipeline = {
    configs.add(piplline.handler.HandlerConfig(handler, executor))
    this
  }

  private def registry(handler: MessageHandler, threadCount: Int): Pipeline = {
    configs.add(piplline.handler.HandlerConfig(handler, Executors.newFixedThreadPool(threadCount)))
    this
  }


  def regForCPUType(handler: MessageHandler): Pipeline = {
    registry(handler, Runtime.getRuntime.availableProcessors)
  }

  def regForCPUType(handler: MessageHandler, threadCount: Int): Pipeline = {
    registry(handler, threadCount)
  }

  def regForCPUType(handler: MessageHandler, executor: ExecutorService): Pipeline = {
    registry(handler, executor)
  }

  def regForIOType(handler: MessageHandler): Pipeline = {
    registry(handler, 30)
  }

  def regForIOType(handler: MessageHandler, threadCount: Int): Pipeline = {
    registry(handler, threadCount)
  }

  def regForIOType(handler: MessageHandler, executor: ExecutorService): Pipeline = {
    registry(handler, executor)
  }


  def regGroup(group: Set[HandlerConfig]): Pipeline = {
    configs.addAll(group)
    this
  }

  def start() = {
    //fill queue
    this.queue = msgQueueProxy

    val mesTypeMap = configs.to(LazyList)
      .map { c =>
        c.handler.queue = msgQueueProxy
        c
      }
      .groupBy {
        _.handler.getClass()
      }
    //start message driven
    dispatchExecutor.execute { () =>
      var running = true
      while ( {
        running
      }) try {
        val msg = msgQueueProxy.poll()
        if (msg != null) {

          msg match {
            case _: Shutdown =>
              running = false

              stop()
            case userMsg@UserMessage(target, _) =>
              mesTypeMap.get(target) match {
                case Some(configs) =>
                  for (c <- configs) {
                    //exec actor
                    c.executor.execute { () =>
                      val handler = c.handler
                      try {
                        handler.process(userMsg)
                      }
                      catch {
                        case e: Exception =>
                          logger.error(s"Common exception catch => From:$handler", e)
                      }
                    }
                  }
                case None =>
                  logger.error(s"No message handler for: ${target}")
              }
            case _ =>
              logger.error("Unknown Message catch!System shutdown!")
              running = false

              stop()
          }

        }
        else {
          logger.info("listener task time out!!!")
          running = false

          stop()
        }
      } catch {
        case e: InterruptedException =>
          e.printStackTrace()
          running = false
      }

    }

    logger.info("Pipeline system start")


    this
  }

  def stop() = {
    //wait for all ok
    for (config <- configs) {
      config.executor.shutdown()
    }

    dispatchExecutor.shutdown()

    logger.info("Pipeline system shutdown")
  }
}