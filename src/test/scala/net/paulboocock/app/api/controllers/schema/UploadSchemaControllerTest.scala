package net.paulboocock.app.api.controllers.schema

import net.paulboocock.app.Utils
import org.json4s.jackson.Serialization.write
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.test.scalatest._

class UploadSchemaControllerTest extends ScalatraFunSuite {

  implicit lazy val jsonFormats: Formats = DefaultFormats

  addServlet(classOf[SchemaController], "/schema/*")

  test("POST /schema/config-schema with a valid JSON Schema should return status 201") {
    post("/schema/config-schema", Utils.loadFile("config-schema.json") -> "") {
      status should equal (201)
      body should equal (write(SchemaResponse("uploadSchema", "config-schema", "success")))
    }
  }

  test("POST /schema/config-schema with no JSON Schema should return status 400") {
    post("/schema/config-schema", Seq.empty, Seq.empty) {
      status should equal (400)
      body should equal (write(SchemaResponse("uploadSchema", "config-schema", "error", Some("JSON Schema required"))))
    }
  }

  test("POST /schema/config-schema with invalid JSON should return status 400") {
    post("/schema/config-schema", Utils.loadFile("config-schema-invalid.json") -> "") {
      status should equal (400)
      body should equal (write(SchemaResponse("uploadSchema", "config-schema", "error", Some("Invalid JSON"))))
    }
  }

}