package util.pageProcessor;

import java.util.Iterator;
import java.util.function.Supplier;

import util.endable.EndableLineParser;

public class SimplePageProcessor implements PageParser{
	
	private final EndableLineParser parser;
	
	public SimplePageProcessor(EndableLineParser parser) {
		this.parser = parser;
	}

	@Override
	public boolean tryToProccessPage(Supplier<Iterator<String>> lines) {
		
		Iterator<String> parseIterator = lines.get();
		
		while(parseIterator.hasNext()) {
			parser.readLine(parseIterator.next());
			
			if(parser.isEnded()) {
				return false;
			}
		}
		
		return true;
	}
}
