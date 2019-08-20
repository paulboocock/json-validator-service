package net.paulboocock.app.api.schema

import com.fasterxml.jackson.core.JsonProcessingException
import net.paulboocock.app.api.utils.request.{JsonRequestParams, JsonRequestParser}
import net.paulboocock.app.api.utils.response.error.{ErrorCode, ErrorResponse}
import net.paulboocock.app.api.utils.response.{CustomFormats, Status}
import net.paulboocock.app.core.schema.JsonSchemaService
import org.scalatra._
import org.scalatra.json.JacksonJsonSupport

class SchemaController(jsonSchemaService: JsonSchemaService) extends ScalatraServlet with JacksonJsonSupport with CustomFormats {

  before() {
    contentType = formats("json")
  }

  notFound {
    ErrorResponse("notFound", Some("Route not found"), ErrorCode.NOT_FOUND)
  }

  post("/:schemaid") {
    val jsonRequestParams = JsonRequestParser.parseParams(params)

    try {
      jsonRequestParams match {
        case JsonRequestParams(Some(schemaId), Some(json)) => jsonSchemaService.addSchema(schemaId, parse(json))
        case JsonRequestParams(None, _) => halt(
          400,
          body = ErrorResponse("uploadSchema", Some("SchemaID is required"), ErrorCode.SCHEMA_ID_REQUIRED)
        )
        case JsonRequestParams(Some(schemaId), None) => halt(
          400,
          body = SchemaResponse("uploadSchema", schemaId, Status.ERROR, Some("JSON Schema required"))
        )
      }
    } catch {
      case _: JsonProcessingException => halt(
        400,
        body = SchemaResponse("uploadSchema", jsonRequestParams.schemaId.head, Status.ERROR, Some("Invalid JSON"))
      )
    }

    Created(SchemaResponse("uploadSchema", jsonRequestParams.schemaId.head))
  }

  get("/:schemaid") {
    val jsonRequestParams = JsonRequestParser.parseParams(params)

    jsonRequestParams match {
      case JsonRequestParams(Some(schemaId), _) =>
        Ok(
          jsonSchemaService.getSchema(schemaId) getOrElse halt(
            404,
            body = SchemaResponse("downloadSchema", schemaId, Status.ERROR, Some("Schema not found"))
          )
        )
      case JsonRequestParams(None, _) => halt(
        400,
        body = ErrorResponse("downloadSchema", Some("SchemaID is required"), ErrorCode.SCHEMA_ID_REQUIRED)
      )
    }
  }
}