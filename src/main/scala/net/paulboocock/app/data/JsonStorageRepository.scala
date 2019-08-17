package net.paulboocock.app.data

import com.redis.RedisClient
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods.{compact, parse}

class JsonStorageRepository(redisClient: RedisClient) extends KeyValueRepository[JValue] {

  def set(schemaId:String, json: JValue): Boolean = {
      redisClient.set(schemaId, compact(json))
  }

  def get(schemaId:String): Option[JValue] = {
    redisClient.get[String](schemaId) match {
      case Some(json) => Some(parse(json))
      case None => None
    }
  }
}