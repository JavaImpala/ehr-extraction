package util.sequence;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import util.endable.EndableLineParser;
import util.lineListeners.LineParser;
import util.matcher.Matcher;
import util.matcher.MatchingState;

public class SequenceLineListeners implements EndableLineParser{
	private final EndableLineParser lineListener;
	
	private SequenceLineListeners(List<SequenceLineListener> makers) {
		
		//MutableObject<String> prevLine=new MutableObject<>("");
		MutableInt currentIndex=new MutableInt(0);
		
		lineListener=new EndableLineParser() {
			
			private boolean ended=false;
			
			private LinkedList<String> undigested=new LinkedList<>();
			private LineEater currentEater;
			
			@Override
			public void readLine(String s) {
				
				if(currentEater!=null) {
					LineEaterState response=currentEater.digest(s);
					
					if(response==LineEaterState.EATEN) {
						currentEater=null;
						
						if(!undigested.isEmpty()) {
							readLine(undigested.pop());
						}
					}else if(response==LineEaterState.EATING) {
						undigested.add(s);
					}else if(response==LineEaterState.GOTONEXT) {
						currentEater=null;
						currentIndex.increment();
						
						readLine(undigested.pop());
					}
					
				}else if(currentIndex.getValue()<makers.size()-1) {
					
					currentEater=new LineEater(
							makers.get(currentIndex.getValue()+1).shouldStart(),//should next start
							makers.get(currentIndex.getValue()).shouldEnd(),
							makers.get(currentIndex.getValue()));
					
					readLine(s);
				}else if(currentIndex.getValue()<makers.size()) {
					currentEater=new LineEater(
							Optional.empty(),//no next
							makers.get(currentIndex.getValue()).shouldEnd(),
							makers.get(currentIndex.getValue()));
					
					readLine(s);
				}
				
			}

			@Override
			public boolean isEnded() {
				return ended;
			}

			@Override
			public void end() {
				ended=true;
				makers.get(currentIndex.getValue()).end();
			}
		};
	}
	
	@Override
	public void readLine(String line) {
		lineListener.readLine(line);
	}
	
	@Override
	public boolean isEnded() {
		return lineListener.isEnded();
	}

	@Override
	public void end() {
		lineListener.end();
	}
	
	public static class Builder{
		private final List<SequenceLineListener> makers=new ArrayList<>();
		
		public Builder() {
			
		}
		
		public Builder addListener(SequenceLineListener maker) {
			this.makers.add(maker);return this;
		}
		
		
		public SequenceLineListeners build() {
			return new SequenceLineListeners(this.makers);
		}
	}
	
	private static class LineEater{
		
		
		private boolean hasDigested=false;
		
		private Optional<Matcher> start;
		private Optional<Matcher> end;
		
		private final LineParser lineListener;
		
		private LineEater(
				Optional<Matcher> start,
				Optional<Matcher> end,
				LineParser lineListener) {
			
			this.start=start;
			this.end=end;
			
			this.lineListener=lineListener;
		}
		
		private LineEaterState digest(String line) {
			
			end.ifPresent(m->m.readLine(line));
			
			if(start.isPresent() ) {
				start.get().readLine(line);
				
				if(start.get().getState()==MatchingState.MATCHED) {
					return LineEaterState.GOTONEXT;
				}else if(start.get().getState()==MatchingState.UNMATCHED) {
					start=Optional.empty();
				}
			}
			
			if(end.isPresent() ) {
				end.get().readLine(line);
				
				if(end.get().getState()==MatchingState.MATCHED) {
					return LineEaterState.GOTONEXT;
				}else if(start.get().getState()==MatchingState.UNMATCHED) {
					end=Optional.empty();
				}
			}
			
			if(start.isEmpty() && end.isEmpty()) {
				this.lineListener.readLine(line);
				return LineEaterState.EATEN;
			}
			
			return LineEaterState.EATING;
		}
	}
	
	private enum LineEaterState{
		EATING,EATEN,GOTONEXT;
	}
	
}
