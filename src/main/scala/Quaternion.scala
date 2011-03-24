package we.MCC.Math

import we.MCC.Vector3._

case class Quaternion[A: Numeric](s: A, v: Vector3[A]) {
  val num = implicitly[Numeric[A]]

  private implicit def AtoRichA(x:A):RichA = new RichA(x)
  private class RichA(val x:A) {
    def +(that: A): A = num.plus(x,that)
    def -(that: A): A = num.minus(x,that)
    def unary_-(): A = num.minus(num.zero,x)
    def *(that: A): A = num.times(x,that)
  }

  def *(that: Quaternion[A]): Quaternion[A] = {
    Quaternion(s * that.s - v.dot(that.v), (that.v * s) + (v * that.s) + (v cross that.v))
  }

  /*def +(that: Quaternion[A]): Quaternion[A] = {
    Quaternion
  }*/
}