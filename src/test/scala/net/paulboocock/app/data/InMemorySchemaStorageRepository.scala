package net.paulboocock.app.data

import org.json4s.JsonAST.JValue

import scala.collection.mutable

class InMemorySchemaStorageRepository extends KeyValueRepository[JValue] {
  var database: mutable.Map[String, JValue] = mutable.Map()

  override def set(key: String, value: JValue): Boolean = {
    database += key -> value
    true
  }

  override def get(key: String): Option[JValue] = {
    database.get(key)
  }
}
