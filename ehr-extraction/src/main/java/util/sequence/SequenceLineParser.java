package util.sequence;

import java.util.Optional;

import util.endable.EndableLineParser;
import util.matcher.Matcher;

public interface SequenceLineParser extends EndableLineParser{
	public Optional<Matcher> getNewShouldStart();
	public Optional<Matcher> getNewShouldEnd();
	
}
