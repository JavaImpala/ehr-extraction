package messages.readers;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import util.matcher.ChainMatch;
import util.matcher.Matcher;
import util.matcher.MatchingUntilRegex;
import util.matcher.NegativeMatcher;
import util.matcher.SingleRegexLineMatcher;

public class MessageStartMatcher {
	public static final Supplier<Matcher> startMatcher=()->new ChainMatch.Builder()
			.addMatcher(MatchingUntilRegex.create(Pattern.compile("^Elektronisk\\s+meldings\\s+vedlegg:\\s+\\d+ Registrert\\s+av:\\s+.*Side\\s\\d+\\s*/\\s*\\d")))
			.addMatcher(NegativeMatcher.wrap(SingleRegexLineMatcher.wrapPattern(Pattern.compile(".*MELDINGSIDENTIFIKASJON.*")))) //og Applikasjonskvittering
			.build();
}
