package reports;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import util.lineParser.LineParser;
import util.matcher.ChainMatch;
import util.matcher.Matcher;
import util.matcher.RetryMatcher;

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
					.addMatcher(RetryMatcher.create(ProfilReportMaker.initiateRegexPattern,20))
					.build();
	};
	
	public static Supplier<Matcher> newEntryMatcher=()->{
		return new ChainMatch
				.Builder()
				.addSingleLinePattern(Pattern.compile("^Endret av:\\s+.*"))
				.addSingleLinePattern(Pattern.compile("Fra\\s[-]\\sTil:\\s([0-9]{2}[.][0-9]{2}[.][0-9]{4}).*([0-9]{2}[.][0-9]{2}[.][0-9]{4})"))
				.build();
	};
	
	private final CarePlan plan;
	private CarePlanDescription currentCarePlanDesc=null;
	
	public ProfilCarePlanDescriptionMaker(CarePlan plan) {
		this.plan = plan;
	}

	@Override
	public void readLine(String line) {
		
	}
	
	
	
	
}
