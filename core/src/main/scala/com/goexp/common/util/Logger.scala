package com.goexp.common.util

import com.typesafe.scalalogging

trait Logger {
  final protected val logger = scalalogging.Logger(this.getClass)
}
