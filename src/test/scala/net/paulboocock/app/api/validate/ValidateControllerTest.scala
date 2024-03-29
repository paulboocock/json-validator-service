package net.paulboocock.app.api.validate

import net.paulboocock.app.Utils
import net.paulboocock.app.api.utils.response.{CustomFormats, Status}
import net.paulboocock.app.api.schema.{SchemaController, SchemaResponse}
import net.paulboocock.app.core.InMemorySchemaService
import org.json4s.jackson.JsonMethods
import org.json4s.jackson.Serialization.write
import org.json4s.{DefaultFormats, Formats}
import org.scalatest.BeforeAndAfter
import org.scalatra.test.scalatest._

class ValidateControllerTest extends ScalatraFunSuite with BeforeAndAfter with JsonMethods with CustomFormats {

  val configSchema: String = Utils.loadFile("config-schema.json")

  addServlet(new ValidateController(InMemorySchemaService), "/validate/*")

  before {
    InMemorySchemaService.addSchema("config-schema", parse(configSchema))
  }

  test("POST /validate/config-schema with no JSON should return status 400") {
    post("/validate/config-schema", Seq.empty, Seq.empty) {
      status should equal (400)
      body should equal (write(SchemaResponse("validateDocument", "config-schema", Status.ERROR, Some("JSON file required"))))
    }
  }

  test("POST /validate/config-schema with compliant JSON should return status 200") {
    post("/validate/config-schema", Utils.loadFile("config.json") -> "") {
      status should equal (200)
      body should equal (write(SchemaResponse("validateDocument", "config-schema")))
    }
  }

  test("POST /validate/config-schema with compliant JSON that contains null values which should be cleansed should return status 200") {
    post("/validate/config-schema", Utils.loadFile("config-with-nulls.json") -> "") {
      status should equal (200)
      body should equal (write(SchemaResponse("validateDocument", "config-schema")))
    }
  }

  test("POST /validate/config-schema with JSON that has field with wrong type should return status 400") {
    post("/validate/config-schema", Utils.loadFile("config-wrong-type.json") -> "") {
      status should equal (200)
      body should equal (write(SchemaResponse("validateDocument", "config-schema", Status.ERROR, Some("instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])"))))
    }
  }

  test("POST /validate/config-schema with JSON that has missing field should return status 400") {
    post("/validate/config-schema", Utils.loadFile("config-missing-required.json") -> "") {
      status should equal (200)
      body should equal (write(SchemaResponse("validateDocument", "config-schema", Status.ERROR, Some("object has missing required properties ([\"destination\"])"))))
    }
  }

  test("POST /validate/config-schema with invalid JSON should return status 400") {
    post("/validate/config-schema", Utils.loadFile("config-invalid.json") -> "") {
      status should equal (400)
      body should equal (write(SchemaResponse("validateDocument", "config-schema", Status.ERROR, Some("Invalid JSON"))))
    }
  }
}
