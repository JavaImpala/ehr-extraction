package util.lineParser;

import java.util.LinkedList;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import util.matcher.Matcher;
import util.matcher.MatchingState;

public class RepeatLineParser implements LineParser{
	private final static Logger log=LogManager.getLogger(RepeatLineParser.class.getSimpleName());
	
	private Supplier<Matcher> shouldRestartSupplier;
	private Supplier<LineParser> readers;
	
	private LinkedList<String> undigested=new LinkedList<>();
	
	private LineParser currentParser=null;
	private Matcher shouldRestart=null;
	
	public RepeatLineParser(Supplier<Matcher> shouldRestartSupplier, Supplier<LineParser> readers) {
		this.shouldRestartSupplier = shouldRestartSupplier;
		this.readers = readers;
		
		this.shouldRestart=this.shouldRestartSupplier.get();
		this.currentParser=this.readers.get();
	}
	
	@Override
	public void readLine(String s) {
		undigested.add(s);
		consumeFromQueue(undigested.size()-1);
	}
	
	private void consumeFromQueue(int head) {
		if(head>=undigested.size()) {
			log.info("returned fordi vi ikke har nok");
			return;
		}
		
		String s=undigested.get(head);
		
		shouldRestart.readLine(s);
		
		if(shouldRestart.getState()==MatchingState.MATCHING) {
			consumeFromQueue(head+1);
		}else if(shouldRestart.getState()==MatchingState.UNMATCHED) {
			currentParser.readLine(undigested.removeFirst());
			
			shouldRestart=this.shouldRestartSupplier.get();
			
			consumeFromQueue(0);
		}else if(shouldRestart.getState()==MatchingState.MATCHED ) {
			
			shouldRestart=this.shouldRestartSupplier.get();
			
			currentParser=this.readers.get();
			currentParser.readLine(undigested.removeFirst());
			
			consumeFromQueue(0);
		}
	}
}
