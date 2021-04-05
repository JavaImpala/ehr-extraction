package util.sequence;

import java.util.Optional;
import java.util.function.Supplier;

import util.endable.EndableLineParser;
import util.lineParser.TextLine;
import util.matcher.ListenUntilMatchedOrUnmatched;
import util.matcher.Matcher;

public class ListenFrom implements SequenceLineParser{
	
	
	private final EndableLineParser listener;
	private final Matcher shouldStart;
	
	private ListenFrom(EndableLineParser listener,Matcher shouldStart) {
		this.listener=listener;
		this.shouldStart=shouldStart;
	}
	
	public static SequenceLineParser listenFrom(EndableLineParser listener,Matcher shouldStart) {
		return new ListenFrom(
				listener,
				shouldStart);
	}
	
	public static SequenceLineParser listenFromAfterMatch(EndableLineParser listener,Supplier<Matcher> shouldStart) {
		Matcher matcher=shouldStart.get();
		
		return new ListenFrom(
				new SequenceLineParsers
					.Builder()
					.addListener(ListenUntilMatchedOrUnmatched.create(matcher))
					.addListener(SimpleSequenceLineParser.create(listener,()->Optional.empty(),()->Optional.empty()))
					.build(),
				shouldStart.get());
	}


	@Override
	public void readLine(TextLine line) {
		listener.readLine(line);
	}

	

	@Override
	public boolean isEnded() {
		return listener.isEnded();
	}

	@Override
	public void end() {
		listener.end();
	}

	@Override
	public Optional<Matcher> getNewShouldStart() {
		return Optional.empty();
	}

	@Override
	public Optional<Matcher> getNewShouldEnd() {
		return Optional.empty();
	}
}