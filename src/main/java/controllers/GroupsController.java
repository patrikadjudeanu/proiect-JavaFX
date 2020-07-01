package controllers;

import entities.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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


public class GroupsController
{
    @FXML
    private Button backButton;
    @FXML
    private TableView<ListableGroup> groupList;
    @FXML
    private TableView<ListableGroupRequest> groupRequestList;
    @FXML
    private Label noGroupsLabel;
    @FXML
    private Label noGroupRequestsLabel;

    public void initialize() throws Exception
    {
        try
        {
            ArrayList<String> getGroups = Database.getGroups(Main.getCurrentUser().getCode());
            ObservableList<ListableGroup> groups = FXCollections.observableArrayList();

            for(String s : getGroups)
                groups.add(new ListableGroup(Database.loadGroup(s).getName(), Database.loadGroup(s).getIsPrivate(), s));

            if(groups.isEmpty())
                groupList.setVisible(false);
            else
            {

                noGroupsLabel.setVisible(false);

                noGroupRequestsLabel.setVisible(false);


                TableColumn<ListableGroup, String> nameColumn = new TableColumn<>("Name");
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                nameColumn.setPrefWidth(192);
                nameColumn.setResizable(false);
                TableColumn<ListableGroup, String> typeColumn = new TableColumn<>("Type");
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
                typeColumn.setPrefWidth(70);
                typeColumn.setResizable(false);
                TableColumn<ListableGroup, String> codeColumn = new TableColumn<>("Code");
                codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
                codeColumn.setVisible(false);

                groupList.setItems(groups);
                groupList.getColumns().addAll(nameColumn, typeColumn, codeColumn);

                groupList.setOnMousePressed(e ->
                {
                    ListableGroup listableGroup = groupList.getSelectionModel().getSelectedItem();
                    if(listableGroup != null)
                        try
                        {
                            visitGroupProfile(listableGroup.getCode());
                        }
                        catch (Exception ex)
                        {
                            PopUp.display("Error!", "Group not found.");
                        }
                });
            }

            ArrayList<ListableGroupRequest> getGroupRequest = Database.getGroupRequests(Main.getCurrentUser().getCode());
            ObservableList<ListableGroupRequest> groupRequests = FXCollections.observableArrayList();


            for(ListableGroupRequest s : getGroupRequest)
            {
                groupRequests.add(s);
                s.getAcceptButton().setOnAction(e->
                                                {
                                                    try
                                                    {
                                                        Database.addToGroup(s.getUserCode(), s.getGroupCode());
                                                        Database.deleteGroupRequest(s.getUserCode(), s.getGroupCode());
                                                    }
                                                    catch (NoConnectionException | SQLException ex)
                                                    {
                                                        ex.printStackTrace();
                                                    }
                                                    try
                                                    {
                                                        new PageNavigation().goTo(groupList, "/gui/groups.fxml");
                                                    }
                                                    catch (IOException ex)
                                                    {
                                                        ex.printStackTrace();
                                                    }
                                                });

                s.getRejectButton().setOnAction(e->
                                                {
                                                    try
                                                    {
                                                        Database.deleteGroupRequest(s.getUserCode(), s.getGroupCode());
                                                    }
                                                    catch (NoConnectionException | SQLException ex)
                                                    {
                                                        ex.printStackTrace();
                                                    }
                                                    try
                                                    {
                                                        new PageNavigation().goTo(groupList, "/gui/groups.fxml");
                                                    }
                                                    catch (IOException ex)
                                                    {
                                                        ex.printStackTrace();
                                                    }
                                                });
            }

            if(groupRequests.isEmpty())
                groupRequestList.setVisible(false);
            else
            {
                noGroupRequestsLabel.setVisible(false);

                TableColumn<ListableGroupRequest, String> userNameColumn = new TableColumn<>("User name");
                userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
                userNameColumn.setPrefWidth(140);
                userNameColumn.setResizable(false);
                TableColumn<ListableGroupRequest, String> groupNameColumn = new TableColumn<>("Group name");
                groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));
                groupNameColumn.setPrefWidth(139);
                groupNameColumn.setResizable(false);
                TableColumn<ListableGroupRequest, Button> acceptColumn = new TableColumn<>();
                acceptColumn.setCellValueFactory(new PropertyValueFactory<>("acceptButton"));
                acceptColumn.setPrefWidth(79);
                acceptColumn.setResizable(false);
                TableColumn<ListableGroupRequest, Button> declineButton = new TableColumn<>();
                declineButton.setCellValueFactory(new PropertyValueFactory<>("rejectButton"));
                declineButton.setPrefWidth(79);
                declineButton.setResizable(false);
                TableColumn<ListableGroupRequest, String> userCodeColumn = new TableColumn<>();
                userCodeColumn.setCellValueFactory(new PropertyValueFactory<>("userCode"));
                userCodeColumn.setPrefWidth(0);
                userCodeColumn.setVisible(false);
                TableColumn<ListableGroupRequest, String> groupCodeColumn = new TableColumn<>();
                groupCodeColumn.setCellValueFactory(new PropertyValueFactory<>("groupCode"));
                groupCodeColumn.setPrefWidth(0);
                groupCodeColumn.setVisible(false);

                groupRequestList.setItems(groupRequests);
                groupRequestList.getColumns().addAll(userNameColumn, groupNameColumn, acceptColumn, declineButton, userCodeColumn, groupCodeColumn);

            }


        }
        catch(UserNotFoundException ex)
        {
            PopUp.display("Error!", "User not found.");
        }
        catch (NoConnectionException ex)
        {
            PopUp.display("Error!", "Unable to connect to the server");
        }
    }

    public void goBack() throws IOException
    {
        new PageNavigation().goTo(backButton, "/gui/homePage.fxml");
    }

    public void goToGroupCreation() throws IOException
    {
        new PageNavigation().goTo(backButton, "/gui/groupCreation.fxml");
    }

    public void visitGroupProfile(String code) throws IOException
    {
        Main.setLastGroup(new Group(code));
        Main.setLastScene("/gui/groups.fxml");
        new PageNavigation().goTo(backButton, "/gui/groupProfile.fxml");
    }
}
