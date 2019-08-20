package net.paulboocock.app.core.validate

import org.json4s.JsonAST.JValue

trait JsonValidateService {
  def Validate(schema: JValue, jsonToValidate: JValue): JsonValidationReport
}
