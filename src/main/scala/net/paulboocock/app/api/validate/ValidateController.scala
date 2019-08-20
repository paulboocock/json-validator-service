package net.paulboocock.app.api.validate

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.fge.jsonschema.main.JsonSchemaFactory
import net.paulboocock.app.api.utils.request.{JsonRequestParams, JsonRequestParser}
import net.paulboocock.app.api.utils.response.error.{ErrorCode, ErrorResponse}
import net.paulboocock.app.api.utils.response.{CustomFormats, Status}
import net.paulboocock.app.core.schema.JsonSchemaService
import net.paulboocock.app.core.utils.JsonUtils
import net.paulboocock.app.core.validate.JsonSchemaJsonValidateSchema
import org.scalatra._
import org.scalatra.json._

class ValidateController(jsonSchemaService: JsonSchemaService) extends ScalatraServlet with JacksonJsonSupport with CustomFormats {

  before() {
    contentType = formats("json")
  }

  notFound {
    ErrorResponse("notFound", Some("Route not found"), ErrorCode.NOT_FOUND)
  }

  get() {
    halt(404, body = ErrorResponse("notFound", Some("Route not found"), ErrorCode.NOT_FOUND))
  }

  post("/:schemaid") {
    val jsonRequestParams = JsonRequestParser.parseParams(params)

    val(schemaId, schema, json) = jsonRequestParams match {
      case JsonRequestParams(Some(schemaId), Some(json)) =>
        (
          schemaId,
          jsonSchemaService.getSchema(schemaId) getOrElse halt(
            404,
            body = ValidateResponse("validateDocument", schemaId, Status.ERROR, Some("Schema not found"))
          ),
          json
        )
      case JsonRequestParams(None, _) => halt(
        400,
        body = ErrorResponse("validateDocument", Some("SchemaID is required"), ErrorCode.SCHEMA_ID_REQUIRED)
      )
      case JsonRequestParams(Some(schemaId), None) => halt(
        400,
        body = ValidateResponse("validateDocument", schemaId, Status.ERROR, Some("JSON file required"))
      )
    }

    val jsonCleansed = try {
      JsonUtils.RemoveNullFields(parse(json))
    } catch {
      case _: JsonProcessingException => halt(
        400,
        body = ValidateResponse("validateDocument", schemaId, Status.ERROR, Some("Invalid JSON"))
      )
    }

    val report = JsonSchemaJsonValidateSchema.Validate(schema, jsonCleansed)

    if (report.isSuccess) {
      Ok(ValidateResponse("validateDocument", schemaId))
    } else {
      Ok(ValidateResponse("validateDocument", schemaId, Status.ERROR, Some(report.message)))
    }
  }
}