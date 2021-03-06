package com.goexp.db.mongo

import java.util
import java.util.Objects

import org.bson.conversions.Bson

import scala.jdk.CollectionConverters._

object DBQuery {

  class Builder[T] private[DBQuery](private val connStr: String,
                                    private val database: String,
                                    private val table: String,
                                    private val creator: ObjectCreator[T]
                                   ) {
    private var defaultSort: Bson = _
    private var defaultSelect: Bson = _

    def defaultSort(defaultSort: Bson): Builder[T] = {
      this.defaultSort = defaultSort
      this
    }

    def defaultSelect(defaultSelect: Bson): Builder[T] = {
      this.defaultSelect = defaultSelect
      this
    }

    def build = new DBQuery[T](connStr, database, table, creator, defaultSelect, defaultSort)
  }

  def apply[T](connStr: String, database: String, table: String, defaultCreator: ObjectCreator[T]) = new Builder(connStr, database, table, defaultCreator)

  def apply[T](database: String, table: String, defaultCreator: ObjectCreator[T]) = new Builder(null, database, table, defaultCreator)

}

class DBQuery[T] private(connStr: String,
                         database: String,
                         table: String,
                         private val defaultCreator: ObjectCreator[T]
                        ) extends DBTemplate(connStr, database, table) {

  //init start

  require(defaultCreator != null, "defaultCreator can't be null")

  //init end

  private val collection = mongoClient.getDatabase(database).getCollection(table)
  private var defaultSort: Bson = _
  private var defaultSelect: Bson = _

  def this(
            connStr: String,
            database: String,
            table: String,
            defaultCreator: ObjectCreator[T],
            defaultSelect: Bson,
            defaultSort: Bson) = {
    this(connStr, database, table, defaultCreator)

    this.defaultSelect = defaultSelect
    this.defaultSort = defaultSort
  }

  private var where: Bson = _
  private var select: Bson = _
  private var sort: Bson = _

  def where(where: Bson): DBQuery[T] = {
    this.where = where
    this
  }

  def select(select: Bson): DBQuery[T] = {
    this.select = select
    this
  }

  def sort(sort: Bson): DBQuery[T] = {
    this.sort = sort
    this
  }

  private def buildFileIterrableMany = {
    collection.find()
      .filter(where)
      .projection(Option(defaultSelect).getOrElse(select))
      .sort(Option(defaultSort).getOrElse(sort))
  }

  private def buildFileIterrableOne = {
    collection.find()
      .filter(where)
      .limit(1)
  }

  private def getDocumentCount = {
    collection.countDocuments(where)
  }

  def set(userCreator: ObjectCreator[T] = defaultCreator) = {
    Objects.requireNonNull(userCreator)
    docs2Collection(userCreator.create _, new util.HashSet[T])
  }


  def list(userCreator: ObjectCreator[T] = defaultCreator) = {
    Objects.requireNonNull(userCreator)
    docs2Collection(userCreator.create _, new util.ArrayList[T])
  }


  def scalaList(userCreator: ObjectCreator[T] = defaultCreator) = list(userCreator).asScala


  def one(userCreator: ObjectCreator[T] = defaultCreator): Option[T] = {
    Objects.requireNonNull(userCreator)

    val iterable = buildFileIterrableOne.map(userCreator.create _)

    Option(iterable.first)
  }


  def exists: Boolean = getDocumentCount > 0


  private def docs2Collection[A <: util.Collection[T]](userCreator: ObjectCreator[T], clazz: A) =
    buildFileIterrableMany.map(userCreator.create _).into(clazz)

}