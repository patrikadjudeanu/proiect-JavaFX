package entities;

import java.sql.Date;

public class Post
{
    private User poster;
    private Date date;
    private String message;
    private int postID;
    private String posterName;

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public User getPoster()
    {
        return poster;
    }

    public void setPoster(User poster)
    {
        this.poster = poster;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
