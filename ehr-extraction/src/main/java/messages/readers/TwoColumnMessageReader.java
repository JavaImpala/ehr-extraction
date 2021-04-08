package messages.readers;

import java.util.ArrayList;
import java.util.List;

import messages.model.MessageSection;
import util.endable.EndableLineParser;
import util.hocr.Jenks;
import util.hocr.Jenks.Breaks;
import util.lineParser.TextLine;
import util.lineParser.TextLineWord;

public class TwoColumnMessageReader implements EndableLineParser{
	
	private final MessageSection message=new MessageSection();
	
	private List<TextLine> lines=new ArrayList<>();
	
	private double startX=Double.POSITIVE_INFINITY;
	
	private static final double wordDistBreak=50d;
	
	private boolean isEnded=false;
	
	public TwoColumnMessageReader() {
		System.out.println("lager TwoColumnMessageReader");
	}

	@Override
	public void readLine(TextLine line) {
		if(line.getWords().get(0)!=null && line.getWords().get(0).getX()<startX) {
			startX=line.getWords().get(0).getX();
		}
		
		lines.add(line);
		
	}

	@Override
	public boolean isEnded() {
		return isEnded;
	}

	@Override
	public void end() {
		
		TextLineWord dummyStartWord = TextLineWord.createWithoutParent(startX, 0, 0,0,"dummyStartWord", 10);
		
		List<Double> breaks=new ArrayList<>();
		breaks.add(startX);
		
		Jenks jenks=new Jenks();	
		
		//finner kolonneinndeling
		for(TextLine line:lines) {
			
			List<TextLineWord> words=new ArrayList<>();
			
			words.add(dummyStartWord);
			words.addAll(line.getWords());
			
			for(int i=1;i<words.size();i++) {
				
				TextLineWord prevWord = words.get(i-1);
				TextLineWord currentWord = words.get(i);
				
				if(currentWord.getX()>(prevWord.getX()+prevWord.getWidth()+wordDistBreak)) {
					//System.out.println("vi har break mellom "+prevWord.getWord()+" / "+currentWord.getWord()+" "+(currentWord.getX()-(prevWord.getX()+prevWord.getWidth()))+" "+currentWord.getX());
					
					jenks.addValue(currentWord.getX());
				}
			}
		}
		
		Breaks clusteredBreaks=jenks.computeBreaks(2);
		
		List<ColumnBounds> columns=new ArrayList<>();
		
		for(int b=1;b<clusteredBreaks.numClassses();b++) {
			System.out.println(clusteredBreaks.getClassMin(b)+"-"+clusteredBreaks.getClassMax(b));
			
			columns.add(
					new ColumnBounds(
							clusteredBreaks.getClassMin(b-1),
							clusteredBreaks.getClassMax(b)));
		}
		
		columns.add(
				new ColumnBounds(
						clusteredBreaks.getClassMin(clusteredBreaks.numClassses()-1),
						Double.POSITIVE_INFINITY));
		
		
		
		//sorterer ord til kolonner
		for(TextLine line:lines) {
			
			List<TextLineWord> words=line.getWords();
			
			for(int i=0;i<words.size();i++) {
				
				TextLineWord currentWord = words.get(i);
				
				ColumnBounds current=columns.get(0);
				double match=0;
				
				for(ColumnBounds column:columns) {
					
					double candMatch=column.dist(currentWord.getX());
					
					if(candMatch>match) {
						current=column;
						match=candMatch;
					}
				}
				
				current.addWord(currentWord);
			}
		}
		
		for(ColumnBounds column:columns) {
			
			System.out.println(column);
		}
		
		isEnded=true;
	}
	
	private static class ColumnBounds{
		
		@Override
		public String toString() {
			return "ColumnBounds [words=" + words + ", start=" + start + ", stop=" + stop + "]";
		}

		private final List<TextLineWord> words=new ArrayList<>();
		private final double start,stop;

		public ColumnBounds(double start, double stop) {
			this.start = start;
			this.stop = stop;
		}
		
		public double overlap(double beginning,double end) {
			if(beginning>stop || end<start) {
				return 0;
			}
			
			double span=end-beginning;
			double overlap= span-(Double.max(start,beginning)-beginning)-(end-Double.min(end,stop));
			
			return overlap/span;
		}
		
		public double dist(double beginning) {
			return Math.abs(beginning-start);
		}
		
		public void addWord(TextLineWord word) {
			words.add(word);
		}
		
		
	}
	
	
	private static class LegendContent{
		

		private final String legend;
		private final String content;
		
		private LegendContent(String legend, String content) {
			this.legend = legend;
			this.content = content;
		}
		
		public String getLegend() {
			return legend;
		}

		public String getContent() {
			return content;
		}
	}
	
}
