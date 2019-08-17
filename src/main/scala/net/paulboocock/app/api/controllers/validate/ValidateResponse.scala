package net.paulboocock.app.api.controllers.validate

case class ValidateResponse(action: String, id:String, status:String, message:Option[String] = None)
