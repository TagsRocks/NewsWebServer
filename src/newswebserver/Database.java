package newswebserver;

import java.util.ArrayList;
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
        
        this.m_Subscriptions = new ArrayList<String>();
        
        this.m_CurrentSession = Session.getNewSession();
    }
    
    public void subscribeTo(String category)
    {
        this.m_Subscriptions.add(category);
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
    public String m_Category;
    
    public NewsItem(String uri, String headline, String byline, String body, String person, String org, String cat)
    {
        this.m_VersionCreatedDate = LocalDate.now();
        this.m_VersionCreatedTime = LocalTime.now();
        
        this.m_Type = "text";
        this.m_URI = "/content/news/" + uri;
        
        this.m_HeadlineText = headline;
        this.m_BylineText = byline;
        this.m_BodyText = body;
        this.m_Person = person;
        this.m_Organisation = org;
        this.m_Category = cat;
    }
    
    public String getItemAsJSONString()
    {
        return "\"ninjs_item\" : { \"uri\" : \"" + "31.185.147.189:8000/content/news" + this.m_URI + "\","
                + "\"type\" : \"" + this.m_Type + "\","
                + "\"versioncreated\" : \"" + this.m_VersionCreatedDate.toString() + " + " + this.m_VersionCreatedTime.toString() + "\","
                + "\"byline\" : \"" + this.m_BylineText + "\","
                + "\"headline\" : \"" + this.m_HeadlineText + "\","
                + "\"person\" : \"" + this.m_Person + "\","
                + "\"organisation\" : \"" + this.m_Organisation + "\","
                + "\"category\" : \"" + this.m_Category + "\","
                + "\"body_text\" : \"" + this.m_BodyText + "\"}";
    }
}

class NewsDatabase
{
    private final ReentrantLock lock = new ReentrantLock();
    public static ArrayList<NewsItem> m_NewsItemList = new ArrayList<NewsItem>();
    
    public NewsDatabase()
    {
        this.addNewsItem("0", "Cat lost in bath", "Soxville cat owner bewildered as cat lost in bath",
                    "A 40-year-old mother of two cat owner in soxville has been left distraught"
                    + "after her 6 year old cat disappeared during what was a routine bath time."
                    + "<br><br>But where could they have gone? Nobody knows.", 
                    "John Anderson", "Cat Times", "Local");
         
        this.addNewsItem("1", "PM Theresa May bans internet", "From October 1st 2016, the UK's Internet will go dark.",
                    "As Theresa May dismantles the Human Rights Act, she lays out a series of actions to help further combat terrorism."
                    + "<br><br>''These are dangerous circumstances, and unpredictables lives we live in,'' She explains,"
                    + "<br><br>''Never has the UK faced such a level of threat from terrorist organisations, and to combat it we must"
                    + " ensure that no communications data can travel in or around the United Kingdom.''"
                    + "<br><br>''Never again will hard working British people feel opressed by ISIS's digital rampage.''"
                    + "<br><br>We tried to invite some human rights activists to comment, but they have all mysteriously vanished.",
                    "Umesh Portovsky", "RT", "Politics");
    }
    
    public boolean addNewsItem(String uri, String headline, String byline, String body, String person, String org, String cat)
    {
        try
        {
            lock.lock();
            m_NewsItemList.add(new NewsItem(uri, headline, byline, body, person, org, cat));
            return true;
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
    
    public ArrayList<String> getTopTenNews()
    {
        try
        {
            System.out.println("getTopTenNews();");
            ArrayList<String> topTen = new ArrayList<String>();
            int totalNews = 0;
            
            lock.lock();
            for(int i = m_NewsItemList.size()-1; i >= 0; i--)
            {
                topTen.add(m_NewsItemList.get(i).getItemAsJSONString());
                totalNews++;
                if(totalNews >= 10) { break; }
            }
            return topTen;
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
    
    public ArrayList<String> getNewsByCategory(String category, int num)
    {
        try
        {
            ArrayList<String> fromCategory = new ArrayList<String>();
            int totalNews = 0;
            
            lock.lock();
            for(NewsItem news : this.m_NewsItemList)
            {
                if(news.m_Category.equals(category))
                {
                    fromCategory.add(news.getItemAsJSONString());
                    totalNews++;
                }
                if(totalNews >= num) { break; }
            }
            return fromCategory;
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
            user1.subscribeTo("Politics");
            user1.subscribeTo("Technology");
            user1.subscribeTo("Games");     

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
