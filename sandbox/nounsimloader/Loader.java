package nounsimloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;

public class Loader {

	public static String getKey(String w1, String w2, String delimiter) {
		return (w1+"~"+w2);
	}
	
	public static void loadWordNetRedirects(String pathToFile, String credentialsFile){
		BufferedWriter bw =null;
		try{
			bw = new BufferedWriter(new FileWriter("failed.txt"));
		}catch(Exception e){
			e.printStackTrace();
		}
		AmazonSimpleDB db;
		try {
			db = new AmazonSimpleDBClient(new PropertiesCredentials(new File(
					credentialsFile)));
			//db.deleteDomain(new DeleteDomainRequest("wikipediaredirects"));
			db.createDomain(new CreateDomainRequest("wikipediaredirects"));
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		List<ReplaceableItem> items = new ArrayList<ReplaceableItem>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathToFile));
			String line =null;
			int count =0;
			int line_count=0;
			HashSet<String> seen = new HashSet<String>();
			while ((line=br.readLine())!=null){
				ReplaceableItem item = null;
				line_count++;
				List<ReplaceableAttribute> attrs = null;
				try {
					//System.out.println(line);
					line = line.trim();
					String[] split = line.split("\",\"");
					if (split.length != 2)
						continue;
					String root = split[1].substring(0, split[1].length() - 1);
					String variant = split[0].substring(1);
					String key = getKey(root, variant, "~");
					if(seen.contains(key)){
						continue;
					} else {
						seen.add(key);
					} 
					if(seen.size()==50){
						seen.clear();
					}
					
					variant =  variant.toLowerCase();
					item = new ReplaceableItem(key);
					attrs = new ArrayList<ReplaceableAttribute>();
					attrs.add(new ReplaceableAttribute("root", root, new Boolean(true)));
					attrs.add(new ReplaceableAttribute("variation", variant, new Boolean(true)));
					item.setAttributes(attrs);
					items.add(item);
					count++;
					System.out.println("processed "+line_count+" lines");
					if(count==25){
						System.out.println("Put");
						db.batchPutAttributes(new BatchPutAttributesRequest("wikipediaredirects", items));
						items.clear();
						count=0;
					}
					
				}
				catch (Exception e) {
					e.printStackTrace();
					System.out.println("failed for line: "+line);
					bw.write(line+"\n");
					bw.flush();
				}
			}
			if(!items.isEmpty()){
				db.batchPutAttributes(new BatchPutAttributesRequest("wikipediaredirects", items));
			}
				
		} catch (Exception  e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		loadWordNetRedirects("/home/ec2-user/redirects.csv", "/home/ec2-user/AwsCredentials.properties");
	}

}
