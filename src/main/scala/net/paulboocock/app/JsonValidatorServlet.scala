package net.paulboocock.app

import org.scalatra._

class JsonValidatorServlet extends ScalatraServlet {

  get("/") {
    views.html.hello()
  }

}
