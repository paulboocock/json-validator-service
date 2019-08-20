package net.paulboocock.app.api.utils.response.error

import net.paulboocock.app.api.utils.response.error.ErrorCode.ErrorCode
import net.paulboocock.app.api.utils.response.{BaseResponse, Status}
import net.paulboocock.app.api.utils.response.Status.Status

case class ErrorResponse(action: String, message: Option[String], errorCode: ErrorCode, status: Status = Status.ERROR) extends BaseResponse
