package messages.readers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import messages.model.MessageSection;
import util.endable.EndableLineParser;
import util.hocr.HocrColumnSegments;
import util.hocr.HocrColumnSegments.HocrColumnTextLines;
import util.hocr.HocrLegendRowBreaker;
import util.hocr.HocrTable;
import util.hocr.HocrTable.HocrTableRow;
import util.lineParser.TextLine;

public class TwoColumnMessageReader implements EndableLineParser{
	
	private final MessageSection message=new MessageSection();
	
	private String header;
	
	private List<TextLine> lines=new ArrayList<>();
	
	private boolean isEnded=false;
	
	public TwoColumnMessageReader() {
		System.out.println("lager TwoColumnMessageReader");
	}

	@Override
	public void readLine(TextLine line) {
		if(header==null || header=="") {
			header=line.getLineConcatString();
		}else {
			lines.add(line);
		}
		
		
		
		
		
	}

	@Override
	public boolean isEnded() {
		return isEnded;
	}

	@Override
	public void end() {
		
		if(isEnded) {
			return;
		}
		
		HocrColumnSegments columns=new HocrColumnSegments();
		
		for(TextLine line:lines) {
			columns.addLine(line);
		}
		
		HocrColumnTextLines segmented = columns.getSegmentedLines(2);
		
		HocrTable table=new HocrTable(segmented,()->new HocrLegendRowBreaker());
		
		List<LegendContent> entries=new ArrayList<>();
		
		
		for(HocrTableRow tr:table.rows()){
			LegendContent currentEntry=new LegendContent();
			List<Optional<List<TextLine>>> b = tr.get();
			
			b.get(0).ifPresent(t->t.forEach(e->currentEntry.addLegend(e.getLineConcatString()+" ")));
			b.get(1).ifPresent(t->t.forEach(e->currentEntry.addContent(e.getLineConcatString())));
			
			entries.add(currentEntry);
			
			System.out.println(currentEntry);
		}
		
		
		
		
		isEnded=true;
	}
	
	private static class LegendContent{
		

		private final StringBuilder  legend=new StringBuilder();
		private final StringBuilder  content=new StringBuilder();
		
		private LegendContent() {
			
		}
		
		public void addLegend(String lineConcatString) {
			legend.append(lineConcatString);
		}

		public void addContent(String string) {
			content.append(string);
		}

		@Override
		public String toString() {
			return "LegendContent [legend=" + legend + ", content=" + content + "]";
		}
		
		
	}
	
}
