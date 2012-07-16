#!/usr/bin/python

#http://wwwsearch.sourceforge.net/old/ClientCookie/
import ClientCookie
#http://wwwsearch.sourceforge.net/old/ClientForm/
import ClientForm
import json
import sys
		
def _authenticate():
	"""Logs the user in to Facebook"""
	# Create special URL opener (for User-Agent) and cookieJar
	cookieJar = ClientCookie.CookieJar()

	opener = ClientCookie.build_opener(ClientCookie.HTTPCookieProcessor(cookieJar))
	opener.addheaders = [("User-agent","Mozilla/5.0 (compatible)")]
	ClientCookie.install_opener(opener)
	fp = ClientCookie.urlopen("https://www.facebook.com/")
	forms = ClientForm.ParseResponse(fp)
	fp.close()

	form = forms[0]

	# supply user id and pw
	form["email"]  = _usr_id
	form["pass"] = _pswrd

	fp = ClientCookie.urlopen(form.click())
	fp.close()
		
#user's FB id
_user_fb_id = sys.argv[1]
#user's email
_usr_id = sys.argv[2]
#user's password
_pswrd = sys.argv[3]
#log in to FB
_authenticate()
#get the ranking data
f = ClientCookie.urlopen("http://www.facebook.com/ajax/typeahead/search/first_degree.php?__a="+_user_fb_id+"&filter=user&viewer="+_user_fb_id+"&token=&stale_ok=0")

for dic in json.loads(f.read()[9:])["payload"]["entries"]:
	print "".join([str(dic["uid"]),",",str(dic["index"]),",",dic["photo"]])
	