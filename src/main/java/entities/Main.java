package entities;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application
{
    private static User currentUser = null;
    private static User lastUser = null;
    private static Group lastGroup = null;
    private static String lastScene = null;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/signIn.fxml"));
        primaryStage.setTitle("UPTSoc");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void setCurrentUser(User u)
    {
        currentUser = u;
    }

    public static User getCurrentUser()
    {
        return currentUser;
    }

    public static void setLastUser(User u)
    {
        lastUser = u;
    }

    public static User getLastUser()
    {
        return lastUser;
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    public static void setLastScene(String s)
    {
        lastScene = s;
    }

    public static Group getLastGroup()
    {
        return lastGroup;
    }

    public static void setLastGroup(Group lastGroup)
    {
        Main.lastGroup = lastGroup;
    }

    public static String getLastScene()
    {
        return lastScene;
    }
}
