package net.paulboocock.app.api.schema

import net.paulboocock.app.api.response.{BaseResponse, Status}
import net.paulboocock.app.api.response.Status.Status

case class SchemaResponse(action: String, id:String, status:Status = Status.SUCCESS, message:Option[String] = None) extends BaseResponse
