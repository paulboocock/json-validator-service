package net.paulboocock.app

import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

case class schemaResponse(action: String, id:String, status:String, message:Option[String] = None)

class JsonValidatorServlet extends ScalatraServlet with JacksonJsonSupport {

  var schemaDatabase:Map[String, String] = Map()

  before() {
    contentType = formats("json")
  }

  post("/schema/:schemaid") {
    val schemaId:String = params.getOrElse("schemaid", halt(400, body = "Schema ID required."))
    val schema = multiParams.keys.headOption getOrElse halt(400, body = "JSON Schema file required.")

    schemaDatabase += (schemaId -> schema)

    Created(schemaResponse("uploadSchema", schemaId, "success"))
  }

  get("/schema/:schemaid") {
    val schemaId:String = params.getOrElse("schemaid", halt(400))
    Ok(schemaDatabase getOrElse(schemaId, halt(404, body = s"$schemaId not found")))
  }

  post("/validate/:schemaid") {
    val schemaId:String = params.getOrElse("schemaid", halt(400))
    Ok(schemaResponse("validateDocument", schemaId, "success"))
  }

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
}