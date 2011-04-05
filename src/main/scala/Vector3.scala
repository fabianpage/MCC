package we.MCC.Vector3;

object Vector3{
  implicit def VecIntToVecDouble(v: Vector3[Int]) : Vector3[Double] = Vector3[Double](v.x, v.y, v.z)
}

case class Vector3[A: Numeric] (val x:A, val y:A, val z:A){

  private type Vec = Vector3[A]
  private val numeric:Numeric[A] = implicitly[Numeric[A]]

  private implicit def AtoRichA(x:A):RichA = new RichA(x)
  private class RichA(val x:A) {
    def +(that: A): A = numeric.plus(x,that)
    def -(that: A): A = numeric.minus(x,that)
    def unary_-(): A = numeric.minus(numeric.zero,x)
    def *(that: A): A = numeric.times(x,that)
  }

  private def vecOp(op: (A,A) => A)(that:Vec): Vec = {
    new Vec(
      op(x, that.x),
      op(y, that.y),
      op(z, that.z)
    )
  }

  private def scalarOp(op: (A,A) => A)(that:A):Vec = {
    vecOp(op)(new Vec(that,that,that))
  }

  def cross = ×(_)
  def ×(that:Vector3[A]): Vector3[A] = {
    new Vector3[A](
      (y * that.z) - (z * that.y),
      (z * that.x) - (x * that.z),
      (x * that.y) - (y * that.x)
    )
  }

  def ⋅ = dot(_)
  def dot(that:Vector3[A]) = (x * that.x) + (y * that.y) + (z * that.z)

  private lazy val zero = new Vector3[A](numeric.zero, numeric.zero, numeric.zero)
  def unary_- = {
    zero - _
  }

  def * = scalarOp(_ * _) _

  def + = vecOp(_ + _) _

  def - = vecOp(_ - _) _
}

