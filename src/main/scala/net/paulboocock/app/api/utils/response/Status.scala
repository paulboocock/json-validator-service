package net.paulboocock.app.api.utils.response

object Status extends Enumeration {
  type Status = Value

  val SUCCESS: Status = Value("success")
  val ERROR: Status = Value("error")
}
