package util.sequence;

import java.util.Optional;
import java.util.function.Supplier;

import util.endable.EndableLineParser;
import util.matcher.ListenUntilMatched;
import util.matcher.Matcher;

public class ListenFrom implements SequenceLineListener{
	
	
	private final EndableLineParser listener;
	private final Matcher shouldStart;
	
	private ListenFrom(EndableLineParser listener,Matcher shouldStart) {
		this.listener=listener;
		this.shouldStart=shouldStart;
	}
	
	public static SequenceLineListener listenFrom(EndableLineParser listener,Matcher shouldStart) {
		return new ListenFrom(
				listener,
				shouldStart);
	}
	
	public static SequenceLineListener listenFromAfterMatch(EndableLineParser listener,Supplier<Matcher> shouldStart) {
		Matcher matcher=shouldStart.get();
		
		return new ListenFrom(
				new SequenceLineListeners
					.Builder()
					.addListener(ListenUntilMatched.create(matcher))
					.addListener(SimpleSequenceLineListener.create(listener))
					.build(),
				shouldStart.get());
	}


	@Override
	public void readLine(String line) {
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
	public Optional<Matcher> shouldStart() {
		return Optional.empty();
	}

	@Override
	public Optional<Matcher> shouldEnd() {
		return Optional.empty();
	}
}