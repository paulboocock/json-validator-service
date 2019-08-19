package net.paulboocock.app.api.controllers.schema

import net.paulboocock.app.Utils
import net.paulboocock.app.core.InMemorySchemaService
import org.json4s.jackson.Serialization.write
import org.json4s.jackson.parseJson
import org.json4s.{DefaultFormats, Formats}
import org.scalatest.BeforeAndAfter
import org.scalatra.test.scalatest._

class DownloadSchemaControllerTest extends ScalatraFunSuite with BeforeAndAfter {

  implicit lazy val jsonFormats: Formats = DefaultFormats

  val configSchema: String = Utils.loadFile("config-schema.json")

  addServlet(new SchemaController(InMemorySchemaService), "/schema/*")

  before {
    post("/schema/config-schema", configSchema -> "")()
  }

  test("GET /schema/config-schema should return the JSON Schema and status 200") {
    get("/schema/config-schema") {
      status should equal (200)
      parseJson(body) should equal (parseJson(configSchema))
    }
  }

  test("GET /schema/unknown should return status 404") {
    get("/schema/unknown") {
      status should equal (404)
      body should equal (write(SchemaResponse("downloadSchema", "unknown", "error", Some("Schema not found"))))
    }
  }
}
