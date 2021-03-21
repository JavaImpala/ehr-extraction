import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableObject;

import reports.ProfilCarePlanParser;
import util.pageProcessor.PageParserManager;

public class Main {
	public static void main( String[] args )  
    {  
		try {
			String location="/media/tor003/6887-88BA/ABB/forskninguttrekkABB050321full";
			File folder=new File(location);
			
			//number-date(dd.mm.yyyy)-Skrevet av: [name]-Rapport-Rapportert dato: date(dd.mm.yyy
			
			int noteCounter=0;
		
		
			List<Supplier<PageParserManager>> pageParsers=new ArrayList<>();
			
			
			
			//lager reportMaker
			
			
			pageParsers.add(()->ProfilCarePlanParser.create());
			
			MutableObject<Optional<PageParserManager>> currentManager=new MutableObject<>(Optional.empty());	
			MutableObject<Runnable> closeCurrentResource=new MutableObject<>(()->{});
			
			for(File fileEntry:folder.listFiles()) {
				
				
				String type=Files.probeContentType(fileEntry.toPath());
				
				if(type.equals("text/plain")) {
					
						
					//FileReader reader=new FileReader(fileEntry);
					
					IOSupplier<Iterator<String>> getLines=()->{
						
						FileReader reader=new FileReader(fileEntry);
						
						closeCurrentResource.setValue(()->reader.close());
						
						return new BufferedReader(reader).lines().iterator();
						
					};
					
					//tries to see if current parser can take this page as well
					if(currentManager.getValue().isPresent()) {
						boolean success=currentManager
								.getValue()
								.get()
								.getPageParser()
								.tryToProccessPage(getLines);
						
						if(success) {
							
							//if success, skip to next page
							continue;
						}else {
							currentManager.setValue(Optional.empty());
						}
						
					}
						
					//if not sees if anybody else can
					//loops through pageparsers if one accept, keep it
					for(Supplier<PageParserManager> pageParser:pageParsers) {
						
						PageParserManager instance=pageParser.get();
						
						
						boolean success=instance
								.getPageParser()
								.tryToProccessPage(getLines);
						
						if(success) {
							System.out.println("initierer ny parser");
							currentManager.setValue(Optional.of(instance));
							break;
						}
					}
				}
			}
				
		}catch(Exception e) {
			e.printStackTrace();
		}
    }  
	
	interface IOSupplier<E> {
		public E get() throws Exception;
	}
}
