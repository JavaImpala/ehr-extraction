package util.hocr;

import java.util.List;
import java.util.Optional;

import util.hocr.HocrColumnSegments.TextInColumn;

public class HocrBoxRowBreaker implements HocrRowBreaker{
	private Object box;
	
	public HocrBoxRowBreaker() {
		
	}
	
	@Override
	public boolean shoudBreak(List<Optional<TextInColumn>> line) {
		
		if(box==null) {
			for(Optional<TextInColumn> l:line) {
				if(l.isPresent()) {
					
					
					box=l.get().getText().getOpenCVPart();
					
					break;
				}
			}
		}else{
			for(Optional<TextInColumn> l:line) {
				if(l.isPresent()) {
					if(!box.equals(l.get().getText().getOpenCVPart())) {
						return true;
					}
					
					break;
				}
			}
		}
		
		
		return false;
	}
}
