package controllers;

import entities.Database;
import entities.Main;
import entities.PopUp;
import exceptions.WrongCredentialsException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;

public class SignInController
{
    @FXML
    private TextField codeField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;

    public void goTosignUp() throws IOException
    {
        new PageNavigation().goTo(signUpButton, "/gui/signUP.fxml");
    }

    public void signIn() throws Exception
    {
        Connection e = Database.getConnection();
        if(e != null)
        {
            try
            {
                Main.setCurrentUser(Database.findUser(codeField.getText().trim(), passwordField.getText()));
                PageNavigation.switchToBigStage(signInButton);
                new PageNavigation().goTo(signInButton, "/gui/homePage.fxml");
            }
            catch(WrongCredentialsException ex)
            {
                PopUp.display("Wrong credentials!", "Username or password are incorrect.");
            }
        }
    }

}
