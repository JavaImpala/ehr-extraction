package messages;

import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableBoolean;

import messages.readers.MessageSectionReader;
import util.endable.ObservableEndableLineParser;
import util.lineParser.LineListenerState;
import util.lineParser.ObservableLineParser;
import util.lineParser.RepeatLineParser;
import util.lineParser.TextLine;
import util.matcher.SingleLineMatcher;
import util.sequence.SequenceLineParsers;
import util.sequence.SimpleSequenceLineParser;

public class ProfilMessageSectionMaker  implements ObservableLineParser{
	
	private LineListenerState state=LineListenerState.READY;
	private RepeatLineParser lineParser;
	
	private ProfilMessageSectionMaker() {
		
		MutableBoolean atLastSection=new MutableBoolean(false);
		
		this.lineParser=RepeatLineParser.create(
			()->SingleLineMatcher.wrapPredicate(l->{
				
				if(l.getFontSize()>19 && l.getFilling()>0.9) {
					return true;
				}
				
				return false;
				
				
			}),
			()->{
				System.out.println("");
			
				System.out.println("ProfilMessageMaker ny section");
				
				
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
	
	public static ProfilMessageSectionMaker create() {
		return new ProfilMessageSectionMaker();
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