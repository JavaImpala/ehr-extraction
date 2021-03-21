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

import org.apache.commons.lang3.mutable.MutableObject;

import reports.ProfilCarePlanParser;
import util.pageProcessor.PageParserManager;

public class Main {
	public static void main(String[] args ){  
		
		String location="/media/tor003/6887-88BA/ABB/forskninguttrekkABB050321full";
		
		//number-date(dd.mm.yyyy)-Skrevet av: [name]-Rapport-Rapportert dato: date(dd.mm.yyy
		
		List<Supplier<PageParserManager>> pageParsers=new ArrayList<>();
		pageParsers.add(()->ProfilCarePlanParser.create());
		
		//lager reportMaker
		MutableObject<Optional<PageParserManager>> currentManager=new MutableObject<>(Optional.empty());	
		
		new PageLineSupplier(
				location,
				pageLineIteratorSupplier->{
					if(currentManager.getValue().isPresent()) {
						//Iterator<String> 
						
						boolean success=currentManager
								.getValue()
								.get()
								.getPageParser()
								.tryToProccessPage(pageLineIteratorSupplier);
						
						if(success) {
							
							//if success, skip to next page
							return;
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
								.tryToProccessPage(pageLineIteratorSupplier);
						
						if(success) {
							System.out.println("initierer ny parser");
							currentManager.setValue(Optional.of(instance));
							break;
						}
					}
				});
    }  
	
	private static class PageLineSupplier{
		
		
		private PageLineSupplier(String location,Consumer<Supplier<Iterator<String>>> listener) {
			
			try {
				File folder=new File(location);
				
				//number-date(dd.mm.yyyy)-Skrevet av: [name]-Rapport-Rapportert dato: date(dd.mm.yyy
				
				MutableObject<IORunnable> closeCurrentResource=new MutableObject<>(()->{});
				
				for(File fileEntry:folder.listFiles()) {
					
					String type=Files.probeContentType(fileEntry.toPath());
					
					if(type.equals("text/plain")) {
						
							
						FileReader reader=new FileReader(fileEntry);
						
						
						Supplier<Iterator<String>> getLines=()->{
							try {
								
								closeCurrentResource.getValue().run();
								
								
								BufferedReader br=new BufferedReader(reader);
								
								closeCurrentResource.setValue(()->br.close());
								
								Iterator<String> iterator=br.lines().iterator();
								
								return iterator;
							
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							return new ArrayList<String>().iterator();
						};
						
						listener.accept(getLines);
						
						reader.close();
					}
				}
			}catch(Exception e) {
				
			}
		}
	}
	
	private interface IOSupplier<E> {
		public E get() throws Exception;
	}
	
	private interface IORunnable {
		public void run() throws Exception;
	}
}
