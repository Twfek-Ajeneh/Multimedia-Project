package main.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuantizationController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ImageView imageView;

    @FXML
    private ChoiceBox<String> algorithm;

    @FXML
    private TextField maxColorField;

    @FXML
    private Label maxColorLabel;

    private String[] algorithms = {"Octree" , "Floyd Steinberg" , "Simple Algorithm" , "Median Cut" , "K-Means"};

    private String imagePath = "";
    private int maxColor = 10;

    public void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File ("C:\\Users\\ASUS\\Desktop\\University\\4-Th Year\\Chapter 2\\Multimedia\\multimedia-project\\src\\main\\resources\\images"));
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(null);

        if (file != null){
            imagePath = file.getPath();
            Image image = new Image(imagePath);
            imageView.setImage(image);
        } else {
            System.out.println("NO File");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        algorithm.setValue("Octree");
        algorithm.getItems().addAll(algorithms);
        maxColorField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                String value = newValue.replaceAll("[^\\d]", "");
                maxColorField.setText(value);
                if(value.compareTo("")!=0)
                    maxColor = Integer.parseInt(value);
            }
        });
        maxColorField.setDisable(true);
        maxColorLabel.setDisable(true);
    }

    public void isChanged(ActionEvent event) throws IOException {
        if(algorithm.getValue().equals("Median Cut") || algorithm.getValue().equals("K-Means")){
            maxColorField.setDisable(false);
            maxColorLabel.setDisable(false);
        } else {
            maxColorField.setText("");
            maxColorField.setDisable(true);
            maxColorLabel.setDisable(true);
        }
    }

    public void submit(ActionEvent event) throws IOException {
        if(imageView.getImage() != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/QuantizationResult.fxml"));
            root = loader.load();
            QuantizationResultController quantizationResultController = loader.getController();

            if(algorithm.getValue().equals("Octree")){
                quantizationResultController.octree(imagePath);
            } else if(algorithm.getValue().equals("Floyd Steinberg")){
                quantizationResultController.floydSteinberg(imagePath);
            } else if(algorithm.getValue().equals("Simple Algorithm")){
                quantizationResultController.simple(imagePath);
            }else if(algorithm.getValue().equals("Median Cut")){
                quantizationResultController.medianCut(imagePath , maxColor);
            }else if(algorithm.getValue().equals("K-Means")){
                quantizationResultController.kMeans(imagePath , maxColor);
            }

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Please Select Image First");
        }
    }

}
