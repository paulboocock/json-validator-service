package net.paulboocock.app.api.validate

import net.paulboocock.app.api.response.{BaseResponse, Status}
import net.paulboocock.app.api.response.Status.Status

case class ValidateResponse(action: String, id:String, status:Status = Status.SUCCESS, message:Option[String] = None) extends BaseResponse

