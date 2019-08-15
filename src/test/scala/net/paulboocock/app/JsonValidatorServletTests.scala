package net.paulboocock.app

import org.scalatra.test.scalatest._

class JsonValidatorServletTests extends ScalatraFunSuite {

  addServlet(classOf[JsonValidatorServlet], "/*")

  test("GET / on JsonValidatorServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
