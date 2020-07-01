package controllers;

import entities.Database;
import entities.Main;
import entities.PopUp;
import exceptions.NoConnectionException;
import exceptions.UserNotFoundException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Collectors;


public class EditBioController
{
    @FXML
    private ImageView profilePhoto;
    @FXML
    private Button changePhotoButton;
    @FXML
    private TextArea bioField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    public void initialize() throws Exception
    {
        try
        {

            bioField.setText(Database.loadUser(Main.getCurrentUser().getCode()).getBio());
        }
        catch(UserNotFoundException ex)
        {
            PopUp.display("Error!", "User not found.");
        }
        catch(NoConnectionException ex)
        {
            PopUp.display("Error!", "Unable to connect to the server");
        }
    }

    public void goToProfile() throws IOException
    {
        new PageNavigation().goTo(cancelButton, "/gui/userProfile.fxml");
    }

    public void saveChanges() throws Exception
    {
        try
        {
            Database.updateUser(Main.getCurrentUser().getCode(), bioField.getText());
            goToProfile();
        }
        catch(NoConnectionException ex)
        {
            PopUp.display("Error!", "Unable to connect to the server");
        }
    }

    public void changePhoto() throws IOException, SQLException, NoConnectionException
    {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fc.getExtensionFilters().add(imageFilter);
        fc.setTitle("Select profile photo.");
        File selectedFile = fc.showOpenDialog(null);

        String filePath = selectedFile.getAbsolutePath();
        String content = readFile(filePath, StandardCharsets.ISO_8859_1);
        Database.changeProfilePhoto(Main.getCurrentUser().getCode(), content);
        new PageNavigation().goTo(changePhotoButton, "/gui/editBio.fxml");
    }

    public static String readFile(String path, Charset encoding) throws IOException
    {
        return Files.lines(Paths.get(path), encoding).collect(Collectors.joining(System.lineSeparator()));
    }
}
