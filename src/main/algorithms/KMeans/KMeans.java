package main.algorithms.KMeans;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {

    private int numColors;
    private int maxIterations;
    private List<Color> centroids;

    public KMeans(int numColors, int maxIterations) {
        this.numColors = numColors;
        this.maxIterations = maxIterations;
        this.centroids = new ArrayList<>();
    }

    public BufferedImage quantize(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        List<Color> pixels = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixel = new Color(image.getRGB(x, y));
                pixels.add(pixel);
            }
        }
        initializeCentroids(pixels);
        for (int i = 0; i < maxIterations; i++) {
            List<List<Color>> clusters = new ArrayList<>();
            for (int j = 0; j < numColors; j++) {
                clusters.add(new ArrayList<>());
            }
            for (Color pixel : pixels) {
                int clusterIndex = getNearestCentroid(pixel);
                clusters.get(clusterIndex).add(pixel);
            }
            updateCentroids(clusters);
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixel = new Color(image.getRGB(x, y));
                int clusterIndex = getNearestCentroid(pixel);
                Color quantizedPixel = centroids.get(clusterIndex);
                result.setRGB(x, y, quantizedPixel.getRGB());
            }
        }
        return result;
    }

    private void initializeCentroids(List<Color> pixels) {
        Random random = new Random();
        for (int i = 0; i < numColors; i++) {
            int index = random.nextInt(pixels.size());
            Color centroid = pixels.get(index);
            centroids.add(centroid);
        }
    }

    private int getNearestCentroid(Color pixel) {
        int nearestIndex = 0;
        double nearestDistance = Double.MAX_VALUE;
        for (int i = 0; i < centroids.size(); i++) {
            Color centroid = centroids.get(i);
            double distance = getDistance(pixel, centroid);
            if (distance < nearestDistance) {
                nearestIndex = i;
                nearestDistance = distance;
            }
        }
        return nearestIndex;
    }

    private void updateCentroids(List<List<Color>> clusters) {
        for (int i = 0; i <numColors; i++) {
            List<Color> cluster = clusters.get(i);
            if (cluster.isEmpty()) {
                continue;
            }
            int redSum = 0;
            int greenSum = 0;
            int blueSum = 0;
            for (Color pixel : cluster) {
                redSum += pixel.getRed();
                greenSum += pixel.getGreen();
                blueSum += pixel.getBlue();
            }
            int redMean = redSum / cluster.size();
            int greenMean = greenSum / cluster.size();
            int blueMean = blueSum / cluster.size();
            Color newCentroid = new Color(redMean, greenMean, blueMean);
            centroids.set(i, newCentroid);
        }
    }

    private double getDistance(Color c1, Color c2) {
        int redDiff = c1.getRed() - c2.getRed();
        int greenDiff = c1.getGreen() - c2.getGreen();
        int blueDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }
}
