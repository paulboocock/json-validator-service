package net.paulboocock.app.api.controllers.schema

case class SchemaResponse(action: String, id:String, status:String, message:Option[String] = None)
