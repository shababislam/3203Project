import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class CPanelActions implements ActionListener {
	private Simulator simulator;
	private AreaComponent areaComponent;
	private Cpanel cp;
	private Area area;
	
	private CPanelActions(){}
	
	public CPanelActions(Cpanel cp, Simulator s, AreaComponent a)
	{
		this();
		this.simulator = s;
		this.areaComponent = a;
		this.cp = cp;
		this.area = new Area();
		
	}
	
	public void init()
	{
		System.out.println("Initialize Clicked");
		return;
		/*
		this.simulator.reinstaciate(new Simulator(Integer.valueOf(cp.txtNumSensors.getText()), Double.valueOf(cp.txtSensorRadius.getText()), 100, 1, cp.frame));
		this.area = this.simulator.getMethod1();
		this.areaComponent.setArea(this.area);
		this.areaComponent.revalidate();
		this.areaComponent.repaint();
		*/
	}
	
	public void randReset()
	{
		System.out.println("Random Reset Clicked");
		this.init();
	}
	
	public void reset()
	{
		System.out.println("Reset Clicked");
		/*
		this.cp.frame.remove(this.areaComponent);
		this.areaComponent.setArea(new Area(this.simulator.getInitialState()));
		this.cp.frame.add(this.areaComponent);
		this.areaComponent.revalidate();
		this.areaComponent.repaint();
		this.cp.frame.revalidate();
		this.cp.frame.repaint();
		*/
	}
	
	public void simple()
	{
		System.out.println("Simple Algorithm Clicked");
		this.simulator.Simple(this.simulator.getMethod1());
	}
	
	public void rigid()
	{
		System.out.println("Rigid Algorithm Clicked");
		this.simulator.Regid(this.simulator.getMethod2());  

	}
	
	public void twoWay()
	{
		System.out.println("Elimination Algorithm Clicked");
		//this.simulator.Eliminiation(this.area);
	}
	
	public void silentRun()
	{
		System.out.println("Silent Run Clicked");
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg) {
		// TODO Auto-generated method stub
		JButton btn;
		if(arg.getSource() instanceof JButton)
		{
			btn = (JButton)arg.getSource();
			if(btn.getText().equalsIgnoreCase("Initialize"))
				this.init();
			if(btn.getText().equalsIgnoreCase("Simple Algorithm"))
				this.simple();
			if(btn.getText().equalsIgnoreCase("Rigid Algorithm"))
				this.rigid();
			if(btn.getText().equalsIgnoreCase("Elimination Algorithm"))
				this.twoWay();
			if(btn.getText().equalsIgnoreCase("Random Reset"))
				this.randReset();
			if(btn.getText().equalsIgnoreCase("Reset"))
				this.reset();
			if(btn.getText().equalsIgnoreCase("Silent Run"))
				this.silentRun();

		}

	}

}
