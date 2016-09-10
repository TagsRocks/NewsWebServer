package newswebserver;

import java.nio.file.Files;
import java.nio.file.Paths;


public class HTMLView
{
    private String m_HTMLString = "";
    
    
    public HTMLView(String path) throws Exception
    {
        try
        {
            this.m_HTMLString = new String(Files.readAllBytes(Paths.get(path)));
        }
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
    
    public String getHTMLString()
    {
        return this.m_HTMLString;
    }
}
