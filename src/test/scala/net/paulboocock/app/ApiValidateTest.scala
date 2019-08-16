package net.paulboocock.app

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.test.scalatest._
import org.json4s.jackson.Serialization.write
import org.json4s.jackson.parseJson

class ApiValidateTest extends ScalatraFunSuite {

  implicit lazy val jsonFormats: Formats = DefaultFormats

  val configSchema: String = Utils.loadFile("config-schema.json")

  addServlet(classOf[JsonValidatorServlet], "/*")

  test("POST /schema/config-schema with a valid JSON Schema should return status 201") {
    post("/schema/config-schema", configSchema -> "") {
      status should equal (201)
      body should equal (write(SchemaResponse("uploadSchema", "config-schema", "success")))
    }
  }

  test("GET /schema/config-schema should return status 200") {
    get("/schema/config-schema") {
      status should equal (200)
      parseJson(body) should equal (parseJson(configSchema))
    }
  }

  test("POST /validate/config-schema with compliant JSON should return status 200") {
    post("/validate/config-schema", Utils.loadFile("config.json") -> "") {
      status should equal (200)
      body should equal (write(SchemaResponse("validateDocument", "config-schema", "success")))
    }
  }
}
