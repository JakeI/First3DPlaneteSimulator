package PlanetOutput;


import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;

public class Draw extends GLCanvas{
	
	public Draw(GLCapabilities glc, projectionType type, LoadNextTimeStep timeStepper){
		super(glc);
		
		switch(type){
		case XY:
			this.addGLEventListener(new EventListener(0,1, timeStepper));
			break;
		case ZY:
			this.addGLEventListener(new EventListener(2,1, timeStepper));
			break;
		case XZ:
			this.addGLEventListener(new EventListener(0,2, timeStepper));
			break;
		}
	}
	
	public enum projectionType{
		XY, ZY, XZ 
	}
}

/*class EventListenerXY extends EventListener{
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glBegin(GL2.GL_TRIANGLES);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glVertex2f(-0.8f, -0.8f);
			gl.glVertex2f(0.8f, -0.8f);
			gl.glVertex2f(0.0f, 0.8f);
		gl.glEnd();
	}
}

class EventListenerZY extends EventListener{
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glBegin(GL2.GL_TRIANGLES);
			gl.glColor3f(0.0f, 1.0f, 0.0f);
			gl.glVertex2f(-0.8f, -0.8f);
			gl.glVertex2f(0.8f, -0.8f);
			gl.glVertex2f(0.0f, 0.8f);
		gl.glEnd();
	}
}

class EventListenerXZ extends EventListener{
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glBegin(GL2.GL_TRIANGLES);
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			gl.glVertex2f(-0.8f, -0.8f);
			gl.glVertex2f(0.8f, -0.8f);
			gl.glVertex2f(0.0f, 0.8f);
		gl.glEnd();
	}
}*/
