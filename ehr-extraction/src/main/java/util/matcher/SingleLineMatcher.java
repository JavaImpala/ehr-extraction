package util.matcher;

import java.util.regex.Pattern;

public class SingleLineMatcher implements Matcher{
	private MatchingState state=MatchingState.READY;
	private final Pattern pattern;

	private SingleLineMatcher(Pattern pattern) {
		this.pattern=pattern;
	}

	public static Matcher wrapPattern(Pattern pattern) {
		return new SingleLineMatcher(pattern);
	}

	@Override
	public void readLine(String line) {
		if(pattern.matcher(line).matches()) {
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
