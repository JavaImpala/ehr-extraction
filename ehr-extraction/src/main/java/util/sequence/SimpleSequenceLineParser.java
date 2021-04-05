package util.sequence;

import java.util.Optional;
import java.util.function.Supplier;

import util.endable.EndableLineParser;
import util.endable.EndableWrapper;
import util.lineParser.LineParser;
import util.lineParser.TextLine;
import util.matcher.MatchAfterReadsSupplier;
import util.matcher.Matcher;

public class SimpleSequenceLineParser implements SequenceLineParser{
	private final EndableLineParser listener;
	private final Supplier<Optional<Matcher>> startMatcher;
	private final Supplier<Optional<Matcher>> endMatcher;
	
	private SimpleSequenceLineParser(EndableLineParser listener,Supplier<Optional<Matcher>> startMatcher,Supplier<Optional<Matcher>> endMatcher) {
		this.listener=listener;
		this.startMatcher=startMatcher;
		this.endMatcher=endMatcher;
	}
	
	public static SimpleSequenceLineParser create(EndableLineParser listener,Supplier<Optional<Matcher>> startMatcher,Supplier<Optional<Matcher>> endMatcher) {
		return new SimpleSequenceLineParser(listener, startMatcher, endMatcher);
	}
	
	public static SimpleSequenceLineParser create(LineParser listener,Supplier<Optional<Matcher>> startMatcher,Supplier<Optional<Matcher>> endMatcher) {
		return new SimpleSequenceLineParser(EndableWrapper.wrap(listener), startMatcher, endMatcher);
	}
	
	public static SimpleSequenceLineParser listenOnce(EndableLineParser listener) {
		MatchAfterReadsSupplier matchAfterReads=MatchAfterReadsSupplier.create(1);
		
		return new SimpleSequenceLineParser(
				new EndableLineParser() {

					@Override
					public void readLine(TextLine line) {
						listener.readLine(line);
						listener.end();
					}

					@Override
					public boolean isEnded() {
						return listener.isEnded();
					}

					@Override
					public void end() {
						listener.end();
					}
				},
				()->Optional.empty(),
				()->Optional.of(matchAfterReads.get()));
	}
	
	@Override
	public void readLine(TextLine line) {
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
