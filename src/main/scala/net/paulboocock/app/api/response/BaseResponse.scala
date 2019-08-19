package net.paulboocock.app.api.response

import net.paulboocock.app.api.response.Status.Status

trait BaseResponse {
  def action: String
  def status: Status
  def message: Option[String]
}
