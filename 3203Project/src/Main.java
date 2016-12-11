import java.awt.Rectangle;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame f = new JFrame("Swing Paint Demo");
        f.setSize(AreaComponent.MINWIDTH*2+40, AreaComponent.MINHEIGHT*2+40);
		System.out.println("Frame Initialized...!");
		Cpanel cp = new Cpanel();
		Simulator s = new Simulator(5, 0.1, 100, 1,f);
		System.out.println("Simulator Initialized...!");
		AreaComponent a = new AreaComponent(s.getMethod1());
		System.out.println("AreaComponenet Initialized...!");
		Rectangle r = new Rectangle(20,20,a.MINWIDTH*2,a.MINHEIGHT*2);
		f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		f.add(cp);
        f.add(a);
        f.pack();
        f.setVisible(true);
        s.updateFrame();
        try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for(int i = 0; i< s.getMethod1().size(); i ++)
        	System.out.println("Sensor[" + i + "] : " + s.getMethod1().get(i));

        s.run(); System.out.println("Simulation is done!");
        
        for(int i = 0; i< s.getMethod1().size(); i ++)
        	System.out.println("Sensor[" + i + "] : " + s.getMethod1().get(i));
	}

}

/**
 *  Class Sensor (compeleted)
 *  Class Area extends Set (done)
 *  class Controller
 *  Class Sensor View
 *  
 */
