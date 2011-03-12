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

    import Wuerfel._
    val w = List(Face((0,0,0), 0, Left, (1,0,1)),
                 Face((0,0,0), 0, Right, (1,0,1)))


    while (!Display.isCloseRequested()) {
      // Clear the screen and depth buffer
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

      // set the color of the quad (R,G,B,A)
      GL11.glColor3f(0.5f, 0f, 0f);



      // draw quad
      GL11.glBegin(GL11.GL_QUADS);

      for {f <- w
           p <- f.points
      } {
        val c = f.color
        GL11.glColor3f(c._1, c._2, c._3)
        GL11.glVertex3f(p._1, p._2, p._3)
      }
      GL11.glEnd();

      Display.update();
    }

    Display.destroy();
  }
}

object Wuerfel {

  type Point = (Float, Float, Float)
  type Color = (Float, Float, Float)
  sealed abstract class Orientation
  case object Left extends Orientation
  case object Right extends Orientation
  case object Up extends Orientation
  case object Down extends Orientation
  case object Front extends Orientation
  case object Back extends Orientation
  case class Face(center: Point, size: Float, orientation: Orientation, color: Color) {
    val points = orientation match {
      case Left => ((0, 0, 0) ::
                    (0, 1, 0) ::
                    (0, 1, 1) ::
                    (0, 0, 1) :: Nil)
      case Right => ((1, 0, 1) ::
                     (1, 1, 1) ::
                     (1, 1, 0) ::
                     (1, 0, 0) :: Nil)
    }
  }

  /*val w =
      ((0, 0, 0) ::
      (1, 0, 0) ::
      (1, 1, 0) ::
      (0, 1, 0) :: Nil) ::
      ((0, 0, 0) ::
      (0, 1, 0) ::
      (0, 1, 1) ::
      (0, 0, 1) :: Nil) ::
      ((0, 0, 0) ::
      (0, 0, 1) ::
      (1, 0, 1) ::
      (1, 0, 0) :: Nil) ::
      ((1, 1, 1) ::
      (1, 1, 0) ::
      (0, 1, 0) ::
      (0, 1, 1) :: Nil) ::
      ((1, 1, 1) ::
      (1, 1, 0) ::
      (0, 1, 0) ::
      (0, 1, 1) :: Nil) ::
      ((1, 1, 1) ::
      (1, 1, 0) ::
      (0, 1, 0) ::
      (0, 1, 1) :: Nil) :: Nil*/


  def main(args: Array[String]) = {
    val w = new Wuerfel
    w.start
  }
}