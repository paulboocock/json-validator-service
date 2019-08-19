package net.paulboocock.app.api.validate

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.fge.jsonschema.main.JsonSchemaFactory
import net.paulboocock.app.api.request.{JsonRequestParams, JsonRequestParser}
import net.paulboocock.app.api.response.error.{ErrorCode, ErrorResponse}
import net.paulboocock.app.api.response.{CustomFormats, Status}
import net.paulboocock.app.core.JsonSchemaService
import org.json4s.JsonAST.{JField, JNull}
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
      parse(json) removeField {
        case JField(_, JNull) => true
        case _ => false
      }
    } catch {
      case _: JsonProcessingException => halt(
        400,
        body = ValidateResponse("validateDocument", schemaId, Status.ERROR, Some("Invalid JSON"))
      )
    }

    val factory = JsonSchemaFactory.byDefault()
    val jsonSchema = factory.getJsonSchema(asJsonNode(schema))
    val report = jsonSchema.validate(asJsonNode(jsonCleansed))

    if (report.isSuccess) {
      Ok(ValidateResponse("validateDocument", schemaId))
    } else {
      val messages = report.iterator()
      Ok(ValidateResponse(
        "validateDocument", schemaId, Status.ERROR,
        (messages.hasNext match { case true => Some(messages.next().getMessage) case false => None })
      ))
    }
  }
}