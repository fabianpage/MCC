package we.MCC.wuerfel

import org.lwjgl.LWJGLException
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11

class Wuerfel {
    def start = {
        try {
            Display.setDisplayMode(new DisplayMode(800,600));
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

        while (!Display.isCloseRequested()) {
            // Clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	

            // set the color of the quad (R,G,B,A)
            GL11.glColor3f(0.5f,0f,0f);

            val w =
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
            (0, 1, 1) :: Nil):: Nil


            // draw quad
            GL11.glBegin(GL11.GL_QUADS);
            
            for{s <- w
                p <- s
                } {
                    GL11.glVertex3f(p._1, p._2, p._3)
                }
            GL11.glEnd();

            Display.update();
        }

        Display.destroy();
    }
}

object Wuerfel {
    def main (args: Array[String]) = {
        val w = new Wuerfel
        w.start
    }
}
/* 
public class QuadExample {
 
    public void start() {
        try {
	    Display.setDisplayMode(new DisplayMode(800,600));
	    Display.create();
	    } catch (LWJGLException e) {
	    e.printStackTrace();
	    System.exit(0);
	}   

	// init OpenGL
	GL11.glMatrixMode(GL11.GL_PROJECTION);
	GL11.glLoadIdentity();
	GL11.glOrtho(0, 800, 600, 0, 1, -1);
	GL11.glMatrixMode(GL11.GL_MODELVIEW);
 
	while (!Display.isCloseRequested()) {
	    // Clear the screen and depth buffer
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
		
	    // set the color of the quad (R,G,B,A)
	    GL11.glColor3f(0.5f,0.5f,1.0f);
	    	
	    // draw quad
	    GL11.glBegin(GL11.GL_QUADS);
	        GL11.glVertex2f(100,100);
		GL11.glVertex2f(100+200,100);
		GL11.glVertex2f(100+200,100+200);
		GL11.glVertex2f(100,100+200);
	    GL11.glEnd();
 
	    Display.update();
	}
 
	Display.destroy();
    }
 
    public static void main(String[] argv) {
        QuadExample quadExample = new QuadExample();
        quadExample.start();
    }
}
*/