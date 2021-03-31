import java.util.ArrayList;
import java.util.List;

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

class Morphology_3Run {
    public void run(String[] args) {
        // Check number of arguments
        
        // Load the image
        Mat src = Imgcodecs.imread("/media/tor003/6887-88BA/ABB/forskninguttrekkABB050321full-psm1/image-600.jpg");
        // Check if image is loaded fine
        /*
        if( src.empty() ) {
            System.out.println("Error opening image: " + args[0]);
            System.exit(-1);
        }
        */
        // Show source image
        //HighGui.imshow("src", src);
        // Transform source image to gray if it is not already
        Mat gray = new Mat();
        if (src.channels() == 3)
        {
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        }
        else
        {
            gray = src;
        }
        // Show gray image
        //showWaitDestroy("gray" , gray);
        // Apply adaptiveThreshold at the bitwise_not of gray
        Mat bw = new Mat();
        Core.bitwise_not(gray, gray);
        Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);
        // Show binary image
        showWaitDestroy("binary" , bw);
        // Create the images that will use to extract the horizontal and vertical lines
        Mat horizontal = bw.clone();
        
        // Specify size on horizontal axis
        int horizontal_size = horizontal.cols() / 30;
        // Create structure element for extracting horizontal lines through morphology operations
        Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontal_size,7));
        // Apply morphology operations
        Imgproc.erode(horizontal, horizontal, horizontalStructure);
        Imgproc.dilate(horizontal, horizontal, horizontalStructure);
        
        showWaitDestroy("horizontal",horizontal);
        
        List<MatOfPoint> contours = new ArrayList<>();
        
        Imgproc.findContours(horizontal, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        Mat copy = new Mat(src.size(),src.type(),new Scalar(255,255,255));
       
        
        src.copyTo(copy);
        
        MatOfPoint2f[] contoursPoly  = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];
        // Point[] centers = new Point[contours.size()];
        //float[][] radius = new float[contours.size()][1];
        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 1, true);
            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
            //centers[i] = new Point();
            //Imgproc.minEnclosingCircle(contoursPoly[i], centers[i], radius[i]);
        }
        
        double padding=1;
        
        for (int i = 0; i < contours.size(); i++) {
        	MatOfPoint cont = contours.get(i);
        	
        	System.out.println("drawing "+cont.width()+" "+cont.height()+" "+cont.cols()+" "+cont.rows());
        	
            Imgproc.rectangle(
            		copy,
            		new Point(boundRect[i].tl().x-padding,boundRect[i].tl().y-padding),
            		new Point(boundRect[i].br().x+padding,boundRect[i].br().y+padding),
            		new Scalar(255,255,255),-1);
        }
        
        showWaitDestroy("finished",copy);
        
        
        System.exit(0);
    }
    private void showWaitDestroy(String winname, Mat img) {
        HighGui.imshow(winname, img);
        HighGui.moveWindow(winname, 500, 0);
        HighGui.waitKey(0);
        HighGui.destroyWindow(winname);
    }
}
public class Morphology_3 {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new Morphology_3Run().run(args);
    }
}