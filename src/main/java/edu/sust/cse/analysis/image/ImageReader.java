package edu.sust.cse.analysis.image;

import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author fahad_000
 */
public class ImageReader {

    static {
        // Load the OpenCV DLL
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        // Load an image file and display it in a window.
        Mat m1 = Highgui.imread("C:\\Users\\Eaiman\\Downloads\\test2\\Thesis\\test5.jpg");
        imshow("Original", m1);

        // Do some image processing on the image and display in another window.
        Mat m2 = new Mat();
        Imgproc.bilateralFilter(m1, m2, -1, 50, 10);
        Imgproc.Canny(m2, m2, 10, 200);
        imshow("Edge Detected", m2);
       detectLetter(m1);
               
    }//main

    static int CV_THRESH_OTSU = 8;
    static int CV_THRESH_BINARY = 0;
    static int CV_MOP_CLOSE = 3;

    public static void detectLetter(Mat img) {
        ArrayList<Rect> boundRect = new ArrayList<>();
        Mat img_gray, img_sobel, img_threshold, element;
        img_gray = new Mat();
        img_sobel = new Mat();
        img_threshold = new Mat();
        element = new Mat();
        Imgproc.cvtColor(img, img_gray, Imgproc.COLOR_BGRA2GRAY);
        imshow("Rec img_gray", img_gray);
        Imgproc.Sobel(img_gray, img_sobel, CvType.CV_8U, 1, 0, 3, 1, 0, Imgproc.BORDER_DEFAULT);
        imshow("Rec img_sobel", img_sobel);
        Imgproc.threshold(img_sobel, img_threshold, 0, 255, CV_THRESH_OTSU + CV_THRESH_BINARY);
        imshow("Rec img_threshold", img_threshold);

        element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(16, 6));
        
        Imgproc.morphologyEx(img_threshold, img_threshold, CV_MOP_CLOSE, element);
        imshow("Rec img_threshold second", img_threshold);
        
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        //Imgproc.findContours(img_threshold, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(img_threshold, contours, new Mat(), 0, 1);

        for (int i = 0; i < contours.size(); i++) {
            System.out.println(Imgproc.contourArea(contours.get(i)));
//            if (Imgproc.contourArea(contours.get(i)) > 100) {
//                //Imgproc.approxPolyDP( contours.get(i), contours_poly[i], 3, true );
//                Rect rect = Imgproc.boundingRect(contours.get(i));
//                System.out.println(rect.height);
//                if (rect.width > rect.height) {
//                    //System.out.println(rect.x +","+rect.y+","+rect.height+","+rect.width);
//                    Core.rectangle(img, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
//                }
//                    
//                    
//            }
            if (Imgproc.contourArea(contours.get(i)) > 100) {
                MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
                MatOfPoint2f mMOP2f2 = new MatOfPoint2f();
                contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
                Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 3, true);
                mMOP2f2.convertTo(contours.get(i), CvType.CV_32S);
                Rect rect = Imgproc.boundingRect(contours.get(i));
                if (rect.width > rect.height) {
                    Core.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255));
                }
            }
        }
        imshow("Rec Detected", img);
    }

    /**
     * Shows given image in a window. Analogous to cv::imshow() of C++ API.
     *
     * @param title
     * @param img
     */
    public static void imshow(String title, Mat img) {

        // Convert image Mat to a jpeg
        MatOfByte imageBytes = new MatOfByte();
        Highgui.imencode(".jpg", img, imageBytes);

        try {
            // Put the jpeg bytes into a JFrame window and show.
            JFrame frame = new JFrame(title);
            frame.getContentPane().add(new JLabel(new ImageIcon(ImageIO.read(new ByteArrayInputStream(imageBytes.toArray())))));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
