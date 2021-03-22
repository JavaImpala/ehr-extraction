package util.matcher;

import java.util.regex.Pattern;

public class MatchingUntil implements Matcher{
	
	
	private MatchingState state=MatchingState.READY;
	private final Pattern pattern;
	
	private MatchingUntil(Pattern pattern) {
		this.pattern = pattern;
	}
	
	public static MatchingUntil create(Pattern pattern) {
		return new MatchingUntil(pattern);
	}

	@Override
	public void readLine(String line) {
		if(pattern.matcher(line).matches()) {
			state=MatchingState.MATCHED;
		}else if(state!=MatchingState.MATCHED) {
			state=MatchingState.MATCHING;
		}
	}

	@Override
	public MatchingState getState() {
		return state;
	}

}
