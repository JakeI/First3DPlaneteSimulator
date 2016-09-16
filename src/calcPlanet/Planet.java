package calcPlanet;

/**
 * Ein Planet wie für die Simulation benötigt
 * @author Jochen "J" K. Illerhaus
 *
 */
public class Planet {
	
	private float mass;
    private Vec3f pos;
    private Vec3f vilocity;
    
    private static final float MASS_MIN = 0.001f, MASS_MAX = 0.005f;//MASS_MIN = 0.0001f, MASS_MAX = 0.0015f;//
    private static final float MINMAX_XYZ[] = new float[]{-0.4f, 0.4f, -0.4f, 0.4f, -0.4f, 0.4f };//{-0.2f, 0.2f, -0.2f, 0.2f, -0.2f, 0.2f }; //
    private static final float MINMAX_VXVYVZ[] = new float[]{ -0.001f, 0.001f, -0.001f, 0.001f, -0.001f, 0.001f };//{ -0.01f, 0.01f, -0.01f, 0.01f, -0.01f, 0.01f };//
    
    /**
     * Erzeugt ein Planetenobjet mit im Rahmen der in dieser Datei festgelegten Grenzen zufälligen Eigenschaften
     */
    public Planet(){
        this.mass = (float)((Math.random()*(MASS_MAX-MASS_MIN))+MASS_MIN); 
        this.pos = Planet.generateRandom(MINMAX_XYZ);
        this.vilocity = Planet.generateRandom(MINMAX_VXVYVZ);
    }
    
    /**
     * Erzeugt ein Gegenstück mit entgegengesetztem und gleich großem Impuls zufälliger Masse und Position (im Ramen der in dieser Datei angegebenen Grenzen)
     * @param planet Das Objekt zu dem der Impulz des neuen Objektes entgegengesetzt sein soll
     */
    public Planet(Planet planet){//Creates a counter part
        this.mass = (float)((Math.random()*(MASS_MAX-MASS_MIN))+MASS_MIN);
        this.pos = Planet.generateRandom(MINMAX_XYZ);
        this.vilocity = planet.vilocity.times(-planet.mass/this.mass);
    }
    
    /**
     * Erzeugt einen Planeten mit den angegevenen eigenschaften
     * @param m die Masse die der Planet haben soll - !Wird nicht auf plausibilität geprüft!
     * @param p die Position die der Planet haben soll - !Wird nicht auf plausibilität geprüft!
     * @param v die Geschwindigkeit die der Planet haben soll - !Wird nicht auf plausibilität geprüft!
     */
    public Planet(float m, Vec3f p, Vec3f v){
        this.mass = m;
        this.pos = p;
        this.vilocity = v;
    }
    
    /**
     * Erzeugt einenen zufälligen Vektor
     * @param limits Die grenzen innerhalb derer sich das Ergebniss des Zufallsvektors befinden soll in der Form {xMin, xMax, yMin, yMax, zMin, zMax}
     * @return Den Zufälligen Vec3f der sich in dem definierten Quader befindet
     */
    private static Vec3f generateRandom(float limits[]){
        return new Vec3f(
        		(float)((Math.random()*(limits[1]-limits[0]))+limits[0]),
        		(float)((Math.random()*(limits[3]-limits[2]))+limits[2]),
        		(float)((Math.random()*(limits[5]-limits[4]))+limits[4]));
    }
    
    /**
     * Getter für die Position
     * @return Die Position des Planeten
     */
    public Vec3f getPos(){
        return this.pos;
    }
    
    /**
     * Getter für die Masse
     * @return Die Masse des Planeten
     */
    public float getMass(){
    	return this.mass;
    }
    
    /**
     * Gibt einen Planeten mit gleicher Masse und der Geschwindigkeit und Position die nach der einwirkung einer Kraft entstehen zurück.
     * @param F Die auf den Planeten Wirkende Gesamptkraft
     * @param dt Die Zeit über die die Kraft auf den Planeten wirkt
     * @return Der Planet mit den Eigenschaften die durch die Einwirkung der Kraft auf den ursprünglichen Planeten entstehen
     */
    public Planet Force(Vec3f F, float dt){
        return new Planet(this.mass,
        		pos.add(vilocity.times(dt)),
        		this.vilocity.add(F.times(dt/mass)));
    }
    
    /**
     * Erzeugt die Farbe die entsprechend der Masse zu dem Objekt gehört
     * @return Ein integer der Form 00000000 RRRRRRRR GGGGGGGG BBBBBBBB
     */
    public int getColor(){
        double m = mass;//1.0-Math.exp(-((mass-MASS_MIN)*constMulti));
        return (0xFF000000) | (m>0.0001?0xFF0000:0) | ((m<0.00015 && m>0.000006)?0xFF00:0) | (m<0.000007?0xFF:0);
    }
    public static final double constMulti = -Math.log(0.33)/(MASS_MAX-MASS_MIN); //Konstante: mit ihr muss die Masse multipliziert werden um die Richtigen Farben zu ergeben
    
    /**
     * Berechnet den Impuls des Planeten
     * @return Der impuls (als Vektor)
     */
    public Vec3f getImpulse(){
        return vilocity.times(mass);
    }
    
    /**
     * Erzeugt ein Element das die beiden Planeten in einer total unelastischen Kolision vereinigt
     * @param pla Der Planet mit dem die Kolision volzogen werden soll - !Es wird nicht überprüft ob dieser tatsächlich nahe genug für eine Kolision ist!
     * @return Den aus der Kolision hervorgehenden Planeten
     */
    public  Planet colide(Planet pla){
    	float m = this.mass+pla.mass;
        return new Planet(m,
        		this.pos.add(pla.pos).times(0.5f),
        		this.getImpulse().add(pla.getImpulse()).times(1/m));
    }
    
    /**
     * Convertiert das Element in einen für Menschen lesbaren String
     */
    @Override
    public String toString(){
        return "planet[ m="+this.mass+" x="+this.pos+" v="+this.vilocity+']';
    }
}
