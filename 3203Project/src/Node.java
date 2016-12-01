import java.util.Random;

public class Node {
	private int initPos, xPos, range;
	//boolean ret = false;
	Random random = new Random();

	boolean ret = random.nextBoolean();
	
	public Node(int xPos, int range){
		this.xPos = xPos;
		this.initPos = xPos;
		this.range = range;
	}
	
	public int updatePosition(){
		if(!ret){	
			xPos++;
			if(xPos>=initPos+range){
				ret = true;
			}
		} 
		if (ret){
			xPos--;
			if(xPos<=initPos-range){
				ret = false;
			}
		}
		return xPos;
	}
	
	
}
