package reports;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.RegexTools;
import util.endable.EndableWrapper;
import util.endable.ObservableEndableLineParser;
import util.lineParser.LineListenerState;
import util.lineParser.ObservableLineParser;
import util.lineParser.RepeatLineParser;
import util.matcher.SingleLineMatcher;
import util.sequence.SequenceLineParsers;
import util.sequence.SimpleSequenceLineParser;

public class ProfilReportMaker implements ObservableLineParser{
	
	private LineListenerState state=LineListenerState.READY;
	
	private final static String initiateRegex= "^([0-9]+).+((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).+Skrevet av:(.+)Rapport.+Rapport dato:.+((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).*";
	public final static Pattern initiateRegexPattern=Pattern.compile(initiateRegex);
	
	private RepeatLineParser lineParser;
	
	private ProfilReportMaker() {
		
		this.lineParser=RepeatLineParser.create(
				()->SingleLineMatcher.wrapPattern(initiateRegexPattern),
				()->{
					ProfilReport report=new ProfilReport();
					
					return ObservableEndableLineParser.wrap(
							new SequenceLineParsers.Builder()
								.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(line->{
									Matcher matcher = initiateRegexPattern.matcher(line);
									
									if(matcher.find()){	
										report.setReportNumber(Integer.valueOf(matcher.group(1)));
										report.setAuthor(matcher.group(5).trim());
										report.setStringDate(matcher.group(2));
										report.setWrittenStringDate(matcher.group(6));
									}
									
								})))	
								.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(line->{
									report.setDaytimeReportType(RegexTools.getValueAfter(line,"Vakt:"));
									
									report.setChangedStatus((RegexTools.getValueAfter(line,"Status:")=="Uendret")?false:true);
									report.setChangedPlan((RegexTools.getValueAfter(line,"Endre tiltak:")=="Nei")?false:true);
									
									report.setPriority((RegexTools.getValueAfter(line,"Prioritet gitt:")=="Nei")?false:true);
								})))	
								.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(line->{
									report.addContent(line);
								}))) 	
								.build(),
							()->{
								report.close();
							});
				});
		
	}
	
	
	
	public static  ProfilReportMaker create() {
		return new  ProfilReportMaker();
	}
	
	@Override
	public void readLine(String line) {
		this.lineParser.readLine(line);
	}
	
	

	

	@Override
	public LineListenerState getState() {
		return state;
	}
}
