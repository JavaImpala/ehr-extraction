package util.sequence;

import java.util.Optional;

import util.endable.EndableLineParser;
import util.endable.EndableWrapper;
import util.lineListeners.LineParser;
import util.matcher.Matcher;

public class SimpleSequenceLineListener implements SequenceLineListener{
	private final EndableLineParser listener;
	
	private SimpleSequenceLineListener(EndableLineParser listener) {
		this.listener=listener;
	}
	
	public static SimpleSequenceLineListener create(EndableLineParser listener) {
		return new SimpleSequenceLineListener(listener);
	}
	
	public static SimpleSequenceLineListener create(LineParser listener) {
		return new SimpleSequenceLineListener(EndableWrapper.wrap(listener));
	}
	
	@Override
	public void readLine(String line) {
		this.listener.readLine(line);
	}

	

	@Override
	public void end() {
		listener.end();
	}

	@Override
	public boolean isEnded() {
		return listener.isEnded();
	}

	@Override
	public Optional<Matcher> shouldStart() {
		return Optional.empty();
	}

	@Override
	public Optional<Matcher> shouldEnd() {
		return Optional.empty();
	}
	
	

}
