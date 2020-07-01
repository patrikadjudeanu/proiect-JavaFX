package controllers;

import entities.Database;
import entities.PopUp;
import entities.User;
import exceptions.InvalidInputException;
import exceptions.NoConnectionException;
import exceptions.NoInputException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;


public class AccountCreationController
{
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ChoiceBox<String> genderBox;
    @FXML
    private DatePicker birthdayBox;
    @FXML
    private TextField passwordField;
    @FXML
    private Button accountCreationButton;
    @FXML
    private Button cancelButton;

    private static String code;


    public void initialize()
    {
        genderBox.setItems(FXCollections.observableArrayList("M","F"));
    }


    public void createAccount() throws Exception
    {
        try
        {
            validateEntries();
            User u = buildUser(code, passwordField.getText(), firstNameField.getText().trim(), lastNameField.getText().trim(),
                    genderBox.getSelectionModel().getSelectedItem().equals("M"), java.sql.Date.valueOf(birthdayBox.getValue()));
            Database.createUser(u);
            Database.setCodeTaken(code);
            PopUp.display("Congratulations!", "Your account has been successfully created!");
            goToLogIn();
        }
        catch(InvalidInputException ex)
        {
            PopUp.display("Incorrect credentials!", "The information has not been entered properly.");
        }
        catch (NoInputException ex)
        {
            PopUp.display("Incorrect credentials!", "Please enter all the required information.");
        }
        catch(NoConnectionException ex)
        {
            PopUp.display("Error!", "Unable to connect to the server");
        }
    }

    public void goToSignUp() throws IOException
    {
        AccountCreationController.setCode(null);
        new PageNavigation().goTo(cancelButton, "/gui/signUp.fxml");
    }

    public void goToLogIn() throws IOException
    {
        AccountCreationController.setCode(null);
        new PageNavigation().goTo(accountCreationButton, "/gui/signIn.fxml");
    }

    public void validateEntries() throws InvalidInputException, NoInputException
    {
        if(firstNameField.getText().length() == 0)
            throw new NoInputException();
        if(lastNameField.getText().length() == 0)
            throw new NoInputException();
        if(genderBox.getSelectionModel().isEmpty())
            throw new NoInputException();
        if(birthdayBox.getValue()==null)
            throw new NoInputException();
        if(passwordField.getText().length() == 0)
            throw new NoInputException();
        for(int i = 0; i < firstNameField.getText().length(); i++)
            if(!Character.isLetter(firstNameField.getText().charAt(i)))
                throw new InvalidInputException();
        for(int i = 0; i < lastNameField.getText().length(); i++)
            if(!Character.isLetter(lastNameField.getText().charAt(i)))
                throw new InvalidInputException();
        if(birthdayBox.getValue().compareTo(LocalDate.now()) > 0)
            throw new InvalidInputException();
    }

    public static User buildUser(String code, String password, String firstName, String lastName, boolean gender, Date birthday)
    {
        User u = new User(code, password);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setGender(gender);
        u.setBirthDate(birthday);

        return u;
    }
    public static void setCode(String c)
    {
        code = c;
    }
}
