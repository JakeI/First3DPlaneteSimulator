package PlanetOutput;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import PlanetOutput.Draw;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;

public class PlanetOutput {
	
	public PlanetOutput(File data){
		
		GLCapabilities capabilities = new GLCapabilities(GLProfile.getDefault());
	    capabilities.setRedBits(8);
	    capabilities.setBlueBits(8);
	    capabilities.setGreenBits(8);
	    capabilities.setAlphaBits(8);
	    
	    LoadNextTimeStep steper = new LoadNextTimeStep(data);
	    new Timer().schedule(steper, 0, 1000/60);
	    
	    Draw drawXY = new Draw(capabilities, Draw.projectionType.XY, steper);
	    Draw drawZY = new Draw(capabilities, Draw.projectionType.ZY, steper);
	    Draw drawXZ = new Draw(capabilities, Draw.projectionType.XZ, steper);
	    
	    new Animator(drawXY).start();
	    new Animator(drawZY).start();
	    new Animator(drawXZ).start();
	    
		JFrame jF = new JFrame();
		jF.setTitle("Graphics test");
		
		jF.setLayout(new GridLayout(2,2, 5, 5));
		jF.add(drawXY);
		jF.add(drawZY);
		jF.add(drawXZ);
		jF.add(new JPanel(){			
			@Override
			public void paintComponent(Graphics g) {
			    super.paintComponent(g);
			    Image image;
				try {
					image = ImageIO.read(new File("Sinnbild.png")); //schlecht (TODO: behalte ein Kopie im RAM)
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			    int w = image.getWidth(null)/4;
			    int h = image.getHeight(null)/4;
			    int x = (this.getWidth() - w) / 2;
			    int y = (this.getHeight() - h) / 2;
			    g.drawImage(image,x, y, w, h, null);
			}
		});
		jF.pack();
		
		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension FrameSize = new Dimension(800, 800);
        jF.setBounds((ScreenSize.width - FrameSize.width)/2, 
                (ScreenSize.height - FrameSize.height)/2, 
                FrameSize.width, FrameSize.height);
		//jF.setSize(800, 800);//für das abfilmen
		
		jF.setBackground(Color.WHITE);
		jF.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jF.setResizable(true);
		
		jF.setVisible(true);
	}
}
