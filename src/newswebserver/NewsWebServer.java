/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newswebserver;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import java.util.concurrent.Executors;

public class NewsWebServer
{
    public static void main(String[] args)
    {
        try
        {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new DefaultHandler());
            server.createContext("/home", new HomeHandler());
            server.createContext("/registercontent", new RegisterHandler());
            server.createContext("/allnews", new AllNewsHandler());
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
    
}
