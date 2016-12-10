
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
//import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

public class Ruler extends JComponent implements ComponentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3973450432677831359L;
	private static final int MINWIDTH = 400;
	private static final int MINHEIGHT = 20;
	private static int unit;
	private int MaxLengthInPixels;
	private int GapBeforeInPixels;
	private int GapAfterInPixels;
	private int MaxHeight;
	private int scale;
	private InstanciableObservable observers;
	
	@SuppressWarnings("unused")
	private Ruler(){this(50, 800); }
	public Ruler(int gap, int width){ this(gap, width, 20, 100); }
	public Ruler(int gap, int width, int height, int scale)
	{
		super();
		this.GapBeforeInPixels = gap;
		this.GapAfterInPixels = gap;
		this.MaxLengthInPixels = width;
		this.MaxHeight = height;
		this.scale = scale;
		this.observers = new InstanciableObservable();
		Ruler.unit = 1;
		this.resetSize();
		this.setVisible(true);
	}
	public void addNotify()
	{
		System.out.println("Ruler.addNotify() " + this.getParent().getInsets());
		this.resetSize();
		this.revalidate();
		this.repaint(this.getVisibleRect());
		this.setChanged();
		this.notifyObservers(this);
		//this.resetSize();
		//Sensor.setScale();
	}
	
	public int getScale() { return this.scale; }
	
	/**
	 * recalculates the Units in Pixels and returns the resulting value;
	 * @return
	 */
	public int getUnit()
	{
		Ruler.unit = (this.getWidth() - (2*this.getGap()))/scale;
		if(Ruler.unit < 1) Ruler.unit = 1;
		return Ruler.unit; 
	}
	public int getGap(){ return this.GapBeforeInPixels; }
	public int getMaxLength() { return this.MaxLengthInPixels; }
	public int getPixelsUnit() 
	{ 
		if (MaxLengthInPixels/scale > 1) 
			return MaxLengthInPixels/scale; 
		else 
		{ 
			resetScale(); 
			return 1;
		} 
	}
	public void resetScale()
	{
		int length;
		if(this.getParent() == null)
			length = MINWIDTH;
		else
			length = this.getParent().getSize().width;
		//if(this.GapBeforeInPixels + this.MaxLengthInPixels + this.GapAfterInPixels > length) 
		this.MaxLengthInPixels = length - (this.GapBeforeInPixels + this.GapAfterInPixels);
		/*
		if (MaxLengthInPixels < scale)
		{
			scale = MaxLengthInPixels;
		}
		*/
	}
	public void resetSize()
	{
		Dimension minSize, newSize;
		Rectangle parentSize = new Rectangle(0,0,400,100); // assuming a minimum parent size
		minSize = new Dimension(MINWIDTH, MINHEIGHT);
		if(this.getParent()!= null) {
			parentSize = ((JComponent)this.getParent()).getBounds() ; 
			newSize = new Dimension(parentSize.width, this.MaxHeight);
		}
		else
		{
			newSize = minSize;
		}
		if(newSize.width < minSize.width) newSize.width = minSize.width;
		if(newSize.height < minSize.height) newSize.height = minSize.height;
		System.out.println("Ruler(): " + "ParentSize: "+parentSize);
		Rectangle newBounds = new Rectangle(0, parentSize.height-this.MaxHeight, parentSize.width, this.MaxHeight);
		this.setBounds(newBounds);
		//System.out.println("Ruler(): " + "newBounds: "+newBounds);
		this.setSize(newSize);
		this.setMaximumSize(newSize);
		this.setPreferredSize(newSize);
		this.setMinimumSize(minSize);
		if(this.getParent()!=null){
			this.getParent().revalidate();
			Rectangle p = this.getParent().getBounds();
			this.getParent().repaint(p.x,p.y,p.width,p.height);
		}
		this.resetScale();
	}
	
	public void paintComponent(Graphics g)
	{
		g.clearRect(this.getVisibleRect().x, this.getVisibleRect().y, this.getVisibleRect().width, this.getVisibleRect().height);
		this.resetSize();
		super.paintComponent(g);
		int gap = this.GapBeforeInPixels;
		int unit = this.MaxLengthInPixels/100;
		//System.out.println("Ruler(): " + "gap: "+gap + "\tunit: " + unit + "\t g:" + g);
		int unitLength = this.MaxHeight / 3;
		int unitTickness = 1;
		int unitHalfTenthLength = this.MaxHeight / 2;
		int unitHalfTenthTickness = 2;
		int unitTenthLength = this.MaxHeight;
		int unitTenthTickness = 2;
		g.setColor(Color.RED);
		g.fillRect(this.GapBeforeInPixels, 0, 100* unit, this.MaxHeight/4);
		g.drawRect(this.GapBeforeInPixels, 0, 100* unit, this.MaxHeight/4);
		for(int i = 0; i <= 100; i ++)
		{
			if(i % 10 == 0) // drawing a line for every 10 units;
			{
				g.setColor(Color.red);
				g.fillRect(gap+(i*unit)-(unitTenthTickness/2), 0, unitTenthTickness, unitTenthLength);
				g.drawRect(gap+(i*unit)-(unitTenthTickness/2), 0, unitTenthTickness, unitTenthLength);
			}
			else if(i % 5 == 0) // drawing a line for every 5 units
			{
				g.setColor(Color.pink);
				g.fillRect(gap+(i*unit)-(unitHalfTenthTickness/2), 0, unitHalfTenthTickness, unitHalfTenthLength);
				g.drawRect(gap+(i*unit)-(unitHalfTenthTickness/2), 0, unitHalfTenthTickness, unitHalfTenthLength);				
			}
			else // drawing a line for every unit
			{
				g.setColor(Color.black);
				g.fillRect(gap+(i*unit)-(unitTickness), 0, unitTickness, unitLength);
				g.drawRect(gap+(i*unit)-(unitTickness), 0, unitTickness, unitLength);
			}
		}
	}
	@Override
	public void componentHidden(ComponentEvent arg0) {
		this.getComponentGraphics(null).clearRect(this.getVisibleRect().x, this.getVisibleRect().y, this.getVisibleRect().width, this.getVisibleRect().height);
	}
	@Override
	public void componentMoved(ComponentEvent arg0) {
		this.repaint(this.getVisibleRect());
	}
	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		this.getParent().repaint();
		this.repaint();
		observers.setChanged();
		observers.notifyObservers(this);
	}
	@Override
	public void componentShown(ComponentEvent arg0) {
		this.getParent().repaint();
		this.repaint();
	}
	public void addObserver(Observer o)
	{ 
		observers.addObserver(o); 
	}
	
	/**
	 * implementing Observable Method clearChanged()
	 */
	protected synchronized void clearChanged() {
		observers.clearChanged();
	}
	/**
	 * implementing Observable Method countObservers()
	 */
	public synchronized int countObservers() {
		return observers.countObservers();
	}
	/**
	 * implementing Observable Method deleteObserver()
	 */
	public synchronized void deleteObserver(Observer o) {
		observers.deleteObserver(o);
	}
	/**
	 * implementing Observable Method deleteObservers()
	 */
	public synchronized void deleteObservers() {
		observers.deleteObservers();
	}
	/**
	 * implementing Observable Method hasChanged()
	 */
	public synchronized boolean hasChanged() {
		return observers.hasChanged();
	}
	/**
	 * implementing Observable Method notifyObservers()
	 */
	public void notifyObservers() {
		observers.notifyObservers();
	}
	/**
	 * implementing Observable Method notifyObservers()
	 */
	public void notifyObservers(Object arg) {
		observers.notifyObservers(arg);
	}
	/**
	 * implementing Observable Method setChanged()
	 */
	protected synchronized void setChanged() {
		observers.setChanged();
	}
}
