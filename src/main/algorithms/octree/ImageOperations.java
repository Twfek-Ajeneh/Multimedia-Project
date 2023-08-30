package main.algorithms.octree;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageOperations {
    public int[][] convertTo2DUsingGetRGB(BufferedImage image) {
      int width = image.getWidth();
      int height = image.getHeight();
      int[][] result = new int[height][width];

      for (int row = 0; row < height; row++) {
         for (int col = 0; col < width; col++) {
            result[row][col] = image.getRGB(col, row);
         }
      }

      return result;
   }
    
    public BufferedImage writeImage(int r, int c, int[][] res) {
        BufferedImage newImage = new BufferedImage(r, c, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < r; x++) {
            for (int y = 0; y < c; y++) {
                newImage.setRGB(x, y, res[x][y]);
            }
        }

        return rotateImage(newImage);
    }

    public BufferedImage rotateImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage rotated = new BufferedImage(h, w, image.getType());
        Graphics2D g2d = rotated.createGraphics();
        g2d.rotate(Math.toRadians(90));
        g2d.drawImage(image, 0, -h, null);
        g2d.dispose();
        return flipHorizontal(rotated);
    }

    public static BufferedImage flipHorizontal(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        return applyTransform(tx, image);
    }

    private static BufferedImage applyTransform(AffineTransform tx, BufferedImage image) {
        BufferedImage flippedImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g = flippedImage.createGraphics();
        g.transform(tx);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return flippedImage;
    }


}
