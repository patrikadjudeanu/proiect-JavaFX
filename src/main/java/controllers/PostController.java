package controllers;

import entities.Database;
import entities.Main;
import exceptions.NoConnectionException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.sql.SQLException;

public class PostController
{
    @FXML
    private TextArea postArea;
    @FXML
    private Button cancelButton;

    public void post() throws SQLException, NoConnectionException, IOException
    {
        String p = postArea.getText().trim();
        if(p.isEmpty())
            goBack();
        else
        {
            Database.addPost(p, Main.getCurrentUser().getCode(), Main.getLastGroup().getCode());
            goBack();
        }
    }

    public void goBack() throws IOException
    {
        new PageNavigation().goTo(cancelButton, "/gui/groupProfile.fxml");
    }
}
