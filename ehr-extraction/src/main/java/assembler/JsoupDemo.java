package assembler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.sourceforge.tess4j.TesseractException;
import util.RegexTools;
import util.lineParser.HocrPartition;
import util.lineParser.TextLine;
import util.lineParser.TextLineWord;

public class JsoupDemo {
	public static void main(String[] args) throws TesseractException {
    	
        String location="/media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/image-600-proc_struct_font.hocr";
       
        File file=new File(location);
       
        
        
        //Leptonica leptInstance = Leptonica.INSTANCE;
        //Pix pix = leptInstance.pixRead(file.getPath());
        
     
        
        // tesseract.createDocumentsWithResults("",null, null, 1).getWords().forEach(w->w.);
        
        /*
         * 
         
		for(File fileEntry:folder.listFiles()) {
			
			String result = tesseract.doOCR(fileEntry);
	        System.out.println(result);
		}
		*/
    }
}
