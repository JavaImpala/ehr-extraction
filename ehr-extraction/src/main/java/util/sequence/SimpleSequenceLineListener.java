package util.sequence;

import java.util.Optional;
import java.util.function.Supplier;

import util.endable.EndableLineParser;
import util.endable.EndableWrapper;
import util.lineListeners.LineParser;
import util.matcher.Matcher;

public class SimpleSequenceLineListener implements SequenceLineListener{
	private final EndableLineParser listener;
	private final Supplier<Optional<Matcher>> startMatcher;
	private final Supplier<Optional<Matcher>> endMatcher;
	
	private SimpleSequenceLineListener(EndableLineParser listener,Supplier<Optional<Matcher>> startMatcher,Supplier<Optional<Matcher>> endMatcher) {
		this.listener=listener;
		this.startMatcher=startMatcher;
		this.endMatcher=endMatcher;
	}
	
	public static SimpleSequenceLineListener create(EndableLineParser listener,Supplier<Optional<Matcher>> startMatcher,Supplier<Optional<Matcher>> endMatcher) {
		return new SimpleSequenceLineListener(listener, startMatcher, endMatcher);
	}
	
	public static SimpleSequenceLineListener create(LineParser listener,Supplier<Optional<Matcher>> startMatcher,Supplier<Optional<Matcher>> endMatcher) {
		return new SimpleSequenceLineListener(EndableWrapper.wrap(listener), startMatcher, endMatcher);
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
	public Optional<Matcher> getNewShouldStart() {
		return this.startMatcher.get();
	}

	@Override
	public Optional<Matcher> getNewShouldEnd() {
		return this.endMatcher.get();
	}
	
	

}
