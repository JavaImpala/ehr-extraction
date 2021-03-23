package reports.readers;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import reports.CarePlan;
import reports.ProfilCarePlanMaker;
import reports.ProfilReportMaker;
import util.PageHeaderIdentifier;
import util.matcher.ListenUntilMatchedOrUnmatched;
import util.matcher.Matcher;
import util.matcher.MatchingState;
import util.pageProcessor.PageParser;
import util.sequence.EndableToSequenceLineParser;
import util.sequence.SequenceLineParsers;
import util.sequence.SimpleSequenceLineParser;

public class FirstPageReportReader implements PageParser{
	private final static Logger log=LogManager.getLogger(FirstPageReportReader.class.getSimpleName());
	
	private Optional<ProfilReportMaker> currentReport;

	private FirstPageReportReader() {
		
	}
	
	public static FirstPageReportReader create() {
		return new FirstPageReportReader();
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
		
		
		System.out.println("first page har match "+validator.getState());
		
		CarePlan carePlan=new CarePlan();
		
		//parse
		if(validator.getState()==MatchingState.MATCHED) {
			Iterator<String> readIterator=lines.get();
			
			SequenceLineParsers reader=new SequenceLineParsers.Builder()
				.addListener(ListenUntilMatchedOrUnmatched.create(PageHeaderIdentifier.matcher.get()))
				.addListener(EndableToSequenceLineParser.wrap(ProfilCarePlanMaker.create(carePlan)))
				.addListener(
					SimpleSequenceLineParser.create(
						l->{
							System.out.println("*************-leser=> "+l);
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
		
		
		
		return true;
	}

	public Optional<ProfilReportMaker> rollOverReport(){
		return currentReport;
	}
}
