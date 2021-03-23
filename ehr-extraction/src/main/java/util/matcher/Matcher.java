package util.matcher;

import util.lineParser.LineParser;

public interface Matcher extends LineParser{
	public MatchingState getState();
}
