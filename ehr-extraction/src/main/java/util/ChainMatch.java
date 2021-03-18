package util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.mutable.MutableObject;

public class ChainMatch implements Matcher{
	private final Consumer<String> lineListener;
	
	private MatchingState state=MatchingState.READY;
	
	private ChainMatch(List<Pattern> patterns) {
		MutableObject<Consumer<String>> listener=new MutableObject<>();
		
		Consumer<String> next=null;
		
		for(int i=patterns.size()-1;i>=0;i--) {
			Pattern pattern=patterns.get(i);
			
			Consumer<String> nextInLine=next; //caches next
			
			Consumer<String> stringConsumer=s->{
				
				if(pattern.matcher(s).matches()) {
					if(nextInLine==null) {
						state=MatchingState.MATCHED;
					}else {
						state=MatchingState.MATCHING;
						
						listener.setValue(nextInLine);
					}
				}else {
					state=MatchingState.UNMATCHED;
				}
			};
			
			next=stringConsumer;
			listener.setValue(stringConsumer);
		}
		
		lineListener=s->listener.getValue().accept(s);
	}
	
	
	public MatchingState getState() {
		return this.state;
	}

	@Override
	public void readLine(String line) {
		lineListener.accept(line);
	}
	
	public static class Builder{
		private final List<Pattern> patterns=new ArrayList<>();
		
		public Builder() {
			
		}
		
		public Builder addPattern(Pattern pattern) {
			this.patterns.add(pattern);return this;
		}
		
		public ChainMatch build() {
			return new ChainMatch(this.patterns);
		}
	}
	
}
