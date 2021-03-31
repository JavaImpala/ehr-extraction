import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.SimpleBlobDetector;
import org.opencv.features2d.SimpleBlobDetector_Params;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class JavaCVDemo {
    public static void main(String[] args) throws Exception {
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String location="/media/tor003/6887-88BA/ABB/forskninguttrekkABB050321full-psm1";
       
        int page=600;
        
        File file=new File(location+"/image-"+page+".jpg");
       
        try {
			
			String path=file.getAbsolutePath();
			System.out.println(file.isFile());
			
			
			
			//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			
			
			
			SimpleBlobDetector_Params parameters=new SimpleBlobDetector_Params();
			
			parameters.set_filterByArea(true);
			parameters.set_maxArea(500);
			
			parameters.set_filterByCircularity(false);
			parameters.set_filterByConvexity(false);
			parameters.set_filterByColor(false);
			parameters.set_filterByInertia(false);
			parameters.set_minDistBetweenBlobs(1);
			parameters.set_maxThreshold(1000);
			
			
			SimpleBlobDetector blobDetector=SimpleBlobDetector.create(parameters);
			
			BufferedImage image = ImageIO.read(file);
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		    ImageIO.write(image, "jpg", byteArrayOutputStream);
		    byteArrayOutputStream.flush();
			
			Mat matImage=Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()),Imgcodecs.IMREAD_GRAYSCALE);
			
			Mat resizeimage = new Mat();
		        
	        Size sz = new Size(800,1200);
	        Imgproc.resize(matImage, resizeimage, sz );
			
			MatOfKeyPoint keyPoints=new MatOfKeyPoint();
			
			blobDetector.detect(resizeimage,keyPoints);
			
			//List<KeyPoint> list = keyPoints.toList();
	       // keyPoints.release();
	        
	       
	        
	        // draw keypoints on image
	        Mat outputImage = new Mat();
	        // Your image, keypoints, and output image
	        Features2d.drawKeypoints(resizeimage, keyPoints, outputImage,new Scalar(255,0,0),0);
	      
	        //resize
	        
	       
	        
	        HighGui.imshow("Image",outputImage);
	        HighGui.waitKey();
	        
	        
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
