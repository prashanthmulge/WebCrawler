import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler{
	private	final static Pattern FILTERS=Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v"+"|rm|smil|wmv|swf|wma|zip|rar|gz|php|iso|ico))$");
	PrintWriter fetchPW;
	PrintWriter visitPW;
	PrintWriter urlPW;
	StringBuilder sb;
	
	/**
     * This function is called just before starting the crawl by this crawler
     * instance. It can be used for setting up the data structures or
     * initializations needed by this crawler instance.
     */
    public void onStart() {
    	try {
			 urlPW =  new PrintWriter(new FileOutputStream(new File("urls_wall_street_journal.csv")));
			 fetchPW = new PrintWriter(new FileOutputStream(new File("fetch_wall_street_journal.csv")));
			 visitPW =  new PrintWriter(new FileOutputStream(new File("visit_wall_street_journal.csv")));
	        urlPW.write("");
	        fetchPW.write("");
	        visitPW.write("");
	        urlPW.close();
	        fetchPW.close();
	        visitPW.close();
		 }catch (Exception e){
			 
		 }
        // Do nothing by default
        // Sub-classed can override this to add their custom functionality
    }

	
	
			 /**
			 * This method receives two parameters. The first parameter is the page
			 * in which we have discovered this new url and the second parameter is
			 * the new url. You should implement this function to specify whether
			 * the given url should be crawled or not (based on your crawling logic).
			 * In this example, we are instructing the crawler to ignore urls that
			 * have css, js, git, ... extensions and to only accept urls that start
			 * with "http://www.viterbi.usc.edu/". In this case, we didn't need the
			 * referringPage parameter to make the decision.
			 */
			 @Override
			 public boolean shouldVisit(Page referringPage, WebURL url) {
				 String href = url.getURL().toLowerCase();
				  try {
						 urlPW =  new PrintWriter(new FileOutputStream(new File("urls_wall_street_journal.csv"), true));
				        	
					 }catch (Exception e){
						 
					 }
					 sb = new StringBuilder();
				        sb.append(url.getURL().replaceAll(",", "-").replaceFirst("https?", ""));
				        sb.append(',');
				        if (href.startsWith("https://www.wsj.com/") || href.startsWith("http://www.wsj.com/"))
				        	sb.append("OK");
				        else
				        	sb.append("N_OK");
				       
				        sb.append('\n');
				        if (urlPW!=null){
					        urlPW.append(sb.toString());
					        urlPW.close();
				        }
				        
				        boolean visit = false;
				       
				        if (FILTERS.matcher(href).matches())
				        	return false;
				        try{
				        	if ((href.startsWith("https://www.wsj.com/") || href.startsWith("http://www.wsj.com/"))){
				        URL url_url = new URL(url.getURL());
				       
				        HttpURLConnection connection = (HttpURLConnection)  url_url.openConnection();
				        connection.setRequestMethod("HEAD");
				        connection.connect();
				        String contentType = connection.getContentType();
				        
				        Pattern filter = Pattern.compile("text/html.*|image/.*|application/pdf.*|application/msword.*");
						 
						visit = filter.matcher(contentType).matches();
						System.out.println(contentType + (visit&&(href.startsWith("https://www.wsj.com/") || href.startsWith("http://www.wsj.com/"))));
								 
				        	}
				        } catch(Exception e){
				        	
				        }
				 
				 
				 
				 return visit&&(href.startsWith("https://www.wsj.com/") || href.startsWith("http://www.wsj.com/"));
					 
				// return FILTERS.matcher(href).matches()
				// && (href.startsWith("https://www.bostonglobe.com/") || href.startsWith("http://www.bostonglobe.com/"));
}
			 
			 
			 @Override
			protected
			 void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
				 
				 try{
					 {
						fetchPW = new PrintWriter(new FileOutputStream(new File("fetch_wall_street_journal.csv"), true));
					 }
						} catch(Exception e){
							
						}
				    sb = new StringBuilder();
			        sb.append(webUrl.getURL().replaceAll(",","-"));
			        sb.append(',');
			        sb.append(Integer.toString(statusCode));
			       
			        sb.append('\n');
			        if (fetchPW!=null){
				        fetchPW.append(sb.toString());
				        fetchPW.close();
			        }
			 }
			 
			 @Override
			 public void visit(Page page) {
			 String url = page.getWebURL().getURL();
			 int statusCode = page.getStatusCode();
			 
			 
		        
			 Set<WebURL> links = new HashSet<WebURL>();
			 
			 System.out.println("URL: " + url);
			 if (page.getParseData() instanceof HtmlParseData) {
			 HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			 String text = htmlParseData.getText();
			 String html = htmlParseData.getHtml();
			 links = htmlParseData.getOutgoingUrls();
			 System.out.println("Text length: " + text.length());
			 System.out.println("Html length: " + html.length());
			 System.out.println("Number of outgoing links: " + links.size());
			 
			 
			 
			 }
			 
			 {
		        	try {
		        		visitPW =  new PrintWriter(new FileOutputStream(new File("visit_wall_street_journal.csv"), true));
		        	}catch(Exception e){
		        		
		        	}
		        	sb = new StringBuilder();
			        sb.append(url.replaceAll(",", "-"));
			        sb.append(',');
			        sb.append(Integer.toString(page.getContentData().length) + "bytes");
			        sb.append(',');
			        sb.append(Integer.toString(links.size()));
			        sb.append(',');
			        sb.append(page.getContentType().split(";")[0]);
			        
			        
			       
			        sb.append('\n');
			        if (visitPW!=null){
				        visitPW.append(sb.toString());
				        visitPW.close();
			        }
			        	
		        }
			 }

}