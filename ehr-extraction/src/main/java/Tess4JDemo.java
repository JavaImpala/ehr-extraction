import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class Tess4JDemo {
	

    public static void main(String[] args) throws TesseractException {
    	Tesseract tesseract = new Tesseract();
 
    	File tessDataFolder=LoadLibs.extractTessResources("tessdata");
 
    	tesseract.setDatapath(tessDataFolder.getAbsolutePath());
    	tesseract.setTessVariable("user_defined_dpi","600");
    	tesseract.setLanguage("eng");
    	tesseract.setHocr(true);
        
        
        
        String location="/media/tor003/6887-88BA/ABB/forskninguttrekkABB050321full-psm1";
       
        int page=600;
        
        File file=new File(location+"/image-"+page+"_psm1.hocr");
       
        try {
			Document doc=Jsoup.parse(file,"UTF-8");
			
			
			for(Element element:doc.getAllElements()) {
				System.out.println(element);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
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
