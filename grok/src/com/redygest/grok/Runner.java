/**
 * 
 */
package com.redygest.grok;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.computation.FeaturesComputation;
import com.redygest.grok.knowledge.Curator;
import com.redygest.grok.repository.FeaturesRepository;

/**
 * Grok Invoker
 *
 */
public class Runner {
	
	/**
	 * Mode Enum
	 *
	 */
	public static enum Mode {
		INDEX, SEARCH;
		
		/**
		 * Get Mode from String
		 * @param str
		 * @return	Mode
		 */
		public static Mode getMode(String str) {
			for(Mode m : Mode.values()) {
				if(m.toString().equalsIgnoreCase(str)) {
					return m;
				}
			}
			
			return null;
		}
	}

	private Mode m = null;
	private String modelName = null;
	private String arg = null;
	
	/**
	 * Constructor
	 * @param m
	 * @param modelName
	 * @param arg
	 */
	public Runner(Mode m, String modelName, String arg) {
		this.m = m;
		this.modelName = modelName;
		this.arg = arg;
	}
	
	/**
	 * Run logic
	 */
	public boolean run() {
		switch (this.m) {
			case INDEX:
				return runIndex();
			case SEARCH:
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Index logic
	 * @return	true on success, false otherwise
	 */
	private boolean runIndex() {
		FeaturesComputation fc = new FeaturesComputation();
		File f = new File(this.arg);
		if(!f.exists()) {
			System.err.println("Data file does not exist: " + this.arg);
			return false;
		} else {
			try {
				BufferedReader rdr = new BufferedReader(new FileReader(f));
				String line = null;
				List<Data> dataSet = new ArrayList<Data>();
				int row = 0;
				while((line = rdr.readLine()) != null) {
					Data d = new Tweet(line, String.valueOf(row));
					dataSet.add(d);
					
					if(row % 10 == 0) {
						fc.computeFeatures(dataSet);
						dataSet = new ArrayList<Data>();
					}
					
					row++;
				}
			} catch(Exception e) {
				e.printStackTrace();
				System.err.println("Error reading file: " + this.arg + ". Exception: " + e.getMessage());
				return false;
			}
			
			FeaturesRepository repository = FeaturesRepository.getInstance();
			Curator c = new Curator(this.modelName);
			c.addRepository(repository);
		}
		
		return true;
	}
	
	/**
	 * Function to check command line args
	 * @param args
	 * @return
	 */
	public static boolean checkUsage(String[] args) {
		if(args == null || args.length != 3) {
			return false;
		} else {
			if(Mode.getMode(args[0]) == null) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Function to print usage
	 */
	public static void printUsage() {
		System.out.println("java -jar grok.jar <MODE> <MODEL_NAME> <ARG>");
		System.out.println("	- MODE -- index/search");
		System.out.println("	- MODEL_NAME -- name of the model");
		System.out.println("	- ARG -- path of data file (for index mode)");
		System.out.println("	      -- query (for search mode)");
		System.exit(1);
	}
	
	public static void main(String[] args) {
		if(!checkUsage(args)) {
			printUsage();
		}
		
		Runner r = new Runner(Mode.getMode(args[0]), args[1], args[2]);
		r.run();
	}

}
