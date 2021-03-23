package util.sequence;

import java.util.Optional;

import util.endable.EndableLineParser;
import util.matcher.UnmatchUntilEnoughReads;
import util.matcher.Matcher;

public class EndableToSequenceLineParser implements SequenceLineParser{
	
	

	private final EndableLineParser wrapped;
	
	
	
	private EndableToSequenceLineParser(EndableLineParser wrapped) {
		
		this.wrapped = wrapped;
	}
	
	public static EndableToSequenceLineParser wrap(EndableLineParser wrapped) {
		return new EndableToSequenceLineParser(wrapped);
	}

	@Override
	public void readLine(String line) {
		wrapped.readLine(line);
	}

	@Override
	public boolean isEnded() {
		return wrapped.isEnded();
	}

	@Override
	public void end() {
		wrapped.end();
	}

	@Override
	public Optional<Matcher> getNewShouldStart() {
		return Optional.empty();
	}

	@Override
	public Optional<Matcher> getNewShouldEnd() {
		if(wrapped.isEnded()) {
			return Optional.of(UnmatchUntilEnoughReads.alwaysMatch());
		}
		
		return Optional.empty();
	}

}
