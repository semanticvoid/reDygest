// articley

var articlely = {
	
	// fetch the story json
	getStory : function(id) {
		$.ajax({
  			url: 'api/story.php?id=' + id,
  			success: function(data) {
				//alert(data);
    				var obj = jQuery.parseJSON(data);
				articlely.setTitle(obj.title);
				articlely.setBody(obj.body);
    				//alert('Load was performed.');
  			}
		});
	},
	
	// set the title
	setTitle : function(title) {
		var titleEle = document.getElementById("articlely-title");
		if(titleEle != null) {
			titleEle.innerHTML = title;
		}
	},
	
	// set the story lines
	setBody : function(lines) {
		var bodyEle = document.getElementById("articlely-body");
		var bodyHtml = "";
		for (var i=0; i < lines.length; i++) {
		  line = lines[i];
		  bodyHtml += this._generateLineBlock(line); 
		};
		bodyEle.innerHTML = bodyHtml;
	},
	
	// generate line
	_generateLineBlock : function(line) {
		var txt = line["text"];
		var time = line["time"];
		var lineDate = new Date(time);
		var currDate = new Date();
		var timeDiff = currDate - lineDate;
		timeDiff = Math.floor(timeDiff/(1000*60*60));
		return "<blockquote class=\"pull-left\">" +
				"<p>" + txt + "</p><small>" + timeDiff + " hours ago</small></blockquote>";
	},
	
	getURLParameter : function(name) {
    	return decodeURI(
        	(RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1])
   }
}
