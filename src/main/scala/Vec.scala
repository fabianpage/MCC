package we.MCC.Vec;

case class Vector3D[A: Numeric]( x:A, y:A, z:A)/*(implicit numeric:Numeric[A])*/ {

  type Vec = Vector3D[A]
  val numeric:Numeric[A] = implicitly[Numeric[A]]

  implicit def AtoRichA(x:A):RichA = new RichA(x)

  class RichA(val x:A) {
    def +(that: A): A = numeric.plus(x,that)
    def -(that: A): A = numeric.minus(x,that)
    def unary_-(): A = numeric.minus(numeric.zero,x)
    def *(that: A): A = numeric.times(x,that)
  }

  def vecOp(op: (A,A) => A)(that:Vec): Vec = {
    new Vec(
      op(x, that.x),
      op(y, that.y),
      op(z, that.z)
    )
  }

  def scalarOp(op: (A,A) => A)(that:A):Vec = {
    vecOp(op)(new Vec(that,that,that))
  }

  //def plus(that:Vec):Vec = vecOp((_ + _)) _


  def crossProd = ×(_)
  // Mathematisches Symbol?
  def ×(that:Vector3D[A]): Vector3D[A] = {
    new Vector3D[A](
      (y * that.z) - (z * that.y),
      (z * that.x) - (x * that.z),
      (x * that.y) - (y * that.x)
    )
  }

  def ⋅ = dotProduct(_)
  //def ⋅(that: Vector3D[A]) = dotProduct(that)
  def dotProduct = vecOp(_ * _) _
  /*def dotProduct(that: Vector3D[A]): A = {
    (x * that.x) + (y * that.y) + (z *that.z)
  }*/

  def unary_- : Vector3D[A] = {
    new Vector3D[A](
      -x,
      -y,
      -z
    )
  }

  def * = scalarOp(_ * _) _
  /*def *(f:A) = scalarProd(f)
  def scalarProd(f:A):Vector3D[A] = {
    new Vector3D[A](
      x * f,
      y * f,
      z * f
    )
  }  */

  def + = vecOp(_ + _) _
  /*def +(that: Vector3D[A]) : Vector3D[A] = {
    Vector3D[A](
      x + that.x,
      y + that.y,
      z + that.z
    )
  }*/

  def - = vecOp(_ + _) _
  /*def -(that: Vector3D[A]) : Vector3D[A] = {
    Vector3D[A](
      x - that.x,
      y - that.y,
      z - that.z
    )
  }*/
}

object VecMain {
  def noMain(args: Array[String]) = {
    def add[A](x: A, y: A)(implicit numeric: Numeric[A]): A = numeric.plus(x, y)
    implicit def VecFloatToVecDouble(f:Vector3D[Float]) = new Vector3D[Double](f.x,f.y,f.z)

    println(add(1,2))
    val v1 = new Vector3D(0,0,1.1)
    val v2: Vector3D[Float] = new Vector3D(0,1,0)
    println(v1.crossProd(v1))
    println(v1 × v1)
    println(v1.crossProd(v2))
    println(v1.×(v2))
    println(v1 × v2)
    println(v1.dotProduct(v2))
    println(v1 * 3)
    println(v1 * 3)
    println(v1 ⋅ v2)
    println(- v1)
    //println(v1 `+: v2)
  }
}