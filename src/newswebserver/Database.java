package newswebserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.*;
import java.time.*;

class Session
{
    private final ReentrantLock lock = new ReentrantLock();
    
    private static long TotalSessions = 0;
    private long m_SessionID;
    private LocalDate m_ExpiryDate;
    
    private Session() { }
    
    public long getSessionID()
    {
        return this.m_SessionID;
    }
    
    public LocalDate getExpiryDate()
    {
        return this.m_ExpiryDate;
    }
    
    public String getSessionDataAsJSONString()
    {
        return "{" + "\"sessionID\": \"" + String.valueOf(this.getSessionID()) + "\"}";
    }
    
    public synchronized void setSessionID()
    {
        lock.lock();
        try
        {
            this.m_SessionID = TotalSessions;
            TotalSessions++;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void setDate()
    {
        try
        {
            this.m_ExpiryDate = LocalDate.now();
            this.m_ExpiryDate = this.m_ExpiryDate.plusDays(2);
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    public void generateSession()
    {
        lock.lock();
        try
        {
            this.m_SessionID = TotalSessions;
            TotalSessions++;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public static Session getNewSession()
    {
        try
        {
            Session newSession = new Session();
            newSession.setSessionID();
            newSession.setDate();
            
            return newSession;
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    @Override
    public String toString()
    {
        return "{ \"session\": {" + "\"sessionID\": \"" + String.valueOf(this.m_SessionID) + "\","
                + "\"expieryDate\": \"" + String.valueOf(this.m_ExpiryDate.toString()) + "\"}}";
    }
}

class User
{
    public String m_Username;
    public String m_Password;
    public ArrayList<String> m_Subscriptions;
    public Session m_CurrentSession;
    
    public User(String user, String pass)
    {
        this.m_Username = user;
        this.m_Password = pass;
        
        this.m_CurrentSession = Session.getNewSession();
    }
}

class NewsItem
{
    public String m_URI;
    public String m_Type;
    
    public LocalDate m_VersionCreatedDate;
    public LocalTime m_VersionCreatedTime;
    
    public String m_HeadlineText;
    public String m_BylineText;
    public String m_BodyText;
    
    public String m_Person;
    public String m_Organisation;
}


public class Database
{
    private final ReentrantLock lock = new ReentrantLock();
    public static ArrayList<User> m_UserList = new ArrayList<User>();
    
    public Database()
    {
        try
        {
            lock.lock();
            User user1 = new User("Owen", "Test");
        
                user1.m_Subscriptions = new ArrayList<String>();
                user1.m_Subscriptions.add("Politics");
                user1.m_Subscriptions.add("Technology");
                user1.m_Subscriptions.add("Games");     

            m_UserList.add(user1);
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public User FindUserByName(String input_user)
    {
        try
        {
            lock.lock();
            for(User user : this.m_UserList)
            {
               if(user.m_Username.equals(input_user))
               {
                    return user;
               }
            }
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            lock.unlock();
        }
        return null;
    }
    
    public User FindBySessionID(String sessionid)
    {
        try
        {
            lock.lock();
            for(User user : this.m_UserList)
            {
                if(String.valueOf(user.m_CurrentSession.getSessionID()).equals(sessionid))
                {
                    return user;
                }
            }
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            lock.unlock();
        }
        return null;
    }
    
    public boolean AddUser(User new_user)
    {
        try
        {
            lock.lock();
            if(FindUserByName(new_user.m_Username) == null)
            {
                m_UserList.add(new_user);
                return true;
            }
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            lock.unlock();
        }
        return false;
    }
}
