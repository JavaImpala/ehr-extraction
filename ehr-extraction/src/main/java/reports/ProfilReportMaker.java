package reports;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.mutable.MutableObject;

import util.lineListeners.LineListenerState;
import util.lineListeners.ObservableLineParser;

public class ProfilReportMaker implements ObservableLineParser{
	private final ProfilReport report;
	
	private LineListenerState state=LineListenerState.READY;
	
	private final static String initiateRegex= "^([0-9]+).+((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).+Skrevet av:(.+)Rapport.+Rapport dato:.+((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).*";
	public final static Pattern initiateRegexPattern=Pattern.compile(initiateRegex);
	
	
	private final Consumer<String> lineReaders;
	
	private ProfilReportMaker() {
		
		report=new ProfilReport();
		
		MutableObject<Consumer<String>> reader=new MutableObject<>();
		
		Consumer<String> reportReader=line->{
			if(initiateRegexPattern.matcher(line).matches()) {
				System.out.println("FFFUUUUUUUUUUUUUUUUUUUUUUCKUP");
			}
			
			report.addContent(line);
		};
		
		
		/*
		 * Vakt: Dagvakt Status: Uendret Endre tiltak: Nei Prioritet gitt: Nei
		 * 
		 */ 
		
		Consumer<String> secondLineReader=line->{
			report.setDaytimeReportType(getValue(line,"Vakt:"));
			
			report.setChangedStatus((getValue(line,"Status:")=="Uendret")?false:true);
			report.setChangedPlan((getValue(line,"Endre tiltak:")=="Nei")?false:true);
			
			report.setPriority((getValue(line,"Prioritet gitt:")=="Nei")?false:true);
			
			reader.setValue(reportReader);
		};
		
		Pattern p = Pattern.compile("(\\w+)");
		
		//"^([0-9]+).+((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).+Skrevet av:(.+)Rapport.+Rapport dato:.+((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).*"
		//3 01.03.2021 Skrevet av: Torbjørn Torsvik Rapport Rapport dato: 01.03.2021
		
		Consumer<String> firstLineReader=line->{
			Matcher matcher = initiateRegexPattern.matcher(line);
			
			if(matcher.find()){	
				report.setReportNumber(Integer.valueOf(matcher.group(1)));
				report.setAuthor(matcher.group(5).trim());
				report.setStringDate(matcher.group(2));
				report.setWrittenStringDate(matcher.group(6));
			}
			
			reader.setValue(secondLineReader);
		};
		
		
		
		reader.setValue(firstLineReader);
		
		this.lineReaders=s->{
			reader.getValue().accept(s);
		};
		
	}
	
	public static String getValue(String testStr, String key){
        Pattern p = Pattern.compile("(?<="+key+"\\s)(\\w+)");
        Matcher m = p.matcher(testStr);
       
        return  m.find() ? m.group(1): null;
    }
	
	public static  ProfilReportMaker create() {
		return new  ProfilReportMaker();
	}
	
	@Override
	public void readLine(String line) {
		this.lineReaders.accept(line);
		
		
	}
	
	public void settle() {
		report.close();
		
		state=LineListenerState.DONE;
	}

	@Override
	public String toString() {
		return "ProfileReportMaker [report=" + report + "]";
	}

	@Override
	public LineListenerState getState() {
		return state;
	}
}
