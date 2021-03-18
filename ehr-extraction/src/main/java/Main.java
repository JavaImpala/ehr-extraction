import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.apache.commons.lang3.mutable.MutableObject;

import profilTools.ProfileReportMaker;
import util.ElementMaker;
import util.PageHeaderIdentifier;

public class Main {
	public static void main( String[] args )  
    {  
		
		String location="/media/tor003/6887-88BA/ABB/forskninguttrekkABB050321full";
		File folder=new File(location);
		
		
		
		Map<Predicate<String>,Supplier<ElementMaker>> elementMakers=new HashMap<>();
		
		//lager reportMaker
		
		
		elementMakers.put(
				line->{
					return ProfileReportMaker.initiateRegexPattern.matcher(line).matches();
				}, 
				()->ProfileReportMaker.create());
		
		
		//number-date(dd.mm.yyyy)-Skrevet av: [name]-Rapport-Rapportert dato: date(dd.mm.yyy
		
		
		int noteCounter=0;
		
		MutableObject<ElementMaker> activeMaker=new MutableObject<>();
		
		for(File fileEntry:folder.listFiles()) {
			
			if(activeMaker.getValue()!=null){
				activeMaker.getValue().settle();
				
				System.out.println(activeMaker.getValue());
				
				noteCounter++;
				
				activeMaker.setValue(null);
			}
			
			try {
				String type=Files.probeContentType(fileEntry.toPath());
				
				if(type.equals("text/plain")) {
					
					try{
						
						
						
						BufferedReader br=new BufferedReader(new FileReader(fileEntry));
						String line;
						
						Predicate<String> atEndOfHeader=s->PageHeaderIdentifier.lastHeaderLinePattern.matcher(s).matches();
						boolean inHeader=true;
						
						while((line=br.readLine())  !=null) {
							
							if(inHeader) {
								if(atEndOfHeader.test(line)) {
									inHeader=false;
								}
								
								continue;
							}
							
							for(Entry<Predicate<String>,Supplier<ElementMaker>> entry:elementMakers.entrySet()) {
								if(entry.getKey().test(line)) {
									if(activeMaker.getValue()!=null) {
										
										activeMaker.getValue().settle();
										
										System.out.println(activeMaker.getValue());
										
										noteCounter++;
									}
									
									ElementMaker newMaker = entry.getValue().get();
									
									newMaker.setHappyListener(()->{
										activeMaker.setValue(null);
									});
									
									activeMaker.setValue(newMaker);
									
									break;
								}
							}
							
							if(activeMaker.getValue()!=null) {
								activeMaker.getValue().readLine(line);
							}	
						}	
					}catch(Exception e) {
						e.printStackTrace();
					}
					
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		System.out.println("ferdig å se gjennom. Fant "+noteCounter+" rapporter");
    }  

}
