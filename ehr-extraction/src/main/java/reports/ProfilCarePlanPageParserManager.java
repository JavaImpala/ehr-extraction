package reports;

import java.util.Optional;
import java.util.function.Supplier;

import reports.readers.FirstPageReportReader;
import reports.readers.NormalReportLineParser;
import reports.readers.ReportEndMatcher;
import util.PageHeaderIdentifier;
import util.matcher.ListenUntilMatchedOrUnmatched;
import util.pageProcessor.PageParser;
import util.pageProcessor.PageParserManager;
import util.sequence.EndableToSequenceLineParser;
import util.sequence.SequenceLineParsers;
import util.sequence.SimpleSequenceLineParser;

public class ProfilCarePlanPageParserManager implements PageParserManager{
	
	Supplier<PageParser> currentPageParserSupplier;
	
	private ProfilCarePlanPageParserManager() {
		//lager parser
		
		CarePlan carePlan=new CarePlan();
		ProfilCarePlanDescriptionMaker carePlanDescriptionMaker=ProfilCarePlanDescriptionMaker.create(carePlan);
		
		ProfilReportMaker reportMaker=ProfilReportMaker.create();
		
		SequenceLineParsers reader=new SequenceLineParsers.Builder()
				.addListener(ListenUntilMatchedOrUnmatched.create(PageHeaderIdentifier.matcher.get()))
				.addListener(EndableToSequenceLineParser.wrap(ProfilCarePlanMaker.create(carePlan)))
				.addListener(
					SimpleSequenceLineParser.create(
							l->{
								carePlanDescriptionMaker.readLine(l);
							},
							()->Optional.empty(),
							()->Optional.of(ProfilCarePlanDescriptionMaker.endDescriptions.get())
						))
				.addListener(
					SimpleSequenceLineParser.create(
						l->{
							reportMaker.readLine(l);
						},
						()->Optional.empty(),
						()->Optional.of(ReportEndMatcher.endMatcher.get())
					))
				.build();
		
		currentPageParserSupplier=()->{
			FirstPageReportReader firstParser= FirstPageReportReader.create(reader);
			
			currentPageParserSupplier=()->NormalReportLineParser.create(reader);
			
			return firstParser;
		};
		
		
	}
	
	public static ProfilCarePlanPageParserManager create() {
		return new ProfilCarePlanPageParserManager();
	}	

	@Override
	public PageParser getPageParser() {
		return currentPageParserSupplier.get();
	}
}
