package main.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;


public class MainController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void quantization(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/main/resources/fxml/Quantization.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void Search(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/main/resources/fxml/Search.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
