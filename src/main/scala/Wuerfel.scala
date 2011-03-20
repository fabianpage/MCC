package we.MCC.wuerfel

import org.lwjgl.LWJGLException
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11
import we.MCC.Vec._

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
    GL11.glOrtho(0, 10, 10, 0, 10, -10)
    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    GL11.glTranslatef(5, 5, 5)
    GL11.glRotatef(-10, 1, 1, 1)
    GL11.glEnable(GL11.GL_DEPTH_TEST)
    GL11.glDepthFunc(GL11.GL_LEQUAL)
    GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT,GL11.GL_NICEST)
    GL11.glDisable(GL11.GL_CULL_FACE)
    GL11.glEnable(GL11.GL_TEXTURE_2D)

    import MCC._
    import org.newdawn.slick.opengl.TextureLoader
    import java.io.FileInputStream

    val texture = TextureLoader.getTexture("JPG", new FileInputStream("src/main/resources/textureEis.jpg"))
    texture.bind

    while (Display.isActive) {
      // Clear the screen and depth buffer
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)

      Shapes.drawCube(Vector3D(0.0,0,0), 2, (_:Orientation) => (1,1,1))
      Shapes.drawCube(Vector3D(2.0,2,0), 2, (_:Orientation) => (1,1,1))

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
  type Vec = Vector3D[Double]
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
      val v1:Vector3D[Double] = orient match {
        case Left =>  Vector3D(0, 1, 1)
        case Right => Vector3D(0, 1, 1)
        case Front => Vector3D(1, 1, 0)
        case Back =>  Vector3D(1, 1, 0)
        case Up =>    Vector3D(1, 0, 1)
        case Down =>  Vector3D(1, 0, 1)
      }
      val v2 = v1 × orient.vect
      val v3 = v2 × orient.vect
      val v4 = v3 × orient.vect
      val vs = (v1 :: v2 :: v3 :: v4 :: Nil).map(_ * (size/2)).map(_ + center)
      val tcs = (0.5,0.5) :: (0.5,0.0) :: (.0,.0) :: (.0,0.5) :: Nil
      // draw quad
      GL11.glBegin(GL11.GL_QUADS)
      GL11.glColor3d(color._1, color._2, color._3)
      println("start:")
      for{(v,t) <- vs.zip(tcs)} {
        println(t)
        println(v)
        GL11.glTexCoord2d(t._1, t._2)
        GL11.glVertex3d(v.x, v.y, v.z)
      }
      GL11.glEnd()
    }
    def drawCube(center: Vec, size: Double, color: Orientation => Color) = {
      val fs = Left :: Right :: Up :: Down :: Front :: Back :: Nil
      for(f <- fs) {
        drawFace(center + (f.vect * (size / 2)), size, f, color(f))
      }
    }

  }

  def main(args: Array[String]) = {
    val w = new MCC
    w.start
  }
}