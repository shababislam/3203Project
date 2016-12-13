import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Cpanel extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2085047586268860629L;
	public JFrame frame; // reference to parent frame
	public Simulator simulator;
	public AreaComponent area;
	public GridLayout layout;
	public JButton btnInitiate; // to construct a simulator object
	public JButton btnReset; // to reset back to initialized state of the simulator
	public JButton btnRandReset; // to initiate the simulator object with another random set of sensors
	public JButton btnSimpleRun ; // to run with Simple Algorithm
	public JButton btnRigidRun ; // to run with Rigid Algorithm
	public JButton btnNewRun ; // in case we have 3rd method
	public JLabel lblNumSensors;
	public JLabel lblSensorRadius;
	public JTextField txtNumSensors;
	public JTextField txtSensorRadius;
	public static CPanelActions actions;
	public Cpanel()
	{
		// initializing and setting the layout manager
		int rows = 2;
		int cols = 5;
		int spacing = 3; // 3px spacing between components
		
		this.layout = new GridLayout(rows,cols, spacing,spacing);
		this.setLayout(this.layout);
		// initializing the layout components
		this.btnInitiate = new JButton("Initialize");
		this.btnReset = new JButton("Reset");
		this.btnRandReset = new JButton("Random Reset");
		this.btnSimpleRun = new JButton("Simple Algorithm");
		this.btnRigidRun = new JButton("Rigid Algorithm");
		this.btnNewRun = new JButton("Our New Algorithm");
		this.lblNumSensors = new JLabel("Number of Sensors");
		this.lblSensorRadius = new JLabel("Sensor Radius");
		this.txtNumSensors = new JTextField("5", 3); //textField is set to maximum 3 characters
		this.txtSensorRadius = new JTextField("0.10", 4 ); // textField is Set to maximum 4 characters
		// setting the Constrains and Action Listeners
		
		// adding the components to this layout
		this.add(this.lblNumSensors);
		this.add(this.txtNumSensors);
		this.add(this.lblSensorRadius);
		this.add(this.txtSensorRadius);
		this.add(this.btnInitiate);
		this.add(this.btnSimpleRun);
		this.add(this.btnRigidRun);
		this.add(this.btnNewRun);
		this.add(this.btnRandReset);
		this.add(this.btnReset);
	}
	
	public Cpanel(JFrame f, Simulator s, AreaComponent a)
	{
		this();
		this.frame = f;
		this.simulator = s;
		this.area = a;
		// setting the actionListeners
		Cpanel.actions = new CPanelActions(this, s,a);
		for(int i = 0; i < this.getComponentCount(); i ++)
			if(this.getComponent(i) instanceof JButton)
				((JButton)this.getComponent(i)).addActionListener(actions);
		
		
	}
	

}
