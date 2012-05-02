// articley

var articlely = {
	
	// fetch the story json
	getStory : function(id) {
		$.ajax({
  			url: 'api/story?id=' + id,
  			success: function(data) {
    			var obj = jQuery.parseJSON('{"title":"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante venenatis.", "body":["Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante venenatis.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante venenatis."]}');
				this.setTitle(obj.title);
				this.setBody(obj.body);
    			alert('Load was performed.');
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
		return "<blockquote class=\"pull-left\">" +
				"<p>" + line + "</p><small>9 hours ago</small></blockquote>";
	},
	
	getURLParameter : function(name) {
    	return decodeURI(
        	(RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1])
   }
}