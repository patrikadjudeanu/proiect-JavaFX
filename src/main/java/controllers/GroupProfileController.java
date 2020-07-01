package controllers;

import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class GroupProfileController
{
    @FXML
    private Label groupNameLabel;
    @FXML
    private Label adminNameLabel;
    @FXML
    private TableView<Post> postsTable;
    @FXML
    private TableView<ListableUser> membersTable;
    @FXML
    private Button backButton;
    @FXML
    private Label noPostsLabel;
    @FXML
    private Button postButton;

    public void initialize() throws Exception
    {
        groupNameLabel.setText(Database.loadGroup(Main.getLastGroup().getCode()).getName());
        adminNameLabel.setText("Admin: " + Database.loadGroup(Main.getLastGroup().getCode()).getAdministrator().getFirstName()
                                + " " + Database.loadGroup(Main.getLastGroup().getCode()).getAdministrator().getLastName());

        ArrayList<Integer> postCodes = Database.getPostsFromGroup(Main.getLastGroup().getCode());
        ObservableList<Post> posts = FXCollections.observableArrayList();
        for(int i : postCodes)
            posts.add(Database.loadPost(i));

        if(posts.isEmpty())
            postsTable.setVisible(false);
        else
        {
            noPostsLabel.setVisible(false);

            TableColumn<Post, String> nameColumn = new TableColumn<>("Member");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("posterName"));
            nameColumn.setPrefWidth(155);
            nameColumn.setResizable(false);
            TableColumn<Post, String> messageColumn = new TableColumn<>("Message");
            messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
            messageColumn.setPrefWidth(322);
            messageColumn.setResizable(false);
            TableColumn<Post, Date> dateColumn = new TableColumn<>("Date");
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            dateColumn.setPrefWidth(100);
            dateColumn.setResizable(false);
            TableColumn<Post, Integer> postIDColumn = new TableColumn<>("ID");
            postIDColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            postIDColumn.setVisible(false);
            postIDColumn.setResizable(false);

            postsTable.setItems(posts);
            postsTable.getColumns().addAll(nameColumn, messageColumn, dateColumn, postIDColumn);

        }

        ArrayList<String> userCodes = Database.getUsersFromGroup(Main.getLastGroup().getCode());
        ObservableList<ListableUser> members = FXCollections.observableArrayList();
        for(String s : userCodes)
            members.add(new ListableUser(s, Database.loadUser(s).getFirstName() + " " + Database.loadUser(s).getLastName()));


        TableColumn<ListableUser, String> memberColumn = new TableColumn<>("Members");
        memberColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        memberColumn.setPrefWidth(219);
        memberColumn.setResizable(false);
        TableColumn<ListableUser, String> codeColumn = new TableColumn<>("Code");
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeColumn.setVisible(false);
        codeColumn.setResizable(false);

        membersTable.setItems(members);
        membersTable.getColumns().addAll(memberColumn, codeColumn);

        membersTable.setOnMousePressed(e ->
        {
            ListableUser listableuser = membersTable.getSelectionModel().getSelectedItem();
            if(listableuser != null)
                try
                {
                    Main.setLastScene("/gui/groupProfile.fxml");
                    visitMemberProfile(listableuser.getCode());
                }
                catch (Exception ex)
                {
                    PopUp.display("Error!", "User not found.");
                }
        });
    }

    public void goBack() throws IOException
    {
        Main.setLastGroup(null);
        new PageNavigation().goTo(backButton, "/gui/homePage.fxml");
    }

    public void visitMemberProfile(String code) throws IOException
    {
        Main.setLastUser(new User(code));
        new PageNavigation().goTo(membersTable, "/gui/userProfile.fxml");
    }

    public void post() throws IOException
    {
        new PageNavigation().goTo(postButton, "/gui/post.fxml");
    }
}
