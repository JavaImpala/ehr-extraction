package util.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.mutable.MutableInt;

public class ChainMatch implements Matcher{
	private Consumer<String> lineListener=s->{};
	
	private MatchingState state=MatchingState.READY;
	
	private ChainMatch(List<Pattern> patterns) {
		
		MutableInt i=new MutableInt(0);
		
		
		lineListener=s->{
			
			Pattern pattern=patterns.get(i.getValue());
			
			if(pattern.matcher(s).matches()) {
				
				
				if(i.getValue()>=patterns.size()-1) {
					state=MatchingState.MATCHED;
					lineListener=(o)->{};
				}else {
					state=MatchingState.MATCHING;
					
				}
			}else {
				if(state!=MatchingState.READY) {
					state=MatchingState.UNMATCHED;
					lineListener=(o)->{};
				}
				
			}
			
			if(state!=MatchingState.READY) {
				i.increment();
			}
		};
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
