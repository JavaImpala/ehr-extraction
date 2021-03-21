package util.sequence;

import java.util.Optional;

import util.endable.EndableLineParser;
import util.endable.EndableWrapper;
import util.lineListeners.LineParser;
import util.matcher.MatchAfterReads;
import util.matcher.Matcher;

public class ListenUntilClosed implements SequenceLineListener{
	private Optional<Matcher> shouldEnd=Optional.empty();
	private EndableLineParser wrapped;
	
	private ListenUntilClosed(EndableLineParser wrapped) {
		this.wrapped=wrapped;
	}
	
	public static ListenUntilClosed wrap(EndableLineParser wrapped) {
		return new ListenUntilClosed(wrapped);
	}
	
	public static SequenceLineListener wrap(LineParser wrapped) {
		return new ListenUntilClosed(EndableWrapper.wrap(wrapped));
	}
	
	public static SequenceLineListener create() {
		return new ListenUntilClosed(EndableWrapper.wrap(s->{}));
	}
	
	@Override
	public void readLine(String line) {
		wrapped.readLine(line);
		shouldEnd=Optional.of(MatchAfterReads.alwaysMatch());
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
	public Optional<Matcher> shouldStart() {
		return Optional.empty();
	}

	@Override
	public Optional<Matcher> shouldEnd() {
		return this.shouldEnd;
	}
	
	
}
