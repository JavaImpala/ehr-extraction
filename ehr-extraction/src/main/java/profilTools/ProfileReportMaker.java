package profilTools;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.mutable.MutableObject;

import util.ElementMaker;

public class ProfileReportMaker implements ElementMaker{
	private final ProfilReport report;
	
	private Runnable onHappy=()->{};
	
	public final static String initiateRegex= "^([0-9]+).+((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).+Skrevet av:(.+)Rapport.+Rapport dato:.+((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).*";
	
	private final Consumer<String> lineReaders;
	
	private ProfileReportMaker() {
		
		report=new ProfilReport();
		
		
		MutableObject<Consumer<String>> reader=new MutableObject<>();
		
		Pattern p = Pattern.compile("\\b"+"Vakt:"+"[ ]*:[ ]*'(.+?)'");
		
		Consumer<String> reportReader=line->{
			
			System.out.println("REST=> "+line);
			
			
			/*
			if(matcher.matches()){
				System.out.println("****************** "+line);
				
				for(int i=0;i<matcher.groupCount();i++) {
					
				}
				
				int reportNumber=Integer.valueOf(matcher.group(1));
				String stringDate=matcher.group(2);
				String author=matcher.group(5);
				String writtenStringDate=matcher.group(6);
				
			}
			*/
		};
		
		
		Consumer<String> secondLineReader=line->{
			
			System.out.println("SECOND=> "+line);
			
			System.out.println(getValue(line,"Vakt: "));
			System.out.println(getValue(line,"Status:"));
			System.out.println(getValue(line,"Endre tiltak:"));
			System.out.println(getValue(line,"Prioritet gitt:"));
			
			/*
			 * Vakt: Dagvakt Status: Uendret Endre tiltak: Nei Prioritet gitt: Nei
			 * 
			if(matcher.matches()){
				System.out.println("****************** "+line);
				
				for(int i=0;i<matcher.groupCount();i++) {
					
				}
				
				int reportNumber=Integer.valueOf(matcher.group(1));
				String stringDate=matcher.group(2);
				String author=matcher.group(5);
				String writtenStringDate=matcher.group(6);
				
			}
			*/
			
			reader.setValue(reportReader);
		};
		
		Consumer<String> firstLineReader=line->{
			
			
			System.out.println("FIRST=> "+line);
			
			/*
			if(matcher.matches()){
				System.out.println("****************** "+line);
				
				for(int i=0;i<matcher.groupCount();i++) {
					
				}
				
				int reportNumber=Integer.valueOf(matcher.group(1));
				String stringDate=matcher.group(2);
				String author=matcher.group(5);
				String writtenStringDate=matcher.group(6);
				
			}
			*/
			
			reader.setValue(secondLineReader);
		};
		
		
		
		reader.setValue(firstLineReader);
		
		this.lineReaders=s->{
			reader.getValue().accept(s);
		};
		
	}
	
	public static String getValue(String testStr, String key){
        Pattern p = Pattern.compile("(?<=\b"+key+"\bs)(bw+)");
        Matcher m = p.matcher(testStr);
        return  m.find() ? m.group(1): null;
    }
	
	public static  ProfileReportMaker create() {
		return new  ProfileReportMaker();
	}
	
	@Override
	public void readLine(String line) {
		this.lineReaders.accept(line);
	}
	
	@Override
	public void setHappyListener(Runnable onHappy) {
		this.onHappy=onHappy;
	}
	
	@Override
	public void terminate() {
		
	}
}
