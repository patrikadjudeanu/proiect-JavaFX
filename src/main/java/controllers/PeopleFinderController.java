package controllers;

import entities.Database;
import entities.ListableUser;
import entities.Main;
import entities.PopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class PeopleFinderController
{
    @FXML
    private TextField searchField;
    @FXML
    private TableView<ListableUser> peopleTable;
    @FXML
    private Label noUsersLabel;
    @FXML
    private Button backButton;

    public void initialize() throws Exception
    {
        ArrayList<String> userCodes = Database.getAllUsers();
        ObservableList<ListableUser> users = FXCollections.observableArrayList();
        for(String s : userCodes)
            users.add(new ListableUser(Database.loadUser(s).getFirstName() + " " + Database.loadUser(s).getLastName(),
                    Database.loadUser(s).getGender() ? "M" : "F", Database.loadUser(s).getBirthDate(), s));

        if(users.isEmpty())
            peopleTable.setVisible(false);
        else
        {
            noUsersLabel.setVisible(false);

            TableColumn<ListableUser, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            nameColumn.setPrefWidth(240);
            nameColumn.setResizable(false);
            TableColumn<ListableUser, String> genderColumn = new TableColumn<>("Gender");
            genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
            genderColumn.setPrefWidth(80);
            genderColumn.setResizable(false);
            TableColumn<ListableUser, Date> birthColumn = new TableColumn<>("Birthday");
            birthColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
            birthColumn.setPrefWidth(178);
            birthColumn.setResizable(false);
            TableColumn<ListableUser, String> codeColumn = new TableColumn<>("Code");
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
            codeColumn.setVisible(false);

            peopleTable.setItems(users);
            peopleTable.getColumns().addAll(nameColumn, genderColumn, birthColumn, codeColumn);

            peopleTable.setOnMousePressed(e ->
                                            {
                                                ListableUser listableUser = peopleTable.getSelectionModel().getSelectedItem();
                                                if(listableUser != null)
                                                try
                                                {
                                                    Main.setLastScene("/gui/peopleFinder.fxml");
                                                    visitProfile(listableUser.getCode());
                                                }
                                                catch (Exception ex)
                                                {
                                                    PopUp.display("Error!", "User not found.");
                                                }
                                            });
        }
    }

    public void search() throws Exception
    {
        String toSearch = searchField.getText().trim();

        ArrayList<String> result = Database.searchByName(toSearch);
        if(result.isEmpty())
        {
            noUsersLabel.setVisible(true);
            peopleTable.setVisible(false);
        }
        else
        {
            peopleTable.setVisible(true);
            peopleTable.getColumns().clear();
            noUsersLabel.setVisible(false);

            ObservableList<ListableUser> usersFound = FXCollections.observableArrayList();
            for(String s : result)
                usersFound.add(new ListableUser(Database.loadUser(s).getFirstName() + " " + Database.loadUser(s).getLastName(),
                        Database.loadUser(s).getGender() ? "M" : "F", Database.loadUser(s).getBirthDate(), s));

            TableColumn<ListableUser, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            nameColumn.setPrefWidth(240);
            nameColumn.setResizable(false);
            TableColumn<ListableUser, String> genderColumn = new TableColumn<>("Gender");
            genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
            genderColumn.setPrefWidth(80);
            genderColumn.setResizable(false);
            TableColumn<ListableUser, Date> birthColumn = new TableColumn<>("Birthday");
            birthColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
            birthColumn.setPrefWidth(178);
            birthColumn.setResizable(false);
            TableColumn<ListableUser, String> codeColumn = new TableColumn<>("Code");
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
            codeColumn.setVisible(false);

            peopleTable.setItems(usersFound);
            peopleTable.getColumns().addAll(nameColumn, genderColumn, birthColumn, codeColumn);

            peopleTable.setOnMousePressed(e ->
            {
                ListableUser listableUser = peopleTable.getSelectionModel().getSelectedItem();
                if(listableUser != null)
                    try
                    {
                        Main.setLastScene("/gui/peopleFinder.fxml");
                        visitProfile(listableUser.getCode());
                    }
                    catch (Exception ex)
                    {
                        PopUp.display("Error!", "User not found.");
                    }
            });
        }
    }

    public void visitProfile(String code) throws Exception
    {
        Main.setLastUser(Database.loadUser(code));
        new PageNavigation().goTo(peopleTable, "/gui/userProfile.fxml");
    }

    public void goBack() throws IOException
    {
        new PageNavigation().goTo(backButton, "/gui/homePage.fxml");
    }
}
