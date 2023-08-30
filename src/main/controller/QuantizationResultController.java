package main.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.algorithms.IndexedImage.ImageConverter;
import main.algorithms.KMeans.KMeans;
import main.algorithms.MedianCut.MedianCut;
import main.algorithms.floydSteinberg.FloydSteinberg;
import main.algorithms.octree.Quantize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static main.algorithms.floydSteinberg.FloydSteinberg.applyDitheredPalette;

public class QuantizationResultController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Set<Color> palette = new HashSet<Color>();

    private int[] redHistogram = new int[256];
    private int[] greenHistogram = new int[256];
    private int[] blueHistogram = new int[256];

    @FXML
    private ListView colorPalette;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView originalImage;

    @FXML
    private AreaChart histogramChart;

    @FXML
    private Label newImageLabel;

    public void octree(String imagePath) {
        Image image = new Image(imagePath);
        Quantize quantization = new Quantize();
        Image newImage = quantization.start(image);
        imageView.setImage(newImage);
        originalImage.setImage(image);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(newImage, null);
        histogramAndColorPalette(bufferedImage);
        newImageLabel.setText("Octree Image : ");
    }

    public void floydSteinberg(String imagePath) {
        Image image = new Image(imagePath);
        BufferedImage original = SwingFXUtils.fromFXImage(image, null);
        FloydSteinberg floyed = new FloydSteinberg();
        BufferedImage newImage = applyDitheredPalette(original, floyed.PALETTE);
        Image result = SwingFXUtils.toFXImage(newImage, null);
        imageView.setImage(result);
        originalImage.setImage(image);
        histogramAndColorPalette(newImage);
        newImageLabel.setText("floyd Steinberg Image : ");

    }

    public void simple(String imagePath) {
        int MASK_0 = 0x00800000; // 0 bits per channel (except red)
        int MASK_1 = 0xff808080; // 1 bit per channel
        int MASK_2 = 0xffc0c0c0; // 2 bits per channel
        int MASK_3 = 0xffe0e0e0; // 3 bits per channel
        int MASK_4 = 0xfff0f0f0; // 4 bits per channel

        Image image = new Image(imagePath);
        BufferedImage original = SwingFXUtils.fromFXImage(image, null);
        int w = original.getWidth();
        int h = original.getHeight();
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        // Go through every pixel of the image
        for(int x=0; x< w; x++){
            for(int y=0; y< h; y++){
                // Apply mask to original value and save it in result image
                newImage.setRGB(x,y, original.getRGB(x, y) & MASK_2);
            }
        }
        Image result = SwingFXUtils.toFXImage(newImage, null);
        imageView.setImage(result);
        originalImage.setImage(image);
        histogramAndColorPalette(newImage);
        newImageLabel.setText("Simple Algorithm Image : ");
    }

    public void medianCut(String imagePath , int maxColor){
        Image image = new Image(imagePath);
        BufferedImage original = SwingFXUtils.fromFXImage(image, null);
        MedianCut medianCut = new MedianCut();
        BufferedImage newImage = medianCut.apply(original , maxColor);
        Image result = SwingFXUtils.toFXImage(newImage, null);
        imageView.setImage(result);
        originalImage.setImage(image);
        histogramAndColorPalette(newImage);
        newImageLabel.setText("Median Cut Algorithm Image : ");
    }

    public void kMeans(String imagePath , int maxColor){
        Image image = new Image(imagePath);
        BufferedImage original = SwingFXUtils.fromFXImage(image, null);
        KMeans kMeans = new KMeans(maxColor , 20);
        BufferedImage newImage = kMeans.quantize(original);
        Image result = SwingFXUtils.toFXImage(newImage, null);
        imageView.setImage(result);
        originalImage.setImage(image);
        histogramAndColorPalette(newImage);
        newImageLabel.setText("K-Means Algorithm Image : ");
    }

    public void goBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/main/resources/fxml/Main.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void save(ActionEvent event) throws IOException {
        Image image = imageView.getImage();
        if(image != null){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File ("C:\\Users\\ASUS\\Desktop\\University\\4-Th Year\\Chapter 2\\Multimedia\\multimedia-project\\src\\main\\resources\\images"));
            FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
            fileChooser.getExtensionFilters().add(imageFilter);
            File file = fileChooser.showSaveDialog(null);
            if(file != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                root = FXMLLoader.load(getClass().getResource("/main/resources/fxml/Main.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } else {
            System.out.println("There is no Image");
        }
    }

    public void IndexedImage() throws  IOException{
        Image image = imageView.getImage();
        BufferedImage original = SwingFXUtils.fromFXImage(image, null);

        ImageConverter converter = new ImageConverter();
        // you can change the number of color you want;
        BufferedImage indexedImage = converter.convertToIndexed(original , 255);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File ("C:\\Users\\ASUS\\Desktop\\University\\4-Th Year\\Chapter 2\\Multimedia\\multimedia-project\\src\\main\\resources\\images\\Indexed Images"));
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showSaveDialog(null);
        if(file != null) {
            ImageIO.write(indexedImage, "png", file);
        }
    }

    public void histogramAndColorPalette(BufferedImage newImage){
        for (int y = 0; y < newImage.getHeight(); y++) {
            for (int x = 0; x < newImage.getWidth(); x++) {
                //Retrieving contents of a pixel
                int pixel = newImage.getRGB(x, y);
                //Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                redHistogram[color.getRed()]++;
                greenHistogram[color.getGreen()]++;
                blueHistogram[color.getBlue()]++;
                palette.add(color);
            }
        }

        colorPalette.setCellFactory(list -> new ListCell<Rectangle>() {
            private final Pane pane = new Pane();

            @Override
            protected void updateItem(Rectangle item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    pane.getChildren().setAll(item);
                    setGraphic(pane);
                }
            }
        });
        for(Color color : palette){
            colorPalette.getItems().add(new Rectangle(270 , 30 , javafx.scene.paint.Color.rgb(color.getRed() , color.getGreen() , color.getBlue())));
        }
        XYChart.Series<Number, Number> redSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> greenSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> blueSeries = new XYChart.Series<>();

        for(int i = 0; i < 255 ; i++){
            redSeries.getData().add(new XYChart.Data<>(i, redHistogram[i]));
            greenSeries.getData().add(new XYChart.Data<>(i , greenHistogram[i]));
            blueSeries.getData().add(new XYChart.Data<>(i , blueHistogram[i]));

        }
        redSeries.setName("Red");
        greenSeries.setName("Green");
        blueSeries.setName("Blue");
        histogramChart.getData().add(redSeries);
        histogramChart.getData().add(blueSeries);
        histogramChart.getData().add(greenSeries);
    }

}
