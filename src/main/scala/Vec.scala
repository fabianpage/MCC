package we.MCC.Vec;

object Vector3D {
  def apply[A: Numeric](x:A, y:A, z:A) = {
    new Vector3D(x,y,z)
  }
}

class Vector3D[A: Numeric] private ( val v:List[A]){

  val x = v.head
  val y = v.tail.head
  val z = v.tail.tail.head

  def this(xx:A, yy:A, zz:A) = {
    this(xx::yy::zz::Nil)
  }

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
    /*new Vec(
      op(x, that.x),
      op(y, that.y),
      op(z, that.z)
    )*/
    new Vector3D[A](v.zip(that.v).map(e => op(e._1,e._2)))
  }

  def scalarOp(op: (A,A) => A)(that:A):Vec = {
    vecOp(op)(new Vec(that,that,that))
  }



  def crossProd = ×(_)
  def ×(that:Vector3D[A]): Vector3D[A] = {
    new Vector3D[A](
      (y * that.z) - (z * that.y),
      (z * that.x) - (x * that.z),
      (x * that.y) - (y * that.x)
    )
  }

  def ⋅ = dotProduct(_)
  def dotProduct = vecOp(_ * _) _


  def unary_- = {
    val zero = new Vector3D[A](numeric.zero, numeric.zero, numeric.zero)
    zero - _
  }

  def * = scalarOp(_ * _) _

  def + = vecOp(_ + _) _

  def - = vecOp(_ + _) _
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