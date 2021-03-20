package util.sequence;

import java.util.Optional;

import util.endable.EndableLineParser;
import util.endable.EndableWrapper;
import util.lineListeners.LineParser;
import util.matcher.Matcher;

public class ListenOnce implements SequenceLineListener	{

	private final Matcher matcher=null;
	private EndableLineParser wrapped;
	
	private ListenOnce(EndableLineParser wrapped) {
		this.wrapped=wrapped;
	}
	
	public static ListenOnce wrap(EndableLineParser wrapped) {
		return new ListenOnce(wrapped);
	}
	
	public static ListenOnce wrap(LineParser wrapped) {
		return new ListenOnce(EndableWrapper.wrap(wrapped));
	}
	
	public static SequenceLineListener create() {
		return new ListenOnce(EndableWrapper.wrap(s->{}));
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
	public Optional<Matcher> shouldStart() {
		return Optional.empty();
	}

	@Override
	public Optional<Matcher> shouldEnd() {
		return Optional.of(matcher);
	}
}
