from wsgiref.simple_server import make_server
import MySQLdb
from cgi import parse_qs, escape

conn = MySQLdb.connect(host = "localhost",user = "root",db = "redygest")

def application(environ, start_response):
	d = parse_qs(environ['QUERY_STRING'])
	story_id = d.get('id',[''])[0]
	story_id = escape(story_id)
	print "Fetching story_id: " + story_id
	response_body = ""
	if story_id is None:
		response_headers = [('Content-Type', 'text/plain'),
	                        ('Content-Length', str(len(response_body)))]
		start_response(status, response_headers)
		return "story_id_not_given"
	response_body = getData(story_id)
	status = '200 OK'
	response_headers = [('Content-Type', 'text/plain'),
                        ('Content-Length', str(len(response_body)))]
	start_response(status, response_headers)
	return response_body

def getData(story_id):
	response_body = ""
	global conn
	try:
		if conn is None:
			print "Creating Connection"
			response_body = response_body.join("Creating Connection once more\n")
			conn = MySQLdb.connect(host = "localhost",user = "root",db = "redygest")
		cursor = conn.cursor()
		cursor.execute ("SELECT story_json from stories where id = %s" , story_id)
		rows = cursor.fetchall()
		for row in rows:
			response_body = response_body.join("%s " % (row[0]))
		cursor.close()
			
	except MySQLdb.Error, e:
		print "Error %d: %s" % (e.args[0], e.args[1])
		conn.close()
		conn = MySQLdb.connect(host = "localhost",user = "root",db = "redygest")
		getData(story_id)
		
	return response_body

httpd = make_server('localhost', 8051, application)
httpd.serve_forever()
