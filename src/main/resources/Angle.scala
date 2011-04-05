package

/**
 * Created by IntelliJ IDEA.
 * User: fabian
 * Date: 12.12.10
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */

trait Angle /*extends Numeric[Angle]*/ {
  def radian: Double
  def degree: Double
  def +(rhs: Angle) = Angle.radian(this.radian + rhs.radian)
  def -(rhs: Angle) = Angle.radian(this.radian - rhs.radian)
  def *(rhs: Double) = Angle.radian(this.radian * rhs)
  def /(rhs: Double) = Angle.radian(this.radian / rhs)
  override def toString = "" + degree + "°"

  // TODO violation of the hashCode/equals contract
  // TODO search for an nice solution
  //override val hashCode: Int = scala.math.floor(radian * 10000)
  override val hashCode: Int = 0
  // TODO a = b = c aber a != c möglich!
  override def equals(other: Any):Boolean = {
    other match {
      case that: Angle => {
        import scala.math._
        if(abs(abs(radian) - abs(that.radian)) < 0.0001) true else false
      }
      case _ => false
    }
  }
}

object Angle {
  def apply(r: Double) = {
    radian(r)
  }
  def radian(r: Double) = {
    new AngleImplRadian(r)
  }

  def degree(d: Double) = {
    new AngleImplDegree(d)
  }

  class AngleImplRadian(val radian:Double) extends Angle {
    lazy val degree = radian.toDegrees
  }
  class AngleImplDegree(val degree:Double) extends Angle {
    lazy val radian = degree.toRadians
  }
}
