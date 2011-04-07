var app = require('express').createServer();

app.get('/', function(req, res){
	var Client = require('mysql').Client,
    	client = new Client();

	client.user = 'root';
	client.password = 'root';
	client.host = 'localhost';
	client.port = 3306;
	client.connect();
	client.end();
  	res.send('hello world');
});

app.listen(3000);

