1
Homework: Web Crawling
1. Objective
In this assignment, you will work with a simple web crawler to measure aspects of a crawl, study the
characteristics of the crawl, download web pages from the crawl and gather webpage metadata, all
from pre-selected news websites.
2. Preliminaries
To begin we will make use of an existing open source Java web crawler called crawler4j. This
crawler is built upon the open source crawler4j library which is located on github. For complete
details on downloading and compiling see
https://github.com/yasserg/crawler4j
Also see the document “Instructions for Installing Eclipse and Crawler4j” located on the
Assignments web page for help.
3. Crawling
Your task is to configure and compile the crawler and then have it crawl a news website. In the
interest of distributing the load evenly and not overloading the news servers, we have pre-assigned
the news sites to be crawled according to your USC ID number, given in the table below.
The maximum pages to fetch can be set in crawler4j and it should be set to 20,000 to ensure a
reasonable execution time for this exercise. Also, maximum depth should be set to 16 to ensure that
we limit the crawling.
