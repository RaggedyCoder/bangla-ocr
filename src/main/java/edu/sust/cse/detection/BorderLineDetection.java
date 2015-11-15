package edu.sust.cse.detection;

import edu.sust.cse.item.BorderItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by sajid on 11/16/2015.
 */
public class BorderLineDetection implements Detector {
    static int dx[] = {0, -1, -1, -1, 0, 1, 1, 1};
    static int dy[] = {-1, -1, 0, 1, 1, 1, 0, -1};

    int d[][];
    int width, height;

    ArrayList<BorderItem> borderItems = new ArrayList<>();

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    long countAt;
    int minX, maxX, minY, maxY;
    int CV_MOP_CLOSE = 4;

    public BorderDetection() { }

    public ArrayList<BorderItem> getBorder(Mat imageOri,int width,int height) {
        Mat image = new Mat();
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 2));
        Imgproc.morphologyEx(imageOri, image, CV_MOP_CLOSE, element);

        //NewsAnalysis.imshow("Morph", image);

        int result = 0;
        this.width = width;
        this.height = height;
        this.d = new int[height][width];
        //ReadFile();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                minX = 1000000;
                maxX = -1;
                minY = 1000000;
                maxY = -1;
                if (d[i][j] == 0 && image.get(i, j)[0]==255) {
                    result++;
                    countAt = 1;
                    BFS(image,i, j);

                    if (countAt > 1500) {
                        //canMaxiMizeBorder(minX, maxX, minY, maxY,width, height);
                        BorderItem item = new BorderItem(countAt, minX, maxX, minY, maxY, maxX-minX+1, maxY-minY+1);
                        borderItems.add(item);


                    }
                }
            }
        }

        int i = 0;

        return borderItems;

    }

    public ArrayList<BorderItem> getBorder(int[][] image,int width,int height) {
        int result = 0;
        this.width = width;
        this.height = height;
        this.d = image;
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
                    BFS(image,i, j);

                    if (countAt > 1500) {
                        canMaxiMizeBorder(minX, maxX, minY, maxY, width, height);


                    }
                }
            }
        }

        int i = 0;

        return borderItems;

    }

    public void BFS(int[][] image,int x, int y) {
        Queue<Integer> q = new LinkedList<Integer>();

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

    public void BFS(Mat image,int x, int y) {
        Queue<Integer> q = new LinkedList<Integer>();

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
                if (IsIn(x1, y1) && d[x1][y1] == 0 && image.get(x1, y1)[0]==255) {
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
    public boolean IsIn(int x, int y) {

        return (x >= 0 && y >= 0 && x < height && y < width);
    }

    private void canMaxiMizeBorder(int minX, int maxX, int minY, int maxY, int width, int height) {
        int minXTemp; int maxXTemp; int minYTemp; int maxYTemp;

        if(minX >1 && minX < maxX-1) minXTemp = minX-2;
        else if(minX >0 && minX < maxX) minXTemp = minX-1;
        else minXTemp = minX;

        if(maxX < width -2 && maxX > minX-1 ) maxXTemp = maxX+2;
        else if(maxX < width-1  && maxX > minX ) maxXTemp = maxX+1;
        else maxXTemp = maxX;

        if(minY >1 && minY < maxY-1) minYTemp = minY-2;
        else if(minY >0 && minY < maxY) minYTemp = minY-1;
        else minYTemp = minY;

        if(maxY < height -2 && maxY > minY-1 ) maxYTemp = maxY+2;
        else if(maxY < height-1  && maxY > minY ) maxYTemp = maxY+1;
        else maxYTemp = maxY;

        BorderItem item = new BorderItem(countAt, minXTemp, maxXTemp, minYTemp, maxYTemp,
                maxXTemp-minXTemp+1, maxYTemp-minYTemp+1);
        borderItems.add(item);
    }
}
