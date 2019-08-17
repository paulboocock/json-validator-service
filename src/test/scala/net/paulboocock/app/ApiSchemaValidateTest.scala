package net.paulboocock.app

import org.json4s.jackson.Serialization.write
import org.json4s.jackson.parseJson
import org.json4s.{DefaultFormats, Formats}
import org.scalatest.BeforeAndAfter
import org.scalatra.test.scalatest._

class ApiSchemaValidateTest extends ScalatraFunSuite with BeforeAndAfter {

  implicit lazy val jsonFormats: Formats = DefaultFormats

  val configSchema: String = Utils.loadFile("config-schema.json")

  addServlet(classOf[JsonValidatorServlet], "/*")

  before {
    post("/schema/config-schema", configSchema -> "")()
  }

  test("POST /validate/config-schema with compliant JSON should return status 200") {
    post("/validate/config-schema", Utils.loadFile("config.json") -> "") {
      status should equal (200)
      body should equal (write(SchemaResponse("validateDocument", "config-schema", "success")))
    }
  }

  test("POST /validate/config-schema with compliant JSON that contains null values which should be cleansed should return status 200") {
    post("/validate/config-schema", Utils.loadFile("config-with-nulls.json") -> "") {
      status should equal (200)
      body should equal (write(SchemaResponse("validateDocument", "config-schema", "success")))
    }
  }

  test("POST /validate/config-schema with JSON that has field with wrong type should return status 400") {
    post("/validate/config-schema", Utils.loadFile("config-wrong-type.json") -> "") {
      status should equal (200)
      body should equal (write(SchemaResponse("validateDocument", "config-schema", "error", Some("instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])"))))
    }
  }

  test("POST /validate/config-schema with JSON that has missing field should return status 400") {
    post("/validate/config-schema", Utils.loadFile("config-missing-required.json") -> "") {
      status should equal (200)
      body should equal (write(SchemaResponse("validateDocument", "config-schema", "error", Some("object has missing required properties ([\"destination\"])"))))
    }
  }

  test("POST /validate/config-schema with invalid JSON should return status 400") {
    post("/validate/config-schema", Utils.loadFile("config-invalid.json") -> "") {
      status should equal (400)
      body should equal (write(SchemaResponse("validateDocument", "config-schema", "error", Some("Invalid JSON"))))
    }
  }
}
