package we.MCC.Math

import we.MCC.Vector3._

case class Radian(val ang: Double) {
  import scala.math.Pi
  def toGrad = Grad((ang * 180.0) / Pi)
}
case class Grad(val ang: Double) {
  import scala.math.Pi
  def toRadian = Radian((ang / 180.0) * Pi)
}

object Quaternion {
  def fromAngle(α: Radian, u: Vector3[Double]): Quaternion = {
    import scala.math._
    
    Quaternion(cos(α.ang / 2), u * sin(α.ang / 2))
  }
  
  def fromAngle(α: Grad, u: Vector3[Double]): Quaternion = {
    fromAngle(α.toRadian, u)
  }
  
}

case class Quaternion(s: Double, v: Vector3[Double]) {
  import scala.math._
  
  def *(that: Quaternion): Quaternion = {
    Quaternion(s * that.s - v.dot(that.v), (that.v * s) + (v * that.s) + (v cross that.v))
  }
  
  def norm = sqrt(pow(s, 2) + pow(v.x, 2) + pow(v.y, 2) + pow(v.z, 2))
  
  def conjugate = Quaternion(s, Vector3(-v.x, -v.y, -v.z))

  def rotate(v:Vector3[Double]) = {
    val rotV = this * Quaternion(0,v) * this.conjugate
    rotV.v
  }

  def rotate(q:Quaternion) = {
    this * q * this.conjugate
  }
}