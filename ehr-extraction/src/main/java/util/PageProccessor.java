package util;

import java.util.Iterator;

public interface PageProccessor {
	public boolean tryToProccessPage(Iterator<String> lines);
	
	public PageProccessorState state();
	
	//public void setSettledListener(Runnable onSettled);
	//public void forceToSettle() ;
}
