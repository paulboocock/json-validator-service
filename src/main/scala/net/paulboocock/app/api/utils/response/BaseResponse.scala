package net.paulboocock.app.api.utils.response

import net.paulboocock.app.api.utils.response.Status.Status

trait BaseResponse {
  def action: String
  def status: Status
  def message: Option[String]
}
