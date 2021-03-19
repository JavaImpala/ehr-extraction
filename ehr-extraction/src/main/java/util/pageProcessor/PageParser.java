package util.pageProcessor;

import java.util.Iterator;
import java.util.function.Supplier;

public interface PageParser {
	public boolean tryToProccessPage(Supplier<Iterator<String>> lines);
	
	//public void setSettledListener(Runnable onSettled);
	//public void forceToSettle() ;
}
