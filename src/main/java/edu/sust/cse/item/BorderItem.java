package edu.sust.cse.item;

/**
 * Created by sajid on 11/16/2015.
 */
public class BorderItem {
    long countAt;
    int minX, maxX, minY, maxY, width, height;
    boolean isImage = false, isPortionImage = false;
    Mat block;

    public BorderItem() {
    }

    public BorderItem(long countAt, int minX, int maxX, int minY, int maxY, int width, int height) {
        this.countAt = countAt;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getCountAt() {
        return countAt;
    }

    public void setCountAt(long countAt) {
        this.countAt = countAt;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }

    public boolean getIsPortionImage() {
        return isPortionImage;
    }

    public void setIsPortionImage(boolean isPortionImage) {
        this.isPortionImage = isPortionImage;
    }

    public Mat getBlock() {
        return block;
    }

    public void setBlock(Mat block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "BorderItem{" + "countAt=" + countAt + ", minX=" + minX + ", maxX=" + maxX + ", minY=" + minY + ", maxY=" + maxY + '}';
    }
}
