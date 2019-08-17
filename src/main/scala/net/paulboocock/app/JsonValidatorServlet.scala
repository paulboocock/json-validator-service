package net.paulboocock.app

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.redis._
import org.json4s.JsonAST.{JField, JNull}
import org.json4s.jackson.parseJson
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class JsonValidatorServlet extends ScalatraServlet with JacksonJsonSupport {

  before() {
    contentType = formats("json")
  }

  post("/schema/:schemaid") {
    val redisClient = new RedisClient("localhost", 6379)
    val schemaId = params.getOrElse("schemaid", halt(400, body = SchemaResponse("uploadSchema", "unknown", "error", Some("SchemaID is required"))))
    val schema = multiParams.to[List] match {
      case List(a,_) => a._1
      case _ => halt(400, body = SchemaResponse("uploadSchema", schemaId, "error", Some("JSON Schema required")))
    }

    try {
      val jsonSchema = asJsonNode(parseJson(schema))
      redisClient.set(schemaId, jsonSchema)
    } catch {
      case _: JsonProcessingException => halt(400, body = SchemaResponse("uploadSchema", schemaId, "error", Some("Invalid JSON")))
    }

    Created(SchemaResponse("uploadSchema", schemaId, "success"))
  }

  get("/schema/:schemaid") {
    val redisClient = new RedisClient("localhost", 6379)
    val schemaId = params.getOrElse("schemaid", halt(400, body = SchemaResponse("downloadSchema", "unknown", "error", Some("SchemaID is required"))))
    Ok(redisClient.get[String](schemaId) getOrElse halt(404, body = SchemaResponse("downloadSchema", schemaId, "error", Some("Schema not found"))))
  }

  post("/validate/:schemaid") {
    val redisClient = new RedisClient("localhost", 6379)
    val schemaId = params.getOrElse("schemaid", halt(400, body = SchemaResponse("validateDocument", "unknown", "error", Some("SchemaID is required"))))
    val schema = redisClient.get[String](schemaId) getOrElse halt(404, body = SchemaResponse("downloadSchema", schemaId, "error", Some("Schema not found")))
    val jsonToValidate = multiParams.to[List] match {
      case List(a,_) => a._1
      case _ => halt(400, body = SchemaResponse("validateDocument", schemaId, "error", Some("JSON file required")))
    }

    try {
      val json = parseJson(jsonToValidate)
      val jsonCleansed = json removeField {
        case JField(_, JNull) => true
        case _ => false
      }
      val factory = JsonSchemaFactory.byDefault()
      val jsonSchema = factory.getJsonSchema(asJsonNode(parseJson(schema)))
      val report = jsonSchema.validate(asJsonNode(jsonCleansed))

      if (report.isSuccess) {
        Ok(SchemaResponse("validateDocument", schemaId, "success"))
      } else {
        val messages = report.iterator()
        Ok(SchemaResponse("validateDocument", schemaId, "error", messages.hasNext match { case true => Some(messages.next().getMessage) case false => None }))
      }
    } catch {
      case _: JsonProcessingException => halt(400, body = SchemaResponse("validateDocument", schemaId, "error", Some("Invalid JSON")))
    }
  }

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
}