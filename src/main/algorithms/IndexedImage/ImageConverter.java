package main.algorithms.IndexedImage;

import java.awt.*;
import java.awt.image.*;
import java.util.Arrays;

public class ImageConverter {

    public BufferedImage convertToIndexed(BufferedImage srcImage, int numColors) {
        // Create a color model with the desired number of colors
        IndexColorModel colorModel = createColorIndex(srcImage);

        // Create a compatible BufferedImage with the same dimensions as the source image
        BufferedImage indexedImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, colorModel);

        // Create a Graphics2D object and draw the source image onto the indexed image
        Graphics2D g2d = indexedImage.createGraphics();
        g2d.drawImage(srcImage, 0, 0, null);
        g2d.dispose();

        return indexedImage;
    }

    public IndexColorModel createColorIndex(BufferedImage image) {
        int numColors = 256; // Maximum number of colors in the index

        // Collect unique color values from the image
        int[] rgbValues = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        // Remove duplicates and ensure the color count does not exceed the limit
        int[] uniqueRGBValues = Arrays.stream(rgbValues).distinct().limit(numColors).toArray();

        // Create the color index model
        int colorCount = uniqueRGBValues.length;
        byte[] reds = new byte[colorCount];
        byte[] greens = new byte[colorCount];
        byte[] blues = new byte[colorCount];

        for (int i = 0; i < colorCount; i++) {
            int rgb = uniqueRGBValues[i];
            reds[i] = (byte) ((rgb >> 16) & 0xFF);
            greens[i] = (byte) ((rgb >> 8) & 0xFF);
            blues[i] = (byte) (rgb & 0xFF);
        }

        return new IndexColorModel(8, colorCount, reds, greens,blues);
    }
}
