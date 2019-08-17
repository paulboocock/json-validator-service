package net.paulboocock.app.data

trait KeyValueRepository[T] {
  def set(key:String, value:T): Boolean
  def get(key:String): Option[T]
}
