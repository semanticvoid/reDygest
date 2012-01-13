

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class Qwitter {

	public static String clean(String text) throws IOException {
		text = text.toLowerCase().replaceFirst("^rt [a-z0-9@: ]+:", "");
		text = text.replaceAll("http://[a-z.0-9+/%&#~\\\\-_]+", "");
		text = text.replaceAll("\\\\ \"", "");
		text = text.replaceAll("-[ ]+$", " .");
		text = text.replaceAll("[^a-zA-Z0-9\\s]", ".\n");
		if (!text.endsWith(".")) {
			text += ".";
		}
		text += "\n";
		return text;
	}

	private static String extract(JSONObject jsonObject) {
		JSONObject jObj = JSONObject.fromObject(jsonObject);
		if (jObj.containsKey("text")) {
			return (String) jObj.get("text");
		} else {
			return null;
		}
	}
	
	private static List<String> parseJson(String json){
		ArrayList<String> tweets = new ArrayList<String>();
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(json);
		Iterator<JSONObject> itr = jsonArray.iterator();
		while (itr.hasNext()) {
			String tweetText = extract(itr.next());
			tweets.add(tweetText);
		}
		return tweets;
	}

	public static List<String> anotherParseJson(String json){
		ArrayList<String> tweets = new ArrayList<String>();
		 JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON( json );
		 tweets.add(extract(jsonObj));
		return tweets;
	}
	public List<String> fetchTweets(String user, int count) {
		ArrayList<String> tweets = new ArrayList<String>();

		for (int i = 1; i < 5; i++) {
			String urlStr = "http://api.twitter.com/statuses/user_timeline/"
					+ user + ".json?count=" + count+"&include_rts=true&page="+i;

			try {
				URL url = new URL(urlStr);
				URLConnection yc = url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(yc
						.getInputStream()));
				String inputLine;

				StringBuffer buf = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					buf.append(inputLine);
				}

				JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(buf
						.toString());
				Iterator<JSONObject> itr = jsonArray.iterator();
				while (itr.hasNext()) {
					String tweetText = extract(itr.next());
					tweets.add(tweetText);
				}

				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tweets;
	}

	public static void main(String args[]) {
		Qwitter t = new Qwitter();
		String  s = null;
		try{
			BufferedReader br = new BufferedReader(new FileReader("lokpal_json"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("lokpal-parsed.txt"));
			while((s=br.readLine())!=null){
				List<String> tweets = anotherParseJson(s);
				for(String tw : tweets){
					bw.write(tw+"\n");
				}
				bw.flush();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}