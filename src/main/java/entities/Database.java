package entities;

import exceptions.GroupNotFoundException;
import exceptions.NoConnectionException;
import exceptions.UserNotFoundException;
import exceptions.WrongCredentialsException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

public class Database
{
    public static Connection getConnection()
    {
        try
        {
            String driver = "org.h2.Driver";
            String url = "jdbc:h2:tcp://localhost/~/test";
            String username = "sa";
            String password = "";

            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        }
        catch(Exception e)
        {
            PopUp.display("Connection error!", "Could not connect to the server. Please try later.");
            return null;
        }
    }

    public static User findUser(String code, String password) throws Exception
    {
        boolean found = false;

        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();
        PreparedStatement statement = con.prepareStatement("SELECT code,password FROM users");
        ResultSet result = statement.executeQuery();

        while(result.next())
        {
            if(result.getString("code").equals(code) && result.getString("password").equals(password))
            {
                found = true;
                break;
            }
        }

        if(!found) throw new WrongCredentialsException();
        else
            return new User(code, password);
    }

    public static void validateCode(String code) throws Exception
    {
        boolean found = false;

        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();
        PreparedStatement statement = con.prepareStatement("SELECT id, is_taken FROM codes");
        ResultSet result = statement.executeQuery();

        while(result.next())
        {
            if(result.getString("id").equals(code) && (!result.getBoolean("is_taken")))
            {
                found = true;
                break;
            }
        }

        if(!found) throw new WrongCredentialsException();
    }

    public static void setCodeTaken(String code) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();
        PreparedStatement update = con.prepareStatement("UPDATE codes SET is_taken = true WHERE id = ?");
        update.setString(1, code);
        update.executeUpdate();
    }

    public static void createUser(User u) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();
        PreparedStatement update = con.prepareStatement("INSERT INTO users (code, password, first_name, last_name, gender, birthday)VALUES (?, ?, ?, ?, ?, ?)");
        update.setString(1, u.getCode());
        update.setString(2, u.getPassword());
        update.setString(3, u.getFirstName());
        update.setString(4, u.getLastName());
        update.setBoolean(5, u.getGender());
        update.setDate(6, (Date) u.getBirthDate());
        update.executeUpdate();
    }

    public static User loadUser(String code) throws Exception
    {
        User user = new User(code);

        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();
        PreparedStatement load = con.prepareStatement("SELECT first_name, last_name, gender, birthday, bio FROM users WHERE code = ?");
        load.setString(1, code);
        ResultSet result = load.executeQuery();
        if(result.next())
        {
            String firstName = result.getString("first_name");
            String lastName = result.getString("last_name");
            boolean gender = result.getBoolean("gender");
            Date birthday = result.getDate("birthday");
            String bio = result.getString("bio");

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setGender(gender);
            user.setBirthDate(birthday);
            user.setBio(bio);

            PreparedStatement loadImage = con.prepareStatement("SELECT profile_photo FROM profile_photos WHERE user_id = ?");
            loadImage.setString(1, code);
            ResultSet res = loadImage.executeQuery();
            if(result.next())
            {
                Blob blob = res.getBlob("profile_photo");
                InputStream in = blob.getBinaryStream();
                BufferedImage image = ImageIO.read(in);
                user.setProfilePhoto(image);
            }
            return user;
        }
        else
            throw new UserNotFoundException();
    }

    public static void updateUser(String code, String bio) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();
        PreparedStatement update = con.prepareStatement("UPDATE users SET bio = ? where code = ?");
        update.setString(1, bio);
        update.setString(2, code);
        update.executeUpdate();
    }

    public static ArrayList<String> getFriends(String code) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        PreparedStatement find = con.prepareStatement("SELECT friends FROM users WHERE code = ?");
        find.setString(1, code);

        ArrayList<String> codes = new ArrayList<>();
        ResultSet rs = find.executeQuery();
        if(rs.next())
        {
            Array array = rs.getArray("friends");
            if (array != null)
            {
                Object[] a = (Object[]) array.getArray();
                for (Object o : a)
                    codes.add(o.toString());
            }
        }
        return codes;
    }

    public static ArrayList<String> getFriendRequests(String code) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        PreparedStatement find = con.prepareStatement("SELECT friend_requests FROM users WHERE code = ?");
        find.setString(1, code);

        ArrayList<String> codes = new ArrayList<>();
        ResultSet rs = find.executeQuery();
        if(rs.next())
        {
            Array array = rs.getArray("friend_requests");
            if (array != null)
            {
                Object[] a = (Object[]) array.getArray();
                for (Object o : a)
                    codes.add(o.toString());
            }
        }
        return codes;
    }

    public static void removeFriend(String a, String b) throws Exception
    {
        Connection con = getConnection();
        if (con == null)
            throw new NoConnectionException();

        ArrayList<String> getFriendsOfA = Database.getFriends(a);
        ArrayList<String> getFriendsOfB = Database.getFriends(b);
        getFriendsOfA.remove(b);
        getFriendsOfB.remove(a);

        if (!getFriendsOfA.isEmpty())
        {
            Object[] friends_of_a = getFriendsOfA.toArray();
            Array arrayA = con.createArrayOf("varchar(25)", friends_of_a);
            PreparedStatement update1 = con.prepareStatement("UPDATE users set friends = ? where code = ?");
            update1.setArray(1, arrayA);
            update1.setString(2, a);
            update1.executeUpdate();
        }
        else
            {
                PreparedStatement update1 = con.prepareStatement("UPDATE users set friends = null where code = ?");
                update1.setString(1, a);
                update1.executeUpdate();
            }

        if (!getFriendsOfB.isEmpty())
        {
            Object[] friends_of_b = getFriendsOfB.toArray();
            Array arrayB = con.createArrayOf("varchar(25)", friends_of_b);
            PreparedStatement update1 = con.prepareStatement("UPDATE users set friends = ? where code = ?");
            update1.setArray(1, arrayB);
            update1.setString(2, a);
            update1.executeUpdate();
        }
        else
            {
                PreparedStatement update1 = con.prepareStatement("UPDATE users set friends = null where code = ?");
                update1.setString(1, b);
                update1.executeUpdate();
            }
    }

    public static void sendFriendRequest(String a, String b) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        ArrayList<String> getRequests = Database.getFriendRequests(b);
        if(!getRequests.contains(a))
            getRequests.add(a);

        Object[] requests_of_b = getRequests.toArray();
        Array array = con.createArrayOf("varchar(25)", requests_of_b);
        PreparedStatement update = con.prepareStatement("UPDATE users set friend_requests = ? where code = ?");
        update.setArray(1, array);
        update.setString(2, b);
        update.executeUpdate();
    }

    public static void addFriend(String a, String b) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        Array array;
        PreparedStatement update;

        ArrayList<String> friendRequests = Database.getFriendRequests(a);
        friendRequests.remove(b);
        if(!friendRequests.isEmpty())
        {
            Object[] requests = friendRequests.toArray();
            array = con.createArrayOf("varchar(25)", requests);
            update = con.prepareStatement("UPDATE users SET friend_requests = ? where code = ?");
            update.setArray(1, array);
            update.setString(2, a);
        }
        else
        {
            update = con.prepareStatement("UPDATE users SET friend_requests = null where code = ?");
            update.setString(1, a);
        }
        update.executeUpdate();

        ArrayList<String> friendsOfA = Database.getFriends(a);
        friendsOfA.add(b);
        Object[] friendsA = friendsOfA.toArray();
        array = con.createArrayOf("varchar(25)", friendsA);
        update = con.prepareStatement("UPDATE users SET friends = ? where code = ?");
        update.setArray(1, array);
        update.setString(2, a);
        update.executeUpdate();

        ArrayList<String> friendsOfB = Database.getFriends(b);
        friendsOfB.add(a);
        Object[] friendsB = friendsOfB.toArray();
        array = con.createArrayOf("varchar(25)", friendsB);
        update = con.prepareStatement("UPDATE users SET friends = ? where code = ?");
        update.setArray(1, array);
        update.setString(2, b);
        update.executeUpdate();
    }

    public static ArrayList<String> searchByName(String name) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        ArrayList<String> found = new ArrayList<>();

        PreparedStatement search = con.prepareStatement("SELECT first_name, last_name, code FROM users");
        ResultSet rs = search.executeQuery();

        while(rs.next())
        {
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String code = rs.getString("code");
            String name1 = firstName.concat(" ").concat(lastName);
            String name2 = lastName.concat(" ").concat(firstName);

            if(name1.toLowerCase().contains(name) || name2.toLowerCase().contains(name))
                found.add(code);
        }

        return found;
    }

    public static ArrayList<String> getAllUsers() throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        ArrayList<String> users = new ArrayList<>();

        PreparedStatement search = con.prepareStatement("SELECT code FROM users");
        ResultSet rs = search.executeQuery();

        while(rs.next())
        {
            String code = rs.getString("code");
            users.add(code);
        }

        return users;
    }

    public static ArrayList<String> getGroups(String userCode) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        ArrayList<String> codes = new ArrayList<>();

        PreparedStatement search = con.prepareStatement("SELECT groups FROM users WHERE code = ?");
        search.setString(1, userCode);
        ResultSet rs = search.executeQuery();
        if(rs.next())
        {
            Array array = rs.getArray("groups");
            if(array != null)
            {
                Object[] a = (Object[]) array.getArray();
                for(Object o : a)
                    codes.add(o.toString());
            }
        }

        return codes;
    }

    public static ArrayList<ListableGroupRequest> getGroupRequests(String userCode) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        ArrayList<ListableGroupRequest> requests = new ArrayList<>();

        PreparedStatement getPrivateGroups = con.prepareStatement("SELECT requests, code, name FROM groups WHERE administrator = ? AND is_private = 1");
        getPrivateGroups.setString(1, userCode);
        ResultSet rs = getPrivateGroups.executeQuery();
        while(rs.next())
        {
            Array array = rs.getArray("requests");
            if(array != null)
            {
                Object[] a = (Object[]) array.getArray();
                for (Object o : a)
                    requests.add(new ListableGroupRequest((String) o, rs.getString("code"), rs.getString("name")));
            }
        }

        return requests;
    }

    public static Group loadGroup(String code) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        PreparedStatement load = con.prepareStatement("SELECT name, is_private, code, administrator, members FROM groups WHERE code = ?");
        load.setString(1, code);
        ResultSet rs = load.executeQuery();
        Group group;
        if(rs.next())
        {
            String name = rs.getString("name");
            boolean is_private = rs.getBoolean("is_private");
            User administrator = Database.loadUser(rs.getString("administrator"));

            ArrayList<String> members = null;
            if(rs.getArray("members") != null)
            {
                members = new ArrayList<>();
                Object[] a = (Object[]) rs.getArray("members").getArray();
                for(Object o : a)
                    members.add(o.toString());
            }

            group = new Group(code, name, is_private, administrator, members);
        }
        else
            throw new GroupNotFoundException();

        return group;
    }

    public static void createGroup(String groupName, boolean is_private, String adminCode) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        PreparedStatement ps = con.prepareStatement("INSERT INTO groups(name, is_private, administrator) VALUES (?, ?, ?)");
        ps.setString(1, groupName);
        ps.setBoolean(2, is_private);
        ps.setString(3, adminCode);
        ps.executeUpdate();

        Main.getCurrentUser().addToGroup(Database. getGroupCode(groupName));
    }

    public static String getGroupCode(String groupName) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        PreparedStatement ps = con.prepareStatement("SELECT code FROM groups WHERE name = ?");
        ps.setString(1, groupName);
        ResultSet rs = ps.executeQuery();

        if(rs.next())
            return rs.getString("code");
        return null;
    }

    public static void addToGroup(String userCode, String groupCode) throws NoConnectionException, SQLException
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        PreparedStatement ps;
        ResultSet rs;
        ps = con.prepareStatement("SELECT groups FROM users WHERE code = ?");
        ps.setString(1, userCode);
        rs = ps.executeQuery();
        ArrayList<String> groups = new ArrayList<>();
        if(rs.next())
        {
            if (rs.getArray("groups") != null)
            {
                Object[] a = (Object[]) rs.getArray("groups").getArray();
                for (Object o : a)
                {
                    if (!groups.contains(o.toString()))
                        groups.add(o.toString());
                }
            }
            if (!groups.contains(groupCode))
                groups.add(groupCode);

            Object[] gr = groups.toArray();
            Array groupsArray = con.createArrayOf("varchar", gr);
            ps = con.prepareStatement("UPDATE users SET groups = ? where code = ?");
            ps.setArray(1, groupsArray);
            ps.setString(2, userCode);
            ps.executeUpdate();
        }

        ps = con.prepareStatement("SELECT members FROM groups WHERE code = ?");
        ps.setString(1, groupCode);
        rs = ps.executeQuery();
        ArrayList<String> members = new ArrayList<>();
        if(rs.next())
        {
            if (rs.getArray("members") != null)
            {
                Object[] a = (Object[]) rs.getArray("members").getArray();
                for (Object o : a)
                    members.add(o.toString());
            }
            members.add(userCode);

            Object[] mem = members.toArray();
            Array membersArray = con.createArrayOf("varchar(10)", mem);
            ps = con.prepareStatement("UPDATE groups SET members = ? WHERE code = ?");
            ps.setArray(1, membersArray);
            ps.setString(2, groupCode);
            ps.executeUpdate();
        }
    }

    public static ArrayList<String> getAllGroups() throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        ArrayList<String> groups = new ArrayList<>();

        PreparedStatement search = con.prepareStatement("SELECT code FROM groups");
        ResultSet rs = search.executeQuery();

        while(rs.next())
        {
            String code = rs.getString("code");
            groups.add(code);
        }

        return groups;
    }

    public static ArrayList<String> searchGroupByName(String name) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        ArrayList<String> found = new ArrayList<>();

        PreparedStatement search = con.prepareStatement("SELECT name, code FROM groups");
        ResultSet rs = search.executeQuery();

        while(rs.next())
        {
            String groupName = rs.getString("name");
            String groupCode = rs.getString("code");

            if(groupName.toLowerCase().contains(name.toLowerCase()))
                found.add(groupCode);
        }

        return found;
    }

    public static ArrayList<Integer> getPostsFromGroup(String s) throws NoConnectionException, SQLException
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        ArrayList<Integer> postIDs = new ArrayList<>();

        PreparedStatement search = con.prepareStatement("SELECT post_id FROM posts WHERE group_code = ? ORDER BY post_id DESC");
        search.setString(1, s);
        ResultSet rs = search.executeQuery();

        while(rs.next())
        {
            int id = rs.getInt("post_id");
            postIDs.add(id);
        }

        return postIDs;
    }

    public static Post loadPost(int post_id) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        Post p = new Post();

        PreparedStatement load = con.prepareStatement("SELECT user_code, post_date, content FROM posts WHERE post_id = ?");
        load.setInt(1, post_id);
        ResultSet rs = load.executeQuery();

        if(rs.next())
        {
            String uc = rs.getString("user_code");
            Date id = rs.getDate("post_date");
            String cont = rs.getString("content");

            p.setPosterName(Database.loadUser(uc).getFirstName() + " " + Database.loadUser(uc).getLastName());
            p.setDate(id);
            p.setMessage(cont);
        }

        return p;
    }

    public static ArrayList<String> getUsersFromGroup(String groupCode) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        return Database.loadGroup(groupCode).getMembers();
    }

    public static void addPost(String message, String userCode, String groupCode) throws NoConnectionException, SQLException
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        java.util.Date utilDate = new java.util.Date();
        PreparedStatement add = con.prepareStatement("INSERT INTO posts(user_code, content, post_date, group_code) VALUES (?, ?, ?, ?)");
        add.setString(1, userCode);
        add.setString(2, message);
        add.setDate(3, new Date(utilDate.getTime()));
        add.setString(4, groupCode);

        add.executeUpdate();
    }

    public static boolean hasMember(String groupCode, String userCode) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        Group loadedGroup = loadGroup(groupCode);
        return loadedGroup.getMembers().contains(userCode);
    }

    public static void sendGroupRequest(String groupCode, String userCode) throws Exception
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        ArrayList<String> requests = new ArrayList<>();

        PreparedStatement getRequest = con.prepareStatement("SELECT requests FROM GROUPS WHERE code = ?");
        getRequest.setString(1, groupCode);
        ResultSet rs = getRequest.executeQuery();

        if(rs.next())
        {
            Array array = rs.getArray("requests");
            if(array != null)
            {
                Object[] a = (Object[]) array.getArray();
                for (Object o : a)
                    requests.add(o.toString());
            }
            requests.add(userCode);
        }

        Object[] updatedRequests = requests.toArray();
        Array requestsArray = con.createArrayOf("varchar(10)", updatedRequests);

        PreparedStatement updateRequests = con.prepareStatement("UPDATE groups SET requests = ? WHERE code = ?");
        updateRequests.setArray(1, requestsArray);
        updateRequests.setString(2, groupCode);
        updateRequests.executeUpdate();
    }

    public static void deleteGroupRequest(String userCode, String groupCode) throws NoConnectionException, SQLException
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();

        ArrayList<String> requests = new ArrayList<>();

        PreparedStatement getRequest = con.prepareStatement("SELECT requests FROM GROUPS WHERE code = ?");
        getRequest.setString(1, groupCode);
        ResultSet rs = getRequest.executeQuery();

        if(rs.next())
        {
            Array array = rs.getArray("requests");
            if(array != null)
            {
                Object[] a = (Object[]) array.getArray();
                for (Object o : a)
                    requests.add(o.toString());
            }
            requests.remove((userCode));
        }

        if(requests.isEmpty())
        {
            PreparedStatement p = con.prepareStatement("UPDATE groups SET requests = null WHERE code = ?");
            p.setString(1, groupCode);
            p.executeUpdate();
        }
        else
        {
            Object[] updatedRequests = requests.toArray();
            Array requestsArray = con.createArrayOf("varchar(10)", updatedRequests);

            PreparedStatement updateRequests = con.prepareStatement("UPDATE groups SET requests = ? WHERE code = ?");
            updateRequests.setArray(1, requestsArray);
            updateRequests.setString(2, groupCode);
            updateRequests.executeUpdate();
        }
    }

    public static void changeProfilePhoto(String userCode, String fileContent) throws NoConnectionException, SQLException
    {
        Connection con = getConnection();
        if(con == null)
            throw new NoConnectionException();


        PreparedStatement findUserCode = con.prepareStatement("SELECT * FROM profile_photos WHERE user_id = ?");
        findUserCode.setString(1, userCode);
        ResultSet find = findUserCode.executeQuery();
        if(find.next())
        {
            PreparedStatement editPhoto = con.prepareStatement("UPDATE profile_photos SET profile_photo = ? WHERE user_id = ?");
            editPhoto.setString(1, fileContent);
            editPhoto.setString(2, userCode);
            editPhoto.executeUpdate();
        }
        else
        {
            PreparedStatement insertPhoto = con.prepareStatement("INSERT INTO profile_photos(user_id, profile_photo) VALUES (?, ?)");
            insertPhoto.setString(1, userCode);
            insertPhoto.setString(2, fileContent);
            insertPhoto.executeUpdate();
        }
    }

}
