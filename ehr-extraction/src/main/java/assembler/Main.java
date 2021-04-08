package assembler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import messages.ProfilMessageParserManager;
import reports.ProfilCarePlanPageParserManager;
import util.pageProcessor.Page;
import util.pageProcessor.PageParserManager;
import util.struct.StructPage;

public class Main {
	
	private final static Logger log=LogManager.getLogger(Main.class.getSimpleName());
	
	public static void main(String[] args ){  
		
		log.info("starting application");
		
		String location="/media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1";
		
		//number-date(dd.mm.yyyy)-Skrevet av: [name]-Rapport-Rapportert dato: date(dd.mm.yyy
		
		List<Supplier<PageParserManager>> pageParsers=new ArrayList<>();
		pageParsers.add(()->ProfilCarePlanPageParserManager.create());
		pageParsers.add(()->ProfilMessageParserManager.create());
		
		//lager reportMaker
		MutableObject<Optional<PageParserManager>> currentManager=new MutableObject<>(Optional.empty());	
		
		MutableInt counter=new MutableInt();
		
		new PageLineSupplier(
				location,
				pageLineIteratorSupplier->{
					
					
					
					counter.increment();
					
					log.info("");
					log.info("mottar side");
					
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
		
		
		private PageLineSupplier(String location,Consumer<Page> listener) {
			
			int pageNumber=600;
			
			while(true) {
				try {
					
					listener.accept(
							new Page(
									fileToTextLines(location+"/image-"+String.format("%03d",pageNumber)+"-proc_psm1.txt"),
									fileToTextLines(location+"/image-"+String.format("%03d",pageNumber)+"-proc_psm6.txt"),
									new StructPage(new File(location+"/image-"+String.format("%03d",pageNumber)+"-proc_struct_font_300.hocr"),new File(location+"/image-"+String.format("%03d",pageNumber)+"-segments.xml"))));
					pageNumber++;
					
					break;
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
