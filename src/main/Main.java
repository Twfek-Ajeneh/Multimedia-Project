package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/fxml/Main.fxml"));
        primaryStage.getIcons().add(new Image("C:\\Users\\ASUS\\Desktop\\University\\4-Th Year\\Chapter 2\\Multimedia\\multimedia-project\\src\\main\\resources\\images\\logo.jpg"));
        primaryStage.setTitle("Multimedia Project");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
