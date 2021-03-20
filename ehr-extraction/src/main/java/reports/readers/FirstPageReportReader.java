package reports.readers;

import java.util.Iterator;
import java.util.function.Supplier;

import util.matcher.Matcher;
import util.matcher.MatchingState;
import util.pageProcessor.PageParser;

public class FirstPageReportReader implements PageParser{
	
	public static FirstPageReportReader create() {
		return new FirstPageReportReader();
	}

	private FirstPageReportReader() {
		
	}

	@Override
	public boolean tryToProccessPage(Supplier<Iterator<String>> lines) {
		
		//validate that its a reportPage
		
		Iterator<String> matchIterator=lines.get();
		Matcher validator = ReportStartMatcher.startMatcher.get();
		
		while(matchIterator.hasNext()) {
			validator.readLine(matchIterator.next());
			
			if(validator.getState()==MatchingState.MATCHED) {
				break;
			}
			
			if(validator.getState()==MatchingState.UNMATCHED) {
				//System.out.println("first page har IKKE match");
				return false;
			}
		}
		
		
		//System.out.println("first page har match");
		
		//parse
		
		
		
		
		return true;
	}

	
}
