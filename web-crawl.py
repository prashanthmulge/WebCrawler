import mechanize
from bs4 import BeautifulSoup
import urllib                                       
import sys
import thread
import threading
"""br = mechanize.Browser()
br.set_all_readonly(False)
br.set_handle_robots(False)
br.set_handle_refresh(False)
br.addheaders = [('User-agent','Firefox')]

links = set()

res = br.open("http://10.65.1.220/zintel_sample/")

result = res.read()
"""

search = ""
if len(sys.argv) < 3:
	print "Enter the details properly"
	print "Python web-crawl.py URL DEPTH | SEARCH_STRING"
	quit()
url = sys.argv[1]
depth = int(sys.argv[2])
if len(sys.argv) > 3:
	search = sys.argv[3]
#url = "http://10.65.1.220/zintel_sample/"

links = []
temp = set()
newtemp = set()
temp.add(url)
counter = 0
threads = []
threadLock = threading.Lock()
#soup = BeautifulSoup(urllib.urlopen(url).read())

print "First Level"

class myThread (threading.Thread):
	def __init__(self, url):
		threading.Thread.__init__(self)
		self.url = url
	def run(self):
		global counter
		threadLock.acquire()
		counter += 1
		threadLock.release()
		#print "Starting ", counter
		some_func(self.url)
		threadLock.acquire()
		counter -= 1
		threadLock.release()
		#print "Exiting ", counter

def some_func(i):
	global counter
	#print i
	url = i
	if i[-1] != '/':
	#	print "Skipped"
		return
	#print "Not Skipped"
	res = urllib.urlopen(i)
	status = res.getcode()
	if status == 200:
		l = [i, status]
	else:
		for each_td in  soup.findAll('td', {'class':'rsn'}):
			rsn += each_td
		l = [i, status, rsn]
	links.append(l)
	soup = BeautifulSoup(res.read())
	for link in soup.find_all('a'):
		if "http://" in link or "www." in link:
			#print(link.get('href'))
			threadLock.acquire()
			newtemp.add(link.get('href'))
			threadLock.release()

		else:
			#print (url + link.get('href'))
			if link.get('href')[0] != '/':
				threadLock.acquire()
				newtemp.add( url + link.get('href'))
				threadLock.release()


for d in range(depth):
	#print "Level", d
	for i in temp:
		if i[-1] != '/':
			#print "Skipped"
			links.append([i,"NA"])
			continue
		#print "Not Skipped"
		#try:
		while (counter > 30):
			#print "Counter", counter, i
			for t in threads:
				t.join()
			continue
		if counter < 31:
			thread = myThread(i)
			thread.start()
			threads.append(thread)
		#except:
		#	print "Error: unable to start thread"
		#some_func(i)		
	"""	print i
		Url = i
		if i[-1] != '/':
			#print "Skipped"
			continue
		#print "Not Skipped"
		res = urllib.urlopen(i)
		status = res.getcode()
		if status == 200:
			l = [i, status]
		else:
			for each_td soup.findAll('td', {'class':'rsn'}):
				rsn += each_td
			l = [i, status, rsn]
		links.append(l)
		soup = BeautifulSoup(res.read())
		for link in soup.find_all('a'):
			if "http://" in link or "www." in link:
				#print(link.get('href'))
				newtemp.add(link.get('href'))

			else:
				#print (url + link.get('href'))
				if link.get('href')[0] != '/':
					newtemp.add( url + link.get('href'))
	"""
	#print "Threads", len(threads)
	for t in threads:
		t.join()
	temp.clear()
	for j in newtemp:
		temp.add(j)
	newtemp.clear()

for i in temp:
	l = [i, "NA"]
	links.append(l)

print "Printing all urls"

for i in links:
	if search in i[0]:
		print i
