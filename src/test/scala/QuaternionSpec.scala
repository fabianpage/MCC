package we.MCC.test

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Spec, FlatSpec}
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import _root_.we.MCC.Math.{
  Quaternion,
  Grad,
  Radian
}
import _root_.we.MCC.Vector3.Vector3
import scala.math._

class QuaternionSpec extends FlatSpec with ShouldMatchers {
  "A Quaternion" should "be a quaternion when multiplied by another" in {
    Quaternion(1,Vector3(1,0,0)) * Quaternion(1,Vector3(0,1,0)) should be(Quaternion(1,Vector3(1,1,1)))
  }
  it should "correctly multiply with another " in {
    Quaternion(1., Vector3(0,1.,0)) * Quaternion(1.0, Vector3(.5, .5, .75)) should be(Quaternion(.5, Vector3(1.25, 1.5, 0.25)))
  }
  it should "create a quaternion with norm = 1 from an angle and a vector" in {
    val angle = Grad(90)
    val unit = Vector3(1.0, 0, 0)
    val vector = Vector3(0, 1.0, 0)
    
    val q = Quaternion.fromAngle(angle, unit)
    q.s should be (1./sqrt(2.) plusOrMinus (0.0000001))
    q.norm should be (1.0 plusOrMinus (Double.Epsilon))
    q.v should be (unit * (1./sqrt(2.)))
    
    val result = q * Quaternion(0, vector) * q.conjugate
    result.s   should be(0. plusOrMinus 0.0000000001)
    result.v.x should be(0. plusOrMinus 0.0000000001)
    result.v.y should be(0. plusOrMinus 0.0000000001)
    result.v.z should be(1. plusOrMinus 0.0000000001)
    
    result.norm should be (1.0 plusOrMinus (Double.Epsilon))
  }
  
  "Radian" should "convert correctly to grad" in {
    val r = Radian(scala.math.Pi)
    r.toGrad should be(Grad(180.))
  }
  
  "Grad" should "convert correctly to radian" in {
    val g = Grad(180.0)
    g.toRadian should be (Radian(scala.math.Pi))
  }
}