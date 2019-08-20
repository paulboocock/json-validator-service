package net.paulboocock.app.api.utils.response.error

object ErrorCode extends Enumeration {
  val SCHEMA_ID_REQUIRED: ErrorCode = Value(10001)
  val NOT_FOUND: ErrorCode = Value(10000)

  type ErrorCode = Value
}
