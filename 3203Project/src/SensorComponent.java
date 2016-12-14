import javax.swing.JComponent;

import java.awt.Color;
//import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Observable;
import java.util.Observer;

public class SensorComponent extends JComponent implements Observer,ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 785677238396101964L;
	private Sensor sensor;
	public static int MaxAreaLengthInPixels = 800;
	public static final int SPACING = 15; // spacing between the component and the ruler;
	private static int SCALE = 100;
	private int unit; // how many pixels are each 1/100
	private Ruler ruler;
	private int pxRadius, centerDiameter, centerX, centerY, prefWidth, prefHeight;
	//private Component parent;
	 
	private SensorComponent(){super();}
	public SensorComponent(Sensor s)
	{ this(s,null); }
	public SensorComponent(Sensor s, Ruler r)
	{
		this();
		this.sensor = s;
		this.sensor.addObserver(this);
		this.ruler = r;
		this.ruler.addObserver(this);
		this.unit = this.ruler.getPixelsUnit();
		this.setBackground(new Color(0,0,0,0));
		this.resetSize();
	}
	public int getRadiusInPixels(){ return (int)(this.sensor.getRadius() * SensorComponent.SCALE * this.unit); }
	public int getPositionInPixels(){ return ((int)(this.sensor.getPos() * SensorComponent.SCALE))* this.unit; }
	public int getStartPositionInPixels(){ return this.getPositionInPixels() - this.getRadiusInPixels(); }
	public int getHeight(){ return this.getSize().height; }
	
	public Sensor getSensor(){ return this.sensor; }
	public Ruler getRuler(){ return this.ruler; }
	
	private void resetSize()
	{
		SensorComponent.SCALE = this.ruler.getScale();
		pxRadius = this.getRadiusInPixels();
		centerDiameter = (int)(pxRadius * 0.1);
		if(centerDiameter <=6) centerDiameter = 10;
		prefWidth = 2* pxRadius;
		prefHeight = 2* centerDiameter;
		Dimension size = new Dimension(prefWidth, prefHeight); // Resetting the dimensions in case it was changed by layout managers or due to resizing window
		this.setSize(size);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		centerX = getWidth()/2;
		centerY = getHeight()/2;
		this.resetBounds();
	}
	public void paintComponent(Graphics g)
	{
		//int pxRadius, centerDiameter, centerX, centerY, prefWidth, prefHeight;
		this.resetSize();
		//g.clearRect(getVisibleRect().x, getVisibleRect().y, getVisibleRect().width, getVisibleRect().height);
		super.paintComponent(g);
		//drawing the coverage
		if(sensor.isSelected()) g.setColor(Color.green);
		else g.setColor(Color.blue);
		g.fillRect(centerX-pxRadius, centerY-(centerDiameter/2) , 2*pxRadius, centerDiameter);
		g.setColor(Color.red);
		g.fillOval(centerX-centerDiameter, centerY-centerDiameter, 2* centerDiameter, 2* centerDiameter);
		g.setColor(Color.BLACK);
		g.drawRect(centerX-pxRadius, centerY-(centerDiameter/2) , 2*pxRadius, centerDiameter);
		g.drawOval(centerX-centerDiameter, centerY-centerDiameter, 2* centerDiameter, 2* centerDiameter);
		
	}
	private void resetBounds()
	{
		this.unit = this.ruler.getPixelsUnit();
		Rectangle newBound = new Rectangle();
		Rectangle parentBound = new Rectangle(0,0,AreaComponent.MINWIDTH,AreaComponent.MINHEIGHT); // assuming the default parent size;
		JComponent Parent = (JComponent)this.getParent();
		if(Parent != null)
			parentBound = Parent.getBounds();
		int ComponentHeight = this.getRuler().getHeight()+ SPACING +this.centerDiameter;
		newBound.y = parentBound.height - ComponentHeight;
		if(newBound.y < 0) newBound.y = 0;
		newBound.x = this.ruler.getGap() + this.getStartPositionInPixels();
		newBound.height = this.centerDiameter;
		newBound.width = this.getRadiusInPixels()*2;
		//System.out.println("SensorComponenet(): " + "newBound: " + newBound);
		this.setBounds(newBound);
		if(this.getParent()!=null){
			Parent.revalidate();
			Parent.repaint(Parent.getBounds());
		}
		//System.out.println("resetbounds: " + this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if(o instanceof Sensor)
		{
			if(arg.equals("moved"))
				this.updateMe(this.isVisible());
			else
				this.repaint(this.getVisibleRect());
		}
		if(arg instanceof Ruler)
		{
			this.unit = this.ruler.getPixelsUnit();
			SensorComponent.SCALE = this.ruler.getScale();
			repaint(getVisibleRect());
		}
		
	}
	
	public String toString()
	{
		return "SensorComponent(): "+"Unit: "+this.ruler.getUnit() + "getBounds(): " + this.getBounds();
	}
	private void updateMe(boolean setVisible)
	{	
		this.unit = this.ruler.getUnit();
		Rectangle visibleRect = this.getVisibleRect();
		this.setVisible(setVisible);
		this.resetBounds();
		this.repaint(visibleRect);

	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		updateMe(false);
	}
	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		this.updateMe(true);
	}
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		this.updateMe(true);
	}
	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		this.updateMe(true);
	}

}
