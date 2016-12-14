import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
public class Area extends Observable implements Set<Sensor>, Observer {

	private double purelength; // calculated length regardless of overlaps and gaps;
	private double length; // calculates the length, subtracted by overlaps but not gaps;
	private ArrayList<Sensor> path; // to keep the list of Sensors
	public static final double maxLength = 1.0; // maximum distance of this Area that can be covered
	private double prevOverlaps;
	private double prevGaps;
	
	public Area()
	{
		this(1000);
	}
	private Area(double maxLength)
	{
		//this.maxLength = maxLength;
		this.prevGaps = this.prevOverlaps = 0.0;
		this.path = new ArrayList<Sensor>();
	}
	/**
	 * a deep copy constructor
	 * @param Area
	 */
	public Area(Area a)
	{
		this(a.getMaxLength());
		for( Sensor s: a)
			this.add((Sensor)s.clone());
	}
	/**
	 * Getter for the Maximum length
	 * @return double 1.0
	 */
	public double getMaxLength() { return Area.maxLength; }
	public void sort()
	{
		Sensor temp;
		for(int i  = 0; i < path.size(); i ++)
			for(int j  = 0; j < path.size(); j ++)
				if(path.get(i).compareTo(path.get(j)) < 0 )
				{
					temp = path.get(j);
					path.set(j, path.get(i));
					path.set(i, temp);
				}
	}
	/**
	 * this method just calculates the maximum possible coverage that the sensors in this area can provide
	 * It ignores all gaps and all overlaps;
	 * @return double value for the maximum possible coverage length
	 */
	public double getPureLength()
	{
		if(this.size() == 0) return 0;
		this.purelength = this.size()*this.get(0).getCoverageLength() ;
		return this.purelength;
	}
	
	public double getPreviousOverlaps(){ return this.prevOverlaps; }
	public double getPreviousGaps(){ return this.prevGaps; }
	/**
	 * calculates and returns the sum of overlaps within the current coverage area
	 * @return
	 */
	public double getAreaOverlaps()
	{
		double sumOL = 0.0;
		double ol = 0;
		for(int i = 0; i < this.size()-1; i ++) // calculating overlaps
		{
			ol = this.get(i).getOverlap(this.get(i+1));
			if(ol>=0 && ol != 2) sumOL += ol;
			if(this.get(i).getMinCoverage() < 0) sumOL+= Math.abs(this.get(i).getMinCoverage()); // for subtracting the outside coverage
			if(this.get(i).getMaxCoverage() > this.getMaxLength() ) sumOL+= this.getMaxLength() - this.get(i).getMaxCoverage(); // for subtracting the outside coverage
		}
		if(this.get(this.size()-1).getMaxCoverage() < this.getMaxLength() && this.size() > 1 ) sumOL+= this.getMaxLength() - this.get(this.size()-1).getMaxCoverage(); // for subtracting the outside coverage of very last element
		//this.prevOverlaps = sumOL;
		return sumOL;
	}
	/**
	 * calculates and returns the sum of all gaps within the coverage area;
	 * @return
	 */
	public double getAreaGaps()
	{
		double sumGP = 0.0;
		//double prevMaxCoverage = 0.0;
		double gp = 0;
		for(int i = 0; i < this.size()-1; i ++) // calculating overlaps
		{
			gp = this.get(i).getOverlap(this.get(i+1));
			if(gp>=0 && gp != 2 && gp != -1) sumGP += gp;
			if(i == 0 && this.get(i).getMinCoverage() > 0) sumGP+= Math.abs(this.get(i).getMinCoverage()); // for subtracting the outside coverage
			if(i == this.size()-1 && this.get(i).getMaxCoverage() < this.getMaxLength() ) sumGP+= this.getMaxLength() - this.get(i).getMaxCoverage(); // for subtracting the outside coverage
		}
		if(this.get(this.size()-1).getMaxCoverage() > this.getMaxLength() && this.size() > 1 ) sumGP+= this.getMaxLength() - this.get(this.size()-1).getMaxCoverage(); // for subtracting the outside coverage of very last element
		//this.prevGaps = sumGP;
		return sumGP;
	}
	public void saveGapsAndOverlaps()
	{
		this.prevGaps = this.getAreaGaps();
		this.prevOverlaps = this.getAreaOverlaps();
	}

	/**
	 * this method calculates the the total lengths of sensors subtracted by overlaps, but not gaps
	 * it assumes there are no gaps;
	 * @return double lengths
	 */
	public double calculateLength()
	{
		double sumOL = 0; // total overlap;
		double ol = 0;
		for(int i = 0; i < this.size()-1; i ++) // calculating overlaps
		{
			ol = this.get(i).getOverlap(this.get(i+1));
			if(ol>=0 && ol != 2) sumOL += ol;
			if(this.get(i).getMinCoverage() < 0) sumOL+= Math.abs(this.get(i).getMinCoverage()); // for subtracting the outside coverage
			if(this.get(i).getMaxCoverage() > this.getMaxLength() ) sumOL+= this.getMaxLength() - this.get(i).getMaxCoverage(); // for subtracting the outside coverage
		}
		if(this.get(this.size()-1).getMaxCoverage() > this.getMaxLength() && this.size() > 1 ) sumOL+= this.getMaxLength() - this.get(this.size()-1).getMaxCoverage(); // for subtracting the outside coverage of very last element
		this.length = this.getPureLength() - (sumOL);
		return this.length;
	}
	
	public double getMovingCost()
	{
		double cost = 0.0;
		for( int i = 0; i < size(); i ++)
			cost += get(i).getCost();
		return cost;
	}
	
	/**
	 * this is to check if the area is full and cannot accept anymore sensors
	 * @return boolean, true if this area is full
	 */
	public boolean isFull() { if(this.size() == 0) return false; this.calculateLength(); return this.length >= Area.maxLength; }
	/**
	 * return s the element at index i
	 * @param i
	 * @return Sensor
	 */
	public Sensor get(int i){ return this.path.get(i);}
	
	
	@Override
	public boolean add(Sensor s) {
		if(s == null) return false;
		//System.out.println("Area.add(): Sensor was not Null... (" + s.getPos() + ")");
		if(this.contains(s)) return false;
		//System.out.println("Area.add(): Sensor position is not taken...");
		if(s.getPos() > Area.maxLength) return false;
		//System.out.println("Area.add(): Sensor was was not positioned outside this area...");
		boolean res= this.path.add(s);
		//if(res) 		System.out.println("Area.add(): Sensor was added to the Path...");
		//else   			System.out.println("Area.add(): Sensor was not added to the Path...");

		if(res && this.calculateLength() > this.getMaxLength()) return !this.remove(s);
		//System.out.println("Area.add(): Sensor was not removed...");

		if(res) {
			//System.out.println("Area.add(): Sensor is added successfully...");
			s.deleteObserver(this);
			s.addObserver(this);
			this.sort();
			updateObservers(this, s, "NewSensor");

		}
		return res;
		
	}
	 
	/**
	 * implementation of addAll() for this container, it would only add elements if all elements are acceptable
	 */
	@Override
	public boolean addAll(Collection<? extends Sensor> sensors) {
		// TODO Auto-generated method stub
		for (Sensor s: sensors)
		{
			if(this.path.contains(s)) return false;
			if(s.getPos() >= Area.maxLength) return false;
		}
		boolean res =  path.addAll(sensors);
		if(res) 
		{
			this.sort();
			updateObservers();
		}
		return res;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		path.clear();
		updateObservers();
	}

	@Override
	public boolean contains(Object obj) {
		return path.contains(obj);

			
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return path.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.path.isEmpty();
	}

	@Override
	public Iterator<Sensor> iterator() {
		// TODO Auto-generated method stub
		return this.path.iterator();
	}

	@Override
	public boolean remove(Object obj) {
		// TODO Auto-generated method stub
		Sensor s = (Sensor)obj;
		s.deleteObserver(this);
		boolean res = this.path.remove(obj);
		updateObservers();
		return res;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		boolean res = this.path.removeAll(c);
		updateObservers();
		return res;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return this.path.retainAll(c);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.path.size();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return this.path.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return this.path.toArray(a);
	}
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		String message = "";
		if(o instanceof Sensor)
		{
			if(arg instanceof ObservableObject)
			{
				if(((ObservableObject)arg).message.equals("moved"))
				{
					//sort();
				}
				//moved these out of moved condition so if it is selected for instance, it still would update
				message = ((ObservableObject)arg).message;
				updateObservers(this, o, message);
			}
		}
		
	}
	private void updateObservers(){ updateObservers(this,null, ""); }
	private void updateObservers(Observable o, Object arg, String message)
	{
		setChanged();
		notifyObservers(new ObservableObject(o,arg, message));
	}
	

}
