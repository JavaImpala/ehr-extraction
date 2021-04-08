package messages;

import java.util.Optional;

import messages.readers.MessageSectionReader;
import util.endable.ObservableEndableLineParser;
import util.lineParser.LineListenerState;
import util.lineParser.ObservableLineParser;
import util.lineParser.RepeatLineParser;
import util.lineParser.TextLine;
import util.matcher.SingleLineMatcher;
import util.sequence.SequenceLineParsers;
import util.sequence.SimpleSequenceLineParser;

public class ProfilMessageMaker  implements ObservableLineParser{
	
	private LineListenerState state=LineListenerState.READY;
	private RepeatLineParser lineParser;
	
	private ProfilMessageMaker() {
		
		this.lineParser=RepeatLineParser.create(
				()->SingleLineMatcher.wrapPattern(l->{
					return (l.getTallestWord()>19);
				}),
				()->{
					System.out.println("");
					System.out.println("=========");
					System.out.println("ProfilMessageMaker ny section");
					System.out.println("");
					
					MessageSectionReader reader = new MessageSectionReader();
					
					return ObservableEndableLineParser.wrap(
							new SequenceLineParsers.Builder()
								
								.addListener(
									SimpleSequenceLineParser.create(
										reader, 
										()->Optional.empty(),
										()->Optional.empty())) 	
								.build(),
							()->{
								reader.end();
							});
				});
		
	}
	
	
	
	public static ProfilMessageMaker create() {
		return new ProfilMessageMaker();
	}
	
	@Override
	public void readLine(TextLine line) {
		this.lineParser.readLine(line);
	}
	
	@Override
	public LineListenerState getState() {
		return state;
	}
}