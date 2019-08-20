package net.paulboocock.app.core.validate
import com.github.fge.jsonschema.main.JsonSchemaFactory
import org.json4s.JsonAST
import org.json4s.jackson.JsonMethods.asJsonNode

object JsonSchemaJsonValidateSchema extends JsonValidateService {
  override def Validate(schema: JsonAST.JValue, jsonToValidate: JsonAST.JValue): JsonValidationReport = {
    val factory: JsonSchemaFactory = JsonSchemaFactory.byDefault()
    val jsonSchema = factory.getJsonSchema(asJsonNode(schema))
    val report = jsonSchema.validate(asJsonNode(jsonToValidate))
    val messageIter = report.iterator()
    val firstMessage = if (messageIter.hasNext) { messageIter.next().getMessage } else { "" }
    JsonValidationReport(report.isSuccess, firstMessage)
  }
}
