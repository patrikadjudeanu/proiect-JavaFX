package entities;

import java.util.Date;

public class ListableUser
{
    private String name;
    private String gender;
    private Date birthday;
    private String code;

    public ListableUser(String name, String gender, Date birthday, String code)
    {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.code = code;
    }

    public ListableUser(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
