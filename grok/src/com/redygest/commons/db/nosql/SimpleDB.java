package com.redygest.commons.db.nosql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.redygest.commons.config.ConfigReader;

/**
 * Simple DB singleton
 *
 */
public class SimpleDB extends AmazonSimpleDBClient {

	private static SimpleDB instance = null;
	
	private SimpleDB(AWSCredentials creds) {
		super(creds);
	}
	
	public static synchronized SimpleDB getInstance() {
		if(instance == null) {
			ConfigReader config = ConfigReader.getInstance();
			try {
				instance = new SimpleDB(new PropertiesCredentials(new File(config.getAWSCredentialsPath())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return instance;
	}
}
