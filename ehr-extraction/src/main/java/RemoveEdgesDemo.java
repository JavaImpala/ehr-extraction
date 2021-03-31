import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class RemoveEdgesDemo {
	
    private static final String WINDOW_NAME = "Threshold Demo";
    
    private Mat src;
    private Mat srcGray = new Mat();
    private Mat dst = new Mat();
    private JFrame frame;
    private JLabel imgLabel;
    
    public RemoveEdgesDemo(String[] args) {
        String imagePath = "/home/tor003/Downloads/table.png";
    	//String imagePath="/media/tor003/6887-88BA/ABB/forskninguttrekkABB050321full-psm1/image-600.jpg";
    	
        if (args.length > 0) {
            imagePath = args[0];
        }
        // Load an image
        src = Imgcodecs.imread(imagePath);
        if (src.empty()) {
            System.out.println("Empty image: " + imagePath);
            System.exit(0);
        }
        // Convert the image to Gray
        Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
        // Create and set up the window.
        frame = new JFrame(WINDOW_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set up the content pane.
        Image img = HighGui.toBufferedImage(srcGray);
        addComponentsToPane(frame.getContentPane(), img);
        // Use the content pane's default BorderLayout. No need for
        // setLayout(new BorderLayout());
        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    private void addComponentsToPane(Container pane, Image img) {
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        JPanel sliderPanel = new JPanel();
       
        // Create Trackbar to choose type of Threshold
        
        // Create Trackbar to choose Threshold value
        
      
        imgLabel = new JLabel(new ImageIcon(img));
        pane.add(imgLabel, BorderLayout.CENTER);
        
        update();
    }
    private void update() {
    	
    	Mat thresh=new Mat();
        Imgproc.threshold(srcGray,thresh,240,255,Imgproc.THRESH_OTSU);
        
        //Remove horizontal lines
        Mat horizontal_kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(500,2));
        
        Mat morph=new Mat();
        
        Imgproc.morphologyEx(thresh,morph, Imgproc.MORPH_OPEN,horizontal_kernel);
        
        List<MatOfPoint> contours = new ArrayList<>();
        
        
        
        Imgproc.findContours(morph, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        Mat copy = new Mat(srcGray.size(),srcGray.type(),new Scalar(255,255,255));
        srcGray.copyTo(copy);
        
        for (int i = 0; i < contours.size(); i++) {
        	MatOfPoint cont = contours.get(i);
        	
        	System.out.println("drawing "+cont.width()+" "+cont.height()+" "+cont.cols()+" "+cont.rows());
            Imgproc.drawContours(copy, contours,i, new Scalar(255,255,255),2);
            
        }
        
        Image img = HighGui.toBufferedImage(morph);
        imgLabel.setIcon(new ImageIcon(img));
        frame.repaint();
    }
    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RemoveEdgesDemo(args);
            }
        });
    }
}
