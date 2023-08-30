package main.algorithms.octree;

import java.awt.image.BufferedImage;

public interface ImageInterface {
    int[][] convertTo2DUsingGetRGB(BufferedImage image);
    void writeImage(int r, int c, int[][] res);
}
