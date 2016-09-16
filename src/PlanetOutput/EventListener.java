package PlanetOutput;


import calcPlanet.Vec3f;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class EventListener implements GLEventListener{

	int aX = 0, aY = 1;
	
	LoadNextTimeStep coord;
	
	private static final int COUNT_CIRCLE_SLICES = 16;
	private static final float CIRCLE_RADIUS = 0.03f;	
	
	public EventListener(int achsisX, int achsisY, LoadNextTimeStep timeSteper){
		this.aX = achsisX;
		this.aY = achsisY;
		this.coord = timeSteper;
	}
	
	private static final float TEXT_SIZE = 0.03f;
	private void putLetter(GL2 gl, char c, float x, float y){
		switch(c){
		case 'X': case 'x':
			gl.glBegin(GL2.GL_LINES);
				gl.glVertex2f(x-TEXT_SIZE, y+TEXT_SIZE);
				gl.glVertex2f(x+TEXT_SIZE, y-TEXT_SIZE);
				
				gl.glVertex2f(x-TEXT_SIZE, y-TEXT_SIZE);
				gl.glVertex2f(x+TEXT_SIZE, y+TEXT_SIZE);
			gl.glEnd();
			return;
		case 'Y': case 'y':
			gl.glBegin(GL2.GL_LINES);
				gl.glVertex2f(x-TEXT_SIZE, y+TEXT_SIZE);
				gl.glVertex2f(x, y);
								
				gl.glVertex2f(x+TEXT_SIZE, y+TEXT_SIZE);
				gl.glVertex2f(x, y);
				
				gl.glVertex2f(x, y-TEXT_SIZE);
				gl.glVertex2f(x, y);
			gl.glEnd();
			return;
		case 'Z': case 'z':
			gl.glBegin(GL2.GL_LINE_STRIP);
				gl.glVertex2f(x-TEXT_SIZE, y+TEXT_SIZE);
				gl.glVertex2f(x+TEXT_SIZE, y+TEXT_SIZE);
				gl.glVertex2f(x-TEXT_SIZE, y-TEXT_SIZE);
				gl.glVertex2f(x+TEXT_SIZE, y-TEXT_SIZE);	
			gl.glEnd();
			return;
		}
		return;
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		//Zeichenen der Koordinaten Achsen
		gl.glBegin(GL2.GL_LINES);
			gl.glColor3f(1.0f, 1.0f, 1.0f);
			
			gl.glVertex2f(-1.0f, 0.0f);
			gl.glVertex2f(1.0f, 0.0f);
			for(int i=0; i<=18; i++){
				if(i==9)
					continue;
				float x = (i-9)*0.1f;
				gl.glVertex2f(x, 0.02f);
				gl.glVertex2f(x, -0.02f);
				
				gl.glVertex2f(x, -0.98f);
				gl.glVertex2f(x, -1.0f);
				
				gl.glVertex2f(x, 1.0f);
				gl.glVertex2f(x, 0.98f);
			}
			
			gl.glVertex2f(0.0f, -1.0f);
			gl.glVertex2f(0.0f, 1.0f);
			for(int i=0; i<=18; i++){
				if(i==9)
					continue;
				float y = (i-9)*0.1f;
				gl.glVertex2f(0.02f,y);
				gl.glVertex2f(-0.02f,y);
				
				gl.glVertex2f(-0.98f,y);
				gl.glVertex2f(-1.0f,y);
				
				gl.glVertex2f(1.0f,y);
				gl.glVertex2f(0.98f,y);
			}				
		gl.glEnd();
		gl.glBegin(GL2.GL_TRIANGLES);
			gl.glVertex2f(1.0f, 0.0f);
			gl.glVertex2f(0.96f, 0.02f);
			gl.glVertex2f(0.96f, -0.02f);
			
			gl.glVertex2f(0.0f, 1.0f);
			gl.glVertex2f(0.02f, 0.96f);
			gl.glVertex2f(-0.02f, 0.96f);
		gl.glEnd();
		
		putLetter(gl, number2char(aX), 0.95f, 0.05f);
		putLetter(gl, number2char(aY), 0.05f, 0.95f);
		
		//Zeichenen der Planeten an den aktuellen positionen
		if(coord.getCoords().length!=0){
			for(int i=0; i<coord.getCoords().length; i++){
				fillCircle(gl, new float[]{coord.getCoords()[i][aX], coord.getCoords()[i][aY]},
						coord.getColors()[i]);
			}
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
	}

	private void fillCircle(GL2 gl, float M[], byte color[]){
		//TODO: Besseren Algoritmuss benutzen und Vektoren die auf ein Quadrat zeigen normalisieren
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
			gl.glColor3ub(color[0], color[1], color[2]);
			gl.glVertex2f(M[0], M[1]);
			double angleOfSlice = (2*Math.PI)/COUNT_CIRCLE_SLICES;
			for(int i=0; i<=COUNT_CIRCLE_SLICES; i++){
				double angle = angleOfSlice*i;
				gl.glVertex2f((float)(M[0]+(Math.cos(angle)*CIRCLE_RADIUS)),
						(float)(M[1]+(Math.sin(angle)*CIRCLE_RADIUS)));
			}
		gl.glEnd();
	}
	
	private static char number2char(int nr){
		switch(nr){
		case 0:
			return 'x';
		case 1:
			return 'y';
		case 2:
			return 'z';			
		}
		return 'n';
	}
}
