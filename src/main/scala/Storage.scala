package we.MCC.storage


import scala.xml._

trait XMLSerializable {
  def toXML(useAs: String): NodeSeq
}