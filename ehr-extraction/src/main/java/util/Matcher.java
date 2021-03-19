package util;

import util.lineListeners.LineListener;

public interface Matcher extends LineListener{
	public MatchingState getState();
}
