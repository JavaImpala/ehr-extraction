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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import fhir.FHIRResources;
import messages.ProfilMessageParserManager;
import messages.readers.MessageSectionReader;
import reports.ProfilCarePlanPageParserManager;
import util.pageProcessor.Page;
import util.pageProcessor.PageParserManager;
import util.struct.StructPage;

public class MainPDFExtraction {
	
	private final static Logger log=LogManager.getLogger(MainPDFExtraction.class.getSimpleName());
	
	public static void main(String[] args ){  
		
		log.info("starting application");
		
		FhirContext ctx=FhirContext.forR4();
		ctx.getRestfulClientFactory().setSocketTimeout(60*10*1000);
		
		//String serverBase = "http://fhirtest.uhn.ca/baseDstu2";
		String serverBase = "http://localhost:8080/fhir";
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);	
		
		
		System.out.println("vi er her?");
		
		String location="/media/tor003/kingston/forskninguttrekkABB050321full-psm1";
		
		FHIRResources bundle = new FHIRResources(ctx);
		
		List<Supplier<PageParserManager>> pageParsers=new ArrayList<>();
		pageParsers.add(()->ProfilCarePlanPageParserManager.create(bundle));
		pageParsers.add(()->ProfilMessageParserManager.create());
		
		//lager reportMaker
		MutableObject<Optional<PageParserManager>> currentManager=new MutableObject<>(Optional.empty());	
		MutableInt counter=new MutableInt();
		
		new PageLineSupplier(
				location,
				pageLineIteratorSupplier->{
					
					counter.increment();
					
					log.info("");
					log.info("mottar side "+counter.getValue());
					System.out.println("mottar side "+counter.getValue());
					
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
		
		System.out.println("FØRER IKKE TIL DB COMMIT!");
		
		for(String s:MessageSectionReader.dontSupport) {
			System.out.println(s);
		}
		
		client.transaction().withBundle(bundle.getBundle()).execute();
    }  
	
	private static class PageLineSupplier{
		
		
		private PageLineSupplier(String location,Consumer<Page> listener) {
			
			int pageNumber=1;
			
			while(true) {
				try {
					
					listener.accept(
							new Page(
									fileToTextLines(location+"/image-"+String.format("%03d",pageNumber)+"-proc_psm1.txt"),
									fileToTextLines(location+"/image-"+String.format("%03d",pageNumber)+"-proc_psm6.txt"),
									new StructPage(new File(location+"/image-"+String.format("%03d",pageNumber)+"-proc_struct.hocr"),new File(location+"/image-"+String.format("%03d",pageNumber)+"-segments.xml"))));
					pageNumber++;
					
					
				}catch(Exception e) {
					
					
					
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
