package com.redygest.grok.freebase;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchFreebase {
	public static void main(String[] args) throws IOException, ParseException {
	    // 0. Specify the analyzer for tokenizing text.
	    //    The same analyzer should be used for indexing and searching
	    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_29);

	    // 1. create the index
	    Directory index = FSDirectory.getDirectory("/home/sudheer/workspace/Freebase-index");

	    // 2. query
	    String querystr = args.length > 0 ? args[0] : "hospital";

	    // the "title" arg specifies the default field to use
	    // when no field is explicitly specified in the query.
	    Query q = new QueryParser(Version.LUCENE_29, IndexerFieldType.NAME.name(), analyzer).parse(querystr);

	    // 3. search
	    int hitsPerPage = 100;
	    IndexSearcher searcher = new IndexSearcher(index, true);
	    TopScoreDocCollector collector = 
	    TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    
	    // 4. display results
	    System.out.println("Found " + hits.length + " hits.");
	    for(int i=0;i<hits.length;++i) {
	      int docId = hits[i].doc;
	      Document d = searcher.doc(docId);
	      if(!StringUtils.isEmpty(d.get(IndexerFieldType.PROPERTIES.name()))) {
	    	  System.out.println((i + 1) + ". " + d.get(IndexerFieldType.NAME.name()) + "##" + d.get(IndexerFieldType.DOMAIN.name()) + "##" + d.get(IndexerFieldType.TYPE.name()) + "##" + d.get(IndexerFieldType.PROPERTIES.name()));  
	      }
	      
	    }

	    // searcher can only be closed when there
	    // is no need to access the documents any more. 
	    searcher.close();
	  }
	
	public enum IndexerFieldType {
		NAME,
		ID,
		DOMAIN,
		TYPE,
		PROPERTIES,
		PROPERTY_VALUES
	}
}
