package we.MCC.test.Vector3D;

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Spec, FlatSpec}
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import _root_.we.MCC.Vector3.Vector3

class Vector3DSpec extends FlatSpec with ShouldMatchers {
  "A Vector" should "do a crossproduct" in {

    val v1 = Vector3(1,0,0)
    val v2 = Vector3(0,1,0)
    v1 × v2 should be(Vector3(0,0,1))

    v2 × v1 should be(Vector3(0,0,-1))

    v2 cross v1 should be(Vector3(0,0,-1))

    v1 + v2 should be(Vector3(1,1,0))
    v1 - v2 should be(Vector3(1,-1,0))

    val v3 = Vector3(1,2,3)

    v3 * 4 should be(Vector3(4,8,12))

    val v4 = Vector3(3,5,7)
    val v5 = Vector3(8,2,3)



  }
}