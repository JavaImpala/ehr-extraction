package util.lineParser;

public class PagePartition {
	private final double y,x,width,height;

	private PagePartition(double x, double y, double width, double height) {
		this.y = y;
		this.x = x;
		this.width = width;
		this.height = height;
	}
	
	public static PagePartition create(double x1, double y1, double x2, double y2) {
		return new PagePartition(x1, y1,x2-x1,y2-y1);
	}
	
	
}
