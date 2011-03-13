package we.MCC.wuerfel

import org.lwjgl.LWJGLException
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11

class Wuerfel {
  def start = {
    try {
      Display.setDisplayMode(new DisplayMode(800, 600));
      Display.create();
    } catch {
      case e => {
        e.printStackTrace();
        System.exit(0);
      }
    }
    // init OpenGL
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GL11.glOrtho(0, 10, 10, 0, 10, -10);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glTranslatef(5, 5, 0)
    GL11.glRotatef(-10, 1, 1, 1)
    GL11.glEnable(GL11.GL_DEPTH_TEST)
    GL11.glDepthFunc(GL11.GL_LEQUAL)
    GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT,GL11.GL_NICEST)

    import Wuerfel._
    /*val w = List(Face((0,0,0), 0, Left, (1,0,1)),
                 Face((0,0,0), 0, Right, (1,0,1)),
                 Face((0,0,0), 0, Up, (1,0,1)),
                 Face((0,0,0), 0, Down, (1,0,1)),
                 Face((0,0,0), 0, Front, (1,0,1)),
                 Face((0,0,0), 0, Back, (1,0,1))) */
    val w = Cube((0.5,0.5,0.5),2)


    while (!Display.isCloseRequested()) {
      // Clear the screen and depth buffer
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

      // set the color of the quad (R,G,B,A)
      GL11.glColor3f(0.5f, 0f, 0f);



      // draw quad
      GL11.glBegin(GL11.GL_QUADS);

      for {f <- w.faces
           p <- f.points
      } {
        val c = f.color
        GL11.glColor3d(c._1, c._2, c._3)
        GL11.glVertex3d(p._1, p._2, p._3)
      }
      GL11.glEnd();

      Display.update();
    }

    Display.destroy();
  }
}

object Wuerfel {

  // Should be some kind of a mathematical Vector
  type Point = (Double, Double, Double)
  type Color = (Double, Double, Double)
  sealed abstract class Orientation
  case object Left extends Orientation
  case object Right extends Orientation
  case object Up extends Orientation
  case object Down extends Orientation
  case object Front extends Orientation
  case object Back extends Orientation
  case class Face(center: Point, size: Float, orientation: Orientation, color: Color) {
    val lfd = (-1, -1, -1)
    val rfd = ( 1, -1, -1)
    val lbd = (-1, -1,  1)
    val rbd = ( 1, -1,  1)
    val lfu = (-1,  1, -1)
    val rfu = ( 1,  1, -1)
    val lbu = (-1,  1,  1)
    val rbu = ( 1,  1,  1)

    val points = {
      val cs = orientation match {
        case Left => (lbd :: lbu :: lfu :: lfd :: Nil).reverse
        case Right => (rfd :: rfu :: rbu :: rbd :: Nil).reverse
        case Front => (lfu :: rfu :: rfd :: lfd :: Nil).reverse
        case Back => (lbd :: rbd :: rbu :: lbu :: Nil).reverse
        case Up => (lbu :: rbu :: rfu :: lfu :: Nil).reverse
        case Down => (lfd :: rfd :: rbd :: lbd :: Nil).reverse
      }
      cs.map(c => (c._1 * size + center._1, c._2 * size + center._2, c._3 * size + center._3))
      // Would be much easier with .* and .+
    }
  }

  case class Cube(center: Point, size: Float) {
    val r = new scala.util.Random
    val f: Orientation => Face = Face(center, size, _, (r.nextFloat, r.nextFloat, r.nextFloat))
    val faces = (f(Left) :: f(Right) :: f(Up) :: f(Down) :: f(Front) :: f(Back) :: Nil)
  }

  def main(args: Array[String]) = {
    val w = new Wuerfel
    w.start
  }
}