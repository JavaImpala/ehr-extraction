package assembler;
import java.io.File;

import org.opencv.core.Core;

import opencv.TextPreProcessor;

public class ImagePreProcessor {
	public static void main(String[] args) {
		//media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/image-600.png;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//File folder=new File("/media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/");
	   
		File folder=new File("/media/tor003/USB DISK/forskninguttrekkKF050321full-psm1");
		
		for(File fileEntry:folder.listFiles()) {
			
			TextPreProcessor processor = new TextPreProcessor();
	        System.out.println(fileEntry.getAbsolutePath());
			processor.proccess(fileEntry.getAbsolutePath());
	        
	        //break;
	        
		}
		
		/*
		TextPreProcessor processor = new TextPreProcessor();
        //System.out.println(fileEntry.getAbsolutePath());
		processor.proccess(new File("/media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/image-600.png").getAbsolutePath());
		*/
	}
}
