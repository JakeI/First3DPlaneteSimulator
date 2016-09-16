package Planet3D;

import java.io.File;

import PlanetOutput.PlanetOutput;
import log2csv.log2csv;
import calcPlanet.Calc;

public class Planet3D {
	
	public static void main(String[] args){
		
		File fLog = new File("data.log");
		
		long startTime = System.currentTimeMillis();
		Calc calc = new Calc(fLog, 0.001f, 500, 1500000);//new Calc(fLog, 0.0001f, 5000, 15000000);//10secunden Viedeo
		//calc.addRandomPlanets((short)2);
		calc.addListedPlanets(new File("inputPlanets.csv"));
		Thread calcTh = new Thread(calc);
		calcTh.start();
		
		try { //auf das ende von calcTh warten
			calcTh.join();
			System.out.println("Zeit zum Errechnen = "+(System.currentTimeMillis()-startTime));
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*File fCsv = new File("data.csv");
		startTime = System.currentTimeMillis();
		Thread convertTh = new Thread(new log2csv( fLog, fCsv));
		convertTh.start();
		
		try { //auf das ende von convertTh warten
			convertTh.join();
			System.out.println("Zeit zum convertieren = "+(System.currentTimeMillis()-startTime));
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		
		PlanetOutput planetOutput = new PlanetOutput(fLog);
		
		System.out.println("Ende Erreicht");
	}
}
