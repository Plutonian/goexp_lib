package com.goexp.pipeline.handler

private[pipeline]
trait Actor {
  protected type Rec = PartialFunction[Any, Unit]

  def receive: Rec
}
