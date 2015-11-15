package edu.sust.cse.analysis.news;


import com.recognition.software.jdeskew.pre.ImageUtil;
import edu.sust.cse.detection.algorithm.ImageBorderDetectionBFS;
import item.BorderItem;
import news_analysis.headlinedetection.HeadLineDetection;
import news_analysis.isimage.IsImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
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
public class NewsAnalysis {

    static File file;
    static FileWriter fw;
    static BufferedWriter bw;

    static {
        // Load the OpenCV DLL
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException {
//        file = new File("E:\\tannee.txt");
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        fw = new FileWriter(file.getAbsoluteFile());
//        bw = new BufferedWriter(fw);
//        bw.flush();
        // Load an image file and display it in a window.
//        Mat m1 = Highgui.imread("C:\\Users\\Eaiman\\Downloads\\test2\\Thesis\\test1.jpg");
//        Mat m1 = Highgui.imread("C:\\Users\\Eaiman\\Downloads\\test2\\Thesis\\test2.jpg");
//        Mat m1 = Highgui.imread("C:\\Users\\Eaiman\\Downloads\\test2\\Thesis\\test3.jpg");
//        Mat m1 = Highgui.imread("C:\\Users\\Eaiman\\Downloads\\test122\\test122.jpg");
//        Mat m1 = Highgui.imread("C:\\Users\\Eaiman\\Downloads\\test2\\Thesis\\test4.jpg");
        // String deskewedFilePath = RotateClass.performDeskew("C:\\Shoshi\\projects\\test2\\Thesis\\test2.jpg");
        Mat m1 = Highgui.imread("C:\\Shoshi\\projects\\test2\\Thesis\\test2.jpg");
        System.out.println("size: "+m1.rows());
        imshow("Original", m1);

        // Do some image processing on the image and display in another window.
        Mat m2 = new Mat();
        Imgproc.bilateralFilter(m1, m2, -1, 50, 10);
        Imgproc.Canny(m2, m2, 10, 200);
        //Imgproc.cvtColor(m1, m1, Imgproc.COLOR_RGB2GRAY, 0);
        
        imshow("Edge Detected", m2);
        Size sizeA = m2.size();
        System.out.println("width: " + sizeA.width + " Height: " + sizeA.height);
        int width = (int) sizeA.width;
        int hight = (int) sizeA.height;
        int pointLength[][][] = new int[hight][width][2];
        for (int i = 0; i < hight; i++) {
            for (int j = 0; j < width; j++) {
                //double[] data = m2.get(i, j);
                if (m2.get(i, j)[0] != 0) {
                    pointLength[i][j][0] = 0;
                    pointLength[i][j][1] = 0;
                    continue;
                }
                if (j != 0 && m2.get(i, j - 1)[0] == 0) {
                    pointLength[i][j][0] = pointLength[i][j - 1][0];
                } else {
                    int count = 0;
                    for (int k = j + 1; k < width; k++) {
                        if (m2.get(i, k)[0] == 0) {
                            count++;
                        } else {
                            break;
                        }
                    }
                    pointLength[i][j][0] = count;
                }
                if (i != 0 && m2.get(i - 1, j)[0] == 0) {
                    pointLength[i][j][1] = pointLength[i - 1][j][1];
                } else {
                    int count = 0;
                    for (int k = i + 1; k < hight; k++) {
                        if (m2.get(k, j)[0] == 0) {
                            count++;
                        } else {
                            break;
                        }
                    }
                    pointLength[i][j][1] = count;
                }

                //System.out.println(data[0]);
            }
        }
        String temp = "";
        Mat convertArea = m2.clone();

        int[][] balckWhite = new int[hight][width];

        for (int i = 0; i < hight; i++) {
            temp = "";
            for (int j = 0; j < width; j++) {
                if (i == 0 || j == 0 || i == hight - 1 || j == width - 1) {
                    temp = temp + "@";
                    balckWhite[i][j] = 1;

                    double[] data = m2.get(i, j);
                    data[0] = 255.0;
                    convertArea.put(i, j, data);
                } else if (pointLength[i][j][0] > 150 && pointLength[i][j][1] > 6) {
                    temp = temp + "@";
                    balckWhite[i][j] = 1;

                    double[] data = m2.get(i, j);
                    data[0] = 255.0;
                    convertArea.put(i, j, data);
                } else if (pointLength[i][j][0] > 7 && pointLength[i][j][1] > 200) {
                    temp = temp + "@";
                    balckWhite[i][j] = 1;

                    double[] data = m2.get(i, j);
                    data[0] = 255.0;
                    convertArea.put(i, j, data);
                } else {
                    temp = temp + " ";
                    balckWhite[i][j] = 0;

                    double[] data = m2.get(i, j);
                    data[0] = 0.0;
                    convertArea.put(i, j, data);
                }

            }
            //System.out.println(temp+"\n");
            //filewrile(temp);
        }
        imshow("Convertion", convertArea);
        IsImage isImage = new IsImage();
        HeadLineDetection isHeadline = new HeadLineDetection();

        ImageBorderDetectionBFS imgBFS = new ImageBorderDetectionBFS();
        ArrayList<BorderItem> borderItems = imgBFS.getBorder(balckWhite, width, hight, m2, m1);
       // Mat[] subMat = new Mat[borderItems.size()];
//        for (int i = 0; i < borderItems.size(); i++) {
//            subMat[i] = m2.submat(borderItems.get(i).getMinX(), borderItems.get(i).getMaxX(),
//                    borderItems.get(i).getMinY(), borderItems.get(i).getMaxY());
//            if (isImage.isImage(subMat[i])) {
//                System.out.println("subMat" + i + " is an image");
//                imshow("Image" + i, subMat[i]);
//                
//            }else if(isHeadline.isHeadLine(subMat[i])){
//                System.out.println("subMat" + i + " is an Headline");
//                imshow("Headline" + i, subMat[i]);
//            }else{
//                System.out.println("subMat" + i + " is an Column");
//                imshow("Column" + i, subMat[i]);
//            }
//            //imshow("subMat" + i, subMat[i]);
//            bw.close();
//
//        }
        
        boolean[] imageIndexer = new boolean[borderItems.size()];
        int[] lineHeight = new int[borderItems.size()];
        int highestLinheight = -1, lowestLineHeight = 10000;
        int totalHeight = 0, notImage = 0;
        
        for (int i = 0; i < borderItems.size(); i++) {
            lineHeight[i] = 0;
            BorderItem borderItem = borderItems.get(i);
//            subMat[i] = m2.submat(borderItems.get(i).getMinX(), borderItems.get(i).getMaxX(),
//                    borderItems.get(i).getMinY(), borderItems.get(i).getMaxY());
//            if (isImage.isImage(subMat[i])) {
//                System.out.println("subMat" + i + " is an image");
//                imshow("Image" + i, subMat[i]);
//                imageIndexer[i] = true;
//                continue;
//            }else{
//                notImage++;
//                imageIndexer[i] = false;
//            }
            if (borderItem.getIsImage()) {
                System.out.println("subMat" + i + " is an image");
                imshow("Image" + i, borderItem.getBlock());
                imageIndexer[i] = true;
                continue;
            }else{
                notImage++;
                imageIndexer[i] = false;
            }
            
//            totalHeight += lineHeight[i] = getLineHeight(subMat[i]);
            Mat fake = new Mat();
            Imgproc.cvtColor(borderItem.getBlock(), fake, Imgproc.COLOR_RGB2GRAY, 0);
            totalHeight += lineHeight[i] = getLineHeight(fake);
            fake.release();
            System.out.println("line height "+i+": "+lineHeight[i]);
//            imshow("" + i, borderItems.get(i).getBlock());
            if(lineHeight[i] > highestLinheight) highestLinheight = lineHeight[i];
            if(lineHeight[i] < lowestLineHeight) lowestLineHeight = lineHeight[i];
            
//            if(i==7)
//                break;
            

        }
        
        int avgLineHeight = totalHeight / notImage;
        
        for (int i = 0; i < borderItems.size(); i++) {
            if(!imageIndexer[i]){
                if(lineHeight[i] - lowestLineHeight > 13 && lineHeight[i] >= 45){
//                    imshow("Headline" + i, subMat[i]);
                    imshow("Headline" + i, borderItems.get(i).getBlock());
                }else if(lineHeight[i] - lowestLineHeight > 8 && lineHeight[i] >= 21  && lineHeight[i] < 45){
                    imshow("Sub Headline" + i, borderItems.get(i).getBlock());
                }else{
//                    imshow("Column" + i, subMat[i]);
                    imshow("Column" + i, borderItems.get(i).getBlock());
                }
            }
        }

    }

    public static void filewrile(String content) {
        try {
            bw.write(content);
            bw.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private static int getLineHeight(Mat subMat) {
        int lineHeight = 0;
        float width = subMat.width();
        float height = subMat.height();
        
        if(height < 5 || width < 5){
            return lineHeight;
        }
        
        int start = -1, end = -1, biggest = -1;
//        String blacks= "";
        
        for(int i=0; i<height; i++){
            int white = 0;
            for(int j=0; j<width; j++){
                
                if(subMat.get(i, j)[0] <= 140){
                    white++;
                    if(start == -1){
                        start = i;
                    }
//                    blacks +="1";
//                    break;
                }else{
//                    blacks +="0";
                }
            }
//            blacks += "\n";
            
            
//            if(white==0){
//                for(int j=0; j<width; j++){
//                    double[] data = subMat.get(i, j);
//                    if(data != null){
//                        data[0] = 0.0;
//                        subMat.put(i, j, data);
//                    }
//                }
//            }
            
//            if(biggest < white){
//                biggest = white;
//            }
//            System.out.println(blacks);
            if(white == 0 && start != -1){
                if((i-1-start) < 5){
                    lineHeight= i-1-start;
                    start = -1;
                    continue;
                }
                
                if(end == -1)
                    end = i-1;
                lineHeight= end-start;
                    break;
            }
            
            if(i == height-1 && end == -1){
                end = i;
                lineHeight= end-start;
            }
        }
//        System.out.println("start: "+start);
//            System.out.println("end: "+end);
//        if(lineHeight == 50){
//        filewrile(blacks);
//        filewrile("\n\n\n\n\n\n\n\n");
//        }
        return lineHeight;
        
        // Read image as before
//        Mat rgba = subMat.clone();
////        Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_RGB2GRAY, 0);
//
//        // Create an empty image in matching format
//        BufferedImage gray = new BufferedImage(rgba.width(), rgba.height(), BufferedImage.TYPE_BYTE_GRAY);
//
//        // Get the BufferedImage's backing array and copy the pixels directly into it
//        byte[] data = ((DataBufferByte) gray.getRaster().getDataBuffer()).getData();
//        rgba.get(0, 0, data);
//        
    //  return largestBlackBatch1(gray)[1];
    }
    
    public static int[] largestBlackBatch1(BufferedImage cImage){
        int hMin = 0;//(int) ((this.cImage.getHeight()) / 4.0);
	int hMax = cImage.getHeight(); //(int) ((this.cImage.getHeight()) * 3.0 / 4.0);
//        List<Integer> data = new ArrayList<>();
        String blacks= "";
        System.out.println("height: "+hMax);
        int lineIndex = 0, startIndex = 0, endIndex = 0, preVal = 0;
//        boolean isAnyWhitespace = false;       
        
        boolean start = true, end = false, diter = false;
        
	for (int y = hMin; y < hMax; y++) {
            String black= "";
            int blc = 0, wht = 0;
            diter = false;
	    for (int x = 1; x < (cImage.getWidth()); x++) {
		if (ImageUtil.isBlack(cImage, x, y,200)) {
                    black+="1";
                    blc++;
                    
                    if(start){
                        startIndex = y;
                    }
                    
                    start = false;
                    diter = true;
		}else{
                    if(!start){
//                        black+="0";
                    }
                }
	    }
            
            if(!start && !diter){
                if(y-startIndex < 10){
                    start = true;
                    diter = false;
                    continue;
                }
                endIndex = y-1;
                end = true;
            }
//            blacks += black+"\n";
//            System.out.println("balck: "+blc);
            
//            if(!data.isEmpty()){
                if(blc > preVal){
                    preVal = blc;
                    lineIndex =y;
                }
//            }
//            data.add(blc);
//            
//            totalBlack += blc;
            
            
            if(!start && end){
                System.out.println(blacks);
//                System.out.println("\n\n");
//                System.out.println("start Index: "+startIndex);
//                System.out.println("line index: "+lineIndex);
//                System.out.println("preVal: "+preVal);
//                System.out.println("end index: "+endIndex);
//                System.out.println("isAnyWhitespace: "+isAnyWhitespace);
//                System.out.println("\n\n");
//                System.out.println("\n\n");
//                System.out.println("\n\n");
//                System.out.println("\n\n");
                break;
            }
//            System.out.println("white: "+wht);
	}
//        System.out.println(blacks);
        if(endIndex == 0){
            endIndex = hMax;
        }
        int [] resilt = {0, endIndex-startIndex, lineIndex-startIndex, preVal};
        return resilt;
    }
}
