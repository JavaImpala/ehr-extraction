package util.sequence;

import util.endable.EndableLineListener;
import util.endable.EndableWrapper;
import util.lineListeners.LineListener;

public class ListenOnce implements SequenceLineListener	{

	private boolean shouldEnd=false;
	private EndableLineListener wrapped;
	
	private ListenOnce(EndableLineListener wrapped) {
		this.wrapped=wrapped;
	}
	
	public static ListenOnce wrap(EndableLineListener wrapped) {
		return new ListenOnce(wrapped);
	}
	
	public static ListenOnce wrap(LineListener wrapped) {
		return new ListenOnce(EndableWrapper.wrap(wrapped));
	}
	
	public static SequenceLineListener create() {
		return new ListenOnce(EndableWrapper.wrap(s->{}));
	}
	
	@Override
	public void readLine(String line) {
		wrapped.readLine(line);
		shouldEnd=true;
	}

	@Override
	public boolean shouldStart(String line) {
		return false;
	}

	@Override
	public boolean shouldEnd(String line) {
		return shouldEnd;
	}

	@Override
	public boolean isEnded() {
		return wrapped.isEnded();
	}

	@Override
	public void end() {
		wrapped.end();
	}
}
