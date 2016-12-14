import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
public class Simulator  implements Observer{
	public static final double UNITMOVE = 0.01;
	public static final boolean LEFT = true;
	public static final boolean RIGHT = false;
	private JFrame frame; // for updating gui if valid;
	private Area initialState, method1,method2, method3;
	private int numTrys; // for rare ocasions that simulator cannot land sesnors into the areas;
	public static final int MAXTRYS = 25;
	public Simulator()
	{
		this(100, 5, 100, 1, null);
	}
	public Simulator(int numSensors, int scale, int unit, JFrame f)
	{
		this(numSensors, 1.0/numSensors, scale,unit,f);
	}
	public Simulator(double[] sensors, double radius, int scale, int unit, JFrame f)
	{
		this.initialState = new Area();
		this.initialState.addObserver(this);
		this.method1 = new Area();
		this.method1.addObserver(this);
		double pos = 0;
		boolean added  = false;
		//Random r = new Random();
		Sensor Null = (new Sensor(pos, radius, scale, unit, 1, 0)); // to initialize class variables
		Sensor s;
		this.frame = f;

		for(int i  = 0; i < sensors.length ; i ++){
			added = false;
			{
				pos = sensors[i];
				s = new Sensor(pos, i);
				System.out.println("Simulator Object: new Position is: " + pos + " == " + s.getPos());
				added = this.initialState.add(s);
				if(added) System.out.println("Simulator Object: a Sensor has been Initialized and added at position " + pos);
				else System.out.println("Simulator Object: Sensor could not be added at position " + pos);
				this.method1.add(new Sensor(pos, i));
			}
		}
		this.saveGapsAndOverlaps();
	}
	public Simulator(int numSensors, double radius, int scale, int unit, JFrame f)
	{
		this.initialState = new Area();
		this.initialState.addObserver(this);
		this.method1 = new Area();
		this.method1.addObserver(this);
		this.method2 = new Area();
		this.method2.addObserver(this);
		this.method3 = new Area();
		this.method3.addObserver(this);
		double pos = 0;
		boolean added  = false;
		Random r = new Random();
		Sensor Null = (new Sensor(pos, radius, scale, unit, 1, 0)); // to initialize class variables
		Sensor s;
		this.frame = f;
		System.out.println("Simulator Object: initial Sensor has been Initialized...!");
		//Null.move(0.0, true);
		//this.method1.add(Null);

		for(int i  = 0; i < numSensors; i ++){
			added = false;
			this.numTrys = 0;
				while(!added && !initialState.isFull() && this.numTrys <= MAXTRYS){
				this.numTrys++;
				pos = r.nextFloat();
				s = new Sensor(pos, i);
				System.out.println("Simulator Object: new Position is: " + pos + " == " + s.getPos());
				added = this.initialState.add(s);
				if(added) System.out.println("Simulator Object: a Sensor has been Initialized and added at position " + pos);
				else System.out.println("Simulator Object: Sensor could not be added at position " + pos);
				// making 3 copies for separate simulations;
				this.method1.add(new Sensor(pos, i));
				this.method2.add(new Sensor(pos, i));
				this.method3.add(new Sensor(pos, i));
			}
		}
		this.saveGapsAndOverlaps();
	}
	
	/**
	 * makes this instance of Simulator a shallow copy of the other instance s
	 * @param s
	 * @return this
	 */
	public Simulator reinstaciate(Simulator s)
	{ 
		this.frame = s.frame;
		this.initialState = s.initialState;
		this.method1 = s.method1;
		this.method2 = s.method2;
		this.method3 = s.method3;
		this.saveGapsAndOverlaps();
		this.initialState.addObserver(this);
		this.method1.addObserver(this);
		this.method2.addObserver(this);
		this.method3.addObserver(this);
		return this;
	}
	  
	private void saveGapsAndOverlaps()
	{
		if(this.initialState!= null) this.initialState.saveGapsAndOverlaps();
		if(this.method1!= null) this.method1.saveGapsAndOverlaps();
		if(this.method2!= null) this.method2.saveGapsAndOverlaps();
		if(this.method3!= null) this.method3.saveGapsAndOverlaps();
	}
	public Area getMethod1(){ return this.method1; }
	public Area getMethod2(){ return this.method2; }
	public Area getMethod3(){ return this.method3; }
	public Area getInitialState(){ return this.method1; }
	
	public void setMethod1(Area a){ this.method1 = a; }
	public void setMethod2(Area a){ this.method2 = a; }
	public void setMethod3(Area a){ this.method3 = a; }

	
	public void Simple(Area a){this.Simple(a, true);}
	public void Regid(Area a){this.Regid(a, true);}
	@SuppressWarnings("unused")
	public void Simple(Area a, boolean motion)
	{
		long delay = 200;
		double prevMaxCoverage = 0.0;
		double distance = 0.0; // distance that a sensor can move; to be calculated;
		double overlapAllowance = 0.0;
		boolean allowOverlap = true;
		boolean direction = LEFT;
		if(a.size() == 0)
		{
			System.out.println("Error: EmptyArray");
			return;
		}
		//checking the coverage by the left most element; and removing the initial gap;
		try { TimeUnit.MILLISECONDS.sleep(5*delay); } 
		catch (InterruptedException e) { e.printStackTrace(); }
		
		for(int i = 0; i < a.size(); i ++) // going from left to right
		{
			direction = LEFT; // assuming moving direction is to left
			a.get(i).select();
			try { TimeUnit.MILLISECONDS.sleep(delay); } 
			catch (InterruptedException e) { e.printStackTrace(); }
			if(!a.get(i).covers(prevMaxCoverage)){
				if(a.get(i).getMinCoverage() > prevMaxCoverage){ //checking if there is a gap;
					distance = a.get(i).getMinCoverage() - (prevMaxCoverage);
					if(distance < 0 ) distance = 0;
					direction = LEFT;
				}
				if(a.get(i).getMinCoverage() < prevMaxCoverage-overlapAllowance){ //checking if there is a overlap;
					distance = Math.abs( a.get(i).getMinCoverage() - (prevMaxCoverage+overlapAllowance) );
					direction = RIGHT;
				}
			}
			else distance = 0.0;

			if(a.get(i).canMove(distance, direction))
			{
				if(motion) a.get(i).motionMove(distance, direction);
				else a.get(i).move(distance, direction);
				System.out.println("Simulator.Simple(): Sensor[" + i + "] moved");
			}
			prevMaxCoverage = a.get(i).getMaxCoverage();
			a.get(i).deselect();
			try { TimeUnit.MILLISECONDS.sleep(delay); } 
			catch (InterruptedException e) { e.printStackTrace(); }
			if(i == a.size()-1 && prevMaxCoverage > a.getMaxLength()) break;
		}
	}
	@SuppressWarnings("unused")
	public void Regid(Area a, boolean motion)
	{
		long delay = 200;
		double prevMaxCoverage = 0.0;
		double distance = 0.0; // distance that a sensor can move; to be calculated;
		boolean allowOverlap = true;
		boolean direction = LEFT;
		if(a.size() == 0)
		{
			System.out.println("Error: EmptyArray");
			return;
		}
		
		//checking the coverage by the left most element; and removing the initial gap;
		try { TimeUnit.MILLISECONDS.sleep(5*delay); } 
		catch (InterruptedException e) { e.printStackTrace(); }
		
		for(int i = 0; i < a.size(); i ++) // going from left to right
		{
			direction = LEFT; // assuming moving direction is to left
			a.get(i).select();
			try { TimeUnit.MILLISECONDS.sleep(delay); } 
			catch (InterruptedException e) { e.printStackTrace(); }

			if(a.get(i).canMoveTo(prevMaxCoverage+a.get(i).getRadius()) )
			{
				if(motion) a.get(i).motionMoveTo(prevMaxCoverage+a.get(i).getRadius());
				else a.get(i).setPos(prevMaxCoverage+a.get(i).getRadius());
				System.out.println("Simulator.Rigid(): Sensor[" + i + "] moved");
			}
			prevMaxCoverage = a.get(i).getMaxCoverage();
			a.get(i).deselect();
			try { TimeUnit.MILLISECONDS.sleep(delay); } 
			catch (InterruptedException e) { e.printStackTrace(); }
			if(i == a.size()-1 && prevMaxCoverage > a.getMaxLength()) break;
		}
		System.out.println("Rigid Coverage: " + a.size() + " Sensors with Radius of " +a.get(0).getRadius()+ ", costs "+ a.getMovingCost() + " units of distance for full coverage");
	} 
	public void run()
	{
		long delay = 100;
		boolean direction = LEFT;
		Sensor s = this.method1.get(0);
		//s.setPos(0.5);
		//Sensor s1 = new Sensor(0.565);
		//this.method1.add(s1);
		double distance = 0;
		while(true)
		{
			direction = !direction;
				if(direction) distance = 1.0-s.getMaxCoverage();
				else distance = 1-s.getMinCoverage();
			if(s.getPos() == 1.00 || s.getPos()==0.00) distance = 1;

			//while(s.canMove(distance, direction))
			{
				s.select();
				try {
					TimeUnit.MILLISECONDS.sleep(delay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				s.motionMove(distance, direction);
				//System.out.println("Simulator.run(): " + s);
				try {
					TimeUnit.MILLISECONDS.sleep(delay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				s.deselect();
				try {
					TimeUnit.MILLISECONDS.sleep(2*delay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//updateFrame();
				
			}
		}
		//while(this.method1.get(0).canMove(UNITMOVE, LEFT))
			//this.method1.get(0).move(UNITMOVE, LEFT);
		
		
	}
	
	public void updateFrame()
	{
		if(this.frame == null) return;
		frame.revalidate();
		frame.repaint();
	}
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		updateFrame();
	}
	
}
