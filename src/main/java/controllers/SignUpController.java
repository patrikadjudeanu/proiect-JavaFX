package controllers;

import entities.Database;
import entities.PopUp;
import exceptions.WrongCredentialsException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;



public class SignUpController
{
    @FXML
    private TextField codeField;
    @FXML
    private Button validateButton;
    @FXML
    private Button cancelButton;

    public void validate() throws Exception
    {
        try
        {
            Database.validateCode(codeField.getText().trim());
            goToAccountCreation(codeField.getText().trim());
        }
        catch(WrongCredentialsException ex)
        {
            PopUp.display("Wrong credentials!", "The code you provided is either incorrect or in use.");
        }
    }

    private void goToAccountCreation(String code) throws IOException
    {
        AccountCreationController.setCode(code);
        new PageNavigation().goTo(validateButton, "/gui/accountCreation.fxml");
    }

    public void goToSignIn() throws IOException
    {
        new PageNavigation().goTo(cancelButton, "/gui/signIn.fxml");
    }
}
