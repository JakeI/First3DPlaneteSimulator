package log2csv;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Ermöglicht das konvertieren von den durch Calc-Classen erzeugten *.log dateien in ASCII codierte *.csv dateien
 * @author Jochen "J" K. Illerhaus
 *
 */
public class log2csv implements Runnable{
	
	DataInputStream in;
	OutputStreamWriter out;
	
	/**
	 * Dieser Konstruktor muss aufgerufen werden. Er versorgt die Klasse mit allem zwingend notwendigen
	 * @param input Die *.log datei die Konvertiert werden soll
	 * @param output Die *.csv Datei die erzugt werden soll
	 */
	public log2csv(File input, File output){
		try {
			in = new DataInputStream(new FileInputStream(input));
			out = new OutputStreamWriter(new FileOutputStream(output));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Schließt die Stream objekte !muss zum ende der Klasse aufgerufen werden!
	 */
	private void close(){
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Started die Konvetierung - !sollte nicht direkt sonder über die Thread klasse mit der Metode start() aufgerufen werden!
	 * Der Header wird direkt in dieser Datei eingelesen.
	 */
	@Override
	public void run(){
		
		try {
			
			long stepCount = in.readLong();
			in.skipBytes(24);
			for(long i=0; encodeLine()!=-1 && i<stepCount; i++)
				continue;
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.close();
	}
	
	/**
	 * Ließt eine den Zustand zu einem zweitpunkt ein und speichert ihn in die zielDatei ab
	 * @return Im moment nur null TODO: return -1 für Fehler
	 * @throws IOException Wenn die Streams überraschend enden oder es zu anderen Fehlern beim schreiben oder lesen der Dateien kommt.
	 */
	private int encodeLine() throws IOException{
		
		out.write(String.valueOf(in.readLong())+'\t');
		short count = in.readShort();
		out.write(String.valueOf(count)+'\t');
		
		for(int i=0; i<count; i++){
			for(int j=0; j<3; j++){
				out.write(String.valueOf(in.readFloat())+'\t');
			}
			in.skipBytes(1); //überspringe alpha
			for(int j=0; j<3; j++){
				out.write(String.valueOf((short)(in.readByte()&0xFF))+'\t'); //Eventuell noch 128 addieren
			}
		}
		
		in.skipBytes(1);//überspriche Ende TODO: überprüfen ob es sich um das richtige ende handelt
		out.write("\r\n");
		
		return 0;
	}
}
