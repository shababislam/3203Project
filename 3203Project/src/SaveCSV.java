
public class SaveCSV {


	public static String CSVHeaderGen(){
	 return "Initial Position,Final Position,Radius,Distance Moved,Algorithm,Initial Overlap,Final Overlap,Initial Gap,Final Gap";
	}
	
	
	public static String areaCSVData(Area thisArea, String algorithm){
		String initPos = "";
		String finalPos = "";
		String radius = "";
		String distanceMoved = "";
		String initialOverlap = "";
		String finalOverlap = "";
		String initialGap = "";
		String finalGap = "";
		
		for(int i = 0;i<thisArea.size();i++){
			initPos+=String.valueOf(thisArea.get(i).getInitialPos())+",";
	
			finalPos+=thisArea.get(i).getPos();				
		}
		
		radius = String.valueOf(thisArea.get(0).getRadius());
		
		distanceMoved = String.valueOf(thisArea.getMovingCost());
		
		initialOverlap = String.valueOf(thisArea.getPreviousOverlaps());
		finalOverlap = String.valueOf(thisArea.getAreaOverlaps());
		
		initialGap = String.valueOf(thisArea.getPreviousGaps());
		finalGap = String.valueOf(thisArea.getAreaGaps());
		
	
		
		//if initPos or finalPos strings end with a comma, kill it	
		if (initPos != null && initPos.length() > 0 && initPos.charAt(initPos.length()-1)==',') {
	      initPos = initPos.substring(0, initPos.length()-1);
	    }
		
		if (finalPos != null && finalPos.length() > 0 && finalPos.charAt(finalPos.length()-1)==',') {
	      finalPos = finalPos.substring(0, finalPos.length()-1);
	    }
		
		
		return '"'+initPos+'"'+','+'"'+finalPos+'"'+','+radius+','+distanceMoved+','+algorithm+','+initialOverlap+','+finalOverlap+','+initialGap+','+finalGap;
	}
	
}
