package net.paulboocock.app.core

import net.paulboocock.app.data.KeyValueRepository
import org.json4s.JsonAST.JValue

trait JsonSchemaService extends {
  val repository: KeyValueRepository[JValue]

  def addSchema(schemaId: String, schema: JValue): Boolean = {
    repository.set(schemaId, schema)
  }

  def getSchema(schemaId: String): Option[JValue] = {
    repository.get(schemaId)
  }
}
