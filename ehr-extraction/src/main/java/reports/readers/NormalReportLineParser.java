package reports.readers;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import util.matcher.ChainMatch;
import util.matcher.ListenUntilMatchedOrUnmatched;
import util.matcher.Matcher;
import util.matcher.MatchingState;
import util.pageProcessor.PageParser;
import util.sequence.SequenceLineParsers;
import util.sequence.SimpleSequenceLineParser;

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
	private final static Logger log=LogManager.getLogger(NormalReportLineParser.class.getSimpleName());
	
	public static  NormalReportLineParser create() {
		return new  NormalReportLineParser();
	}

	private NormalReportLineParser() {
		log.info("is created!");
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
				log.info("is UNvalidated");
				return false;
			}
		}
		log.info("is validated");
		//System.out.println("subsequent page har match");
		//parse
		
		if(validator.getState()==MatchingState.MATCHED) {
			Iterator<String> readIterator=lines.get();
			
			SequenceLineParsers reader=new SequenceLineParsers.Builder()
				.addListener(ListenUntilMatchedOrUnmatched.create(ReportStartMatcher.startMatcher.get()))
				.addListener(
					SimpleSequenceLineParser.create(
						l->{
							//System.out.println("*************=leser=> "+l);
						},
						()->Optional.empty(),
						()->Optional.of(ReportEndMatcher.endMatcher.get())
						))
				.build();
			
			while(readIterator.hasNext()) {
				
				String line =readIterator.next();
				
				if(!reader.isEnded()) {
					log.info("reads line "+line);
					reader.readLine(line);
				}else {
					log.info("reader is ended!");
					return false;
				}
				
			}
		}
		
		log.info("has read page successfully");
		
		return true;
	}
}
