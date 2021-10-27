import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleToIntFunction;

import static java.rmi.server.LogStream.log;

public class Scraper {

    LinkedList<String> pages = new LinkedList<String>();
    LinkedList<String> links = new LinkedList<String>();
    private String seedUrl = "https://en.wikipedia.org/wiki/United_States"; //let's use this as the default in case one isn't provided
    private int errors = 0;
    private int pageWaitTimeMin = 2;
    private int pageWatTimeMax = 35;
    private List<String> oldUrls = new ArrayList<>();

    public Scraper() {

    }

    public String getSeedUrl() {
        return this.seedUrl;
    }

    public void setSeedUrl(String seedUrl) {
        this.seedUrl = seedUrl;

    }

    public LinkedList<String> getPages() {
        return pages;
    }

    public void scrape() {
        Document doc = null;
        links.add(seedUrl);
            int count = 0;
            for (int i =0; i < links.size(); i++) {
                //Get the page and the html
                //This needs refactoring, will split into separate methods for each of these segments to keep things clean
                try {
                    double wait = Math.random() * (pageWatTimeMax - pageWaitTimeMin + 1) + pageWaitTimeMin;
                    //delay the scrape randomly to avoid being ip blocked
                    System.out.println(String.format("sleep for %03d seconds", (int) wait));
                    //convert wait to integer for TimeUnit
                    TimeUnit.SECONDS.sleep((int) wait);
                    //get the html from the page
                    String listElement = "";
                    listElement = links.element();
                    //remove the link that we are going to grab all the html, links from and add it to our old url arraylist
                    oldUrls.add(links.pop());
                    System.out.println("Current element: " + listElement);
                    doc = Jsoup.connect(listElement).get();
                    Elements elements = doc.getElementsByTag("a");
                    int sublinkCounter = 0;
                    for (Element element : elements) {
                        //System.out.println(sublinkCounter);
                        String absoluteUrl = element.attr("abs:href");
                        if ((absoluteUrl.contains("en.wikipedia.org")) && (sublinkCounter < 25)) {
                            //only process non-duplicates
                            if (!oldUrls.contains(absoluteUrl)){
                                sublinkCounter++;
                                links.add(absoluteUrl);
                            }
                        }
                    }
                    System.out.println("number of links: " + links.size());
                    System.out.println("Current links: " + links.toString());
                    boolean result = store(listElement, elements);
                } catch (InterruptedException | IOException e) {
                    //this will catch an exception with the page or with our sleep timer
                    e.printStackTrace();
                    doc = null;
                    Elements elements = null;
                }
            }
    }
    // This method will take our current page and write its contents to a file using our URL formatter
    public boolean store(String listElement, Elements elements){
        try{
            UrlFormatter formatter = new UrlFormatter();
            listElement = formatter.format(listElement);
            File file = new File("../Scraper/Pages/" + listElement);
            FileWriter myWriter = new FileWriter("../Scraper/Pages/" + listElement);// + seedUrl);
            myWriter.write(elements.toString());
            myWriter.close();
            return true;
        } catch (IOException f){
            //log the failure
            System.out.println("File save failed for " + listElement);
            return false;
        }
    }

}
