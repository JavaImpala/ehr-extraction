package messages.readers;

import java.util.Iterator;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import reports.ProfilReportMaker;
import reports.readers.FirstPageReportReader;
import reports.readers.ReportStartMatcher;
import util.endable.EndableLineParser;
import util.endable.EndableWrapper;
import util.lineParser.TextLine;
import util.matcher.Matcher;
import util.matcher.MatchingState;
import util.pageProcessor.Page;
import util.pageProcessor.PageParser;
import util.sequence.ListenFrom;
import util.sequence.SequenceLineParser;

public class NormalPageMessageReader implements PageParser{
	private final static Logger log=LogManager.getLogger(NormalPageMessageReader.class.getSimpleName());
	
	private Optional<ProfilReportMaker> currentReport;

	private final EndableLineParser parser;

	private NormalPageMessageReader(EndableLineParser contentParser) {
		this.parser=contentParser;
	}
	
	public static NormalPageMessageReader create(EndableLineParser contentParser) {
		return new NormalPageMessageReader(contentParser);
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
