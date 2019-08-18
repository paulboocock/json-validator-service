package net.paulboocock.app.api.controllers.validate

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.redis._
import net.paulboocock.app.api.{JsonRequestParams, JsonRequestParser}
import net.paulboocock.app.data.JsonStorageRepository
import org.json4s.JsonAST.{JField, JNull, JValue}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class ValidateController extends ScalatraServlet with JacksonJsonSupport {

  var schemaStorage: JsonStorageRepository = _ //TODO: Move Repository out of Controller

  before() {
    contentType = formats("json")
    schemaStorage = new JsonStorageRepository(new RedisClient("redis", 6379))
  }

  post("/:schemaid") {
    val jsonRequestParams = JsonRequestParser.parseParams(params)

    val(schemaId, schema, json) = jsonRequestParams match {
      case JsonRequestParams(Some(schemaId), Some(json)) =>
        (
          schemaId,
          schemaStorage.get(schemaId) getOrElse halt(
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

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
}