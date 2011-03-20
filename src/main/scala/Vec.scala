package we.MCC.Vec;

case class Vec3D[A: Numeric]( x:A, y:A, z:A)/*(implicit numeric:Numeric[A])*/ {

  val numeric:Numeric[A] = implicitly[Numeric[A]]

  implicit def AtoRichA(x:A):RichA = new RichA(x)

  class RichA(val x:A) {
    def +(that: A): A = numeric.plus(x,that)
    def -(that: A): A = numeric.minus(x,that)
    def unary_-(): A = numeric.minus(numeric.zero,x)
    def *(that: A): A = numeric.times(x,that)
  }

  def crossProd = ×(_)
  // Mathematisches Symbol?
  def ×(that:Vec3D[A]): Vec3D[A] = {
    new Vec3D[A](
      (y * that.z) + (z * that.y),
      (z * that.x) + (x * that.z),
      (x * that.y) + (y * that.x)
    )
  }

  def ⋅(that: Vec3D[A]) = dotProduct(that)
  def dotProduct(that: Vec3D[A]): A = {
    (x * that.x) + (y * that.y) + (z *that.z)
  }

  def unary_- : Vec3D[A] = {
    new Vec3D[A](
      -x,
      -y,
      -z
    )
  }

  def ∗(f:A) = scalarProd(f)
  def scalarProd(f:A):Vec3D[A] = {
    new Vec3D[A](
      x * f,
      y * f,
      z * f
    )
  }
}

object VecMain {
  def main(args: Array[String]) = {
    def add[A](x: A, y: A)(implicit numeric: Numeric[A]): A = numeric.plus(x, y)
    implicit def VecFloatToVecDouble(f:Vec3D[Float]) = new Vec3D[Double](f.x,f.y,f.z)

    println(add(1,2))
    val v1 = new Vec3D(0,0,1.1)
    val v2: Vec3D[Float] = new Vec3D(0,1,0)
    println(v1.crossProd(v1))
    println(v1 × v1)
    println(v1.crossProd(v2))
    println(v1.×(v2))
    println(v1 × v2)
    println(v1.dotProduct(v2))
    println(v1 ∗ 3)
    println(v1 ∗ 3)
    println(v1 ⋅ v2)
    println(- v1)
    //println(v1 `+: v2)
  }
}