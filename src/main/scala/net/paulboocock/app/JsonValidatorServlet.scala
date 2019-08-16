package net.paulboocock.app

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import org.json4s.jackson.parseJson
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class JsonValidatorServlet extends ScalatraServlet with JacksonJsonSupport {

  var schemaDatabase:Map[String, JsonNode] = Map()

  before() {
    contentType = formats("json")
  }

  post("/schema/:schemaid") {
    val schemaId = params.getOrElse("schemaid", halt(400, body = SchemaResponse("uploadSchema", "unknown", "error", Some("Bad Request: SchemaID is required"))))
    val schema = multiParams.to[List] match {
      case List(a,_) => a._1
      case _ => halt(400, body = SchemaResponse("uploadSchema", schemaId, "error", Some("Bad Request: JSON Schema required")))
    }

    try {
      val jsonSchema = asJsonNode(parseJson(schema))
      schemaDatabase += (schemaId -> jsonSchema)
    } catch {
      case _: JsonProcessingException => halt(400, body = SchemaResponse("uploadSchema", schemaId, "error", Some("Bad Request: Supplied JSON Schema is not valid JSON")))
    }

    Created(SchemaResponse("uploadSchema", schemaId, "success"))
  }

  get("/schema/:schemaid") {
    val schemaId = params.getOrElse("schemaid", halt(400, body = SchemaResponse("downloadSchema", "unknown", "error", Some("Bad Request: SchemaID is required"))))
    Ok(schemaDatabase getOrElse(schemaId, halt(404, body = SchemaResponse("downloadSchema", schemaId, "error", Some("Not Found: Schema not found")))))
  }

  post("/validate/:schemaid") {
    val schemaId = params.getOrElse("schemaid", halt(400, body = SchemaResponse("validateDocument", "unknown", "error", Some("Bad Request: SchemaID is required"))))
    val schema = schemaDatabase getOrElse(schemaId, halt(404, body = SchemaResponse("validateDocument", schemaId, "error", Some("Not Found: Schema not found"))))
    val jsonToValidate = multiParams.to[List] match {
      case List(a,_) => a._1
      case _ => halt(400, body = SchemaResponse("validateDocument", schemaId, "error", Some("Bad Request: JSON file required")))
    }

    try {
      val json = parseJson(jsonToValidate)

      Ok(SchemaResponse("validateDocument", schemaId, "success"))
    } catch {
      case _: JsonProcessingException => halt(400, body = SchemaResponse("uploadSchema", schemaId, "error", Some("Bad Request: Supplied JSON is not valid")))
    }
  }

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
}