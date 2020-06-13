package com.goexp.common.util

import com.typesafe.scalalogging.Logger

trait Logger {
  final val logger = Logger(this.getClass)
}
