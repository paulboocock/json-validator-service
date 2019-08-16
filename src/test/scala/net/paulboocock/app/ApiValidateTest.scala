package net.paulboocock.app

import org.scalatra.test.scalatest._

class ApiValidateTest extends ScalatraFunSuite {

  addServlet(classOf[JsonValidatorServlet], "/*")

  test("POST /schema/config-schema with a valid JSON Schema should return status 201") {
    post("/schema/config-schema", Utils.loadFile("config-schema.json") -> "") {
      status should equal (201)
    }
  }

  test("GET /schema/config-schema should return status 200") {
    get("/schema/config-schema") {
      status should equal (200)
    }
  }

  test("POST /validate/config-schema with compliant JSON should return status 200") {
    post("/validate/config-schema", Utils.loadFile("config.json") -> "") {
      status should equal (200)
    }
  }
}
