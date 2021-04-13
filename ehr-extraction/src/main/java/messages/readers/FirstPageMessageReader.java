package messages.readers;

import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import reports.ProfilReportMaker;
import util.endable.EndableLineParser;
import util.endable.EndableWrapper;
import util.lineParser.TextLine;
import util.pageProcessor.Page;
import util.pageProcessor.PageParser;
import util.sequence.ListenFrom;
import util.sequence.SequenceLineParser;
import util.sequence.SequenceLineParsers;

public class FirstPageMessageReader implements PageParser{
	private final static Logger log=LogManager.getLogger(FirstPageMessageReader.class.getSimpleName());
	
	private Optional<ProfilReportMaker> currentReport;

	private final EndableLineParser parser;

	private FirstPageMessageReader(EndableLineParser contentParser) {
		this.parser=contentParser;
	}
	
	public static FirstPageMessageReader create(EndableLineParser contentParser) {
		return new FirstPageMessageReader(contentParser);
	}

	@Override
	public boolean tryToProccessPage(Page page) {
		
		MutableBoolean reading=new MutableBoolean(false);
		
	
		SequenceLineParser reader = ListenFrom.listenFrom(
					EndableWrapper.wrap(l->{
						
						reading.setTrue();
					
						parser.readLine(l);
					}),
					MessageStartMatcher.startMatcher.get(),
					0
				);
		
		for(TextLine l:page.getStructPage().lines()) {
			
			reader.readLine(l);
		}
		
		return reading.isTrue();
	}

	public Optional<ProfilReportMaker> rollOverReport(){
		return currentReport;
	}
}
