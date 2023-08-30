package main.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class SearchController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ImageView imageView;

    @FXML
    private ListView directoryList;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private CheckBox cut;

    @FXML
    private Label imageWidth;

    @FXML
    private Label imageHeight;

    @FXML
    private TextField newWidth;

    @FXML
    private TextField newHeight;

    @FXML
    private Rectangle cropRect;

    @FXML
    private double startX, startY;

    private String imagePath = "";
    private ArrayList<String> directories = new ArrayList<>();
    private ArrayList<Color> colors = new ArrayList<>();
    private Date date = new Date(1000000000);
    private int width = -1;
    private int height = -1;
//    private int x1 = -1 , y1 = -1 , x2 = -1 , y2 = -1;
    private int nWidth = -1 , nHeight = -1;

    public void initialize() {
        widthField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                String value = newValue.replaceAll("[^\\d]", "");
                widthField.setText(value);
                if(value.compareTo("")!=0)
                    width = Integer.parseInt(value);
            }
        });

        heightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                String value = newValue.replaceAll("[^\\d]", "");
                heightField.setText(value);
                if(value.compareTo("")!=0)
                    height = Integer.parseInt(value);
            }
        });

        newWidth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                String value = newValue.replaceAll("[^\\d]", "");
                newWidth.setText(value);
                if(value.compareTo("")!=0)
                    nWidth = Integer.parseInt(value);
            }
        });

        newHeight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                String value = newValue.replaceAll("[^\\d]", "");
                newHeight.setText(value);
                if(value.compareTo("")!=0)
                    nHeight = Integer.parseInt(value);
            }
        });

    }

    public void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\ASUS\\Desktop\\University\\4-Th Year\\Chapter 2\\Multimedia\\multimedia-project\\src\\main\\resources\\images"));
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(null);

        if (file != null){
            imagePath = file.getPath();
            Image image = new Image(imagePath);
            imageView.setImage(image);
            imageWidth.setText("Image Width : " + image.getWidth());
            imageHeight.setText("Image Height : " + image.getHeight());
        } else {
            System.out.println("NO File");
        }
    }

    public void selectDirectory(ActionEvent event){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("C:\\Users\\ASUS\\Desktop\\University\\4-Th Year\\Chapter 2\\Multimedia\\multimedia-project\\src\\main\\resources\\images"));
        File directory = directoryChooser.showDialog(null);
        if(directory != null){
            directoryList.getItems().add(directory.getAbsolutePath());
            directories.add(directory.getAbsolutePath());
        } else {
            System.out.println("NO Directory");
        }
    }

    public void selectColors(ActionEvent event){
        if(colorPicker.getValue() != null){
            colors.add(
                    new Color(
                            (float)colorPicker.getValue().getRed(),
                            (float)colorPicker.getValue().getGreen(),
                            (float)colorPicker.getValue().getBlue()
                    )
            );
        } else {
            System.out.println("Select Color");
        }
    }

    public void selectDate(ActionEvent event){
        if(datePicker.getValue() != null){
            ZoneId defaultZoneId = ZoneId.systemDefault();
            LocalDate localDate = datePicker.getValue();
            date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        } else {
            System.out.println("Select Date");
        }
    }

    public void isCut(ActionEvent event){
        if(cut.isSelected()){
            cropRect.setOpacity(1);
            // Add event handlers to the crop rectangle
            cropRect.setOnMousePressed(e -> {
                startX = e.getSceneX();
                startY = e.getSceneY();
                e.consume();
            });
            cropRect.setOnMouseDragged(e -> {
                double deltaX = e.getSceneX() - startX;
                double deltaY = e.getSceneY() - startY;
                cropRect.setX(cropRect.getX() + deltaX);
                cropRect.setY(cropRect.getY() + deltaY);
                startX = e.getSceneX();
                startY = e.getSceneY();
                e.consume();
            });
            cropRect.setOnMouseReleased(e -> {
                e.consume();
            });

            imageView.setOnMousePressed(e -> {
                double x = e.getX();
                double y = e.getY();
                cropRect.setX(x);
                cropRect.setY(y);
                cropRect.setWidth(0);
                cropRect.setHeight(0);
                e.consume();
            });
            imageView.setOnMouseDragged(e -> {
                double x = e.getX();
                double y = e.getY();
                cropRect.setWidth(Math.abs(x - cropRect.getX()));
                cropRect.setHeight(Math.abs(y - cropRect.getY()));
                e.consume();
            });
            imageView.setOnMouseReleased(e -> {
                double x = cropRect.getX();
                double y = cropRect.getY();
                double width = cropRect.getWidth();
                double height = cropRect.getHeight();

                // Crop the image using the selected rectangle
                Image originalImage = imageView.getImage();

//                x1 = (int) x;
//                y1 = (int) y;
//                x2 = x1 + (int) width;
//                y2 = y2 + (int) height;
//                System.out.println(x1);
//                System.out.println(y1);
//                System.out.println(x2);
//                System.out.println(y2);
//                System.out.println("--------------------");

                WritableImage croppedImage = new WritableImage(originalImage.getPixelReader(), (int)x, (int)y, (int)width, (int)height);

                // Display the cropped image
                imageView.setImage(croppedImage);

                // Reset the crop rectangle
                cropRect.setWidth(0);
                cropRect.setHeight(0);
                e.consume();
            });
        } else {
            cropRect.setOpacity(0);
        }
    }

    public void changeDimensions(ActionEvent event){
        Image image = imageView.getImage();
        if(nWidth > 0  &&  nHeight > 0  &&  image != null){
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            BufferedImage newBufferedImage = new BufferedImage(nWidth, nHeight, bufferedImage.getType());
            Graphics2D graphics2D = newBufferedImage.createGraphics();
            graphics2D.drawImage(bufferedImage, 0, 0, nWidth, nHeight, null);
            graphics2D.dispose();
            Image result = SwingFXUtils.toFXImage(newBufferedImage, null);
            imageView.setImage(result);
            imageWidth.setText("Image Width : " + result.getWidth());
            imageHeight.setText("Image Height : " + result.getHeight());

        }
    }

    public void submit(ActionEvent event) throws IOException {
        if(imageView.getImage() != null  &&  !directories.isEmpty()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/SearchResult.fxml"));
            root = loader.load();
            SearchResultController searchResultController = loader.getController();

            searchResultController.SearchResult(imageView.getImage() , directories , colors , date , width , height);

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Please Select Image First And Select Directory");
        }
    }
}
