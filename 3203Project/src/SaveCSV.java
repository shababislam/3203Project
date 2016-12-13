
public class SaveCSV {


	public static String CSVHeaderGen(){
	 return "Initial Position,Final Position,Radius,Distance Moved,Algorithm,Initial Overlap,Final Overlap,Initial Gap,Final Gap";
	}
	
	
	public static String areaCSVData(Area thisArea, String algorithm, int scale){
		String initPos = "";
		String finalPos = "";
		String radius = "";
		String distanceMoved = "";
		String initialOverlap = "";
		String finalOverlap = "";
		String initialGap = "";
		String finalGap = "";
		
		for(int i = 0;i<thisArea.size();i++){
			initPos+=String.valueOf(Sensor.round(thisArea.get(i).getInitialPos()*scale,2))+",";
	
			finalPos+=String.valueOf(Sensor.round(thisArea.get(i).getPos()*scale,2))+",";				
		}
		
		radius = String.valueOf(Sensor.round(thisArea.get(0).getRadius()*scale,2));
		
		distanceMoved = String.valueOf(Sensor.round(thisArea.getMovingCost()*scale,2));
		
		initialOverlap = String.valueOf(Sensor.round(thisArea.getPreviousOverlaps()*scale,2));
		finalOverlap = String.valueOf(Sensor.round(thisArea.getAreaOverlaps()*scale,2));
		
		initialGap = String.valueOf(Sensor.round(thisArea.getPreviousGaps()*scale,2));
		finalGap = String.valueOf(Sensor.round(thisArea.getAreaGaps()*scale,2));
		
	
		
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
