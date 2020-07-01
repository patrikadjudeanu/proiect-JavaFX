package entities;

import javafx.scene.control.Button;

public class ListableGroupRequest
{
    private String userCode;
    private String userName;
    private String groupCode;
    private String groupName;
    private Button acceptButton;
    private Button rejectButton;

    public ListableGroupRequest(String userCode, String groupCode, String groupName) throws Exception
    {
        this.userCode = userCode;
        this.userName = Database.loadUser(userCode).getFirstName().concat(" ").concat(Database.loadUser(userCode).getLastName());
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.acceptButton = new Button("Accept");
        this.rejectButton = new Button("Decline");
    }

    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getGroupCode()
    {
        return groupCode;
    }

    public void setGroupCode(String groupCode)
    {
        this.groupCode = groupCode;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public Button getAcceptButton()
    {
        return acceptButton;
    }

    public void setAcceptButton(Button acceptButton)
    {
        this.acceptButton = acceptButton;
    }

    public Button getRejectButton()
    {
        return rejectButton;
    }

    public void setRejectButton(Button rejectButton)
    {
        this.rejectButton = rejectButton;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
