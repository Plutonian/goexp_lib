package com.goexp.db.mongo

import java.util.Objects

import com.mongodb.client.MongoCollection
import org.bson.Document

class DBOperator(
                  connStr: String,
                  database: String,
                  table: String
                ) extends DBTemplate(connStr, database, table) {


  def exec(callback: MongoCollection[Document] => Unit): Unit = {
    Objects.requireNonNull(callback)

    val db = mongoClient.getDatabase(database)
    callback(db.getCollection(table))
  }
}