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
import util.lineParser.PagePartition;
import util.lineParser.TextLine;
import util.lineParser.TextLineWord;

public class JsoupDemo {
	public static void main(String[] args) throws TesseractException {
    	
        String location="/media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/image-600-proc_struct_font.hocr";
       
        File file=new File(location);
       
        try {
			Document doc=Jsoup.parse(file,"UTF-8");
			
			List<TextLine> lines=new ArrayList<>();
			
			for(Element segment:doc.getElementsByClass("ocr_par")) {
				
				List<String> segmentTitleAttr=Arrays.asList(segment.attr("title").split(";"));
				List<String> segmentBounds = RegexTools.getMatches(segmentTitleAttr.get(0).trim(),"(\\d+)");
				
				PagePartition partition = PagePartition.create(
						Double.valueOf(segmentBounds.get(0)), 
						Double.valueOf(segmentBounds.get(1)), 
						Double.valueOf(segmentBounds.get(2)), 
						Double.valueOf(segmentBounds.get(3)));
				
				for(Element line:segment.children()) {
					
					List<String> lineTitleAttr=Arrays.asList(line.attr("title").split(";"));
					List<String> lineBounds = RegexTools.getMatches(lineTitleAttr.get(0).trim(),"(\\d+)");
					
					double lineY=Double.valueOf(lineBounds.get(1));
					double lineHeight=Double.valueOf(lineBounds.get(3))-lineY;
					
					TextLine textLine=TextLine.create(partition, lineY, lineHeight);
					
					for(Element word:line.children()) {
						if(!word.hasText()) {
							continue;
						}
						
						//System.out.println(word);
						
						List<String> wordTitleAttr=Arrays.asList(word.attr("title").split(";"));
						List<String> wordBounds = RegexTools.getMatches(wordTitleAttr.get(0).trim(),"(\\d+)");
						
						double y=Double.valueOf(wordBounds.get(1));
						double height=Double.valueOf(wordBounds.get(3))-y;
						double x=Double.valueOf(wordBounds.get(0));
						double width=Double.valueOf(wordBounds.get(2))-x;
					
						double fontSize=Double.valueOf(RegexTools.getMatches(word.attr("title"),"(?<=x_fsize\\s)(\\d+)").get(0).trim());
						
						TextLineWord textLineWord=TextLineWord.create(
								x,
								y,
								width,
								height,
								word.text(),
								fontSize,
								textLine);
						
						textLine.addWord(textLineWord);
					}
					
					lines.add(textLine);
				}
			}
		
			Collections.sort(lines,(a,b)->a.getY().compareTo(b.getY()));
			
			
			for(TextLine line:lines) {
				if(line.getTallestWord()>10) {
					System.out.println(line);
				}
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
