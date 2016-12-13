import javax.swing.JComponent;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

public class AreaComponent extends JComponent implements Observer, ComponentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6917262847034238282L;
	public static final int MINWIDTH = 400;
	public static final int MINHEIGHT = 200;
	private Area area;
	private Ruler ruler;
	private int unitInPixels;
	private static int gap = 50;
	private int MaxHeight;
	public AreaComponent(){}
	public AreaComponent(Area area)
	{
		super();
		this.setLayout(null);
		this.resetSize();
		Rectangle bounds = this.getBounds();
		AreaComponent.gap = (int) (bounds.width * 0.1); // setting the gap to 10% of the width
		if(AreaComponent.gap < 10) AreaComponent.gap = 25;
		this.ruler = new Ruler(gap, bounds.width);
		this.add(ruler);
		this.addComponentListener(ruler);
		//this.ruler.addComponentListener(this);
		//System.out.println("AreaComponenet(): " + "this.area.size: " + this.area.size());
		this.setArea(area);
		bounds.width = 1000;
		bounds.height = 200;
		this.MaxHeight = bounds.height;
		this.setBounds(bounds);
		this.revalidate();
		this.repaint();		
	} 
	public void setArea(Area area)
	{
		if (this.area != null)
		{
			this.area.deleteObserver(this);
			for (int i = 0; i < this.getComponentCount(); i++)
				if(this.getComponent(i) instanceof SensorComponent)
				{
					this.remove(this.getComponent(i));
					this.removeComponentListener((ComponentListener)this.getComponent(i));
				}
		}
		this.area = area;
		this.area.addObserver(this);
		for(int i = 0; i < this.area.size(); i++){
			this.add(this.area.get(i).generateComponent(this.ruler));
			this.addComponentListener((ComponentListener) this.area.get(i).getComponent());
			System.out.println("AreaComponenet(): [" + i + "] " + this.area.get(i).getComponent());
		}


	}
	
	public void addNotify()
	{
		this.getParent().addComponentListener(this);
		this.resetSize();
		this.ruler.resetSize();
		this.repaint(this.getVisibleRect());
	}
	
	public void resetSize()
	{
		Dimension minSize, newSize;
		Rectangle parentSize = new Rectangle(0,0,MINWIDTH,MINHEIGHT);
		if(this.getParent() != null)
		{
			parentSize = this.getParent().getBounds(); 
		}
		newSize = new Dimension(parentSize.width, this.MaxHeight);
		//Rectangle newBounds = new Rectangle(0, parentSize.height-this.MaxHeight, parentSize.width, this.MaxHeight);
		//this.setBounds(newBounds);
		minSize = new Dimension(MINWIDTH, MINHEIGHT);
		if(newSize.width < minSize.width) newSize.width = minSize.width;
		if(newSize.height < minSize.height) newSize.height = minSize.height;
		//System.out.println("AreaComponent(): " + "newSize: "+newSize);
		//this.setSize(newSize);
		//this.setMaximumSize(newSize);
		this.setPreferredSize(minSize);//newSize);
		this.setMinimumSize(minSize);
		this.revalidate();
		this.repaint(this.getVisibleRect());
		//if(this.getRootPane()!=null) System.out.println("AreaComponent(): " + "rootpane: "+this.getRootPane().getSize());

	}
	
//	public void paintComponent(Graphics g)
//	{
//		for(int i = 0; i < this.getComponentCount(); i ++)
//		{
//			this.getComponent(i).update(g);
//			System.out.println("AreaComponent(): " + "["+i+"]" + this.getComponent(i));
//		}
//	}
	
	@SuppressWarnings("unused")
	private void readjustLayout()
	{
		if(ruler.getPixelsUnit() == this.unitInPixels) return;
		this.unitInPixels = this.ruler.getPixelsUnit();
		this.removeAll();
		SensorComponent s;
		for(int i = 0; i < this.area.size(); i ++)
		{
			// should reposition every component and redraw;
			s = (SensorComponent)area.get(i).getComponent();
			this.add(s);
		}
	}
	@Override
	public void update(Observable arg0, Object arg) {
		// TODO Auto-generated method stub
		if(arg instanceof Ruler)
		{
			this.unitInPixels = ((Ruler)arg).getPixelsUnit();
			this.repaint(this.getVisibleRect());
		}
		if(arg instanceof ObservableObject)
		{
			if(((ObservableObject)arg).message.equals("NewSensor"))
			{
				Sensor s = ((Sensor)((ObservableObject)arg).object);
				s.generateComponent(this.ruler);
				this.add(s.getComponent());
			}
		}
	}
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentMoved(ComponentEvent e) {
		this.unitInPixels = (this.ruler.getPixelsUnit());
		this.repaint(this.getVisibleRect());
		
	}
	@Override
	public void componentResized(ComponentEvent e) {
		this.unitInPixels = (this.ruler.getPixelsUnit());
		this.repaint(this.getVisibleRect());
	}
	@Override
	public void componentShown(ComponentEvent e) {
		//this.ruler.componentShown(e);
		this.unitInPixels = (this.ruler.getPixelsUnit());
		this.repaint(this.getVisibleRect());
	}
}
