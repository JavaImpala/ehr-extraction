package util.matcher;

import java.util.regex.Pattern;

import util.lineParser.TextLine;

public class SingleRegexLineMatcher implements Matcher{
	private MatchingState state=MatchingState.READY;
	private final Pattern pattern;

	private SingleRegexLineMatcher(Pattern pattern) {
		this.pattern=pattern;
	}

	public static Matcher wrapPattern(Pattern pattern) {
		return new SingleRegexLineMatcher(pattern);
	}

	@Override
	public void readLine(TextLine line) {
		if(pattern.matcher(line.getLineConcatString()).matches()) {
			state=MatchingState.MATCHED;
		}else {
			state=MatchingState.UNMATCHED;
		}
	}

	@Override
	public MatchingState getState() {
		return state;
	}

}
