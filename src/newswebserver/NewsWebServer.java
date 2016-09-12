/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newswebserver;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class NewsWebServer
{
    public static NewsDatabase ndb = new NewsDatabase();
    public static Database db = new Database();
    public static void main(String[] args)
    {
        try
        {
            Database db = new Database();
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new DefaultHandler());
            server.createContext("/homecontent", new HomeHandler());
            server.createContext("/registercontent", new RegisterHandler());
            server.createContext("/allnewscontent", new AllNewsHandler());
            server.createContext("/signincontent", new SignInRequestHandler());
            server.createContext("/RegisterRequest", new RegisterRequestHandler());
            server.createContext("/LoginRequest", new LoginRequestHandler());
            server.createContext("/CookieLoginRequest", new CookieLoginRequestHandler());
            server.createContext("/userpagecontent", new UserPageHandler());
            server.createContext("/content/AllNews", new AllNewsRequestHandler());
            server.createContext("/content/MyNews", new MyNewsRequestHandler());
            server.setExecutor(Executors.newFixedThreadPool(10)); // creates a default executor
            server.start();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    static class DefaultHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                HTMLView testView = new HTMLView("content/default.html");
                String response = testView.getHTMLString();
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static class MyNewsRequestHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
                BufferedReader br = new BufferedReader(isr);

                int b;
                StringBuilder buf = new StringBuilder(512);
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }

                br.close();
                isr.close();
                
                String buffer = buf.toString();
                
                String[] splitParams = buffer.split("&");
                Map<String, String> params = new HashMap<String, String>();
                for(String param : splitParams)
                {
                    String[] keyValue = param.split("=");
                    params.put(keyValue[0], keyValue[1]);
                }
                
                String username = params.get("username");
                String password = params.get("password");
                
                String added = "User already exists";
                
                User curUser = db.FindUserByName(username);
                if(curUser != null)
                {
                    System.out.println("AllNewsRequest");
                    String ninjs_response = "{\"ninjs\" : [";

                    boolean addCommaOuter = false;
                    for(String category : curUser.m_Subscriptions)
                    {
                        boolean addComma = false;
                        ArrayList<String> newsItemsByCategory = ndb.getNewsByCategory(category, 10);
                        for(String ninjs_news_item : newsItemsByCategory)
                        {
                            if(addComma == true || (newsItemsByCategory.size() >= 1 && addCommaOuter)) { ninjs_response += ","; }
                            ninjs_response += "{" + ninjs_news_item + "}";
                            addComma = true;
                        }
                        addCommaOuter = true;
                        
                    }
                    ninjs_response += "]}";

                    System.out.println(ninjs_response);

                    String response = ninjs_response;
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
                else
                {
                    String response = "User not found";
                    t.sendResponseHeaders(404, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static class AllNewsRequestHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                System.out.println("AllNewsRequest");
                String ninjs_response = "{\"ninjs\" : [";
                boolean addComma = false;
                for(String ninjs_news_item : ndb.getTopTenNews())
                {
                    if(addComma == true) { ninjs_response += ","; }
                    ninjs_response += "{" + ninjs_news_item + "}";
                    addComma = true;
                }
                ninjs_response += "]}";
                
                System.out.println(ninjs_response);
                
                String response = ninjs_response;
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static class HomeHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                HTMLView testView = new HTMLView("content/homecontent.html");
                String response = testView.getHTMLString();
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static class AllNewsHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                HTMLView testView = new HTMLView("content/allnewscontent.html");
                String response = testView.getHTMLString();
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static class RegisterHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                HTMLView testView = new HTMLView("content/registercontent.html");
                String response = testView.getHTMLString();
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static class SignInRequestHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                HTMLView testView = new HTMLView("content/signincontent.html");
                String response = testView.getHTMLString();
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static class RegisterRequestHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
                BufferedReader br = new BufferedReader(isr);

                int b;
                StringBuilder buf = new StringBuilder(512);
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }

                br.close();
                isr.close();
                
                String buffer = buf.toString();
                
                String[] splitParams = buffer.split("&");
                Map<String, String> params = new HashMap<String, String>();
                for(String param : splitParams)
                {
                    String[] keyValue = param.split("=");
                    params.put(keyValue[0], keyValue[1]);
                }
                
                String username = params.get("username");
                String password = params.get("password");
                
                String added = "User already exists";
                
                if(db.FindUserByName(username) == null)
                {
                    User registerUser = new User(username, password);
                    db.AddUser(registerUser);
                    
                    added = "User added";
                    System.out.println(username + " registered");
                    
                }
                
                HTMLView testView = new HTMLView("content/registercontent.html");
                String response = added;
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static class LoginRequestHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
                BufferedReader br = new BufferedReader(isr);

                int b;
                StringBuilder buf = new StringBuilder(512);
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }

                br.close();
                isr.close();
                
                String buffer = buf.toString();
                
                String[] splitParams = buffer.split("&");
                Map<String, String> params = new HashMap<String, String>();
                for(String param : splitParams)
                {
                    String[] keyValue = param.split("=");
                    params.put(keyValue[0], keyValue[1]);
                }
                
                String username = params.get("username");
                String password = params.get("password");
                
                User user = db.FindUserByName(username);
                if(user != null && user.m_Password.equals(password))
                {
                    user.m_CurrentSession.generateSession();
                    HTMLView testView = new HTMLView("content/registercontent.html");
                    String response = ("{\"login\": true, \"username\": \"" + user.m_Username 
                                       + "\", \"password\": \"" + user.m_Password + "\","
                                       + "\"session\": " + user.m_CurrentSession.getSessionDataAsJSONString() + "}"); 
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    System.out.println(username + " logged in");
                }
                else
                {
                    HTMLView testView = new HTMLView("content/registercontent.html");
                    String response = ("{\"login\": false, \"username\": \"" + username + "\"}"); 
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();   
                    System.out.println(username + " failed to log in (Username/password incorrect)");
                }
                
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static class CookieLoginRequestHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
                BufferedReader br = new BufferedReader(isr);

                int b;
                StringBuilder buf = new StringBuilder(512);
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }

                br.close();
                isr.close();
                
                String buffer = buf.toString();
                
                String[] splitParams = buffer.split("&");
                Map<String, String> params = new HashMap<String, String>();
                for(String param : splitParams)
                {
                    String[] keyValue = param.split("=");
                    params.put(keyValue[0], keyValue[1]);
                }
                
                String sessionid = params.get("cookie");
                
                User user = db.FindBySessionID(sessionid);
                if(user != null)
                {
                    System.out.println("User("+user.m_Username+") found by sessionID: " + String.valueOf(user.m_CurrentSession.getSessionID()));
                    System.out.println(user.m_CurrentSession.toString());
                }
                
                if(user != null && user.m_CurrentSession.getExpiryDate().isAfter(LocalDate.now()))
                {
                    HTMLView testView = new HTMLView("content/registercontent.html");
                    String response = ("{\"login\": true, \"username\": \"" + user.m_Username 
                                       + "\", \"password\": \"" + user.m_Password + "\","
                                       + "\"session\": " + user.m_CurrentSession.getSessionDataAsJSONString() + "}"); 
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    System.out.println(user.m_Username + " logged in via cookie");
                }
                else
                {
                    HTMLView testView = new HTMLView("content/registercontent.html");
                    String response = ("{\"login\": false, \"sessionid\": \"" + sessionid + "\"}"); 
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();   
                    System.out.println(sessionid + " failed to log in (Session(" + sessionid + ") expired or not found)");
                }
                
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static class UserPageHandler implements HttpHandler 
    {
        public void handle(HttpExchange t) throws IOException 
        {
            try
            {
                HTMLView testView = new HTMLView("content/userpagecontent.html");
                String response = testView.getHTMLString();
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
