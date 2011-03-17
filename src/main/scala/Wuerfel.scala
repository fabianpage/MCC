package we.MCC.wuerfel

import org.lwjgl.LWJGLException
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11

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
    GL11.glEnable(GL11.GL_CULL_FACE)
    GL11.glEnable(GL11.GL_TEXTURE_2D)

    import MCC._
    import org.newdawn.slick.opengl.TextureLoader
    import java.io.FileInputStream

    val texture = TextureLoader.getTexture("JPG", new FileInputStream("src/main/resources/prinzessin_lalilu.jpg"))
    texture.bind

    while (Display.isActive) {
      // Clear the screen and depth buffer
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)

      Shapes.drawCube((0,0,0), 1, (_:Orientation) => (0,0,1))
      Shapes.drawCube((1,1,0), 1, (_:Orientation) => (0,1,0))

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
  type Vector = (Double, Double, Double)
  sealed abstract class Orientation {
      val vect: Vector
  }
  case object Left extends Orientation  { val vect: Vector = (-1, 0, 0) }
  case object Right extends Orientation { val vect: Vector = (+1, 0, 0) }
  case object Up extends Orientation    { val vect: Vector = ( 0,+1, 0) }
  case object Down extends Orientation  { val vect: Vector = ( 0,-1, 0) }
  case object Front extends Orientation { val vect: Vector = ( 0, 0,+1) }
  case object Back extends Orientation  { val vect: Vector = ( 0, 0,-1) }
  
  object Shapes {
    def vectProd(v1: Vector, v2: Vector): Vector = (
      v1._2 * v2._3 - v1._3 * v2._2,
      v1._3 * v2._1 - v1._1 * v2._3,
      v1._1 * v2._2 - v1._2 * v2._1
    )
    def vectScale(v: Vector, f: Double): Vector = (v._1 * f, v._2 * f, v._3 * f)
    def vectAdd(v1: Vector, v2: Vector): Vector = (v1._1 + v2._1, v1._2 + v2._2, v1._3 + v2._3)
    def drawFace(center: Vector, size: Double, orient: Orientation, color: Color = (r.nextDouble, r.nextDouble, r.nextDouble)) = {
      val v1:Vector = orient match {
        case Left =>  (0, 1, 1)
        case Right => (0, 1, 1)
        case Front => (1, 1, 0)
        case Back =>  (1, 1, 0)
        case Up =>    (1, 0, 1)
        case Down =>  (1, 0, 1)
      }
      val v2 = vectProd(v1, orient.vect)
      val v3 = vectProd(v2, orient.vect)
      val v4 = vectProd(v3, orient.vect)
      val vs = (v1 :: v2 :: v3 :: v4 :: Nil).map(vectScale(_, size / 2)).map(vectAdd(center, _))
      val tcs = (0,0) :: (0,1) :: (1,1) :: (1,0) :: Nil
      // draw quad
      GL11.glBegin(GL11.GL_QUADS)
      GL11.glColor3d(color._1, color._2, color._3)
      for{(v,t) <- vs.zip(tcs)} {
        GL11.glTexCoord2d(t._1, t._2)
        GL11.glVertex3d(v._1, v._2, v._3)
      }
      GL11.glEnd()
    }
    def drawCube(center: Vector, size: Double, color: Orientation => Color) = {
      val fs = Left :: Right :: Up :: Down :: Front :: Back :: Nil
      for(f <- fs) {
        drawFace(vectAdd(center, vectScale(f.vect, size / 2)), size, f, color(f))
      }
    }

  }
  
  /*
  case class Face(val center: Vector, val size: Double, val orientation: Orientation, val color: Color) extends Drawable {
    val lfd = (-1, -1, -1)
    val rfd = ( 1, -1, -1)
    val lbd = (-1, -1,  1)
    val rbd = ( 1, -1,  1)
    val lfu = (-1,  1, -1)
    val rfu = ( 1,  1, -1)
    val lbu = (-1,  1,  1)
    val rbu = ( 1,  1,  1)

    val Vectors = {
      val cs = orientation match {
        case Left => (lbd :: lbu :: lfu :: lfd :: Nil)
        case Right => (rfd :: rfu :: rbu :: rbd :: Nil)
        case Front => (lfu :: rfu :: rfd :: lfd :: Nil)
        case Back => (lbd :: rbd :: rbu :: lbu :: Nil)
        case Up => (lbu :: rbu :: rfu :: lfu :: Nil)
        case Down => (lfd :: rfd :: rbd :: lbd :: Nil)
      }
      cs.map(c => (c._1 * size + center._1, c._2 * size + center._2, c._3 * size + center._3))
      // Would be much easier with .* and .+ (matlab notation)
    }
    
    override def draw = {

    }
  }
  

  case class Cube(val center: Vector, val size: Double = 1, val color: Color = ) extends Drawable {
    val f: Orientation => Face = Face(center, size, _, color)
    val faces = (f(Left) :: f(Right) :: f(Up) :: f(Down) :: f(Front) :: f(Back) :: Nil)
    override def draw = {
        for(f <- faces)
            f.draw
    }
  }
  
  case class Grid(private val cubes: List[Cube] = Nil) extends Drawable {
      def add(where: Orientation, what: Cube): Grid = (cubes.headOption.getOrElse(Cube((.0,.0,.0))), where) match {
          case (c, Left) => copy(cubes = cubes :+ Cube((c.center._1 + 1,c.center._2,c.center._3)))
          case (c, Right) => copy(cubes = cubes :+ Cube((c.center._1 - 1,c.center._2,c.center._3)))
      }
      
      def draw = {
          for(c <- cubes)
            c.draw
      }
  }
  */

  def main(args: Array[String]) = {
    val w = new MCC
    w.start
  }
}