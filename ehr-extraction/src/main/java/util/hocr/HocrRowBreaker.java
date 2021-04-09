package util.hocr;
import java.util.List;
import java.util.Optional;

import util.hocr.HocrColumnSegments.TextInColumn;

public interface HocrRowBreaker {
	
	public boolean shoudBreak(List<Optional<TextInColumn>> line);
	
}
