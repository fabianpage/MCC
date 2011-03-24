package we.MCC.test.Vector3D;

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Spec, FlatSpec}
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import _root_.we.MCC.Vector3.Vector3
import org.scalacheck.Prop.forAll
import org.scalatest.prop.Checkers

import org.scalacheck._
import Gen._
import Arbitrary.arbitrary

class Vector3DSpec extends FlatSpec with ShouldMatchers with Checkers {
  val v1 = Vector3(1,0,0)
  val v2 = Vector3(0,1,0)
  val e = scala.Double.Epsilon
  val genVector3Int = {
    for {
      x <- arbitrary[Int]
      y <- arbitrary[Int]
      z <- arbitrary[Int]
      } yield Vector3(x, y, z)
  }
  
  val genVector3Double = {
    for {
      x <- arbitrary[Double]
      y <- arbitrary[Double]
      z <- arbitrary[Double]
      } yield Vector3(x, y, z)
  }
      
  "A Vector" should "do a crossproduct" in {
    v1 × v2 should be(Vector3(0,0,1))
    v2 × v1 should be(Vector3(0,0,-1))
    v2 cross v1 should be(Vector3(0,0,-1))
  }
  
  it should "do an addition" in {
    v1 + v2 should be(Vector3(1,1,0))
  }
  
  it should "do a substraction" in {
    v1 - v2 should be(Vector3(1,-1,0))
  }
  
  it should "do a dot product" in {
    (v1 dot v2) should be(0)
    
    Vector3(1,2,3) * 4 should be(Vector3(4,8,12))

    Vector3(3,5,7) ⋅ Vector3(8,2,3) should be(8*3+5*2+7*3)

    Vector3(1,0,0) dot Vector3(0,1,0) should be(0)
    
    check(forAll(genVector3Int, genVector3Int) {
      (v1: Vector3[Int], v2: Vector3[Int]) => (v1.dot(v2) == (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z)) 
    })
    
/*    check(forAll(genVector3Double, genVector3Double) {
      (v1: Vector3[Double], v2: Vector3[Double]) => {
        val l = v1.dot(v2)
        val r = ((v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z))
        
        scala.math.abs(l - r) < e
      }
    })
*/
  }
}