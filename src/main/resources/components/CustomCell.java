package main.resources.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class CustomCell extends GridPane {
    private Label label;
    private ImageView imageView;

    public CustomCell(String labelText, Image image) {
        label = new Label(labelText);
        imageView = new ImageView(image);
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);

        // Add label and imageView to cell
        add(label, 0, 0);
        add(imageView, 0, 1);

        // Set cell constraints
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10));
    }
}