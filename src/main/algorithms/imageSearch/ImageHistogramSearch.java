package main.algorithms.imageSearch;

import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ImageHistogramSearch{
    public ArrayList<Pair<Double , BufferedImage>> start(ArrayList<BufferedImage> images , BufferedImage target , ArrayList<Color> colors){
        ArrayList<Pair<Double , BufferedImage>> result = new ArrayList<>();

        int targetWidthStart = 0 , targetWidthEnd = target.getWidth();
        int targetHeightStart = 0 , targetHeightEnd = target.getHeight();

        System.out.println(targetWidthStart);
        System.out.println(targetWidthEnd);
        System.out.println(targetHeightEnd);
        System.out.println(targetHeightEnd);



        int targetWidth = targetWidthEnd - targetWidthStart;
        int targetHeight = targetHeightEnd - targetHeightStart;

        int[] targetRedHistogram = new int[256];
        int[] targetGreenHistogram = new int[256];
        int[] targetBlueHistogram = new int[256];

        if(colors.isEmpty()){
            for (int i = targetWidthStart ; i < targetWidthEnd ; i++){
                for (int j = targetHeightStart; j < targetHeightEnd ; j++){
                    int redValue = target.getRaster().getSample(i, j, 0);
                    int greenValue = target.getRaster().getSample(i, j, 1);
                    int blueValue = target.getRaster().getSample(i, j, 2);

                    targetRedHistogram[redValue]++;
                    targetGreenHistogram[greenValue]++;
                    targetBlueHistogram[blueValue]++;
                }
            }
        } else {
            for (Color color : colors){
                int redValue = color.getRed();
                int greenValue = color.getGreen();
                int blueValue = color.getBlue();

                targetRedHistogram[redValue]++;
                targetGreenHistogram[greenValue]++;
                targetBlueHistogram[blueValue]++;
            }
        }


        int numPixels1 = targetWidth * targetHeight;

        for (int i = 0; i < targetRedHistogram.length; i++) {
            targetRedHistogram[i] = (int) Math.round((double) targetRedHistogram[i] / numPixels1 * 100);
            targetGreenHistogram[i] = (int) Math.round((double) targetGreenHistogram[i] / numPixels1 * 100);
            targetBlueHistogram[i] = (int) Math.round((double) targetBlueHistogram[i] / numPixels1 * 100);
        }

        for (BufferedImage image : images){
            int[] imageRedHistogram = new int[256];
            int[] imageGreenHistogram = new int[256];
            int[] imageBlueHistogram = new int[256];

            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    int redValue = image.getRaster().getSample(i, j, 0);
                    int greenValue = image.getRaster().getSample(i, j, 1);
                    int blueValue = image.getRaster().getSample(i, j, 2);

                    imageRedHistogram[redValue]++;
                    imageGreenHistogram[greenValue]++;
                    imageBlueHistogram[blueValue]++;
                }
            }

            int numPixels2 = image.getWidth() * image.getHeight();

            for (int i = 0; i < imageRedHistogram.length; i++) {
                imageRedHistogram[i] = (int) Math.round((double) imageRedHistogram[i] / numPixels2 * 100);
                imageGreenHistogram[i] = (int) Math.round((double) imageGreenHistogram[i] / numPixels2 * 100);
                imageBlueHistogram[i] = (int) Math.round((double) imageBlueHistogram[i] / numPixels2 * 100);
            }

            double difference = 0;
            for (int i = 0; i < targetRedHistogram.length; i++) {
                double redDiff = Math.abs(targetRedHistogram[i] - imageRedHistogram[i]);
                double greenDiff = Math.abs(targetGreenHistogram[i] - imageGreenHistogram[i]);
                double blueDiff = Math.abs(targetBlueHistogram[i] - imageBlueHistogram[i]);
                difference += redDiff + greenDiff + blueDiff;
            }

            double similarity = 1 - (difference / (3 * targetWidth * targetHeight * 255));
            System.out.println(similarity);
            result.add(new Pair<>(similarity , image));
        }

        Collections.sort(result, new Comparator<Pair<Double, BufferedImage>>() {
            @Override
            public int compare(Pair<Double, BufferedImage> p1, Pair<Double, BufferedImage> p2) {
                return Double.compare(p2.getKey(), p1.getKey());
            }
        });

        return result;
    }
}
