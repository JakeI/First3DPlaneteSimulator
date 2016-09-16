package calcPlanet;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe zum berechnen der Planeten Bahen und abspeichern in einer Datei
 * @author Jochen "J" K. Illerhaus
 *
 */
public class Calc implements Runnable{

	private DataOutputStream outputStream;
	private ArrayList<Planet> planets = new ArrayList<Planet>();
	
	private final float timeStep; // Länge eines Schrittes
	private final long stepCount; // Anzahl der Schritte die Gespeichert werden sollen
	private final int logNStep; // Beim wievielten durchrechen Gespeicher werden soll
	
	private boolean allowColision = false;
	
	private static final float INFINITE_DIST = 50.0f;
	private static final float MIN_DIST = 0.01f;//0.005f; //
	private static final float GRAFITY_CONSTANT = 0.1f; //0.0005f; //
	private static final float COLIDE_DIST = 0.02f;//0.01f; //
	private static final int TIME_STAPS_BEFORE_COLIDE = 200000;
	
	/**
	 * Construktor muss aufgerufen werden. Füttert die Klasse mit allen notwendigen Parametern
	 * @param outputFile Das File Objekt in das die Ergebnisse der Berechnung geschrieben werden sollen
	 * @param time Die läne der enzelnen Schritte in der Berchnung
	 * @param numberOfSteps Anzahl der Schritte die Berechnet und geschrieben werden sollen
	 */
	public Calc(File outputFile, float time, int logNStep, long numberOfSteps){
		try {
			this.outputStream = new DataOutputStream(new FileOutputStream(outputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.timeStep = time;
		this.stepCount = numberOfSteps;
		this.logNStep = logNStep;
		
		try {
			outputStream.writeLong(stepCount/logNStep);
			for(int i=0; i<24; i++){
				outputStream.writeByte(0);
			}
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Bendet die Berechnungen und insbesondere die Datei in die die Ausgabe geschrieben wird
	 */
	private void close(){
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Started die Berechnungen - !sollte nicht direkt sonder über die Thread klasse mit der Metode start() aufgerufen werden! 
	 */
	@Override
	public void run() {
		writePlanets(0);
		long countWritten = 0;
        for(long i=0; i<stepCount; i++){
            if(i==TIME_STAPS_BEFORE_COLIDE)
                allowColision = true;
            incrementPlanets();
            if((i%logNStep)==0){
            	++countWritten;
            	writePlanets(countWritten);
            }
        }
        this.close();
	}
	
	/**
	 * Berechnet die Positionen der Planeten nach einem wieteren Zeitschritt und legt diese in der this.planets ab
	 */
	private void incrementPlanets(){
		
		if(allowColision){
            for(int i=0; i<planets.size(); i++){
                for(int j=0; j<planets.size(); j++){
                    if(j==i) continue;
                    double dist = planets.get(j).getPos().subtract(planets.get(i).getPos()).abs();
                    if(dist < COLIDE_DIST){
                        planets.add(planets.get(i).colide(planets.get(j)));
                        if(j<i){
                            planets.remove(i);
                            planets.remove(j);
                        }else{
                            planets.remove(j);
                            planets.remove(i);
                        }
                    }
                }
            }
        }
        
        ArrayList<Planet> buffer = new ArrayList<Planet>();
        int size = planets.size();
        for(int i=0; i<size; i++) {
            Vec3f F = new Vec3f();
            for(int j=0; j<size; j++){
                if(j==i) continue;
                Vec3f vecR = planets.get(j).getPos().subtract(planets.get(i).getPos());
                double dist = vecR.abs();
                if(dist > INFINITE_DIST) continue;
                dist = (dist>MIN_DIST)?dist:MIN_DIST;//can't be 0
                F = F.add(vecR.direction().times(
                		(float)((GRAFITY_CONSTANT*planets.get(i).getMass()*
                				planets.get(j).getMass())/(dist))));//Eventuell nur ein mal dist
            }
            buffer.add(planets.get(i).Force(F, this.timeStep));
        }
        planets.clear();
        planets.addAll(buffer);
	}
	
	/**
	 * Schreibt den aktuellen in this.planets gespricherten Zustand in die datei
	 * @param time Die Anzhal der bereits berechneten Zeitschritte
	 */
	private void writePlanets(long time){
		
		try {
			outputStream.writeLong(time);
			outputStream.writeShort((short)planets.size());
			for(Planet planet : planets){
				float f[] = planet.getPos().toArray();
				for(int i=0; i<f.length; i++){
					outputStream.writeFloat(f[i]);
				}
				outputStream.writeInt(planet.getColor());
			}
			outputStream.writeByte('\n');
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fügt dem Sonnensystem zufällige planeten hinzu. Deren Impulse in Summe NULL ergeben
	 * @param countPairs Anzhal der Planetenpaare die erzeugt werden sollen
	 */
	public void addRandomPlanets(short countPairs){
		for(int i=0; i<(countPairs*2); i+=2){
			this.planets.add(new Planet());
            this.planets.add(new Planet(this.planets.get(i)));
		}
	}
	
	/**
	 * Fügt planeten die angegebenen Planeten hinzu.
	 * @param list Die datei (*.csv) die nach dem muster m\tx_1\tx_2\tx_3\tv_1\tv_2\tv_3\t die für Planeten nötigen informationen enthält.
	 * Die Erste Zeile ist als dateiKopf vorgesehen und wird nicht berücksichtigt
	 */
	public void addListedPlanets(File list){
		try {
			Scanner  sc = new Scanner(list);
			
			if(sc.hasNextLine()){
				sc.nextLine();
			}else{
				sc.close();
				return;
			}
			
			while(sc.hasNextLine()){
				int begin = 0;
				String s = sc.nextLine();
				int tab = s.indexOf('\t', begin);
				if(tab==-1){
					tab = s.length();
				}
				float m = Float.parseFloat(s.substring(begin, tab-1));
				begin = tab;
				float x[] = new float[3];
				for(int i=0; i<3; i++){
					tab = s.indexOf('\t', begin+1);
					if(tab==-1){
						tab = s.length();
					}
					x[i] = Float.parseFloat(s.substring(begin, tab));
					begin = tab;
				}
				float v[] = new float[3];
				for(int i=0; i<3; i++){
					tab = s.indexOf('\t', begin+1);
					if(tab==-1){
						tab = s.length();
					}
					v[i] = Float.parseFloat(s.substring(begin, tab));
					begin = tab;
				}
				this.planets.add(new Planet(m, new Vec3f(x), new Vec3f(v)));
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
}
