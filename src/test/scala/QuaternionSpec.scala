package we.MCC.test

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Spec, FlatSpec}
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import _root_.we.MCC.Math.Quaternion
import _root_.we.MCC.Vector3.Vector3

class QuaternionSpec extends FlatSpec with ShouldMatchers {
  "A Quaternion" should "do what a quaternion does" in {

    val q1 = Quaternion(1,Vector3(1,0,0))
    val q2 = Quaternion(1,Vector3(0,1,0))


    q1 * q2 should be(Quaternion(1,Vector3(1,1,1)))
  }
}