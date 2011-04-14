package we.MCC.wuerfel

import org.lwjgl.LWJGLException
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11
import we.MCC.Vector3._
import we.MCC.Math._
import we.MCC.wuerfel.MCC._

import org.newdawn.slick.opengl.{
  TextureLoader,
  Texture
}
import java.io.FileInputStream

class MCC {
  def start = {
    try {
      Display.setDisplayMode(new DisplayMode(800, 600))
      Display.create()
    } catch {
      case e => {
        e.printStackTrace()
        System.exit(0)
      }
    }
    // init OpenGL
    GL11.glMatrixMode(GL11.GL_PROJECTION)
    GL11.glLoadIdentity()
    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    GL11.glEnable(GL11.GL_DEPTH_TEST)
    GL11.glDepthFunc(GL11.GL_LEQUAL)
    GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT,GL11.GL_NICEST)
    GL11.glDisable(GL11.GL_CULL_FACE)
    GL11.glEnable(GL11.GL_TEXTURE_2D)
    
    GL11.glViewport(0,0, 800, 600)




    lazy val texture: Map[Orientation, Option[Texture]] = Map(
      Left ->  Some(TextureLoader.getTexture("JPG", new FileInputStream("src/main/resources/textureEis.jpg"))),
      Right -> Some(TextureLoader.getTexture("JPG", new FileInputStream("src/main/resources/textureZwei.jpg"))),
      Up ->    Some(TextureLoader.getTexture("JPG", new FileInputStream("src/main/resources/textureDrei.jpg"))),
      Down ->  Some(TextureLoader.getTexture("JPG", new FileInputStream("src/main/resources/textureVier.jpg"))),
      Front -> Some(TextureLoader.getTexture("JPG", new FileInputStream("src/main/resources/textureFuef.jpg"))),
      Back ->  Some(TextureLoader.getTexture("JPG", new FileInputStream("src/main/resources/textureSechs.jpg")))
    )

    val color: Map[Orientation, Color] = Map(
      Left ->  (1,1,0),
      Right -> (1,0,1),
      Up ->    (1,1,1),
      Down ->  (0,1,1),
      Front -> (1,1,1),
      Back ->  (1,1,1)
    )

    val grid = new Grid(
          GridEntry(Vector3(0,0,0),GreenCube) ::
          GridEntry(Vector3(1,0,0),RedCube) ::
          GridEntry(Vector3(0,1,-1),BlueCube) ::
          Nil
      ,1.0)

    def glRotated(a: Double, b: Double, c: Double, d: Double) = GL11.glRotatef(a.toFloat, b.toFloat, c.toFloat, d.toFloat)

    var angle = 0.1
    val step = 0.5

    while (Display.isActive) {

      // Clear the screen and depth buffer
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)
      GL11.glBindTexture(GL11.GL_TEXTURE_2D,0)

      GL11.glLoadIdentity
      GL11.glOrtho(0, 10, 10, 0, 10, -10)
      GL11.glTranslatef(5, 5, 5)
      GL11.glRotatef(-10, 1, 1, 1)

      angle += step
      glRotated(angle, 0,1,0)

      //Shapes.drawCube(Vector3(0.0,0,0), 2, color(_), texture(_))
      //Shapes.drawCube(Vector3(2.0,2,0), 2, color(_), texture(_))
      grid.draw

      Display.update()
    }

    Display.destroy()
  }
}

object MCC {

  val r = new scala.util.Random
    trait Drawable {
        def draw: Unit
    }

  // Should be some kind of a mathematical Vector
  type Color = (Double, Double, Double)
  type Vec = Vector3[Double]
  sealed abstract class Orientation {
      val vect: Vec
  }
  case object Left extends Orientation  { val vect: Vec = new Vec(-1, 0, 0) }
  case object Right extends Orientation { val vect: Vec = new Vec(+1, 0, 0) }
  case object Up extends Orientation    { val vect: Vec = new Vec( 0,+1, 0) }
  case object Down extends Orientation  { val vect: Vec = new Vec( 0,-1, 0) }
  case object Front extends Orientation { val vect: Vec = new Vec( 0, 0,+1) }
  case object Back extends Orientation  { val vect: Vec = new Vec( 0, 0,-1) }

  object Shapes {
    def drawFace(center: Vec, size: Double, orient: Orientation, color: Color = (r.nextDouble, r.nextDouble, r.nextDouble)) = {
      import _root_.we.MCC.Vector3.Vector3
      val v1:Vector3[Double] = orient match {
        case Left =>  Vector3(0, 1, 1)
        case Right => Vector3(0, 1, 1)
        case Front => Vector3(1, 1, 0)
        case Back =>  Vector3(1, 1, 0)
        case Up =>    Vector3(1, 0, 1)
        case Down =>  Vector3(1, 0, 1)
      }
      val v2 = v1 × orient.vect
      val v3 = v2 × orient.vect
      val v4 = v3 × orient.vect
      val vs = (v1 :: v2 :: v3 :: v4 :: Nil).map(_ * (size/2)).map(_ + center)
      val tcs = (1.0,1.0) :: (1.0,0.0) :: (.0,.0) :: (.0,1.0) :: Nil
      // draw quad
      GL11.glBegin(GL11.GL_QUADS)
      GL11.glColor3d(color._1, color._2, color._3)
      for{(v,t) <- vs.zip(tcs)} {
        GL11.glTexCoord2d(t._1, t._2)
        GL11.glVertex3d(v.x, v.y, v.z)
      }
      GL11.glEnd()
    }
    def drawCube(center: Vec, size: Double, color: Orientation => Color, texture: Orientation => Option[Texture]) = {
      val fs = Left :: Right :: Up :: Down :: Front :: Back :: Nil
      for(f <- fs) {
        texture(f).map(_.bind)
        drawFace(center + (f.vect * (size / 2)), size, f, color(f))
      }
    }

  }

  def main(args: Array[String]) = {
    val w = new MCC
    w.start
  }
}
import scala.xml._
import we.MCC.storage._




class CubeType(val color: Orientation => Color, val texture: Orientation => Option[Texture]) /*extends XMLSerializable*/ {
  def toXML: NodeSeq = //<cubeType>{name}</cubeType>
}
case object GreenCube extends CubeType((_) => (0.,1.,0.), (_) => None)
case object RedCube extends CubeType((_) => (1.,0.,0.), (_) => None)
case object BlueCube extends CubeType((_) => (0.,0.,1.), (_) => None)


case class GridEntry(location: Vector3[Int], cubeType:CubeType) /*extends XMLSerializable */{
  //def toXML: NodeSeq = <cube>{location.toXML ++ cubeType.toXML}</cube>
}

class Grid(val entries: List[GridEntry], val spacing:Double) extends XMLSerializable{
  def draw = {
    for( GridEntry(l,t) <- entries) {
      val dl:Vector3[Double] = l
      Shapes.drawCube(dl * spacing, spacing, t.color, t.texture)
    }
  }

  def toXML = {
    val cubeTypes:IndexedSeq[CubeType] = entries.map(_.cubeType).distinct
    <grid>
      <cubes>
        {entries.map(entryToXml(_))}
      </cubes>
      <cubeTypes>
        {cubeTypes.map()
      </cubeTypes>
    </grid>
  }

  def entryToXml(entry: GridEntry) = {
    <cube>
      <location>
        {entry.location.toXML}
      </location>
      <cubeType>
        // TODO add Code here
      </cubeType>
    </cube>
  }





}