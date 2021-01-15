package com.goexp.db.mongo

import com.mongodb.client.{MongoClient, MongoClients}

import java.util.Objects

object DBTemplate {
  private var mongoClientCache: MongoClient = _
}

abstract class DBTemplate(private val connStr: String,
                          protected val database: String,
                          protected val table: String
                         ) {
  protected val mongoClient: MongoClient = {

    if (DBTemplate.mongoClientCache == null)
      if (connStr == null) {
        DBTemplate.mongoClientCache = MongoClients.create
      } else {
        DBTemplate.mongoClientCache = MongoClients.create(connStr)
      }

    DBTemplate.mongoClientCache
  }

  Objects.requireNonNull(database)
  Objects.requireNonNull(table)


}