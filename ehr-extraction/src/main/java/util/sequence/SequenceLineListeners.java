package util.sequence;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableInt;

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
			
			private LineEater getEater(String food) {
				System.out.println("lager ny eater basert på index "+currentIndex.getValue()+" "+makers.size());
				
				if(currentIndex.getValue()<makers.size()-1) {
					
					return new LineEater(
							makers.get(currentIndex.getValue()+1).getNewShouldStart(),//should next start
							makers.get(currentIndex.getValue()).getNewShouldEnd(),
							food,
							makers.get(currentIndex.getValue()));
				}else if(currentIndex.getValue()<makers.size()) {
					
					return new LineEater(
							Optional.empty(),//no next
							makers.get(currentIndex.getValue()).getNewShouldEnd(),
							food,
							makers.get(currentIndex.getValue()));
				}else {
					throw new IllegalArgumentException("vi er ended"); 
				}
			}
			
			
			
			@Override
			public void readLine(String s) {
				undigested.add(s);
				consumeFood(0);
			}
			
			private void consumeFood(int head) {
				
				
				if(head>=undigested.size()) {
					return;
				}
				
				String s=undigested.get(head);
				
				
				
				if(currentEater==null) {
					if(head!=0) {
						throw new IllegalArgumentException("det skjer noe jeg ikke forstår"); 
					}
					
					currentEater=getEater(s);
				}
				
				
				
				currentEater.digest(s);
				
				System.out.println("forsøker å spise "+s+" "+currentEater.end+" "+currentEater.start);
				
				if(currentEater.state==LineEaterState.EATEN) {
					System.out.println("===================>SPISER "+s);
					
					currentEater.eater.readLine(undigested.removeFirst());
					currentEater=null;
					consumeFood(0);
				}else if(currentEater.state==LineEaterState.EATING) {
					consumeFood(head+1);
				}else if(currentEater.state==LineEaterState.GOTONEXT) {
					
					System.out.println("VI GÅR TIL NESTE!============="+undigested.get(0));
					
					currentIndex.increment();
					currentEater=null;
					
					if(currentIndex.getValue()>=makers.size()) {
						ended=true;
					}else {
						consumeFood(0);
					}
					
					
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
		
		private Optional<Matcher> start;
		private Optional<Matcher> end;
		
		private LineEaterState state=LineEaterState.EATING;	
		
		private final String toBeEaten;
		private final LineParser eater;
		
		private LineEater(
				Optional<Matcher> start,
				Optional<Matcher> end,
				String toBeEaten,
				LineParser eater) {
			
			this.start=start;
			this.end=end;
			
			this.toBeEaten=toBeEaten;
			this.eater=eater;
		}
		
		private void updateState() {
			if(start.isPresent() ) {
				if(start.get().getState()==MatchingState.MATCHED) {
					state=LineEaterState.GOTONEXT;
				}else if(start.get().getState()==MatchingState.UNMATCHED) {
					start=Optional.empty();
				}
			}
			
			if(end.isPresent() ) {
				if(end.get().getState()==MatchingState.MATCHED) {
					state=LineEaterState.GOTONEXT;
				}else if(end.get().getState()==MatchingState.UNMATCHED) {
					System.out.println("end er unmatched");
					end=Optional.empty();
				}
			}
			
			if(state==LineEaterState.GOTONEXT) {
				return;
			}
			
			if(end.isEmpty() && start.isEmpty()) {
				state=LineEaterState.EATEN;
			}else {
				state=LineEaterState.EATING;
			}
			
		}
		
		private LineEaterState digest(String line) {
			end.ifPresent(m->{
				System.out.println("end spiser line "+m+" "+line+" "+m);
				m.readLine(line);
			});
			start.ifPresent(m->m.readLine(line));
			
			updateState();
			return state;
		}
	}
	
	private enum LineEaterState{
		EATING,EATEN,GOTONEXT;
	}
	
}
