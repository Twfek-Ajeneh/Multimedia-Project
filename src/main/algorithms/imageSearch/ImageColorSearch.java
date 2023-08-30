package main.algorithms.imageSearch;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class ImageColorSearch {

    public ArrayList<BufferedImage> start(ArrayList<BufferedImage> images , BufferedImage target , ArrayList<Color> colors , int x1 , int y1 , int x2 , int y2){
        ArrayList<Double> targetColor = getRatio(target , colors , x1 , y1 , x2 , y2);
        ArrayList<ArrayList<Double>> imagesColor = new ArrayList<>();
        for (int i = 0 ; i < images.size() ; i++) {
            ArrayList<Double> templist = getRatio(images.get(i) , new ArrayList<Color>() , -1 , -1 , -1 , -1);
            templist.add((double)(i));
            imagesColor.add(templist);
        }

        imagesColor.sort(
                new Comparator<ArrayList<Double>>() {
                    @Override
                    public int compare(ArrayList<Double> o1, ArrayList<Double> o2) {
                        double total = targetColor.get(0) + targetColor.get(1) + targetColor.get(2);
                        double redRatioTarget = targetColor.get(0) / total;
                        double greenRatioTarget = targetColor.get(1) / total;
                        double blueRatioTarget = targetColor.get(2) / total;

                        total = o1.get(0) + o1.get(1) + o1.get(2);
                        double redRatioO1 = o1.get(0) / total;
                        double greenRatioO1 = o1.get(1) / total;
                        double blueRatioO1 = o1.get(2) / total;

                        total = o2.get(0) + o2.get(1) + o2.get(2);
                        double redRatioO2 = o2.get(0) / total;
                        double greenRatioO2 = o2.get(1) / total;
                        double blueRatioO2 = o2.get(2) / total;

                        double redDifferenceO1 = Math.abs(redRatioTarget - redRatioO1);
                        double greenDifferenceO1 = Math.abs(greenRatioTarget - greenRatioO1);
                        double blueDifferenceO1 = Math.abs(blueRatioTarget - blueRatioO1);

                        double totalDifferenceO1 = redDifferenceO1 + blueDifferenceO1 + greenDifferenceO1;

                        double redDifferenceO2 = Math.abs(redRatioTarget - redRatioO2);
                        double greenDifferenceO2 = Math.abs(greenRatioTarget - greenRatioO2);
                        double blueDifferenceO2 = Math.abs(blueRatioTarget - blueRatioO2);

                        double totalDifferenceO2 = redDifferenceO2 + blueDifferenceO2 + greenDifferenceO2;

                        return Double.compare(totalDifferenceO1 , totalDifferenceO2);
                    }
                }
        );

        ArrayList<BufferedImage> result = new ArrayList<>();
        for (ArrayList<Double> item : imagesColor){
            result.add(images.get( (int) Math.round(item.get(3)) ));
        }

        return result;
    }


    public ArrayList<Double> getRatio(BufferedImage image , ArrayList<Color> colors , int x1 , int y1 , int x2 , int y2){
        ArrayList<Double> list = new ArrayList<>();
        double totalRed = 0 , totalBlue = 0 , totalGreen = 0;
        if(colors.isEmpty()){
            int w1 = 0 , w2 = image.getWidth() , h1 = 0 , h2 = image.getHeight();
            if(x1 != -1  &&  x2 != -1  &&  y1 != -1  &&  y2 != -1){
                w1 = Math.max(0 , x1);
                w2 = Math.min(image.getWidth() , x2);
                h1 = Math.max(0 , y1);
                h2 = Math.min(image.getWidth() , y2);
            }
            for (int i = w1 ; i < w2 ; i++){
                for (int j = h1; j < h2 ; j++){
                    int rgb = image.getRGB(i , j);
                    Color color = new Color(rgb , true);
                    totalRed+=color.getRed();
                    totalGreen+=color.getGreen();
                    totalBlue+=color.getBlue();
                }
            }
        } else {
            for (Color color : colors){
                totalRed+=color.getRed();
                totalGreen+=color.getGreen();
                totalBlue+=color.getBlue();
            }
        }
        list.add(totalRed);
        list.add(totalGreen);
        list.add(totalBlue);
        return list;
    }
}