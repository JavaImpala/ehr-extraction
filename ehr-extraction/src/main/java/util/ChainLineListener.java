package util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.mutable.MutableObject;

public class ChainLineListener implements LineListener{
	private final Consumer<String> lineListener;
	
	private ChainLineListener(List<LineListener> makers) {
		MutableObject<Consumer<String>> globalListener=new MutableObject<>();
		
		Consumer<String> next=null;
		
		for(int i=makers.size()-1;i>=0;i--) {
			LineListener listener=makers.get(i);
			
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
		
		lineListener=s->globalListener.getValue().accept(s);
	}
	
	@Override
	public void readLine(String line) {
		lineListener.accept(line);
	}
	
	public static class Builder{
		private final List<LineListener> makers=new ArrayList<>();
		
		public Builder() {
			
		}
		
		public Builder addPattern(LineListener maker) {
			this.makers.add(maker);return this;
		}
		
		public ChainLineListener build() {
			return new ChainLineListener(this.makers);
		}
	}
}
