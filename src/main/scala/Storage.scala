package we.MCC.storage


import scala.xml._

trait XMLSerializable {
  def toXML: NodeSeq
}