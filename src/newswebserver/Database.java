package newswebserver;

import java.util.ArrayList;

class User
{
    public String m_Username;
    public String m_Password;
    public ArrayList<String> m_Subscriptions;
}

public class Database
{
    public static ArrayList<User> m_UserList = new ArrayList<User>();
    
    public Database()
    {
        User user1 = new User();
            user1.m_Username = "Owen";
            user1.m_Password = "Test";
            user1.m_Subscriptions = new ArrayList<String>();
            user1.m_Subscriptions.add("Politics");
            user1.m_Subscriptions.add("Technology");
            user1.m_Subscriptions.add("Games");     
        
        m_UserList.add(user1);
    }
    
    public User FindUserByName(String input_user)
    {
        for(User user : this.m_UserList)
        {
           if(user.m_Username.equals(input_user))
           {
                return user;
           }
        }
        return null;
    }
    
    public boolean AddUser(User new_user)
    {
        if(FindUserByName(new_user.m_Username) == null)
        {
            m_UserList.add(new_user);
            return true;
        }
        return false;
    }
}
