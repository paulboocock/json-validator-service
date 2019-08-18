package net.paulboocock.app.api.controllers.schema

import com.fasterxml.jackson.core.JsonProcessingException
import com.redis._
import net.paulboocock.app.api.{JsonRequestParams, JsonRequestParser}
import net.paulboocock.app.data.JsonStorageRepository
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class SchemaController extends ScalatraServlet with JacksonJsonSupport {

  var schemaStorage: JsonStorageRepository = _ //TODO: Move Repository out of Controller

  before() {
    contentType = formats("json")
    schemaStorage = new JsonStorageRepository(new RedisClient("redis", 6379))
  }

  post("/:schemaid") {
    val jsonRequestParams = JsonRequestParser.parseParams(params)

    try {
      jsonRequestParams match {
        case JsonRequestParams(Some(schemaId), Some(json)) => schemaStorage.set(schemaId, parse(json))
        case JsonRequestParams(None, _) => halt(
          400,
          body = SchemaResponse("uploadSchema", "unknown", "error", Some("SchemaID is required"))
        )
        case JsonRequestParams(Some(schemaId), None) => halt(
          400, body = SchemaResponse("uploadSchema", schemaId, "error", Some("JSON Schema required"))
        )
      }
    } catch {
      case _: JsonProcessingException => halt(
        400,
        body = SchemaResponse("uploadSchema", jsonRequestParams.schemaId.head, "error", Some("Invalid JSON"))
      )
    }

    Created(SchemaResponse("uploadSchema", jsonRequestParams.schemaId.head, "success"))
  }

  get("/:schemaid") {
    val jsonRequestParams = JsonRequestParser.parseParams(params)

    jsonRequestParams match {
      case JsonRequestParams(Some(schemaId), _) =>
        Ok(
          schemaStorage.get(schemaId) getOrElse halt(
            404,
            body = SchemaResponse("downloadSchema", schemaId, "error", Some("Schema not found"))
          )
        )
      case JsonRequestParams(None, _) => halt(
        400,
        body = SchemaResponse("downloadSchema", "unknown", "error", Some("SchemaID is required"))
      )
    }
  }

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
}