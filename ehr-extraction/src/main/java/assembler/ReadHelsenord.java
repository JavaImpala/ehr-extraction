package assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import util.pageProcessor.Page;
import util.struct.StructPage;

public class ReadHelsenord {
	
	public static void main(String[] args ){  
		
		String path="/media/tor003/kingston/hnuttrekk.sql";
		
		FileReader reader;
		try {
			
			BufferedReader lines=new BufferedReader(new InputStreamReader(new FileInputStream(path),"WINDOWS-1252"));
			
			String line="not set";
			
			int counter=0;
			
			while((line=lines.readLine())!=null) {
				System.out.println(line);
				
				counter++;
				
				if(counter>200) {
					break;
				}
			}
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
    }  
	
	private static class PageLineSupplier{
		
		
		private PageLineSupplier(String location,Consumer<Page> listener) {
			
			int pageNumber=200;
			
			while(true) {
				try {
					System.out.println("leser side: "+pageNumber);
					listener.accept(
							new Page(
									fileToTextLines(location+"/image-"+String.format("%03d",pageNumber)+"-proc_psm1.txt"),
									fileToTextLines(location+"/image-"+String.format("%03d",pageNumber)+"-proc_psm6.txt"),
									new StructPage(new File(location+"/image-"+String.format("%03d",pageNumber)+"-proc_struct.hocr"),new File(location+"/image-"+String.format("%03d",pageNumber)+"-segments.xml"))));
					pageNumber++;
					
					
				}catch(Exception e) {
					System.out.println("break at page: "+pageNumber);
					e.printStackTrace();
					break;
					//e.printStackTrace();
				}
			}
			
		}
		
		private List<String> fileToTextLines(String path) throws Exception{
			FileReader reader=new FileReader(path);
			List<String> lines=new BufferedReader(reader).lines().collect(Collectors.toList());
		
			reader.close();
			
			return lines;
		}
	}
}
