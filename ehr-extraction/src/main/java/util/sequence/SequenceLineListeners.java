package util.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import util.endable.EndableLineParser;

public class SequenceLineListeners implements EndableLineParser{
	private final EndableLineParser lineListener;
	
	private SequenceLineListeners(List<SequenceLineListener> makers) {
		MutableObject<Consumer<String>> globalListener=new MutableObject<>();
		
		Consumer<String> next=null;
		
		for(int i=makers.size()-1;i>=0;i--) {
			SequenceLineListener listener=makers.get(i);
			
			Consumer<String> nextInLine=next; //caches next
			
			Consumer<String> stringConsumer=s->{
				
				listener.readLine(s);
				
				if(nextInLine!=null) {
					globalListener.setValue(nextInLine);
				}
				
			};
			
			next=stringConsumer;
			globalListener.setValue(stringConsumer);
		}
		
		
		
		//MutableObject<String> prevLine=new MutableObject<>("");
		MutableInt currentIndex=new MutableInt(0);
		
		lineListener=new EndableLineParser() {
			
			private boolean ended=false;
			
			@Override
			public void readLine(String s) {
				if(currentIndex.getValue()<makers.size()-1) {
					boolean startNext=makers.get(currentIndex.getValue()+1).shouldStart(s);
					
					if(startNext) {
						makers.get(currentIndex.getValue()).end();
						currentIndex.increment();
						readLine(s);
						return;
					}
				}
				
				if(makers.get(currentIndex.getValue()).shouldEnd(s)) {
					makers.get(currentIndex.getValue()).end();
					currentIndex.increment();
					
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

	
}
