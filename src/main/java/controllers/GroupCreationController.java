package controllers;

import entities.Database;
import entities.Main;
import entities.PopUp;
import exceptions.NameTakenException;
import exceptions.NoInputException;
import exceptions.WrongCredentialsException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import java.io.IOException;


public class GroupCreationController
{
    @FXML
    private Button backButton;
    @FXML
    private TextField groupNameField;
    @FXML
    private RadioButton privateSelector;


    public void goBack() throws IOException
    {
        new PageNavigation().goTo(backButton, "/gui/groups.fxml");
    }

    public void validateEntries() throws Exception
    {
        if(groupNameField.getText().trim().isEmpty())
            throw new NoInputException();
        if(Database.getGroupCode(groupNameField.getText().trim()) != null)
            throw new NameTakenException();
        for(int i = 0; i < groupNameField.getText().length(); i++)
            if(!Character.isLetterOrDigit(groupNameField.getText().charAt(i)) && groupNameField.getText().charAt(i) != ' ')
                throw new WrongCredentialsException();
    }

    public void createGroup() throws Exception
    {
        try
        {
            validateEntries();
            Database.createGroup(groupNameField.getText().trim(), privateSelector.isSelected(), Main.getCurrentUser().getCode());
            PopUp.display("Congratulations!", "The group has been successfully created!");
            new PageNavigation().goTo(backButton, "/gui/groups.fxml");
        }
        catch(NoInputException ex)
        {
            PopUp.display("Incorrect credentials!", "Please fill all the required fields.");
        }
        catch(WrongCredentialsException ex)
        {
            PopUp.display("Incorrect credentials!", "The information has not been entered properly");
        }
        catch(NameTakenException ex)
        {
            PopUp.display("Incorrect credentials!", "Group name is already taken.");
        }
    }
}
