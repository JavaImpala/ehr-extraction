package util.matcher;

import java.util.regex.Pattern;

public class RetryMatcher implements Matcher{
	
	

	private final Pattern pattern;
	
	private final int maxLines;
	private int lineCounter=0;
	
	private MatchingState state=MatchingState.READY;
	
	private RetryMatcher(Pattern pattern, int maxLines) {
		this.pattern = pattern;
		this.maxLines = maxLines;
	}
	
	public static RetryMatcher create(Pattern pattern, int maxLines) {
		return new RetryMatcher(pattern, maxLines);
	}

	@Override
	public void readLine(String line) {
		if(state==MatchingState.MATCHED || state==MatchingState.UNMATCHED) {
			return;
		}
		
		if(this.pattern.matcher(line).matches()){
			state=MatchingState.MATCHED;
		}else if(lineCounter<maxLines){
			state=MatchingState.MATCHING;
		}else {
			state=MatchingState.UNMATCHED;
		}
		
		lineCounter++;
	}

	@Override
	public MatchingState getState() {
		return state;
	}

	@Override
	public String toString() {
		return "RetryMatcher [pattern=" + pattern.pattern() + ", maxLines=" + maxLines + ", lineCounter=" + lineCounter
				+ ", state=" + state + "]";
	}
	
	
	
}
