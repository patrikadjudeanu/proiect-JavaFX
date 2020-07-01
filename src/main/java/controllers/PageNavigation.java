package controllers;

import entities.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class PageNavigation
{
    public static void switchToSmallStage(Node n)
    {
        Stage stage = ((Stage)n.getScene().getWindow());
        stage.setHeight(400);
        stage.setWidth(600);
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    public static void switchToBigStage(Node n)
    {
        Stage stage = ((Stage)n.getScene().getWindow());
        stage.setHeight(700);
        stage.setWidth(1000);
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    public void goTo(Node n, String s) throws IOException
    {
        Stage stage = (Stage) n.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(s));
        stage.setScene(new Scene(root));
    }

    public void goBack(Node n) throws IOException
    {
        Stage stage = (Stage) n.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(Main.getLastScene()));
        stage.setScene(new Scene(root));
    }
}
