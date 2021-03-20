import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableObject;

import reports.ProfilCarePlanParser;
import util.pageProcessor.PageParserManager;

public class Main {
	public static void main( String[] args )  
    {  
		
		String location="/media/tor003/6887-88BA/ABB/forskninguttrekkABB050321full";
		File folder=new File(location);
		
		
		
		List<Supplier<PageParserManager>> pageParsers=new ArrayList<>();
		
		
		
		//lager reportMaker
		
		
		pageParsers.add(()->ProfilCarePlanParser.create());
		
		
		//number-date(dd.mm.yyyy)-Skrevet av: [name]-Rapport-Rapportert dato: date(dd.mm.yyy
		
		int noteCounter=0;
		
		MutableObject<Optional<PageParserManager>> currentManager=new MutableObject<>(Optional.empty());
		
		for(File fileEntry:folder.listFiles()) {
			
			
			try {
				String type=Files.probeContentType(fileEntry.toPath());
				
				if(type.equals("text/plain")) {
					
					try{
						
						FileReader reader=new FileReader(fileEntry);
						
						//tries to see if current parser can take this page as well
						if(currentManager.getValue().isPresent()) {
							boolean success=currentManager.getValue().get().getPageParser().tryToProccessPage(()->new BufferedReader(reader).lines().iterator());
							
							if(success) {
								reader.close();
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
							
							
							boolean success=instance.getPageParser().tryToProccessPage(()->new BufferedReader(reader).lines().iterator());
							
							if(success) {
								System.out.println("initierer ny parser");
								currentManager.setValue(Optional.of(instance));
								break;
							}
						}
						
						reader.close();
						
						
					}catch(Exception e) {
						e.printStackTrace();
					}
					
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		
    }  
}
