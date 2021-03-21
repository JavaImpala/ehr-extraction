package util.matcher;

public class MatchAfterReads implements Matcher{
	
	private MatchingState state=MatchingState.READY;
	private int linesLeft;	
	
	private MatchAfterReads(int linesToRead) {
		this.linesLeft=linesToRead;
	}
	

	public static MatchAfterReads create(int linesToRead) {
		return new MatchAfterReads(linesToRead);
	}

	public static MatchAfterReads alwaysMatch() {
		return new MatchAfterReads(0);
	}
	
	@Override
	public void readLine(String line) {
		
		
		if(linesLeft<=0) {
			state=MatchingState.MATCHED;
		}else {
			state=MatchingState.MATCHING;
		}
		
		linesLeft--;
		
	}

	@Override
	public MatchingState getState() {
		return state;
	}

}
