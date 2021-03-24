package reports.readers;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import reports.ProfilReportMaker;
import util.endable.EndableLineParser;
import util.matcher.Matcher;
import util.matcher.MatchingState;
import util.pageProcessor.PageParser;

public class FirstPageReportReader implements PageParser{
	private final static Logger log=LogManager.getLogger(FirstPageReportReader.class.getSimpleName());
	
	private Optional<ProfilReportMaker> currentReport;

	private final EndableLineParser parser;

	private FirstPageReportReader(EndableLineParser contentParser) {
		this.parser=contentParser;
	}
	
	public static FirstPageReportReader create(EndableLineParser contentParser) {
		return new FirstPageReportReader(contentParser);
	}

	@Override
	public boolean tryToProccessPage(Supplier<Iterator<String>> lines) {
		
		//validate that its a reportPage
		
		Iterator<String> matchIterator=lines.get();
		Matcher validator =  ReportStartMatcher.startMatcher.get();
		
		while(matchIterator.hasNext()) {
			
			String next=matchIterator.next();
			
			//System.out.println("validating "+next);
			
			validator.readLine(next);
			
			if(validator.getState()==MatchingState.MATCHED) {
				break;
			}
			
			if(validator.getState()==MatchingState.UNMATCHED) {
				//System.out.println("first page har IKKE match");
				return false;
			}
		}
		
		if(validator.getState()==MatchingState.READY || validator.getState()==MatchingState.MATCHING) {
			return false;
		}
		
		
		log.info("first page har match "+validator.getState());
		
		
		
		//parse
		if(validator.getState()==MatchingState.MATCHED) {
			Iterator<String> readIterator=lines.get();
			
			while(readIterator.hasNext()) {
				
				String line =readIterator.next();
				
			
				
				if(!parser.isEnded()) {
					log.info("reads line "+line);
					parser.readLine(line);
				}else {
					log.info("reader is ended!");
					return false;
				}
			}
		}
		
		
		
		return true;
	}

	public Optional<ProfilReportMaker> rollOverReport(){
		return currentReport;
	}
}
