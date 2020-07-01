package entities;

import javafx.scene.control.Button;

public class ListableGroup
{
    private String code;
    private String name;
    private Button joinButton;
    private String type;

    public ListableGroup(String name, boolean is_private, String code, Button b)
    {
        this.name = name;
        this.type = is_private ? "private" : "public";
        this.code = code;
        this.joinButton= b;
    }

    public ListableGroup(String name, boolean is_private, String code)
    {
        this.name = name;
        this.type = is_private ? "private" : "public";
        this.code = code;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Button getJoinButton()
    {
        return joinButton;
    }

    public void setJoinButton(Button joinButton)
    {
        this.joinButton = joinButton;
    }
}
