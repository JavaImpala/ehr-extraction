package reports;

import java.util.function.Supplier;

import reports.readers.FirstPageReportReader;
import reports.readers.NormalReportLineParser;
import util.pageProcessor.PageParser;
import util.pageProcessor.PageParserManager;

public class ProfilCarePlanParser implements PageParserManager{
	
	Supplier<PageParser> currentPageParserSupplier;
	
	private ProfilCarePlanParser() {
		currentPageParserSupplier=()->{
			FirstPageReportReader firstParser= FirstPageReportReader.create();
			
			currentPageParserSupplier=()->NormalReportLineParser.create();
			
			return firstParser;
		};
		
		
	}
	
	public static ProfilCarePlanParser create() {
		return new ProfilCarePlanParser();
	}	

	@Override
	public PageParser getPageParser() {
		return currentPageParserSupplier.get();
	}
}
