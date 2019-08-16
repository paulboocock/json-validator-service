package net.paulboocock.app

import org.scalatra.test.scalatest._

class ApiSchemaUploadTest extends ScalatraFunSuite {

  addServlet(classOf[JsonValidatorServlet], "/*")

  test("POST /schema/config-schema with a valid JSON Schema should return status 201") {
    post("/schema/config-schema", Utils.loadFile("config-schema.json") -> "") {
      status should equal (201)
    }
  }

  test("POST /schema/config-schema with no JSON Schema should return status 400") {
    post("/schema/config-schema") {
      status should equal (400)
    }
  }

  test("POST /schema/config-schema with invalid JSON should return status 400") {
    post("/schema/config-schema", Utils.loadFile("config-schema-invalid.json") -> "") {
      status should equal (400)
    }
  }

}
