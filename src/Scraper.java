import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleToIntFunction;

import static java.rmi.server.LogStream.log;

public class Scraper {

    private String seedUrl = "https://cnn.com/"; //let's use this as the default in case one isn't provided
    LinkedList<String> pages = new LinkedList<String>();
    private int errors = 0;
    private int pageWaitTimeMin = 6;
    private int pageWatTimeMax = 240;

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
            double wait = Math.random() * (pageWatTimeMax - pageWaitTimeMin + 1) + pageWaitTimeMin;
            // delay the scrape randomly to avoid being ip blocked
            System.out.println(String.format("value is %03d",(int)wait));
            //convert wait to integer for TimeUnit
            TimeUnit.SECONDS.sleep((int)wait);
            doc = Jsoup.connect(this.seedUrl).get();
        } catch (IOException | InterruptedException e) {
            //this will catch an exception with the page or with our sleep timer
            e.printStackTrace();
        }
        System.out.println(doc.title());
        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            System.out.println(headline.attr("title"));
        }
    }

}
