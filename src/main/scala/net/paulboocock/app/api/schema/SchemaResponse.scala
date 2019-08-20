package net.paulboocock.app.api.schema

import net.paulboocock.app.api.utils.response.{BaseResponse, Status}
import net.paulboocock.app.api.utils.response.Status.Status

case class SchemaResponse(action: String, id:String, status:Status = Status.SUCCESS, message:Option[String] = None) extends BaseResponse
