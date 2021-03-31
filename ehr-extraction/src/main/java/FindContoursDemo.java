import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;
import org.opencv.core.CvType;
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



class FindContours {
    private Mat srcGray = new Mat();
    private JFrame frame;
    private JLabel imgSrcLabel;
    private JLabel imgContoursLabel;
    private int threshold1 = 130;
    
    //morph
    private static final String[] MORPH_OP = { "Opening", "Closing", "Gradient", "Top Hat", "Black Hat" };
    private static final int[] MORPH_OP_TYPE = { Imgproc.MORPH_OPEN, Imgproc.MORPH_CLOSE,
            Imgproc.MORPH_GRADIENT, Imgproc.MORPH_TOPHAT, Imgproc.MORPH_BLACKHAT };
    private static final String[] ELEMENT_TYPE = { "Rectangle", "Cross", "Ellipse" };
    private static final int MAX_KERNEL_SIZE = 21;
    
    private int blur=3;
    private int kernelSize = 3;
    
    //threshold
    private static int MAX_VALUE = 255;
    private static int MAX_TYPE = 4;
    private static int MAX_BINARY_VALUE = 255;
    private int thresholdValue =150;
    private int thresholdType = 0;
    
    private double globalScale=0.8;
    
    //size
    private double contourAreaLimit=2000d;
    
    private Random rng = new Random(12345);
    
    public FindContours(String[] args) {
        String filename = args.length > 0 ? args[0] : "/home/tor003/Downloads/table.png";
        //String filename="/media/tor003/6887-88BA/ABB/forskninguttrekkABB050321full-psm1/image-600.jpg";
    	
    	Mat src = Imgcodecs.imread(filename,1);
    	
        if (src.empty()) {
            System.err.println("Cannot read image: " + filename);
            System.exit(0);
        }
        
        Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
        
      //Creating an empty matrix to store the result
        Mat scaled = new Mat();

        //Scaling the Image
        
        double scale=globalScale;
        
       
        
        Imgproc.resize(srcGray, scaled, new Size(src.cols()*scale, src.rows()*scale), 0, 0, Image.SCALE_DEFAULT);
        
        
        //Imgproc.blur(srcGray, srcGray, new Size(3, 3));
        // Create and set up the window.
        frame = new JFrame("Finding contours in your image demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set up the content pane.
        
        
        Image img = HighGui.toBufferedImage(src);
        
        Image scaledImg = img.getScaledInstance((int)(scaled.width()),(int)(scaled.height()),Image.SCALE_DEFAULT);
        
        addComponentsToPane(frame.getContentPane(),scaledImg);
        
        // Use the content pane's default BorderLayout. No need for
        // setLayout(new BorderLayout());
        // Display the window.
        frame.pack();
        frame.setVisible(true);
        update();
    }
    
    private void addComponentsToPane(Container pane, Image img) {
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
       
        /*
         * tresh
         */
        
        JSlider sliderThreshType = new JSlider(0,MAX_TYPE, thresholdType);
        sliderThreshType.setMajorTickSpacing(1);
        sliderThreshType.setMinorTickSpacing(1);
        sliderThreshType.setPaintTicks(true);
        sliderThreshType.setPaintLabels(true);
        
        sliderPanel.add(new JLabel("threshtype (0 Binary,1 BinaryInv,2 truncate,3 toZero,4 toZeroInv)"));
        sliderPanel.add(sliderThreshType);
        
        
        
        // Create Trackbar to choose Threshold value
        
        JTextField treshField=new JTextField(thresholdValue+"");
        
        
        treshField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int area=Integer.parseInt(treshField.getText());
				
				thresholdValue=area;
				update();
			}
            
        });
        sliderPanel.add(new JLabel("threshlimit"));
        sliderPanel.add(treshField);
        
        sliderThreshType.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                thresholdType = source.getValue();
                update();
            }
        });
       
        
        /*
         *  canny minThresh
         */
        
     
        
        JTextField cannyField=new JTextField(threshold1+"");
        
        
        cannyField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int area=Integer.parseInt(cannyField.getText());
				
				threshold1=area;
				update();
			}
            
        });
        
        sliderPanel.add(new JLabel("Canny min-threshold: "));
        sliderPanel.add(cannyField);
        
        JTextField cannyKernal=new JTextField(kernelSize+"");
        
        
        cannyKernal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int area=Integer.parseInt(cannyKernal.getText());
				
				kernelSize=area;
				update();
			}
            
        });
        
        sliderPanel.add(new JLabel("Canny kernel: "));
        sliderPanel.add( cannyKernal);
        
        /*
         * contourArea
         */
        
        JTextField areaField=new JTextField(contourAreaLimit+"");
        
       
        areaField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				double area=Integer.parseInt(areaField.getText());
				
				contourAreaLimit=area;
				update();
			}
            
        });
        
        sliderPanel.add(new JLabel("Cont size threshold: "));
        sliderPanel.add(areaField);
        
        /*
         * blur kernal
         */
        

        JTextField blurField=new JTextField(blur+"");
        
       
        blurField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int area=Integer.parseInt(blurField.getText());
				
				blur=area;
				update();
			}
            
        });
        
        sliderPanel.add(new JLabel("blur: "));
        sliderPanel.add(blurField);
       
        pane.add(sliderPanel, BorderLayout.PAGE_START);
        JPanel imgPanel = new JPanel();
        imgSrcLabel = new JLabel(new ImageIcon(img));
        imgPanel.add(imgSrcLabel);
        
        Mat blackImg = Mat.zeros(srcGray.size(), CvType.CV_8U);
        imgContoursLabel = new JLabel(new ImageIcon(HighGui.toBufferedImage(blackImg)));
        imgPanel.add(imgContoursLabel);
        pane.add(imgPanel, BorderLayout.CENTER);
    }
    private void update() {
        System.out.println("working1");
    	Mat thresholdOutput = Mat.zeros(srcGray.size(), CvType.CV_8UC3);
    	
    	//Threshold
    	Imgproc.threshold(
    			srcGray,
    			thresholdOutput, 
    			thresholdValue, 
    			MAX_BINARY_VALUE, 
    			thresholdType);
    	
    	//blur
    	
    	Mat blurOutput = Mat.zeros(srcGray.size(), CvType.CV_8UC3);
    	Imgproc.blur(thresholdOutput,blurOutput,new Size(blur,blur));
    	
    	//CANNY
    	Mat cannyOutput = Mat.zeros(srcGray.size(), CvType.CV_8UC3);
        
        Imgproc.Canny(
        		blurOutput, 
        		cannyOutput, 
        		Integer.min(255,threshold1*2),
        		threshold1,
        		kernelSize,
        		false);
       
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        
        Imgproc.findContours(
        		thresholdOutput, 
        		contours, 
        		hierarchy, 
        		Imgproc.RETR_CCOMP, 
        		Imgproc.CHAIN_APPROX_SIMPLE);
        
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
       
       
        List<MatOfPoint> contoursPolyList = new ArrayList<>(contoursPoly.length);
        for (MatOfPoint2f poly : contoursPoly) {
            contoursPolyList.add(new MatOfPoint(poly.toArray()));
        }
        
        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8U);
        Mat mask = Mat.zeros(cannyOutput.size(), CvType.CV_8U);
        
        for (int i = 0; i < contours.size(); i++) {
        	Scalar color = new Scalar(255,255,255);
           
            
            Rect rect=boundRect[i];
            
            double rectDimLimit=5;
            // && rect.width<(rect.height*rectDimLimit) && rect.height<(rect.width*rectDimLimit)
            
            double padding=0;
            
            if(rect.area()<this.contourAreaLimit) {
            	System.out.println(rect.area());
            	
            	Point p1 = new Point(boundRect[i].tl().x+padding,boundRect[i].tl().y+padding);
            	Point p2 = new Point(boundRect[i].tl().x-padding,boundRect[i].tl().y-padding);
            	
            	Imgproc.rectangle(mask, boundRect[i].tl(), boundRect[i].br(), color,-1);
            	//Imgproc.rectangle(mask,p1,p2, color,-1); 
            	
            	//Scalar contColor = new Scalar(255,0,0);
                //Imgproc.drawContours(drawing, contoursPolyList, i, contColor);
            }
            
            
            
            //Scalar contColor = new Scalar(255,0,0);
            //Imgproc.drawContours(drawing, contoursPolyList, i, contColor);
            
            //System.out.println(i+" "+contours.size());
            
            //Imgproc.circle(drawing, centers[i], (int) radius[i][0], color, 2);
        }
        
        System.out.println("working7");
        
        Mat croppedOriginal = new Mat(srcGray.size(),CvType.CV_8U,new Scalar(255,255,255));
        srcGray.copyTo(croppedOriginal,mask);
        
        double scale=globalScale;
        imgContoursLabel.setIcon(new ImageIcon(HighGui.toBufferedImage(croppedOriginal).getScaledInstance((int)(croppedOriginal.width()*scale),(int)(croppedOriginal.height()*scale),Image.SCALE_DEFAULT)));
        
        
        frame.repaint();
    }
}
public class FindContoursDemo {
    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FindContours(args);
            }
        });
    }
}

/*
for (int i = 0; i < contours.size(); i++) {
    Scalar color = new Scalar(255,255,255);
    
    double contourSize = Imgproc.contourArea(contours.get(i));
    
    
    
    if(contourSize<contourAreaLimit) {
    	 Imgproc.drawContours(
         		mask, 
         		contours,
         		i, 
         		color, 
         		1, 
         		Imgproc.LINE_8,
         		hierarchy,
         		0, 
         		new Point());
    	 
    	 
    }
    
   
}
*/

//Mat cropped = new Mat();
// srcGray.copyTo(cropped, mask );

//imgContoursLabel.setIcon(new ImageIcon(HighGui.toBufferedImage(mask)));
