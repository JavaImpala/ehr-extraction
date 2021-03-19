package util.endable;

import util.lineListeners.LineParser;

public class EndableWrapper implements EndableLineParser{
	
	private final LineParser listener;
	private boolean isEnded=false;
	
	private EndableWrapper(LineParser listener) {
		this.listener = listener;
	}
	
	public static EndableWrapper wrap(LineParser listener) {
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
