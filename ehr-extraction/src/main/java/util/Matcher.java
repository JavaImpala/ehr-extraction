package util;

import util.lineListeners.LineParser;

public interface Matcher extends LineParser{
	public MatchingState getState();
}
