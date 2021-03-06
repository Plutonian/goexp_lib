package com.goexp.db.mongo

import org.bson.Document

@FunctionalInterface trait ObjectCreator[T] {
  def create(doc: Document): T
}