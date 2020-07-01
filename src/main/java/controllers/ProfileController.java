package controllers;

import entities.Database;
import entities.Main;
import entities.PopUp;
import exceptions.NoConnectionException;
import exceptions.UserNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class ProfileController
{
    @FXML
    private ImageView profilePhoto;
    @FXML
    private Label nameLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Label bioLabel;
    @FXML
    private Button utilityButton;
    @FXML
    private Button backButton;

    public void initialize() throws Exception
    {
        try
        {

            nameLabel.setText(Database.loadUser(Main.getLastUser().getCode()).getFirstName() + "\n" + Database.loadUser(Main.getLastUser().getCode()).getLastName());
            boolean gender = Database.loadUser(Main.getLastUser().getCode()).getGender();
            genderLabel.setText(gender ? "M" : "F");
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String bday = df.format(Database.loadUser(Main.getLastUser().getCode()).getBirthDate());
            birthDateLabel.setText(bday);

            if(Database.loadUser(Main.getLastUser().getCode()).getBio() == null)
                bioLabel.setText("This user hasn't updated the bio yet.");
            else
                bioLabel.setText(Database.loadUser(Main.getLastUser().getCode()).getBio());
            if(Main.getLastUser().equals(Main.getCurrentUser()))
                utilityButton.setText("Edit profile");
            else if(Main.getCurrentUser().friendOf(Main.getLastUser()))
                utilityButton.setText("Remove friend");
            else if(Main.getCurrentUser().hasFriendRequest(Main.getLastUser()))
                utilityButton.setText("Accept request");
            else if(Main.getLastUser().hasFriendRequest(Main.getCurrentUser()))
            {
                utilityButton.setText(("Request sent"));
                utilityButton.setDisable(true);
            }
            else
                utilityButton.setText("Send request");

        }
        catch (UserNotFoundException ex)
        {
            PopUp.display("Error!", "User was not found!");
            Main.setLastUser(null);
            new PageNavigation().goBack(backButton);
        }
        catch(NoConnectionException ex)
        {
            PopUp.display("Error!", "Unable to connect to the server");
        }
    }

    public void goBack() throws IOException
    {
        new PageNavigation().goBack(backButton);
        Main.setLastUser(null);
        Main.setLastScene(null);
    }

    public void utilityMethod() throws Exception
    {
        if(Main.getCurrentUser().equals(Main.getLastUser()))
            editBio();
        else if(Main.getCurrentUser().friendOf(Main.getLastUser()))
        {
            Main.getCurrentUser().removeFriend(Main.getLastUser());
            utilityButton.setText("Send request");
        }
        else if(Main.getCurrentUser().hasFriendRequest(Main.getLastUser()))
        {
            Main.getCurrentUser().addFriend(Main.getLastUser());
            utilityButton.setText("Remove friend");
        }
        else
        {
            Main.getCurrentUser().sendFriendRequest(Main.getLastUser());
            utilityButton.setText("Request sent.");
            utilityButton.setDisable(true);
        }
    }

    public void editBio() throws IOException
    {
        new PageNavigation().goTo(utilityButton, "/gui/editBio.fxml");
    }
}
