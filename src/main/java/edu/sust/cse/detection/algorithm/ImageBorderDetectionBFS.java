package edu.sust.cse.detection.algorithm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.sust.cse.analysis.news.NewsAnalysis;
import edu.sust.cse.item.BorderItem;
import item.BorderItem;
import news_analysis.isimage.IsImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 *
 * @author fahad_000
 */
public class ImageBorderDetectionBFS {

    static int dx[] = {0, -1, -1, -1, 0, 1, 1, 1};
    static int dy[] = {-1, -1, 0, 1, 1, 1, 0, -1};
    
    static int d[][];
    static int width, height;
    static Mat m2,m1;

    //static BufferedImage buffImg;

    ArrayList<BorderItem> borderItems = new ArrayList<>();
    ArrayList<BorderItem> imageItems = new ArrayList<>();
    ArrayList<BorderItem> otherItems = new ArrayList<>();
    
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    
    public ImageBorderDetectionBFS(BufferedImage buffImg) {
        //this.buffImg = buffImg;

        //getBorder();
    }

    static long countAt;
    static int minX, maxX, minY, maxY;

    public ImageBorderDetectionBFS() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<BorderItem> getBorder(int[][] image,int width,int height, Mat m2, Mat m1) {
        int result = 0;
        ImageBorderDetectionBFS.width = width;
        ImageBorderDetectionBFS.height = height;
        ImageBorderDetectionBFS.d = image;
        ImageBorderDetectionBFS.m2 = m2.clone();
        ImageBorderDetectionBFS.m1 = m1.clone();
        //ReadFile();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                minX = 1000000;
                maxX = -1;
                minY = 1000000;
                maxY = -1;
                if (d[i][j] == 0 && image[i][j]==0) {
                    result++;
                    countAt = 1;
                    bfs(image,i, j);

                    if (countAt > 1500) {
                        canMaxiMizeBorder(minX, maxX, minY, maxY,width, height);
                        BorderItem item = new BorderItem(countAt, minX, maxX, minY, maxY, maxX-minX+1, maxY-minY+1);
                        categorize(item, m2);                       
                    }
                }
            }
        }

//        int i = 0;
//        System.out.println("SIze: "+otherItems.size());
        //removeImagesFromNonImageBlock();
        eraseImges();
        
        return borderItems;

    }

    static void bfs(int[][] image,int x, int y) {
        Queue<Integer> q = new LinkedList<>();

        d[x][y] = 1;
        

        q.add(x);
        q.add(y);
        int x1, y1;
        while (!q.isEmpty()) {
            x = q.poll();
            y = q.poll();
            for (int i = 0; i < 8; i++) {
                x1 = x + dx[i];
                y1 = y + dy[i];
                if (IsIn(x1, y1) && d[x1][y1] == 0 && image[x1][y1]==0) {
                    q.add(x1);
                    q.add(y1);

                    countAt++;
                    d[x1][y1] = 1;
                    
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
    }
    static boolean IsIn(int x, int y) {
        
        return (x >= 0 && y >= 0 && x < height && y < width);
    }

    private void canMaxiMizeBorder(int minX, int maxX, int minY, int maxY, int width, int height) {
        if(minX >1 && minX < maxX-1) ImageBorderDetectionBFS.minX = minX-2;
        else if(minX >0 && minX < maxX) ImageBorderDetectionBFS.minX = minX-1;
        
        if(maxX < width -1 && maxX > minX-1 ) ImageBorderDetectionBFS.maxX = maxX+2;
        else if(maxX < width  && maxX > minX ) ImageBorderDetectionBFS.maxX = maxX+1;
        
        
        if(minY >1 && minY < maxY-1) ImageBorderDetectionBFS.minY = minY-2;
        else if(minY >0 && minY < maxY) ImageBorderDetectionBFS.minY = minY-1;
        
        if(maxY < height -1 && maxY > minY-1 ) ImageBorderDetectionBFS.maxY = maxY+2;
        else if(maxY < height  && maxY > minY ) ImageBorderDetectionBFS.maxY = maxY+1;
        
    }

    private void categorize(BorderItem item, Mat m2) {
        
        
        Mat subMat2 = m2.submat(item.getMinX(), item.getMaxX(),
                    item.getMinY(), item.getMaxY());
        
        IsImage isImage = new IsImage();
        if (isImage.isImage(subMat2)) {
            Mat subMat = m1.submat(item.getMinX(), item.getMaxX(),
                    item.getMinY(), item.getMaxY());
            item.setIsImage(true);
            item.setBlock(subMat);
            imageItems.add(item);
            borderItems.add(item);
        }else{
            otherItems.add(item);
        }
    }

    private void removeImagesFromNonImageBlock() {
        if(!imageItems.isEmpty() && !otherItems.isEmpty()){
            for(int k=0; k<otherItems.size(); k++){
                BorderItem otherItem = otherItems.get(k);
                
//                Mat m = otherItem.getBlock();
//                int h=m.height(), w = m.width();
//                for(int l=0; l<h; l++){
//                    for(int j=0; j<w; j++){
//                        if(m.get(l, j) == null){
//                            System.out.println(""+k+": null");
//                        }
//                        double[] data = m.get(l, j);
//                        data[0] = 0.0;
//                        m.put(l, j, data);
//                        
//                    }
//                    
//                }
//                NewsAnalysis.imshow(""+1, m);
//                continue;
//                if(otherItem.getBlock() == null){
//                    System.out.println(""+i+": null");
//                }else{
//                    System.out.println(""+i+": not null");
//                }
                        
                int oMinX = otherItem.getMinX();
                int oMinY = otherItem.getMinY();
                int oMaxX = otherItem.getMaxX();
                int oMaxY = otherItem.getMaxY();
                for(BorderItem imageItem : imageItems){
                    if(imageItem.getMinX() >= oMinX && imageItem.getMinY() >= oMinY && imageItem.getMinX() < oMaxX && imageItem.getMinY() < oMaxY &&
                            imageItem.getMaxX() >= oMinX && imageItem.getMaxY() >= oMinY && imageItem.getMaxX() < oMaxX && imageItem.getMaxY() < oMaxY){                        
                        for(int i=imageItem.getMinY()-oMinY; i<imageItem.getMaxY()-oMinY; i++){
                            for(int j=imageItem.getMinX()-oMinX; j<imageItem.getMaxX()-oMinX; j++){
                                double[] data = otherItem.getBlock().get(i, j);
                                data[0] = 0.0;
                                otherItem.getBlock().put(i, j, data);
                                otherItem.setIsPortionImage(true);
                                otherItems.set(k, otherItem);
                            }
                        }
                    }else if(imageItem.getMinX() >= oMinX && imageItem.getMinY() >= oMinY && imageItem.getMinX() < oMaxX && imageItem.getMinY() < oMaxY){                        
                        int breakPointY = imageItem.getMaxY();
                        int breakPointX = imageItem.getMaxX();
                        if(breakPointY > oMaxY){
                            breakPointY = oMaxY;
                        }
                        if(breakPointX > oMaxX){
                            breakPointX = oMaxX;
                        }
                        for(int i=imageItem.getMinY()-oMinY; i<breakPointY-oMinY; i++){
                            for(int j=imageItem.getMinX()-oMinX; j<breakPointX-oMinX; j++){
                                double[] data = otherItem.getBlock().get(i, j);
                                data[0] = 0.0;
                                otherItem.getBlock().put(i, j, data);
                                otherItem.setIsPortionImage(true);
                                otherItems.set(k, otherItem);
                            }
                        }
                    }else if(imageItem.getMaxX() >= oMinX && imageItem.getMaxY() >= oMinY && imageItem.getMaxX() < oMaxX && imageItem.getMaxY() < oMaxY){
                        int breakPointY = imageItem.getMinY();
                        int breakPointX = imageItem.getMinX();
                        if(breakPointY < oMinY){
                            breakPointY = oMinY;
                        }
                        if(breakPointX < oMinX){
                            breakPointX = oMinX;
                        }
                        
                        for(int i=imageItem.getMaxY()-oMinY; i>=breakPointY-oMinY; i--){
                            for(int j=imageItem.getMaxX()-oMinX; j>=breakPointX-oMinX; j--){
                                double[] data = otherItem.getBlock().get(i, j);
                                data[0] = 0.0;
                                otherItem.getBlock().put(i, j, data);
                                otherItem.setIsPortionImage(true);
                                otherItems.set(k, otherItem);
                            }
                        }
                    }else if(imageItem.getMaxX() >= oMinX && imageItem.getMinY() >= oMinY && imageItem.getMaxX() < oMaxX && imageItem.getMinY() < oMaxY){
                        int breakPointY = imageItem.getMaxY();
                        int breakPointX = imageItem.getMinX();
                        if(breakPointY > oMaxY){
                            breakPointY = oMaxY;
                        }
                        if(breakPointX < oMinX){
                            breakPointX = oMinX;
                        }
                        
                        for(int i=imageItem.getMinY()-oMinY; i<breakPointY-oMinY; i++){
                            for(int j=imageItem.getMaxX()-oMinX; j>=breakPointX-oMinX; j--){
                                double[] data = otherItem.getBlock().get(i, j);
                                data[0] = 0.0;
                                otherItem.getBlock().put(i, j, data);
                                otherItem.setIsPortionImage(true);
                                otherItems.set(k, otherItem);
                            }
                        }
                    }else if(imageItem.getMinX() >= oMinX && imageItem.getMaxY() >= oMinY && imageItem.getMinX() < oMaxX && imageItem.getMaxY() < oMaxY){
                        int breakPointY = imageItem.getMinY();
                        int breakPointX = imageItem.getMaxX();
                        if(breakPointY < oMinY){
                            breakPointY = oMinY;
                        }
                        if(breakPointX > oMaxX){
                            breakPointX = oMaxX;
                        }
                        
                        for(int i=imageItem.getMaxY()-oMinY; i>=breakPointY-oMinY; i--){
                            for(int j=imageItem.getMinX()-oMinX; j<breakPointX-oMinX; j++){
                                double[] data = otherItem.getBlock().get(i, j);
                                data[0] = 0.0;
                                otherItem.getBlock().put(i, j, data);
                                otherItem.setIsPortionImage(true);
                                otherItems.set(k, otherItem);
                            }
                        }
                    }
                }
                
                borderItems.add(otherItem);
            }
        }
    }

    private void eraseImges() {
        Mat mLocal = ImageBorderDetectionBFS.m1.clone();
        for(BorderItem imageItem : imageItems){
            int iMinX = imageItem.getMinX(), iMinY = imageItem.getMinY(), 
                iMaxX = imageItem.getMaxX(), iMaxY = imageItem.getMaxY();
            double[] data = null;
            for(int i=iMinX; i<iMaxX; i++){
                for(int j=iMinY; j<iMaxY; j++){
                    data = mLocal.get(i, j);
                    data[0] = 255.0;
                    data[1] = 255.0;
                    data[2] = 255.0;
                    mLocal.put(i, j, data);
                }
            }
        }
        
        NewsAnalysis.imshow("img_removed", mLocal);
//        return;
        
        for(int i=0; i<otherItems.size(); i++){
            BorderItem otherItem = otherItems.get(i);
            
            Mat subMat = mLocal.submat(otherItem.getMinX(), otherItem.getMaxX(),
                    otherItem.getMinY(), otherItem.getMaxY());
            otherItem.setBlock(subMat);
            
            otherItems.set(i, otherItem);
            borderItems.add(otherItem);
        }
    }    
}
