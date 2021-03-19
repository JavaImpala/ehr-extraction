package util.endable;

import util.lineListeners.LineListener;

public class EndableWrapper implements EndableLineListener{
	
	private final LineListener listener;
	private boolean isEnded=false;
	
	private EndableWrapper(LineListener listener) {
		this.listener = listener;
	}
	
	public static EndableWrapper wrap(LineListener listener) {
		return new EndableWrapper(listener);
	}


	@Override
	public void readLine(String line) {
		listener.readLine(line);
	}

	@Override
	public boolean isEnded() {
		return isEnded;
	}

	@Override
	public void end() {
		isEnded=true;
	}

}
