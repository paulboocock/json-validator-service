package net.paulboocock.app.core

import net.paulboocock.app.data.{InMemorySchemaStorageRepository, KeyValueRepository}
import org.json4s.JsonAST.JValue

object InMemorySchemaService extends JsonSchemaService {
  override val repository: KeyValueRepository[JValue] = new InMemorySchemaStorageRepository
}
