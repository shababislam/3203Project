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
	private Area initialState, method1,method2;
	public Simulator()
	{
		this(100, 5, 100, 1, null);
	}
	public Simulator(int numSensors, double radius, int scale, int unit, JFrame f)
	{
		this.initialState = new Area();
		this.initialState.addObserver(this);
		this.method1 = new Area();
		this.method1.addObserver(this);
		this.method2 = new Area();
		this.method2.addObserver(this);
		double pos = 0;
		boolean added  = false;
		Random r = new Random();
		Sensor Null = (new Sensor(pos, radius, scale, unit, 1)); // to initialize class variables
		Sensor s;
		this.frame = f;
		System.out.println("Simulator Object: initial Sensor has been Initialized...!");
		//Null.move(0.0, true);
		//this.method1.add(Null);

		for(int i  = 0; i < numSensors; i ++){
			added = false;
			while(!added && !initialState.isFull()){
				pos = r.nextFloat();
				s = new Sensor(pos);
				System.out.println("Simulator Object: new Position is: " + pos + " == " + s.getPos());
				added = this.initialState.add(s);
				if(added) System.out.println("Simulator Object: a Sensor has been Initialized and added at position " + pos);
				else System.out.println("Simulator Object: Sensor could not be added at position " + pos);
				// making 3 copies for separate simulations;
				this.method1.add(new Sensor(pos));
				this.method2.add(new Sensor(pos));
			}
		}
	}
	public Area getMethod1(){ return this.method1; }
	
	@SuppressWarnings("unused")
	public void Simple(Area a)
	{
		if(a.size() == 0)
		{
			System.out.println("Error: EmptyArray");
			return;
		}
		//checking the coverage by the left most element; and removing the initial gap;
		if(a.get(0).getMinCoverage() > 0)
		{
			a.get(0).select();
			while(a.get(0).getMinCoverage() > 0)
				a.get(0).move(UNITMOVE, LEFT);
			a.get(0).deselect();
		}
		//checking the coverage by the right most element; and removing the final gap;
		if(a.size() > 1)
		if(a.get(a.size()-1).getMaxCoverage() < 1)
		{
			a.get(a.size()-1).select();
			while(a.get(a.size()-1).getMaxCoverage() < 1)
				a.get(a.size()-1).move(UNITMOVE, RIGHT);
			a.get(a.size()-1).deselect();
		}
		double ol = 2, maxMoveAllowanceToRight = -1, maxMoveAllowanceToLeft = -1;
		//checking every other element except for first and last one
		Sensor left, center, right;
		if(a.size() >2)
			for(int i = 1; i < a.size()-1; i++)
			{
				left = a.get(i-1);
				center = a.get(i);
				right = a.get(i+1);
				center.select();
				
				ol = center.getOverlap(left);
/*				if(ol < 0) // has gap
				{
					maxMoveAllowanceToLeft = Math.abs(ol);
				}
				else
					maxMoveAllowanceToLeft = 0;

				ol = center.getOverlap(right);
				if(ol < 0) //  gap between
				{
					maxMoveAllowanceToRight = Math.abs(ol);
				}
				else
					maxMoveAllowanceToRight = 0;
*/
				try {
					TimeUnit.MILLISECONDS.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(ol < 0)
					center.move(Math.abs(ol), LEFT);
				try {
					TimeUnit.MILLISECONDS.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				center.deselect();
				try {
					TimeUnit.MILLISECONDS.sleep(334);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
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
