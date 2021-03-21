package util.matcher;

import java.util.Optional;

import util.sequence.SequenceLineListener;

public class ListenUntilMatched implements SequenceLineListener{
	

	private final Matcher matcher;
	
	private Optional<Matcher> endMatcher=Optional.empty();
	private boolean ended=false;
	
	private ListenUntilMatched(Matcher matcher) {
		this.matcher=matcher;
	}
	
	public static ListenUntilMatched create(Matcher matcher) {
		return new ListenUntilMatched(matcher);
	}
	
	@Override
	public void readLine(String line) {
		matcher.readLine(line);
		
		if(matcher.getState()==MatchingState.MATCHED || matcher.getState()==MatchingState.UNMATCHED) {
			ended=true;
			endMatcher=Optional.of(MatchAfterReads.alwaysMatch());
		}
	}

	@Override
	public boolean isEnded() {
		return ended;
	}

	@Override
	public void end() {
		ended=true;
	}

	@Override
	public Optional<Matcher> shouldStart() {
		return Optional.empty();
	}

	@Override
	public Optional<Matcher> shouldEnd() {
		return endMatcher;
	}
	
}
