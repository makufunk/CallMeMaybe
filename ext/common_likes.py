#!/usr/bin/python
import urllib2
import json
import sys
		
		
#user's FB id
_user_fb_id = sys.argv[1]
#friend's FB id
_friend_fb_id = sys.argv[2]
#access_token
access_token = sys.argv[3]

#get the ranking data
my = urllib2.urlopen("https://graph.facebook.com/"+_user_fb_id+"/likes?access_token="+access_token)
fr = urllib2.urlopen("https://graph.facebook.com/"+_friend_fb_id+"/likes?access_token="+access_token)
#my_likes list
my_likes = json.load(my)["data"]
#friend's likes list
fr_likes = json.load(fr)["data"]
#output same likes
for my_like in my_likes:
	for fr_like in fr_likes:
		if my_like["name"] == fr_like["name"]:
			print my_like["name"].encode('ascii','ignore')+"\n"
