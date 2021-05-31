package messages;

import java.util.Optional;
import java.util.function.Supplier;

import messages.readers.FirstPageMessageReader;
import messages.readers.NormalPageMessageReader;
import util.matcher.SingleLineMatcher;
import util.pageProcessor.PageParser;
import util.pageProcessor.PageParserManager;
import util.sequence.SequenceLineParsers;
import util.sequence.SimpleSequenceLineParser;

public class ProfilMessageParserManager implements PageParserManager{

	Supplier<PageParser> currentPageParserSupplier;
	
	private ProfilMessageParserManager() {
		//lager parser
		
		ProfilMessageSectionMaker reportMaker=ProfilMessageSectionMaker.create();
		
		SequenceLineParsers reader=new SequenceLineParsers.Builder()
				.addListener(
						SimpleSequenceLineParser.create(
							l->{
								
							},
							()->Optional.empty(),
							()->Optional.empty()
						))
				.addListener(
					SimpleSequenceLineParser.create(
						l->{
							//System.out.println("readingLine profilMessageParserManager "+l.getLineConcatString());
							reportMaker.readLine(l);
						},
						()->Optional.of(
								SingleLineMatcher.wrapPredicate(
										l->{
											
											if(l.getFontSize()>19 && l.getFilling()>0.9) {
												return true;
											}
											
											return false;
											
											
										}
									)),
						()->Optional.empty()
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
