package net.paulboocock.app.core

import net.paulboocock.app.data.RedisJsonSchemaStorageRepository
import net.paulboocock.app.data.KeyValueRepository
import org.json4s.JsonAST.JValue

object RedisJsonSchemaService extends JsonSchemaService {
  override val repository: KeyValueRepository[JValue] = new RedisJsonSchemaStorageRepository()
}
