package net.paulboocock.app.api.controllers.validate

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.fge.jsonschema.main.JsonSchemaFactory
import net.paulboocock.app.api.{JsonRequestParams, JsonRequestParser}
import net.paulboocock.app.core.JsonSchemaService
import org.json4s.JsonAST.{JField, JNull}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class ValidateController(jsonSchemaService: JsonSchemaService) extends ScalatraServlet with JacksonJsonSupport {

  before() {
    contentType = formats("json")
  }

  post("/:schemaid") {
    val jsonRequestParams = JsonRequestParser.parseParams(params)

    val(schemaId, schema, json) = jsonRequestParams match {
      case JsonRequestParams(Some(schemaId), Some(json)) =>
        (
          schemaId,
          jsonSchemaService.getSchema(schemaId) getOrElse halt(
            404,
            body = ValidateResponse("validateDocument", schemaId, "error", Some("Schema not found"))
          ),
          json
        )
      case JsonRequestParams(None, _) => halt(
        400,
        body = ValidateResponse("validateDocument", "unknown", "error", Some("SchemaID is required"))
      )
      case JsonRequestParams(Some(schemaId), None) => halt(
        400,
        body = ValidateResponse("validateDocument", schemaId, "error", Some("JSON file required"))
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
        body = ValidateResponse("validateDocument", schemaId, "error", Some("Invalid JSON"))
      )
    }

    val factory = JsonSchemaFactory.byDefault()
    val jsonSchema = factory.getJsonSchema(asJsonNode(schema))
    val report = jsonSchema.validate(asJsonNode(jsonCleansed))

    if (report.isSuccess) {
      Ok(ValidateResponse("validateDocument", schemaId, "success"))
    } else {
      val messages = report.iterator()
      Ok(ValidateResponse(
        "validateDocument", schemaId, "error",
        messages.hasNext match { case true => Some(messages.next().getMessage) case false => None }
      ))
    }
  }

  notFound {
    ValidateResponse("notFound", "unknown", "error")
  }

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
}