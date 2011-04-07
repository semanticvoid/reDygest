var app = require('express').createServer();

app.get('/', function(req, res){
	var Client = require('mysql').Client;
	client = new Client();
	
	client.user = 'root';
	client.password = '';
	client.host = 'localhost';
	client.port = 3306;
	client.connect();
	client.query('USE dygest', function useDb(err, results, fields) {
		if (err) {
			console.log("ERROR: " + err.message);
			throw err;
		}
	});

	var buf = '';

	client.query('SELECT title, body FROM stories',function selectCb(err, results, fields) {
		if (err) {
			console.log("ERROR: " + err.message);
			throw err;
		} else {
			for(var i=0; i<results.length; i++) {
				buf = buf + "<b>title</b>: " + results[i]['title'] + 
					"<br><b>body</b>: " + results[i]['body'] + "<br><hr>";
			}
			res.send(buf);
		}
	});
	client.end();
});

app.listen(3000);

