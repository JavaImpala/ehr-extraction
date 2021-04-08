package util.lineParser;

import java.util.ArrayList;
import java.util.List;

public class TextLine {

	

	@Override
	public String toString() {
		return "TextLine [hocrPartition=" + hocrPartition + ", openCVPart=" + openCVPart + ", y=" + y + ", height="
				+ height + ", tallestWord=" + tallestWord + ", shortestWord=" + shortestWord + "]";
	}

	private final HocrPartition hocrPartition;
	private final OpenCVPartition openCVPart;
	
	private final Double y;
	private final double height;
	
	private double tallestWord=-1;
	private double shortestWord=-1;
	
	private final List<TextLineWord> words=new ArrayList<>();


	
	
	private TextLine(HocrPartition hocrPartition,OpenCVPartition openCVPart,double y, double height) {
		this.y = y;
		this.height = height;
		this.hocrPartition=hocrPartition;
		this.openCVPart=openCVPart;
	}
	
	public static TextLine create(HocrPartition hocrPartition,OpenCVPartition openCVPart, double y, double height) {
		return new TextLine(hocrPartition,openCVPart, y, height);
	}
	public Double getY() {
		return y;
	}
	public double getHeight() {
		return height;
	}
	
	public HocrPartition getHocrPartition() {
		return hocrPartition;
	}

	public OpenCVPartition getOpenCVPart() {
		return openCVPart;
	}

	
	public void addWord(TextLineWord word) {
		this.words.add(word);
		
		if(tallestWord<word.getHeight()) {
			tallestWord=word.getFontSize();
		}
		
		if(shortestWord>word.getHeight() || shortestWord<0) {
			shortestWord=word.getFontSize();
		}
	}
	
	public double getTallestWord() {
		return tallestWord;
	}

	public double getShortestWord() {
		return shortestWord;
	}
	
	public List<TextLineWord> getWords(){
		return words;
	}
	
	public String getLineConcatString() {
		StringBuilder line=new StringBuilder();
		
		for(TextLineWord word:words) {
			line.append(word.getWord()+" ");
		}
		
		return line.toString().trim();
	}	
}
