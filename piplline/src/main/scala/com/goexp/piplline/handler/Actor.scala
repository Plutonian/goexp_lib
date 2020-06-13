package com.goexp.piplline.handler

private[piplline]
trait Actor {
  protected type Rec = PartialFunction[Any, Unit]

  def receive: Rec
}
