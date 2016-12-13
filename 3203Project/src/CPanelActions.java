import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class CPanelActions implements ActionListener {
	private Simulator simulator;
	private AreaComponent areaComponent;
	private Cpanel cp;
	
	private CPanelActions(){}
	
	public CPanelActions(Cpanel cp, Simulator s, AreaComponent a)
	{
		this();
		this.simulator = s;
		this.areaComponent = a;
		this.cp = cp;
		
	}
	
	public void init()
	{
		System.out.println("Initialize Clicked");
	}
	
	public void randReset(){System.out.println("Random Reset Clicked");}
	
	public void reset(){System.out.println("Reset Clicked");}
	
	public void simple(){System.out.println("Simple Algorithm Clicked");}
	
	public void rigid(){System.out.println("Rigid Algorithm Clicked");}
	
	public void twoWay(){System.out.println("Elemination Algorithm Clicked");}
	
	public void silentRun(){System.out.println("Silent Run Clicked");}
	
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
