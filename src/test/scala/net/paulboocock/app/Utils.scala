package net.paulboocock.app

object Utils {
  def loadFile(filename: String): String = {
    val source = scala.io.Source.fromResource(filename)
    try source.mkString finally source.close()
  }
}
