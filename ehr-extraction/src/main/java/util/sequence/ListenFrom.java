package util.sequence;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import util.endable.EndableLineListener;

public class ListenFrom implements SequenceLineListener{
	
	
	private final EndableLineListener listener;
	private final Predicate<String> startLine;
	
	private ListenFrom(EndableLineListener listener,Predicate<String> startLine) {
		this.listener=listener;
		this.startLine=startLine;
	}
	
	public static SequenceLineListener listenFrom(EndableLineListener listener,Pattern startLine) {
		return new ListenFrom(listener,s->startLine.matcher(s).matches());
	}
	
	public static SequenceLineListener listenFromAfterMatch(EndableLineListener listener,Pattern startLine) {
		return new ListenFrom(
				new SequenceLineListeners
					.Builder()
					.addListener(ListenOnce.create())
					.addListener(SimpleSequenceLineListener.create(listener))
					.build(),
				s->startLine.matcher(s).matches());
	}


	@Override
	public void readLine(String line) {
		listener.readLine(line);
	}

	@Override
	public boolean shouldStart(String line) {
		return startLine.test(line);
	}

	@Override
	public boolean shouldEnd(String line) {
		return false;
	}

	@Override
	public boolean isEnded() {
		return listener.isEnded();
	}

	@Override
	public void end() {
		listener.end();
	}
}