package util.sequence;

import util.endable.EndableLineParser;
import util.endable.EndableWrapper;
import util.lineListeners.LineParser;

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
