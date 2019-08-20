package net.paulboocock.app.core.utils

import org.json4s.JsonAST.{JField, JNull, JValue}

object JsonUtils {
  def RemoveNullFields(json: JValue): JValue = {
    json removeField {
      case JField(_, JNull) => true
      case _ => false
    }
  }
}
