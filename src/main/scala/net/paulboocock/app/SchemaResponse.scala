package net.paulboocock.app

case class SchemaResponse(action: String, id:String, status:String, message:Option[String] = None)