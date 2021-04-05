package reports.readers;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import util.matcher.ChainMatch;
import util.matcher.Matcher;
import util.matcher.RetryRegexMatcher;

/* 
    Kvitteringer i plan/rapport: 20
 
    Kvitteringeri 20
    
    Kvitteringer i 20
	plan/rapport:
	1 Skrevet av: Torbjørn Torsvik Utført tidspunkt: 03.03.2021 Klokkeslett: 21:20
	2 Skrevet av: Torbjørn Torsvik Utført tidspunkt: 27.02.2021 Klokkeslett: 15:00
	3 Skrevet av: Torbjørn Torsvik Utført tidspunkt: 13.02.2021 Klokkeslett: 14:45
	4 Skrevet av: Torbjørn Torsvik Utført tidspunkt: 25.03.2020 Klokkeslett: 09:00
	5 Skrevet av: Torbjørn Torsvik Utført tidspunkt: 06.03.2020 Klokkeslett:
	6 Skrevet av: Torbjørn Torsvik Utført tidspunkt: 09.02.2020 Klokkeslett:
 */

public class ReportEndMatcher {
	//(\\bSkrevet\\b.*\\bav\\b:)\\s*.*"
	//(\\b\\p{Lu}.*\\b)
	//Utført\\s*tidspunkt:\\s*((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4})\\sKlokkeslett:.*
	public static final Supplier<Matcher> endMatcher=()->new ChainMatch.Builder()
			.addSingleLinePattern(Pattern.compile("^Kvitteringer.*"))
			.addMatcher(RetryRegexMatcher.create(Pattern.compile("^(\\d+).*(\\bSkrevet\\b.*\\bav\\b:)\\s*(\\b\\p{Lu}.*\\b).*Utført\\s*tidspunkt:\\s*((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4})\\sKlokkeslett:.*"),3))
			.build();
}
