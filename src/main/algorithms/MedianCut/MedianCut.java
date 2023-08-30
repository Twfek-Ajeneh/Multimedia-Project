package main.algorithms.MedianCut;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MedianCut{

    public static BufferedImage apply(BufferedImage image, int numColors) {
        List<Color> colorPalette = createColorPalette(image, numColors);
        return applyColorPalette(image, colorPalette);
    }

    private static List<Color> createColorPalette(BufferedImage image, int numColors) {
        List<Color> colorList = new ArrayList<>();
        int width = image.getWidth();
        int height = image.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                colorList.add(color);
            }
        }

        return medianCut(colorList, numColors);
    }

    private static List<Color> medianCut(List<Color> colorList, int numColors) {
        List<List<Color>> colorClusters = new ArrayList<>();
        colorClusters.add(colorList);

        while (colorClusters.size() < numColors) {
            List<Color> longestRangeCluster = findLongestRangeCluster(colorClusters);
            colorClusters.remove(longestRangeCluster);
            List<List<Color>> splitClusters = splitCluster(longestRangeCluster);
            colorClusters.addAll(splitClusters);
        }

        List<Color> colorPalette = new ArrayList<>();
        for (List<Color> cluster : colorClusters) {
            colorPalette.add(calculateAverageColor(cluster));
        }

        return colorPalette;
    }

    private static List<Color> findLongestRangeCluster(List<List<Color>> clusters) {
        List<Color> longestRangeCluster = null;
        int maxRange = -1;

        for (List<Color> cluster : clusters) {
            int[] range = getColorRange(cluster);
            int clusterRange = range[1] - range[0];

            if (clusterRange > maxRange) {
                maxRange = clusterRange;
                longestRangeCluster = cluster;
            }
        }

        return longestRangeCluster;
    }

    private static int[] getColorRange(List<Color> cluster) {
        int min = 255;
        int max = 0;

        for (Color color : cluster) {
            int value = color.getRed() + color.getGreen() + color.getBlue();

            if (value < min) {
                min = value;
            }

            if (value > max) {
                max = value;
            }
        }

        return new int[]{min, max};
    }

    private static List<List<Color>> splitCluster(List<Color> cluster) {
        Collections.sort(cluster, Comparator.comparingInt(color -> color.getRed() + color.getGreen() + color.getBlue()));

        int midIndex = cluster.size() / 2;
        List<Color> firstHalf = cluster.subList(0, midIndex);
        List<Color> secondHalf = cluster.subList(midIndex, cluster.size());

        List<List<Color>> splitClusters = new ArrayList<>();
        splitClusters.add(firstHalf);
        splitClusters.add(secondHalf);

        return splitClusters;
    }

    private static Color calculateAverageColor(List<Color> cluster) {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;

        for (Color color : cluster) {
            redSum += color.getRed();
            greenSum += color.getGreen();
            blueSum += color.getBlue();
        }

        int clusterSize = cluster.size();
        return new Color(redSum / clusterSize, greenSum / clusterSize, blueSum / clusterSize);
    }

    private static BufferedImage applyColorPalette(BufferedImage image, List<Color> colorPalette) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage quantizedImage = new BufferedImage(width, height, image.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color originalColor = new Color(image.getRGB(x, y));
                Color quantizedColor = findClosestColor(originalColor, colorPalette);
                quantizedImage.setRGB(x, y, quantizedColor.getRGB());
            }
        }

        return quantizedImage;
    }

    private static Color findClosestColor(Color originalColor, List<Color> colorPalette) {
        Color closestColor = null;
        double minDistance = Double.MAX_VALUE;

        for (Color color : colorPalette) {
            double distance = calculateColorDistance(originalColor, color);

            if (distance < minDistance) {
                minDistance = distance;
                closestColor = color;
            }
        }

        return closestColor;
    }

    private static double calculateColorDistance(Color color1, Color color2) {
        int redDiff = color1.getRed() - color2.getRed();
        int greenDiff = color1.getGreen() - color2.getGreen();
        int blueDiff = color1.getBlue() - color2.getBlue();

        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }
}
