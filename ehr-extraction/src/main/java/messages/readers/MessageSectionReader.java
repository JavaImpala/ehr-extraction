package messages.readers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

import util.endable.EndableLineParser;
import util.endable.EndableWrapper;
import util.lineParser.TextLine;

public class MessageSectionReader implements EndableLineParser{
	private static final Map<String,Supplier<EndableLineParser>> subParsers=new HashMap<>();
	
	public static HashSet<String> dontSupport=new HashSet<>();
	
	static {
		subParsers.put(
			"Medisinskfaglige opplysninger",
			()->{
				return new TwoColumnMessageReader();
			});
		subParsers.put(
			"Forespørsel",
			()->{
				return new TwoColumnMessageReader();
			});
		subParsers.put(
			"Notat",
			()->{
				return new TwoColumnMessageReader();
			});
		
		subParsers.put(
				"Legemidler",
				()->{
					return new MedicationMessageReader();
				});
		
	}
	
	private static final Supplier<EndableLineParser> dummyParser=()->EndableWrapper.wrap(l->{});
	
	private EndableLineParser parser=null;
	
	@Override
	public void readLine(TextLine line) {
		
		if(parser==null) {
			System.out.println(line.getLineConcatString());
			
			if(subParsers.containsKey(line.getLineConcatString())) {
				parser=subParsers.get(line.getLineConcatString()).get();
			}else {
				dontSupport.add(line.getLineConcatString());
				
				parser=dummyParser.get();
			}
		}
		
		parser.readLine(line);
	}

	@Override
	public boolean isEnded() {
		if(parser==null) {
			return false;
		}
		return parser.isEnded();
	}

	@Override
	public void end() {
		if(parser==null) {
			parser=dummyParser.get();
		}
		
		parser.end();
	}
}
