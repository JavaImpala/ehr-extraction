package messages;

import java.util.Optional;
import java.util.function.Supplier;

import messages.readers.FirstPageMessageReader;
import messages.readers.NormalPageMessageReader;
import reports.ProfilReportMaker;
import reports.readers.ReportEndMatcher;
import util.pageProcessor.PageParser;
import util.pageProcessor.PageParserManager;
import util.sequence.SequenceLineParsers;
import util.sequence.SimpleSequenceLineParser;

public class ProfilMessageParserManager implements PageParserManager{

	Supplier<PageParser> currentPageParserSupplier;
	
	private ProfilMessageParserManager() {
		//lager parser
		
	
		
		ProfilMessageMaker reportMaker=ProfilMessageMaker.create();
		
		SequenceLineParsers reader=new SequenceLineParsers.Builder()
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
			FirstPageMessageReader firstParser= FirstPageMessageReader.create(reader);
			
			currentPageParserSupplier=()->NormalPageMessageReader.create(reader);
			
			return firstParser;
		};
		
		
	}
	
	public static ProfilMessageParserManager create() {
		return new ProfilMessageParserManager();
	}	

	@Override
	public PageParser getPageParser() {
		return currentPageParserSupplier.get();
	}

}
