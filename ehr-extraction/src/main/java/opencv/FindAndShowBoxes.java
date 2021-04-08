package opencv;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class FindAndShowBoxes {
	private static double dilate=4;
	
	public FindAndShowBoxes(){
		
	}
	
    public void proccess(String path) {
        // Load the image
        List<Mat> mats=new ArrayList<>();
    	
    	Mat src = Imgcodecs.imread(path);
        
    	mats.add(src);
        
        // Transform source image to gray if it is not already
        Mat gray = new Mat();
        mats.add(gray);
        
        if (src.channels() == 3){
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        }else{
            gray = src;
        }
        
        // Show gray image
        //showWaitDestroy("gray" , gray);
        // Apply adaptiveThreshold at the bitwise_not of gray
        Mat bw = new Mat();
        mats.add(bw);
        
        Core.bitwise_not(gray, gray);
        
       // Imgproc.threshold(gray,bw, 150, 255,Imgproc.THRESH_OTSU);
        //showWaitDestroy("no lines",bw);
        
        Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);
        //showWaitDestroy("bw",bw);
        
        int kernelSize=5;
     	Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_CROSS, new Size(2 * kernelSize + 1, 2 * kernelSize + 1),
                 new Point(kernelSize, kernelSize));
         
     	//Imgproc.morphologyEx(bw, bw,Imgproc.MORPH_DILATE, element);
     	
        
        // Show binary image
        
        //showWaitDestroy("no lines",bw);
        
        
        //copy of src. we will paint on this after finding lines
        Mat copy = new Mat(src.size(),src.type(),new Scalar(255,255,255));
        src.copyTo(copy);
        
        mats.add(copy);
       
        /*
         * horisontal
         */
        
        List<Line2D> horisontalLines = findHorisontal(
        		Imgproc.getStructuringElement(
                		Imgproc.MORPH_RECT, 
                		new Size(
                				bw.rows() / 30,
                				4)),
        		Imgproc.getStructuringElement(
                		Imgproc.MORPH_RECT, 
                		new Size(
                				bw.rows() / 22,
                				4)),
        		bw,
        		copy);
        
        /*
         * vertical
         */
        
        List<Line2D> verticalLines = findVertical(
        		Imgproc.getStructuringElement(
                		Imgproc.MORPH_RECT, 
                		new Size(
                				2,
                				90)),
        		bw,
        		copy,
        		horisontalLines);
        
       Mat grayCopy = new Mat(copy.size(),copy.type(),new Scalar(255,255,255));
       mats.add(grayCopy);
       
       for(Line2D line:verticalLines) {
    	   Imgproc.rectangle(
    			   grayCopy,
           		new Point(line.getX1()-dilate,line.getY1()-dilate),
           		new Point(line.getX2()+dilate,line.getY2()+dilate),
           		new Scalar(0,0,255),-1);
       }
       
       for(Line2D line:horisontalLines) {
    	  Imgproc.rectangle(
    			   grayCopy,
           		new Point(line.getX1()-dilate,line.getY1()-dilate),
           		new Point(line.getX2()+dilate,line.getY2()+dilate),
           		new Scalar(0,0,255),-1);
       }
     
       //showWaitDestroy("only lines",grayCopy);
      
       List<Rectangle2D> rects = FindRectanglesFromLines.get(horisontalLines, verticalLines);
       
       
      
       for(Rectangle2D rect:rects) {
    	   System.out.println("maler rect "+rect.getMinX()+" "+rect.getMinY()+" "+rect.getMaxX()+" "+rect.getMaxY());
    	   
    	   Imgproc.rectangle(
    			   grayCopy,
           		new Point(rect.getMinX(),rect.getMinY()),
           		new Point(rect.getMaxX(),rect.getMaxY()),
           		new Scalar(
           				(int)(Math.random()*256),
           				(int)(Math.random()*256),
           				(int)(Math.random()*256)),
           		-1);
       }
      
       int counter=0;
       
       int x=30;
       
       for(Rectangle2D rect:rects) {
    	   
    	   double area=rect.getWidth()*rect.getHeight();
    	   
    	   System.out.println(area);
    	   
    	   Imgproc.putText(grayCopy, counter+"",new Point(x,rect.getMinY()+40), 0,2, new Scalar(0,0,255),3);
    	   
    	   Imgproc.rectangle(
    			   grayCopy,
    			   new Point(rect.getMinX(),rect.getMinY()),
              		new Point(rect.getMaxX(),rect.getMaxY()),
           		new Scalar(255,0,0),2);
    	   
    	  
    	   
    	   counter++;
       }
       
       System.out.println(counter);
       
        //scale down copy
       
       showWaitDestroy("no lines",grayCopy);
        
        //threshold til sist
       Mat threshold = new Mat();
       mats.add(threshold);
        
        
       mats.add(grayCopy);
        
       Imgproc.cvtColor(copy, grayCopy, Imgproc.COLOR_BGR2GRAY);
        
       //Imgproc.adaptiveThreshold(grayCopy, threshold, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 501, -2);
       Imgproc.threshold(grayCopy,threshold, 150, 255,Imgproc.THRESH_OTSU);
        
       //Imgcodecs.imwrite(path.substring(0, path.lastIndexOf('.'))+"-proc.tif", threshold);
        
       mats.forEach(m->m.release());
        
      
    }
    
    private  List<Line2D> findHorisontal(Mat kernel,Mat kernel2,Mat bwSource,Mat canvas) {
    	
        // Specify size on horizontal axis
        List<Mat> mats=new ArrayList<>();
        
        // Create structure element for extracting horizontal lines through morphology operations
    	Mat bw = bwSource.clone();
    	mats.add(bw);
        
    	
    	
        
        // Apply morphology operations
        Imgproc.erode(bw,bw,kernel);
        Imgproc.dilate(bw,bw,kernel2);
        
        List<MatOfPoint> contours = new ArrayList<>();
        List<Line2D> rects=new ArrayList<>();
        
        Mat hierarchy = new Mat();
        mats.add(hierarchy);
        
        Imgproc.findContours(bw, contours,hierarchy , Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        
        
        for (int i = 0; i < contours.size(); i++) {
        	MatOfPoint2f poly = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), poly, 1, true);
            
            MatOfPoint matOfPoints = new MatOfPoint(poly.toArray());
            
            Rect rect = Imgproc.boundingRect(matOfPoints);
            
            poly.release();
            matOfPoints .release();
           
            rects.add(new Line2D.Double(
    				rect.x- dilate,
    				rect.y,
    				rect.width+rect.x+ dilate,
    				rect.y));
            
            contours.get(i).release();
        }
        
        mats.forEach(m->m.release());
      
        return rects;
    }
    
    private List<Line2D> findVertical(Mat kernel,Mat bwSource,Mat canvas,List<Line2D> rects) {
    	
        // Specify size on horizontal axis
    	List<Mat> mats=new ArrayList<>();
    	
        List<Line2D> lines=new ArrayList<>();
        // Create structure element for extracting horizontal lines through morphology operations
    	Mat bw = bwSource.clone();
        mats.add(bw);
  
        // Apply morphology operations
        Imgproc.erode(bw,bw,kernel);
        Imgproc.dilate(bw,bw,kernel);
        
        List<MatOfPoint> contours = new ArrayList<>();
        
        Mat hierarchy=new Mat();
        mats.add(hierarchy);
        
        Imgproc.findContours(bw, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
       
        for (int i = 0; i < contours.size(); i++) {
        	MatOfPoint2f poly = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), poly, 1, true);
            
            MatOfPoint matOfPoints = new MatOfPoint(poly.toArray());
            
            Rect candidateRect = Imgproc.boundingRect(matOfPoints);
            
            Line2D candidateLine=new Line2D.Double(
    				candidateRect.x,
    				candidateRect.y- dilate,
    				candidateRect.x,
    				candidateRect.y+candidateRect.height+ dilate);
           
            poly.release();
            matOfPoints.release();
            
            lines.add(candidateLine);
            
            contours.get(i).release();
        }
        
        mats.forEach(m->m.release());
        
        return lines;
    }
    
    private  void showWaitDestroy(String winname, Mat src) {
    	
    	Mat scaled = new Mat();

         //Scaling the Image
         
        double scale=0.2;
         
        Imgproc.resize(src, scaled, new Size(src.cols()*scale, src.rows()*scale), 0, 0, Image.SCALE_DEFAULT);
    	
        HighGui.imshow(winname, scaled );
        HighGui.moveWindow(winname, 500, 0);
        HighGui.waitKey(0);
        HighGui.destroyWindow(winname);
    }

	
}
