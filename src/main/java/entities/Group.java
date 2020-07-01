package entities;

import java.util.ArrayList;

public class Group
{
    private String name;
    private String code;
    private User administrator;
    private ArrayList<String> members;
    private boolean is_private;

    public Group(String code)
    {
        this.code = code;
    }

    public Group(String code, String name, boolean is_private, User administrator, ArrayList<String> members)
    {
        this.code = code;
        this.name = name;
        this.is_private = is_private;
        this.administrator = administrator;
        this.members = members;
    }


    public String getName()
    {
        return this.name;
    }

    public boolean getIsPrivate()
    {
        return this.is_private;
    }

    public String getCode()
    {
        return this.code;
    }

    public User getAdministrator()
    {
        return this.administrator;
    }

    public ArrayList<String> getMembers()
    {
        return this.members;
    }

    public boolean hasMember(String userID) throws Exception
    {
        return Database.hasMember(this.code, userID);
    }
}
