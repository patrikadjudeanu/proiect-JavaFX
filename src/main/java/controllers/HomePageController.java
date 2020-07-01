package controllers;

import entities.Database;
import entities.Main;
import entities.PopUp;
import exceptions.NoConnectionException;
import exceptions.UserNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.io.IOException;


public class HomePageController
{
    @FXML
    private ImageView profilePhoto;
    @FXML
    private Button personalProfileButton;
    @FXML
    private Button viewFriendsButton;
    @FXML
    private Button viewGroupsButton;
    @FXML
    private Button searchForFriendsButton;
    @FXML
    private Button searchForGroupsButton;
    @FXML
    private Button signOutButton;
    @FXML
    private Label username;


    public void initialize() throws Exception
    {
        try
        {
            username.setText(Database.loadUser(Main.getCurrentUser().getCode()).getFirstName().concat(" " + Database.loadUser(Main.getCurrentUser().getCode()).getLastName()));
        }
        catch(UserNotFoundException ex)
        {
            PopUp.display("Error!", "User not found!");
        }
        catch(NoConnectionException ex)
        {
            PopUp.display("Error!", "Unable to connect to the server");
        }
    }

    public void goToMyProfile() throws IOException
    {
        Main.setLastUser(Main.getCurrentUser());
        Main.setLastScene("/gui/homePage.fxml");
        new PageNavigation().goTo(personalProfileButton, "/gui/userProfile.fxml");
    }

    public void goToFriendList() throws IOException
    {
        Main.setLastScene("/gui/homePage.fxml");
        new PageNavigation().goTo(viewFriendsButton, "/gui/friendList.fxml");
    }

    public void goToGroupList() throws IOException
    {
        Main.setLastScene("/gui/homePage.fxml");
        new PageNavigation().goTo(viewGroupsButton, "/gui/groups.fxml");
    }

    public void goToSearchPeople() throws IOException
    {
        Main.setLastScene("/gui/homePage.fxml");
        new PageNavigation().goTo(searchForFriendsButton, "/gui/peopleFinder.fxml");
    }

    public void goToSearchGroups() throws IOException
    {
        Main.setLastScene("/gui/homePage.fxml");
        new PageNavigation().goTo(searchForGroupsButton, "/gui/groupFinder.fxml");
    }

    public void signOut() throws IOException
    {
        Main.setCurrentUser(null);
        Main.setLastUser(null);
        Main.setLastScene(null);
        PageNavigation.switchToSmallStage(signOutButton);
        new PageNavigation().goTo(signOutButton, "/gui/signIn.fxml");
    }
}
