package com.goexp.common.util.string

object Strings {
  @inline
  def isEmpty(str: String): Boolean = str == null || str.isEmpty

  @inline
  def isNotEmpty(str: String): Boolean = str != null && str.nonEmpty
}

object StringOption {
  @inline
  def apply(str: String): Option[String] = if (Strings.isEmpty(str)) None else Some(str)
}