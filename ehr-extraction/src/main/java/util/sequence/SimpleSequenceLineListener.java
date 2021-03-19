package util.sequence;

import util.endable.EndableLineListener;
import util.endable.EndableWrapper;
import util.lineListeners.LineListener;

public class SimpleSequenceLineListener implements SequenceLineListener{
	private final EndableLineListener listener;
	
	private SimpleSequenceLineListener(EndableLineListener listener) {
		this.listener=listener;
	}
	
	public static SimpleSequenceLineListener create(EndableLineListener listener) {
		return new SimpleSequenceLineListener(listener);
	}
	
	public static SimpleSequenceLineListener create(LineListener listener) {
		return new SimpleSequenceLineListener(EndableWrapper.wrap(listener));
	}
	
	@Override
	public void readLine(String line) {
		this.listener.readLine(line);
	}

	@Override
	public boolean shouldStart(String line) {
		return false;
	}

	@Override
	public boolean shouldEnd(String line) {
		return false;
	}

	@Override
	public void end() {
		listener.end();
	}

	@Override
	public boolean isEnded() {
		return listener.isEnded();
	}
	
	

}
