package wikisummary;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
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

    public static void main(String[] args) throws URISyntaxException
    {
       
        String topic = "";
        if (args.length > 0)
        {
            topic = args[0];
            for (int i = 1; i < args.length; i++)
            {
                topic = topic + "_" + args[i];
            }
        }
        else
        {
            while(topic.trim().equals("")){
            Scanner input = new Scanner(System.in, "UTF-8");
            System.out.println("Please enter a topic:");
            topic = input.nextLine();
            }
            
            topic = topic.trim().replaceAll(" ", "_");
        }
        
        String path;
        try
        {
            path = "http://en.wikipedia.org/wiki/" + URLEncoder.encode(topic, "UTF-8");
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(WikiSummary.class.getName()).log(Level.SEVERE, null, ex);
        }
        //URL uri = new URL();
        Document html = null;
        try
        {
            html = Jsoup.connect(path).userAgent("Mozilla").get();
        } catch (IOException e)
        {
            System.err.println("Unable to reach page\nIf using international characters, please call from command line\nEx. java -jar WikiSummary.jar [Topic]");
            System.exit(0);
        }
        

        Element intro = html.select("div#mw-content-text > p").first();
        if (intro.text().contains("may refer to"))
        {
            System.err.println("Reached disambiguation. Try to be more specific.");
        }
        else
        System.out.println(intro.text());

    }
    static String encodeUrl(String urlToEncode) throws URISyntaxException {
        return new URI(urlToEncode).toASCIIString();
    }
}
