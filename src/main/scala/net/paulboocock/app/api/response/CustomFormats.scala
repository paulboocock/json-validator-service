package net.paulboocock.app.api.response

import net.paulboocock.app.api.response.error.ErrorCode
import org.json4s.ext.{EnumNameSerializer, EnumSerializer}
import org.json4s.{DefaultFormats, Formats}

trait CustomFormats {
    protected implicit lazy val jsonFormats: Formats = DefaultFormats +
      new EnumNameSerializer(Status) +
      new EnumSerializer(ErrorCode)
}
