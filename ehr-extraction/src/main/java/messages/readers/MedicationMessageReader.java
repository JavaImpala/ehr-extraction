package messages.readers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import messages.model.MessageSection;
import util.endable.EndableLineParser;
import util.hocr.HocrBoxRowBreaker;
import util.hocr.HocrColumnSegments;
import util.hocr.HocrColumnSegments.HocrColumnTextLines;
import util.hocr.HocrTable;
import util.hocr.HocrTable.HocrTableRow;
import util.lineParser.TextLine;

public class MedicationMessageReader implements EndableLineParser{
	
	private final MessageSection message=new MessageSection();
	
	private String header;
	
	private List<TextLine> lines=new ArrayList<>();
	
	private boolean isEnded=false;
	
	public MedicationMessageReader() {
		//System.out.println("lager MedicationReader");
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
		
		HocrColumnTextLines segmented = columns.getSegmentedLines(8);
		
		HocrTable table=new HocrTable(segmented,()->new HocrBoxRowBreaker());
		
		int rc=0;
		
		for(HocrTableRow tr:table.rows()){
			//System.out.println("row "+rc);
			
			int i=0;
			//System.out.println("fungerer dette?");
			for(Optional<List<TextLine>> v:tr.get()) {
				//System.out.println("col "+i);
				
				if(v.isPresent()) {
					for(TextLine a:v.get()) {
						//System.out.println(a.getLineConcatString());
					}
				}
				
				i++;
			}
			
			rc++;
		}
		
		//System.out.println("end medication");
		
		isEnded=true;
	}
	
	
	
}
