package reports.readers;

import java.util.Iterator;
import java.util.function.Supplier;

import util.matcher.Matcher;
import util.matcher.MatchingState;
import util.pageProcessor.PageParser;

/*
 * 
							for(Entry<Predicate<String>,Supplier<ObservableLineParser>> entry:pageParsers.entrySet()) {
								if(entry.getKey().test(line)) {
									if(activeMaker.getValue()!=null) {
										
										activeMaker.getValue().settle();
										
										System.out.println(activeMaker.getValue());
										
										noteCounter++;
									}
									
									ObservableLineParser newMaker = entry.getValue().get();
									
									newMaker.setHappyListener(()->{
										activeMaker.setValue(null);
									});
									
									activeMaker.setValue(newMaker);
									
									break;
								}
							}
 */

public class NormalReportLineParser implements PageParser{
	public static  NormalReportLineParser create() {
		return new  NormalReportLineParser();
	}

	private NormalReportLineParser() {
		
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
				//System.out.println("subsequent page har IKKE match");
				return false;
			}
		}
		
		//System.out.println("subsequent page har match");
		//parse
		
		
		
		
		return true;
	}
}
