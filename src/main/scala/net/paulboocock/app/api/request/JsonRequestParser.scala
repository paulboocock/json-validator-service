package net.paulboocock.app.api.request

import org.scalatra.Params

object JsonRequestParser {
  def parseParams(params: Params): JsonRequestParams = {
    params.to[List] match {
      case List(Tuple2(json, _),Tuple2("schemaid", schemaId)) => JsonRequestParams(Some(schemaId), Some(json))
      case List(Tuple2("schemaid", schemaId)) => JsonRequestParams(Some(schemaId), None)
      case _ => JsonRequestParams(None, None)
    }
  }
}
