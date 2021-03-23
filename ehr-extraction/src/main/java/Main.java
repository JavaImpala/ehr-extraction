import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import reports.ProfilCarePlanParser;
import util.pageProcessor.PageParserManager;

public class Main {
	
	private final static Logger log=LogManager.getLogger(Main.class.getSimpleName());
	
	public static void main(String[] args ){  
		
		log.info("starting application");
		
		String location="/media/tor003/6887-88BA/ABB/forskninguttrekkABB050321full";
		
		//number-date(dd.mm.yyyy)-Skrevet av: [name]-Rapport-Rapportert dato: date(dd.mm.yyy
		
		List<Supplier<PageParserManager>> pageParsers=new ArrayList<>();
		pageParsers.add(()->ProfilCarePlanParser.create());
		
		//lager reportMaker
		MutableObject<Optional<PageParserManager>> currentManager=new MutableObject<>(Optional.empty());	
		
		MutableInt counter=new MutableInt();
		
		new PageLineSupplier(
				location,
				pageLineIteratorSupplier->{
					
					if(counter.getValue()>1000) {
						return;
					}
					
					counter.increment();
					
					log.info("");
					log.info("mottar side");
					
					System.out.println("ny side");
					
					if(currentManager.getValue().isPresent()) {
						log.info("leser side i current pageSupplier "+currentManager.getValue().get().getClass());
						
						boolean success=currentManager
								.getValue()
								.get()
								.getPageParser()
								.tryToProccessPage(pageLineIteratorSupplier);
						
						if(success) {
							log.info("klarte det, går til neste");
							//if success, skip to next page
							return;
						}else {
							log.info("klarte det ikke");
							currentManager.setValue(Optional.empty());
						}
						
					}
						
					//if not sees if anybody else can
					//loops through pageparsers if one accept, keep it
					for(Supplier<PageParserManager> pageParser:pageParsers) {
						PageParserManager instance=pageParser.get();
						
						log.info("forsøker å lese side med med ny pageparsermanager "+instance.getClass());
						
						boolean success=instance
								.getPageParser()
								.tryToProccessPage(pageLineIteratorSupplier);
						
						if(success) {
							log.info("klarer det!");
							currentManager.setValue(Optional.of(instance));
							return;
						}else {
							log.info("klarer det ikke");
						}
					}
					
					log.info("klarte ikke å lese side");
				});
    }  
	
	private static class PageLineSupplier{
		
		
		private PageLineSupplier(String location,Consumer<Supplier<Iterator<String>>> listener) {
			
			try {
				File folder=new File(location);
				
				for(File fileEntry:folder.listFiles()) {
					
					String type=Files.probeContentType(fileEntry.toPath());
					
					if(type.equals("text/plain")) {
						FileReader reader=new FileReader(fileEntry);
						List<String> lines=new BufferedReader(reader).lines().collect(Collectors.toList());
						reader.close();
						
						Supplier<Iterator<String>> getLines=()->{	
							return lines.iterator();	
						};
						
						listener.accept(getLines);
						
						reader.close();
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
