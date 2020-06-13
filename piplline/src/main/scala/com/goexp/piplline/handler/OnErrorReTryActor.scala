package com.goexp.piplline.handler

import java.util.Objects._
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import com.goexp.piplline.core.{Message, Pipeline}
import com.goexp.piplline.handler.OnErrorReTryActor._

import scala.collection.concurrent.TrieMap

abstract class OnErrorReTryActor(private val retryTimes: Int) extends DefaultActor {
  require(retryTimes > 0, "times must > 0")

  private var waitTime: Int = _
  private var unit: TimeUnit = _

  def this(retryTimes: Int, waitTime: Int, unit: TimeUnit) = {
    this(retryTimes)

    requireNonNull(unit)

    this.waitTime = waitTime
    this.unit = unit
  }

  private def onError(message: Message): Unit = {
    val entity = message.entity

    val timesCounter = getCounter(entity)

    val errorTimes = timesCounter.incrementAndGet()
    if (errorTimes <= retryTimes) {

      logger.trace(s"[Retry times:${errorTimes}] Entry:${entity}")

      //sleep current thread
      if (waitTime > 0)
        unit.sleep(waitTime)

      sendTo(message.target, entity)
    } else {
      map.remove(entity)
      logger.warn(s"Out of retry times! Retry times:$retryTimes Entry:${entity} ")
    }
  }

  final override def process(message: Message): Unit = {
    try {
      super.process(message)
      logger.trace(s"Succ ${message.entity}")
      map.remove(message.entity)
    }
    catch {
      case e: Exception =>
        onError(message)
        throw e
    }
  }


}

private object OnErrorReTryActor {
  private val map = new TrieMap[Any, AtomicInteger]()

  private def getCounter(entity: Any) =

    map.get(entity) match {
      case None =>
        val counter = new AtomicInteger(0)
        map.put(entity, counter)
        counter
      case Some(counter) => counter
    }
}

private object Tester {
  def main(args: Array[String]): Unit = {
    val pipeline = new Pipeline()
      .regForCPUType(new TestActor)
      .start()

    Range(0, 10)
      .foreach {
        i =>
          pipeline.sendTo[TestActor](i)
      }


  }

  class TestActor extends OnErrorReTryActor(3, 1, TimeUnit.SECONDS) {

    var count_1: Int = _
    var count_3: Int = _


    override def receive = {
      case 1 =>
        count_1 += 1

        if (count_1 != 3)
          throw new Exception("Error")
      case 3 =>
        count_3 += 1

        if (count_3 != 5)
          throw new Exception("Error")
      case _ =>
    }
  }

}


