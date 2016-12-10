
import java.util.Observable;
import javax.swing.JComponent;
import java.security.InvalidParameterException;

public class Sensor extends Observable implements Comparable<Sensor>, Cloneable{
	private double x; // keeps track of x position
	private double initialX; // to keep track of the initial position of this sensor for calculating cost
	private boolean selected;
	private boolean lockPosition;
	private SensorComponent component;
	private static double radius; // to save the radius
	private static int scale = 100; // to keep track of the scale ***** may render unnecessary later
	private static double overlapTollorance = 1.0; //in terms of units
	private static double unit = 10.0; //scale divide by Unit would result int 100 units in default scale ***** this also May render Useless
	
	public Sensor(){this(0,1,1000, 10.0, 1);}
	public Sensor(double pos) { this(pos, Sensor.radius, Sensor.scale, Sensor.unit, Sensor.overlapTollorance); }
	public Sensor(double pos, double radius, int scale, double unit, double tollorance) throws InvalidParameterException
	{
		super();
		if(pos > 1 || pos < 0) throw new InvalidParameterException("Position of Sensor has to be strictly between 0 and 1, inclusive.");
		if(radius <= 0 ) throw new InvalidParameterException("Position of Sensor has to be strictly between 0 and 1, inclusive.");

		this.x = pos;
		this.initialX = pos;
		Sensor.radius = radius;
		Sensor.scale = scale;
		Sensor.overlapTollorance = tollorance;
		Sensor.unit = unit;
		this.selected = false;
		this.lockPosition = false;
		this.component = null;
		
	}
	@Override
	public Object clone()
	{
		Sensor newSensor = new Sensor(this.getPos(), this.getRadius(), Sensor.getScale(), Sensor.unit, Sensor.overlapTollorance);
		newSensor.initialX = this.initialX;
		return (Object)newSensor;		
	}
	
	public JComponent generateComponent(Ruler r)
	{
		if(this.component == null)
		{
			this.component = new SensorComponent(this, r);
			this.addObserver(this.component);
			notifyObservers();
			return this.component;
		}
		else
			return this.getComponent();
	}
	public JComponent getComponent(){ return this.component; }
	public void setComponent(SensorComponent c){
		if(this.component != null)
		{
			//maybe notify the observer to clear its representation;
			this.deleteObserver(this.component);
		}
		this.component = c;
		this.addObserver(this.component);
	}
	
	
	public static double getUnit(){ return Sensor.unit; }
	/**
	 * returns the current x position of this Sensor
	 * @return int x
	 */
	public double getPos(){ return this.x; }
	public boolean setPos(double pos) 
	{
		if(pos < 0) return false;
		if(pos > 1) return false;
		this.x = pos;
		return true;
	}
	
	public void select(){this.selected = true; this.setChanged();this.notifyObservers("selected");}
	public void deselect(){this.selected = false; this.setChanged();this.notifyObservers("selected");}
	public boolean isSelected(){ return this.selected;}
	
	/**
	 * returns the radius of the this Sensor
	 * @return double radius
	 */
	public double getRadius(){ return Sensor.radius; }
	/**
	 * returns the current value of the scaling factor for every sensor
	 * @return
	 */
	public static int getScale(){ return scale; }
	/**
	 * returns the length of coverage of this sensor
	 * which will be interpreted as 2*this.getRadious()
	 * @return double Length
	 */
	public double getCoverageLength(){ return 2* Sensor.radius; }
	/**
	 * returns the minimum position that this sensor can cover
	 * @return double position < x
	 */
	public double getMinCoverage(){ return this.getPos() - this.getRadius(); }
	/**
	 * returns the maximum position that this sensor can cover
	 * @return double position > x
	 */
	public double getMaxCoverage(){ return this.getPos() + this.getRadius(); }
	/**
	 * compares this sensor with another sensor, and returns true if this == other or they both have same x position and radius
	 * @param other
	 * @return boolean this == other or not
	 */
	@Override
	public boolean equals(Object other)
	{
		if(this == other) return true;
		if(! (other instanceof Sensor)) return false;
		return this.getPos() == ((Sensor)other).getPos();
		//if(this == otherSensor) return true;
		//return this.getX() == otherSensor.getX() && this.getRadius() == otherSensor.getRadius();
	}
	/**
	 * Overrides the compareTo(other) method of Comparable interface.
	 * compares the x position of this sensor with the x position of other sensor.
	 * @returns -1 if this.getPos() < other.getPos()
	 * @returns 1 if this.getPos() > other.getPos()
	 * @returns 0 if they are equal
	 */
	@Override
	public int compareTo(Sensor other) {
		double res = this.getPos() - other.getPos();
		if(res < 0) return -1;
		if(res > 0) return 1;
		return 0;
	}
	/**
	 * this method returns -1 if two sensors don't reach each other;
	 * otherwise it will return how much the signals would overlap, in terms of Sensor.unit
	 * @param other
	 * @return double overlapping area
	 */
	public boolean overlaps(Sensor other)
	{
		if(this.getOverlap(other) < 0) return false;
		return true;
	}
	/**
	 * checks if the signal of the two sensors reach each other and returns true; returns false otherwise
	 * @param other
	 * @return
	 */
	public boolean reaches(Sensor other)
	{
		if(this.equals(other)) return true;
		if(this.compareTo(other) < 0)
			return this.getMaxCoverage() >= other.getMinCoverage();
		if(this.compareTo(other) > 0)
			return this.getMinCoverage() <= other.getMaxCoverage();
		return false;	
	}
	public double getOverlap(Sensor other)
	{
		if(!this.reaches(other)) return 2;
		if(this.compareTo(other) < 0)
			return Math.abs(this.getMaxCoverage() - other.getMinCoverage());
		if(this.compareTo(other) > 0)
			return Math.abs(this.getMinCoverage() - other.getMaxCoverage());
	return -1;

	}
	// Unnecessary after implementation of updateObservers
	private void notifyMoved(){ this.updateObservers(this, "moved");}
	
	public void lock() { this.lockPosition = true; this.updateObservers(this, "locked"); }
	public void unlock() { this.lockPosition = false; this.updateObservers(this, "unlocked");}
	public boolean isLocked() { return this.lockPosition; }
	
	/**
	 * return true if the this Sensor's position is not locked and the new position will be valid; 
	 * return false otherwise.
	 * @param distance
	 * @param left
	 * @return boolean
	 */
	public boolean canMove(double distance, boolean left)
	{
		if(this.isLocked()) return false;
		if(distance < 0 || distance > 1) return false;
		if(left && this.getPos()-distance >=0) return true;
		if(!left && this.getPos()+distance <=1) return true;
		return false;	
	}
	public boolean move(double distance, boolean left)
	{
		//double distance = units * Sensor.unit;
		if(!this.canMove(distance, left)) return false;
		if(left){
			this.setPos(this.getPos()-distance); 
			this.updateObservers(this, "moved");
			return true;
		}
		else{ // if !left
			this.setPos(this.getPos()+distance); 
			this.updateObservers(this, "moved");
			return true;
		}
	}
	/**
	 * calculates and returns the number of units that this sensor have moved since it was constructed;
	 * @return double Units of distance
	 */
	public double getCost()
	{
		double cost = 0.0;
		if(this.initialX > this.getPos()) return this.initialX - this.getPos();
		if(this.initialX < this.getPos()) return this.getPos() - this.initialX;
		return cost;
	}
	/**
	 * Creates and sends an ObservableObject, with this object as the observable, another object and a String as the message for detailed Change
	 */
	private void updateObservers(){ updateObservers(this,null, ""); }
	private void updateObservers(Object arg, String message){ updateObservers(this, arg, message); }
	private void updateObservers(Observable o, Object arg, String message)
	{
		setChanged();
		notifyObservers(new ObservableObject(o,arg, message));
	}

	public String toString()
	{
		String rad = "";
		for(int i = 0; i < Sensor.radius*100; i ++)
			rad+= "=";
		return "<" + rad + "("+this.getPos()+")" + rad + ">";
	}
	
}