package entities;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUp
{
    public static void display(String title, String message)
    {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);

        Label warning = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> ((Stage)closeButton.getScene().getWindow()).close());

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(15,15,15,15));
        layout.setPrefWidth(400);
        layout.setPrefHeight(200);
        layout.setCenter(warning);
        layout.setBottom(closeButton);
        BorderPane.setAlignment(closeButton, Pos.BOTTOM_RIGHT);

        stage.setScene(new Scene(layout));
        stage.show();
    }
}
