package util.sequence;

import util.endable.EndableLineListener;

public interface SequenceLineListener extends EndableLineListener{
	public boolean shouldStart(String line);
	public boolean shouldEnd(String line);
	
}
