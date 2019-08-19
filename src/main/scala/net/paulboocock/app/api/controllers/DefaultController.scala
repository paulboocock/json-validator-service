package net.paulboocock.app.api.controllers

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

class DefaultController extends ScalatraServlet with JacksonJsonSupport {

  before() {
    contentType = formats("json")
  }

  notFound {
    DefaultResponse("notFound", "unknown", "error")
  }

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
}
