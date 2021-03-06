import java.awt.*;

import javax.swing.JPanel;

public class Graph extends JPanel {
	int startPoint = 25;
	int x = startPoint;
	int nodeSize = 50;
	int nodeRange = 50;
	/*Node takes in two parameters: the starting position, and range */
	Node testNode = new Node(25 + (int)(Math.random() * 725), nodeRange);
	Node testNode2 = new Node(25 + (int)(Math.random() * 725), nodeRange);
	Node testNode3 = new Node(25 + (int)(Math.random() * 725), nodeRange);


	

	//int y = 1;

	private Main3203 main;
	
	public Graph(Main3203 main){
		this.main = main;
	}
	
	
	public void paint(Graphics g){
		
		Node[] nodeArray = new Node[3];
		
		nodeArray[0] = testNode;
		nodeArray[1] = testNode2;
		nodeArray[2] = testNode3;
		super.paintComponent(g);
		g.drawLine(0, 275, 800, 275);
		
		for (int i = 0;i<nodeArray.length;i++){
			g.fillOval(nodeArray[i].updatePosition(), 250, nodeSize, nodeSize);
			g.drawOval(nodeArray[i].updatePosition()-25, 225, 100, 100);
			//g.fillOval(testNode2.updatePosition(), 250, nodeSize, nodeSize);
			//g.fillOval(testNode3.updatePosition(), 250, nodeSize, nodeSize);
		}
		
		//

	}
	
	void Update(){
		
		repaint();

	}
}
	
