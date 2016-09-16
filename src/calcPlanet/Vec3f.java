package calcPlanet;

/**
 * Vektor aus dem R^3 der die Komponenten als floats speichert
 * 
 * @author Jochen "J" K. Illerhaus
 *
 */
public class Vec3f {
	
	public float X, Y, Z;
	
	/**
	 * Konstruktor erzeugt einen Null Vektor
	 */
	public Vec3f(){
		this.X = 0.0f;
		this.Y = 0.0f;
		this.Z = 0.0f;
	}
	
	/**
	 * Konstruktor nach Kartesischen Koordinaten
	 * @param x Erste Komponente
	 * @param y Zweite Komponente
	 * @param z Dritte Komponente
	 */
	public Vec3f(float x, float y, float z){
		this.X = x;
		this.Y = y;
		this.Z = z;
	}
	
	/**
	 * Erzeugt einen vektor mit den angegebenen Kartesischen Koordinaten
	 * @param x Die Kordinaten in der reihenfolge x, y, z die den Vektor definieren sollen
	 */
	public Vec3f(float x[]){
		this.X = x[0];
		this.Y = x[1];
		this.Z = x[2];
	}
	
	/**
	 * Adition this+v
	 * @param v Zu addierender Vektor
	 * @return Ergebnis
	 */
	public Vec3f add(Vec3f v){
		return new Vec3f(this.X+v.X, this.Y+v.Y, this.Z+v.Z);
	}
	
	/**
	 * Subtraktion this-v
	 * @param v Abzuziehender Vektor
	 * @return Ergebnis
	 */
	public Vec3f subtract(Vec3f v){
		return new Vec3f(this.X-v.X, this.Y-v.Y, this.Z-v.Z);
	}
	
	/**
	 * Multiplikation mit Konstante
	 * @param lamda Die Konstante mit der Multipiliziert wierd
	 * @return Das Ergebniss der Muliplikation
	 */
	public Vec3f times(float lamda){
		return new Vec3f(lamda*X, lamda*Y, lamda*Z);
	}
	
	/**
	 * Skalarprodukt zweier Vektoren
	 * @param v der Vec3f mit dem Multipliziert werden soll
	 * @return Skalarprodukt der Vektoren
	 */
	public float scalar(Vec3f v){
		return (this.X*v.X)+(this.Y*v.Y)+(this.Z*v.Z);
	}
	
	/**
	 * Ablolutbetrag
	 * @return Betrag des Vektors
	 */
	public float abs(){
		return (float)Math.sqrt( (X*X)+(Y*Y)+(Z*Z) );
	}
	
	/**
	 * Matematik: vec/abs
	 * @return Ein Vec3f mit Betrag 1 in richtung des elements
	 */
	public Vec3f direction(){
		return this.times(1.0f/this.abs());
	}
	
	public String toString(){
		return "vec[ "+this.X+", "+this.Y+", "+this.Z+"]";
	}
	
	/**
	 * Erzeugt einen Array der die Komponenten des Vektors enthält
	 * @return Die Komponenten des Vektors in der Reihenvolge X, Y, Z
	 */
	public float[] toArray(){
		return new float[]{ this.X, this.Y, this.Z };
	}
}
