package net.paulboocock.app.api.controllers.schema

import com.fasterxml.jackson.core.JsonProcessingException
import net.paulboocock.app.api.{JsonRequestParams, JsonRequestParser}
import net.paulboocock.app.core.JsonSchemaService
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class SchemaController(jsonSchemaService: JsonSchemaService) extends ScalatraServlet with JacksonJsonSupport {

  before() {
    contentType = formats("json")
  }

  post("/:schemaid") {
    val jsonRequestParams = JsonRequestParser.parseParams(params)

    try {
      jsonRequestParams match {
        case JsonRequestParams(Some(schemaId), Some(json)) => jsonSchemaService.addSchema(schemaId, parse(json))
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
          jsonSchemaService.getSchema(schemaId) getOrElse halt(
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