package com.goexp.common.cache

import java.lang.ref.SoftReference

import scala.collection.mutable


class SimpleCache[K, V]() {
  private val imageCache = mutable.Map[K, SoftReference[V]]()

  def get(key: K) = {
    if (imageCache.contains(key)) {
      Option(imageCache(key).get())
    } else {
      None
    }
  }

  def put(key: K, value: V) =
    imageCache.put(key, new SoftReference[V](value))

  def remove(key: K) = imageCache.remove(key)
}