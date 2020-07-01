package entities;

import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.util.Date;

public class User
{
    private String code;
    private String password;
    private String firstName;
    private String lastName;
    private boolean gender;
    private Date birthDate;
    private String bio;
    private BufferedImage profilePhoto;

    public User(String s, String v)
    {
        this.code = s;
        this.password = v;
    }

    public User(String s)
    {
        this.code = s;
    }

    public boolean friendOf(User u) throws Exception
    {
        return Database.getFriends(this.getCode()).contains(u.getCode());
    }


    public boolean hasFriendRequest(User u) throws Exception
    {
        return Database.getFriendRequests(this.getCode()).contains(u.getCode());
    }

    public void removeFriend(User u) throws Exception
    {
        Database.removeFriend(this.getCode(), u.getCode());
    }

    public void sendFriendRequest(User u) throws Exception
    {
        Database.sendFriendRequest(this.getCode(), u.getCode());
    }

    public void addFriend(User u) throws Exception
    {
        Database.addFriend(this.getCode(), u.getCode());
    }

    public void addToGroup(String groupCode) throws Exception
    {
        Database.addToGroup(this.getCode(), groupCode);
    }

    public boolean equals(User u)
    {
        return this.getCode().equals(u.getCode());
    }

    public String getCode()
    {
        return code;
    }

    public String getPassword()
    {
        return password;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public boolean getGender()
    {
        return gender;
    }

    public void setGender(boolean gender)
    {
        this.gender = gender;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public String getBio()
    {
        return this.bio;
    }

    public BufferedImage getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(BufferedImage profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

}
