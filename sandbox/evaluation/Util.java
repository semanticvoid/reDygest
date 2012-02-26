package evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author akishore
 *
 */
public class Util {
	
	public class Record {
		public String text;
		public int line_number;
		public List<String> entities = new ArrayList<String>();
		public String file;
	}
	
	private Record readRecord(String line){
		Record r = new Record();
		r.text= line.trim();
		r.entities = Arrays.asList(r.text.split("\\s+"));
		return r;
	}

	private Record readRecord(String line, String file){
		Record r = new Record();
		r.text= line.trim();
		r.file = file;
		r.entities = Arrays.asList(r.text.split("\\s+"));
		return r;
	}
	
	
	public List<Record> readDataSetDirectory(String path) throws Exception {
		List<Record> records = new ArrayList<Record>();
		File dir = new File(path);
		for(File file : dir.listFiles()){
			if(!file.getName().contains(".annotated"))
				continue;
			BufferedReader rdr = new BufferedReader(new FileReader(file.getAbsolutePath()));	
			String line;
			int line_number =0;
			while((line = rdr.readLine()) != null) {
				Record r = readRecord(line);
				if(r != null) {
					line_number++;
					if(r.text.equalsIgnoreCase("-")){
						continue;
					}
					r.file = file.getName();
					r.line_number = line_number;
					records.add(r);
				}
			}
			
		}
		return records;
	}
	
	
	public  List<Record> readDataSet(String path) throws Exception {
		List<Record> records = new ArrayList<Record>();
		BufferedReader rdr = new BufferedReader(new FileReader(path));	
		String line;
		int line_number =0;
		while((line = rdr.readLine()) != null) {
			Record r = readRecord(line);
			if(r != null) {
				line_number++;
				if(r.text.equalsIgnoreCase("-")){
					continue;
				}	
				r.line_number = line_number;
				records.add(r);
			}
		}
		return records;
	}
	
	
}
