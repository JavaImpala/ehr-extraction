package util.hocr;

import java.util.List;
import java.util.Optional;

import util.hocr.HocrColumnSegments.TextInColumn;

public class HocrBoxRowBreaker implements HocrRowBreaker{
	private String legend="";
	
	@Override
	public boolean shoudBreak(List<Optional<TextInColumn>> line) {
		if(line.get(0).isPresent()) {
			if(legend!="") {
				
			}
		}
		
		return false;
	}
}
