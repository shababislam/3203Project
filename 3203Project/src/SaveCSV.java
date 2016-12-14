import java.awt.Component;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SaveCSV {
	private static String filename;
	private static String fileData;
	private static final String EXTENSION = ".csv";
	private static final char NL = '\n';
	
	public SaveCSV(){ SaveCSV.filename = "outputData";  this.fileData = "";}
  
	public static void setFilename(String file)
	{
		if(file.toLowerCase().contains(EXTENSION)) file.toLowerCase().replaceAll(EXTENSION, "");
		SaveCSV.filename = file;
	}
	public static String getFilename(){ return SaveCSV.filename; }
	
	public static void setFilenameGui(Component parent)
	{
	    JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Comma seperated CSV", "csv");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showSaveDialog(parent);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            chooser.getSelectedFile().getPath() +
	    		   chooser.getSelectedFile().getName());
	       filename = chooser.getSelectedFile().getPath();
	    }

<<<<<<< HEAD
	public static String CSVHeaderGen(){
	 return "Initial Position,Final Position,Radius,Distance Moved,Algorithm,Initial Overlap,Final Overlap,Initial Gap,Final Gap";
	}
	
	
	public static String areaCSVData(Area thisArea, String algorithm, int scale){
=======
	}
	
	public static void addAreaCSVData(Area thisArea, String algorithm)
	{
		if(fileData == "" || fileData == null) fileData = SaveCSV.CSVHeaderGen();
		fileData += SaveCSV.areaCSVData(thisArea, algorithm);
	}
	
	public static boolean SaveData()
	{
		boolean res = true;
		String fName = "output";
		if(filename != null) fName = new String(filename);
		if(!fName.toLowerCase().contains(EXTENSION)) fName += EXTENSION;
		try{
		    PrintWriter writer = new PrintWriter(fName, "UTF-8");
		    writer.println(fileData);
		    writer.close();
		} catch (IOException e) {
			res = false;
			e.printStackTrace();
		   // do something
		}
		
		return res;
	}
	
	private static String CSVHeaderGen(){
	 return "Initial Position,Final Position,Radius,Distance Moved,Algorithm,Initial Overlap,Final Overlap,Initial Gap,Final Gap" + NL;
	}
	
	
	private static String areaCSVData(Area thisArea, String algorithm){
>>>>>>> origin/master
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
		
		
		return '"'+initPos+'"'+','+'"'+finalPos+'"'+','+radius+','+distanceMoved+','+algorithm+','+initialOverlap+','+finalOverlap+','+initialGap+','+finalGap+NL;
	}
	
<<<<<<< HEAD
=======

>>>>>>> origin/master
}
