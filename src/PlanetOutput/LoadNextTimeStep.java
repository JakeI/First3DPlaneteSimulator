package PlanetOutput;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

public class LoadNextTimeStep extends TimerTask{

	DataInputStream in;
	
	private long countFrames;
	private long framesDisplayed = 0; 
	private long time;
	private float coords[][];
	private byte colors[][];
	
	private boolean isRunning = false;
	private static final int WHAIT_NANOS_FOR_RUNNING = 100;//darf maximal 999999 werden (<10^6)
	
	public byte[][] getColors() {
		while(isRunning){
			try{
				Thread.sleep(0, WHAIT_NANOS_FOR_RUNNING);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		return colors;
	}
	public float[][] getCoords() {
		while(isRunning){
			try{
				Thread.sleep(0, WHAIT_NANOS_FOR_RUNNING);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		return coords;
	}
	public long getCountFrames() {
		return countFrames;
	}
	public long getFramesDisplayed() {
		return framesDisplayed;
	}
	public long getTime() {
		return time;
	}
	
	public LoadNextTimeStep(File data){
		
		try{
			this.in = new DataInputStream(new FileInputStream(data));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			countFrames = in.readLong();
			in.skipBytes(24);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.coords = new float[0][0];
		this.colors = new byte[0][0];
		
	}
	
	public void close(){
		try{
			this.in.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		this.isRunning = true;
		++framesDisplayed;
		if(framesDisplayed >= countFrames){
			this.isRunning = false;
			return;
		}
		
		try {
			time = in.readLong();
			short count = in.readShort();
			coords = new float[count][3];
			colors = new byte[count][3];
			for(int i=0; i<count; i++){//Abarbeiten aller Planeten
				for(int j=0; j<3; j++){
					coords[i][j] = in.readFloat();
				}
				in.skipBytes(1);//überspringe Alpha
				for(int j=0; j<3; j++){
					colors[i][j] = in.readByte();
				}
			}
			//überspringen des Nachrichten Endes TODO: Überpfüfen ob die datei follstendig ist
			in.skipBytes(1); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.isRunning = false;
	}
	
}
