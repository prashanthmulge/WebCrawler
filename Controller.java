import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	public static void main(String[] args) throws Exception {
		 String crawlStorageFolder = "C:\\Users\\mulge\\Documents\\Study\\2nd-Year\\3rd Sem\\IR\\Assignment\\HW2\\Data";
		 int numberOfCrawlers = 7;
		 CrawlConfig config = new CrawlConfig();
		 config.setCrawlStorageFolder(crawlStorageFolder);
		 config.setMaxPagesToFetch(20000);
		 config.setMaxDepthOfCrawling(16);
		 config.setIncludeBinaryContentInCrawling(true);
		 /*
		 * Instantiate the controller for this crawl.
		 */
		 PageFetcher pageFetcher = new PageFetcher(config);
		 RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		 RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		 CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		 /*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		 controller.addSeed("https://www.wsj.com/");
		
		 /*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		 controller.start(MyCrawler.class, numberOfCrawlers);
		 
		 BufferedReader urlPW;
		 BufferedReader fetchPW;
		 BufferedReader visitPW;
		 try {
			 urlPW =  new BufferedReader(new FileReader(new File("urls_wall_street_journal.csv")));
			 fetchPW = new BufferedReader(new FileReader(new File("fetch_wall_street_journal.csv")));
			 visitPW =  new BufferedReader(new FileReader(new File("visit_wall_street_journal.csv")));
			 
			 String line;
			 int noFetchesAttempted = 0;
			 int noAbortedFailed = 0;
			 List<Integer> list = new ArrayList<Integer>(); 
			 while((line = fetchPW.readLine()) != null) {
				 if (Integer.parseInt(line.split(",")[1]) >= 300) {
					 noAbortedFailed++;
				 }
				 list.add(Integer.parseInt(line.split(",")[1]));	 
				 noFetchesAttempted++;
			 }
			 
			 int noFetchesSucceeded = 0;
			 int totalURL = 0;
			 int one = 0;
			 int two = 0;
			 int three = 0;
			 int four = 0;
			 int five = 0;
			 
			 int html = 0;
			 int gif = 0;
			 int jpeg = 0;
			 int png = 0;
			 int pdf = 0;
			 int svg = 0;
			 
			 while((line = visitPW.readLine()) != null) {
				 totalURL += Integer.parseInt(line.split(",")[2]);
				 int size = Integer.parseInt(line.split(",")[1].replaceAll("bytes", ""));
				 if (size < 1024) {
					 one++;
				 }
				 if (size >= 1024 && size < 10240)
					 two++;
				 if (size >= 10240 && size < 102400)
					 three++;
				 if (size >= 102400 && size < 1048576)
					 four++;
				 if (size >= 1048576)
					 five++;
				 
				 String content = line.split(",")[3];
				 if (content.contains("html"))
					 html++;
				 if (content.contains("gif"))
					 gif++;
				 if (content.contains("jpeg"))
					 jpeg++;
				 if (content.contains("png"))
					 png++;
				 if (content.contains("pdf"))
					 pdf++;
				 if (content.contains("svg+xml"))
					 svg++;
				 noFetchesSucceeded++;
			 }
			 
			 
			 
			 Set<String> uniqueURL = new HashSet<String>();
			 Set<String> uniqueWithinURL = new HashSet<String>();
			 Set<String> uniqueOutsideURL = new HashSet<String>();
			 while((line = urlPW.readLine()) != null) {
				 String url = line.split(",")[0];
				 String status = line.split(",")[1];
				 uniqueURL.add(url);
				 if (status.equals("OK"))
					 uniqueWithinURL.add(url);
				 else
					 uniqueOutsideURL.add(url);
							
				 
			 }
			 
			 System.out.println("Name: Prashant Mulge");
			 System.out.println("USC ID: 7050819312");
			 System.out.println("News site crawled: https://www.wsj.com/\n");
			 System.out.println("Fetch Statistics\n================");
			 System.out.println("# fetches attempted: " + noFetchesAttempted);
			 System.out.println("# fetches succeeded: " + noFetchesSucceeded);
			 System.out.println("# feteches aborted/failed: " + noAbortedFailed + "\n");
			 System.out.println("Outgoing URLs:\n==============");
			 System.out.println("Total URLs extracted: " + totalURL);
			 System.out.println("# unique URLs extracted: " + uniqueURL.size());
			 System.out.println("# unique URLs within News Site: " + uniqueWithinURL.size());
			 System.out.println("# unique URLs outside News Site: " + uniqueOutsideURL.size() + "\n");
			 System.out.println("Status Codes:\n=============");
			 System.out.println("200 OK: " + Collections.frequency(list, 200));
			 System.out.println("301 Moved Permanently: " + Collections.frequency(list, 301));
			 System.out.println("401 Unauthorized: " + Collections.frequency(list, 401));
			 System.out.println("403 Forbidden: " + Collections.frequency(list, 403));
			 System.out.println("404 Not Found: " + Collections.frequency(list, 404) + "\n");
			 System.out.println("File Sizes:\n===========");
			 System.out.println("< 1KB: " + one);
			 System.out.println("1KB ~ <10KB: " + two);
			 System.out.println("10KB ~ <100KB: " + three);
			 System.out.println("100KB ~ <1MB: " + four);
			 System.out.println(">= 1MB: " + five + "\n");
			 System.out.println("Content Types:\n==============");
			 System.out.println("text/html: " + html);
			 System.out.println("image/gif: " + gif);
			 System.out.println("image/jpeg: " + jpeg);
			 System.out.println("image/png: " + png);
			 System.out.println("image/svg+xml: " + pdf);
			 System.out.println("application/pdf: " + pdf);
			 
		 }catch (Exception e){
			 e.printStackTrace();
		 }
		 }
		
}