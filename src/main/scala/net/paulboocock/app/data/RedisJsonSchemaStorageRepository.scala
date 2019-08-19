package net.paulboocock.app.data

import com.redis.RedisClient
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods.{compact, parse}

class RedisJsonSchemaStorageRepository() extends KeyValueRepository[JValue] {

  def set(schemaId:String, json: JValue): Boolean = {
    val redisClient = new RedisClient("localhost", 6379)
    redisClient.set(schemaId, compact(json))
  }

  def get(schemaId:String): Option[JValue] = {
    val redisClient = new RedisClient("localhost", 6379)
    redisClient.get[String](schemaId) match {
      case Some(json) => Some(parse(json))
      case None => None
    }
  }
}