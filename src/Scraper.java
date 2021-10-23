import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;

import static java.rmi.server.LogStream.log;

public class Scraper {

    private String seedUrl = "https://en.wikipedia.org/"; //let's use this as the default in case one isn't provided
    LinkedList<String> pages = new LinkedList<String>();
    private int errors = 0;

    public Scraper (){

    }
    public String getSeedUrl(){
        return this.seedUrl;
    }
    public void setSeedUrl(String seedUrl){
        this.seedUrl = seedUrl;

    }

    public LinkedList<String> getPages(){
        return pages;
    }

    public void scrape(){
        Document doc = null;
        try {
            doc = Jsoup.connect(this.seedUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(doc.title());
        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            System.out.println(headline.attr("title"));
        }
    }

}
