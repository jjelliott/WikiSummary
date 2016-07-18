package wikisummary;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 *
 */
public class WikiSummary
{
    private WikiSummary(){
        System.err.println("Not an object class, constructor should not be accessed!");
    }
    public static void main(String[] args)
    {
        //inital setup
        String encoding = System.getProperty("file.encoding");
        String topic = "";
        //read from arguments
        if (args.length > 0)
        {
            topic = args[0];
            for (int i = 1; i < args.length; i++)
            {
                topic = topic + "_" + args[i];
            }
        }
        else //or read from console
        {
            while (topic.trim().equals(""))
            {
                Scanner input = new Scanner(System.in, encoding);
                System.out.println("Please enter a topic:");
                topic = input.nextLine();
            }
            
        topic = topic.trim().replaceAll(" ", "_");
        }
        //call method
        System.out.println(getWikiSummary(topic));

    }
    public static String getWikiSummary(String topic)
    {
        //create path from user input
        String path = "";
        try
        {
            path = "http://en.wikipedia.org/wiki/" + URLEncoder.encode(topic, "UTF-8");
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(WikiSummary.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Get page using Jsoup
        Document html = null;
        try
        {
            html = Jsoup.connect(path).userAgent("Mozilla").get();
        } catch (IOException e)
        {
            return "Unable to reach page\nIf using international characters on Windows, please call from command line\nEx. java -jar WikiSummary.jar [Topic]";
        }
        //Get first paragraph
        Element intro = html.select("div#mw-content-text > p").first();
        //Display paragraph or error if disambiguation reached
        if (intro.text().contains("may refer to"))
        {
            return "Reached disambiguation. Try to be more specific.";
        }
        else
        {
            return intro.text();
        }
    }

}
