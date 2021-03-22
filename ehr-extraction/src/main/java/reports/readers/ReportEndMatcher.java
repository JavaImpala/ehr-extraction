package reports.readers;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import util.matcher.ChainMatch;
import util.matcher.Matcher;

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
	
	public static final Supplier<Matcher> endMatcher=()->new ChainMatch.Builder()
			.addSingleLinePattern(Pattern.compile("^Kvitteringer.*"))
			.build();
}
