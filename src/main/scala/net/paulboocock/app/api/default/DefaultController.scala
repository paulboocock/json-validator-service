package net.paulboocock.app.api.default

import net.paulboocock.app.api.response.CustomFormats
import net.paulboocock.app.api.response.error.{ErrorCode, ErrorResponse}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

class DefaultController extends ScalatraServlet with JacksonJsonSupport with CustomFormats {

  before() {
    contentType = formats("json")
  }

  notFound {
    ErrorResponse("notFound", Some("Route not found"), ErrorCode.NOT_FOUND)
  }
}
