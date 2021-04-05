package reports;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import util.endable.EndableLineParser;
import util.lineParser.LineParser;
import util.lineParser.RepeatLineParser;
import util.lineParser.TextLine;
import util.matcher.ChainMatch;
import util.matcher.Matcher;
import util.matcher.RetryRegexMatcher;

/*
Endret Av: Torbjørn Torsvik
Fra - Til: 03.06.2018 - 03.06.2018
Lorem ipsum
Endret Av: Torbjørn Torsvik
Fra - Til: 30.04.2018 - 02.06.2018
Bakgrunn for tiltaket:
Lorem Ipsum
Endret dato: 03.06.2018
Lorem Ipsum
Rapporter: 281
==>reportHeader
 */



public class ProfilCarePlanDescriptionMaker implements LineParser{

	public static Supplier<Matcher> endDescriptions=()->{
		return new ChainMatch
					.Builder()
					.addSingleLinePattern(Pattern.compile("^Rapporter:\\s+\\d+"))
					.addMatcher(RetryRegexMatcher.create(ProfilReportMaker.initiateRegexPattern,20))
					.build();
	};
	
	public static Supplier<Matcher> newEntryMatcher=()->{
		return new ChainMatch
				.Builder()
				.addSingleLinePattern(Pattern.compile("^Endret\\sAv:\\s+.*"))
				.addSingleLinePattern(Pattern.compile("Fra\\s[-]\\sTil:\\s([0-9]{2}[.][0-9]{2}[.][0-9]{4}).*([0-9]{2}[.][0-9]{2}[.][0-9]{4})"))
				.build();
	};
	
	private final CarePlan plan;
	private final LineParser lineParser;
	
	private ProfilCarePlanDescriptionMaker(CarePlan plan) {
		this.plan = plan;
		
		this.lineParser=RepeatLineParser.create(
				newEntryMatcher,
				()->{
					
					return new EndableLineParser() {

						@Override
						public void readLine(TextLine line) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public boolean isEnded() {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public void end() {
							// TODO Auto-generated method stub
							
						}
					};	
				});
	}
	
	public static ProfilCarePlanDescriptionMaker create(CarePlan carePlan) {
		return new ProfilCarePlanDescriptionMaker(carePlan);
	}

	@Override
	public void readLine(TextLine line) {
		this.lineParser.readLine(line);
	}

	
	
	
	
	
}
