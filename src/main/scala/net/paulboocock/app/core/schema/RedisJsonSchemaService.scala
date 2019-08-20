package net.paulboocock.app.core.schema

import net.paulboocock.app.data.{KeyValueRepository, RedisJsonSchemaStorageRepository}
import org.json4s.JsonAST.JValue

object RedisJsonSchemaService extends JsonSchemaService {
  override val repository: KeyValueRepository[JValue] = new RedisJsonSchemaStorageRepository()
}
