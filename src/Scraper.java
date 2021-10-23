import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleToIntFunction;

import static java.rmi.server.LogStream.log;

public class Scraper {

    LinkedList<String> pages = new LinkedList<String>();
    LinkedList<String> links = new LinkedList<String>();
    private String seedUrl = "https://en.wikipedia.org/wiki/Napoleon"; //let's use this as the default in case one isn't provided
    private int errors = 0;
    private int pageWaitTimeMin = 2;
    private int pageWatTimeMax = 35;

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

        //System.out.println(doc.toString());

        links.add(seedUrl);
       // while (links.size() <= 4) {
            //for (String listElement : links)
            int count = 0;
            for (int i =0; i < links.size(); i++){
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
                    links.pop();
                    System.out.println("Current element: " + listElement);
                    doc = Jsoup.connect(listElement).get();
                    Elements elements = doc.getElementsByTag("a");
                    int sublinkCounter = 0;
                    for (Element element : elements) {
                        //System.out.println(sublinkCounter);
                        String absoluteUrl = element.attr("abs:href");
                        if ((absoluteUrl.contains("en.wikipedia.org")) && (sublinkCounter < 10)) {
                            sublinkCounter++;
                            links.add(absoluteUrl);
                        }
                    }
                    System.out.println("number of links: " + links.size());
                    //create the file (if it doesnt already exist) and write the contents
                    UrlFormatter formatter = new UrlFormatter();
                    listElement = formatter.format(listElement);
                    File file = new File("../Scraper/Pages/" + listElement);
                    FileWriter myWriter = new FileWriter("../Scraper/Pages/" + listElement);// + seedUrl);
                    myWriter.write(elements.toString());
                    myWriter.close();
                    //get all the subUrls form current url and them to the list

                } catch (IOException | InterruptedException e) {
                    //this will catch an exception with the page or with our sleep timer
                    e.printStackTrace();
                    doc = null;
                    Elements elements = null;
                }

            }
        //}
    }

}
