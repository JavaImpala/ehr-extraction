package util.sequence;

import util.endable.EndableLineParser;

public interface SequenceLineListener extends EndableLineParser{
	public boolean shouldStart(String line);
	public boolean shouldEnd(String line);
	
}
