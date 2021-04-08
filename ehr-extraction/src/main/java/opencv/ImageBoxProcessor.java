package opencv;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ImageBoxProcessor {
	public static void main(String[] args) {
		//media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/image-600.png;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		/*
		File folder=new File("/media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/");
	   
		for(File fileEntry:folder.listFiles()) {
			
			TextPreProcessor processor = new TextPreProcessor();
	        //System.out.println(fileEntry.getAbsolutePath());
			processor.proccess(fileEntry.getAbsolutePath());
	        
	        //break;
	        
		}
		*/
		int page=1;
		
		String folder="/media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/";
		
		while(true) {
			System.out.println("============");
			
			String stringPage=String.format("%03d",page);
			
			String configFile=folder+"image-"+stringPage+"-segments.xml";
			
			System.out.println("============ "+configFile);
			
			ObjectMapper objectMapper = new XmlMapper();
			
			try {
				
				OpenCVXMLPartition segments=new OpenCVXMLPartition();
				
				List<OpenCVXMLPartition.XMLSegment> segs=new ArrayList<>();
				
				segments.setSegment(segs);
				
				for(Rectangle2D rect:FindBoxes.proccess(new File("/media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/image-"+stringPage+".png").getAbsolutePath())) {
					
					segs.add(new OpenCVXMLPartition.XMLSegment(rect.getMinX(),rect.getMinY(),rect.getWidth(),rect.getHeight()));
					
					
				}
			
				objectMapper.writeValue(new File(configFile),segments);
			
		
			} catch (Exception e) {
				
				e.printStackTrace();
				break;
			} 
			
			page++;
			
		}
		System.out.println("finished");
		
	}
	
}
