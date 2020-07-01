package controllers;

import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;

public class GroupFinderController
{
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<ListableGroup> groupsTable;
    @FXML
    private Label noGroupsLabel;
    @FXML
    private Button backButton;

    public void initialize() throws Exception
    {
        ArrayList<String> groupCodes = Database.getAllGroups();
        ObservableList<ListableGroup> groups = FXCollections.observableArrayList();
        for(String s : groupCodes)
        {
            Button b = new Button();
            if (Database.loadGroup(s).hasMember(Main.getCurrentUser().getCode()))
            {
                b.setText("Member");
                b.setDisable(true);
            }
            else
                b.setText((!Database.loadGroup(s).getIsPrivate()) ? "Join" : "Send join request");

            if(Database.loadGroup(s).getIsPrivate())
            {
                b.setOnAction(e ->
                                {
                                    try
                                    {
                                        Database.sendGroupRequest(Database.loadGroup(s).getCode(), Main.getCurrentUser().getCode());
                                        b.setText("Request sent");
                                        b.setDisable(true);
                                    }
                                    catch (Exception ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                });
            }
            else
            {
                b.setOnAction(e ->
                                {
                                    try
                                    {
                                        Database.addToGroup(Main.getCurrentUser().getCode(), Database.loadGroup(s).getCode());
                                        b.setText("Member");
                                        b.setDisable(true);
                                    }
                                    catch (Exception ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                });
            }


            groups.add(new ListableGroup(Database.loadGroup(s).getName(), Database.loadGroup(s).getIsPrivate(), s, b));

        }

        if(groups.isEmpty())
            groupsTable.setVisible(false);
        else
        {
            noGroupsLabel.setVisible(false);

            TableColumn<ListableGroup, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            nameColumn.setPrefWidth(240);
            nameColumn.setResizable(false);
            TableColumn<ListableGroup, String> typeColumn = new TableColumn<>("Type");
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            typeColumn.setPrefWidth(70);
            typeColumn.setResizable(false);
            TableColumn<ListableGroup, String> joinColumn = new TableColumn<>("Action");
            joinColumn.setCellValueFactory(new PropertyValueFactory<>("joinButton"));
            joinColumn.setPrefWidth(164);
            joinColumn.setResizable(false);
            TableColumn<ListableGroup, String> codeColumn = new TableColumn<>("Action");
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
            codeColumn. setVisible(false);
            codeColumn.setResizable(false);


            groupsTable.setItems(groups);
            groupsTable.getColumns().addAll(nameColumn, typeColumn, joinColumn, codeColumn);

            groupsTable.setOnMousePressed(e ->
            {
                ListableGroup listablegroup = groupsTable.getSelectionModel().getSelectedItem();
                try
                {
                    if(listablegroup != null && Database.loadGroup(listablegroup.getCode()).hasMember(Main.getCurrentUser().getCode()))
                        try
                        {
                            visitGroupProfile(listablegroup.getCode());
                        }
                        catch (Exception ex)
                        {
                            PopUp.display("Error!", "Group not found.");
                        }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            });
        }

    }

    public void search() throws Exception
    {
        String toSearch = searchField.getText().trim();

        ArrayList<String> result = Database.searchGroupByName(toSearch);
        if(result.isEmpty())
        {
            noGroupsLabel.setVisible(true);
            groupsTable.setVisible(false);
        }
        else
        {
            groupsTable.setVisible(true);
            groupsTable.getColumns().clear();
            noGroupsLabel.setVisible(false);

            ObservableList<ListableGroup> groupsFound = FXCollections.observableArrayList();
            for(String s : result)
                groupsFound.add(new ListableGroup(Database.loadGroup(s).getName(), Database.loadGroup(s).getIsPrivate(), s,
                        new Button ((!Database.loadGroup(s).getIsPrivate()) ? "Join" : "Send join request")));

            TableColumn<ListableGroup, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            nameColumn.setPrefWidth(240);
            nameColumn.setResizable(false);
            TableColumn<ListableGroup, String> typeColumn = new TableColumn<>("Type");
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            typeColumn.setPrefWidth(70);
            typeColumn.setResizable(false);
            TableColumn<ListableGroup, String> joinColumn = new TableColumn<>("Action");
            joinColumn.setCellValueFactory(new PropertyValueFactory<>("joinButton"));
            joinColumn.setPrefWidth(164);
            joinColumn.setResizable(false);


            groupsTable.setItems(groupsFound);
            groupsTable.getColumns().addAll(nameColumn, typeColumn, joinColumn);


        }
    }

    public void goBack() throws IOException
    {
        new PageNavigation().goTo(backButton, "/gui/homePage.fxml");
    }

    public void visitGroupProfile(String groupCode) throws IOException
    {
        Main.setLastGroup(new Group(groupCode));
        new PageNavigation().goTo(backButton, "/gui/groupProfile.fxml");
    }
}
