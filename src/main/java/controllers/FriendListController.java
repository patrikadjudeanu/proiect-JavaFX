package controllers;

import entities.*;
import exceptions.NoConnectionException;
import exceptions.UserNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class FriendListController
{
    @FXML
    private Button backButton;
    @FXML
    private TableView<ListableUser> friendList;
    @FXML
    private TableView<ListableUser> friendRequestList;
    @FXML
    private Label noFriendsLabel;
    @FXML
    private Label noFriendRequestsLabel;


    public void goBack() throws IOException
    {
        new PageNavigation().goTo(backButton, "/gui/homePage.fxml");
    }

    public void visitProfile(String code) throws Exception
    {
        Main.setLastUser(Database.loadUser(code));
        new PageNavigation().goTo(friendList, "/gui/userProfile.fxml");
    }

    public void initialize() throws Exception
    {
        try
        {
            ArrayList<String> getFriends = Database.getFriends(Main.getCurrentUser().getCode());
            ObservableList<ListableUser> friends = FXCollections.observableArrayList();
            for(String s : getFriends)
                friends.add(new ListableUser(Database.loadUser(s).getFirstName() + " " + Database.loadUser(s).getLastName(),
                        Database.loadUser(s).getGender() ? "M" : "F", Database.loadUser(s).getBirthDate(), s));
            if(friends.isEmpty())
                    friendList.setVisible(false);
            else
                {
                    noFriendsLabel.setVisible(false);

                    TableColumn<ListableUser, String> nameColumn = new TableColumn<>("Name");
                    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                    nameColumn.setPrefWidth(180);
                    nameColumn.setResizable(false);
                    TableColumn<ListableUser, String> genderColumn = new TableColumn<>("Gender");
                    genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
                    genderColumn.setPrefWidth(68);
                    genderColumn.setResizable(false);
                    TableColumn<ListableUser, Date> birthColumn = new TableColumn<>("Birthday");
                    birthColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
                    birthColumn.setPrefWidth(100);
                    birthColumn.setResizable(false);
                    TableColumn<ListableUser, String> codeColumn = new TableColumn<>("Code");
                    codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
                    codeColumn.setVisible(false);

                    friendList.setItems(friends);
                    friendList.getColumns().addAll(nameColumn, genderColumn, birthColumn, codeColumn);
                    friendList.setOnMousePressed(e ->
                                                    {
                                                        ListableUser listableUser = friendList.getSelectionModel().getSelectedItem();
                                                        if(listableUser != null)
                                                        try
                                                        {
                                                            Main.setLastScene("/gui/friendList.fxml");
                                                            visitProfile(listableUser.getCode());
                                                        }
                                                        catch (Exception ex)
                                                        {
                                                            PopUp.display("Error!", "User not found.");
                                                        }
                                                    });
                }

            ArrayList<String> getFriendRequests = Database.getFriendRequests(Main.getCurrentUser().getCode());
            ObservableList<ListableUser> friendRequests = FXCollections.observableArrayList();

            for(String s : getFriendRequests)
                friendRequests.add(new ListableUser(Database.loadUser(s).getFirstName() + " " + Database.loadUser(s).getLastName(),
                        Database.loadUser(s).getGender() ? "M" : "F", Database.loadUser(s).getBirthDate(), s));
            if(friendRequests.isEmpty())
            {
                friendRequestList.setVisible(false);
            }
            else
            {
                noFriendRequestsLabel.setVisible(false);

                TableColumn<ListableUser, String> nameColumnR = new TableColumn<>("Name");
                nameColumnR.setCellValueFactory(new PropertyValueFactory<>("name"));
                nameColumnR.setPrefWidth(180);
                nameColumnR.setResizable(false);
                TableColumn<ListableUser, String> genderColumnR = new TableColumn<>("Gender");
                genderColumnR.setCellValueFactory(new PropertyValueFactory<>("gender"));
                genderColumnR.setPrefWidth(68);
                genderColumnR.setResizable(false);
                TableColumn<ListableUser, Date> birthColumnR = new TableColumn<>("Birthday");
                birthColumnR.setCellValueFactory(new PropertyValueFactory<>("birthday"));
                birthColumnR.setPrefWidth(100);
                birthColumnR.setResizable(false);
                TableColumn<ListableUser, String> codeColumnR = new TableColumn<>("Code");
                codeColumnR.setCellValueFactory(new PropertyValueFactory<>("code"));
                codeColumnR.setVisible(false);

                friendRequestList.setItems(friendRequests);
                friendRequestList.getColumns().addAll(nameColumnR, genderColumnR, birthColumnR, codeColumnR);
                friendRequestList.setOnMousePressed(e ->
                                                        {
                                                            ListableUser listableUser = friendRequestList.getSelectionModel().getSelectedItem();
                                                            if(listableUser != null)
                                                            try
                                                            {
                                                                Main.setLastScene("/gui/friendList.fxml");
                                                                visitProfile(listableUser.getCode());
                                                            }
                                                            catch (Exception ex)
                                                            {
                                                                PopUp.display("Error!", "User not found.");
                                                            }
                                                        });
            }
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
}

