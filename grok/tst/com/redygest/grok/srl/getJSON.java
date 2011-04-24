package com.redygest.grok.srl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;




public class getJSON {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			BufferedWriter bw = new BufferedWriter(new FileWriter(args[0]+".txt"));
			String line ="";
			while((line=br.readLine())!=null){
			    JSONObject json = (JSONObject)new JSONParser().parse(line);
			    bw.write(json.get("text")+"\n");
			}
			bw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
