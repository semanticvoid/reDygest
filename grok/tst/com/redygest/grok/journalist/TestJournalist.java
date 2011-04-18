package com.redygest.grok.journalist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.redygest.commons.data.Story;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.freebase.SearchFreebase.IndexerFieldType;
import com.redygest.grok.preprocessor.StopWordAnalyzer;
import com.redygest.grok.preprocessor.WordTokenizer;
import com.redygest.grok.twitter.TwitterTokenizer;

public class TestJournalist extends BaseJournalist{

	@Override
	Story process(List<Tweet> tweets) {
		
		List< List<String> > tokenizedSentences = preprocess(tweets);
		try {
			tokenizedSentences = getConcepts(tokenizedSentences);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(List<String> tokenizedSentence : tokenizedSentences) {
			for(String word : tokenizedSentence) {
				System.out.print(word + " ");
			}
			System.out.println();
		}
		
		
		StringBuffer body = new StringBuffer();
		
		for(Tweet t : tweets) {
			body.append(t.getText() + "\n");
		}
		
		int end = 200;
		if(body.length() < 200) {
			end = body.length();
		}
		
		Story s = new Story(body.substring(0, end), body.toString());
		
		return s;
	}
	
	protected boolean write(Story s) {
		//System.out.println(s.getBody());
		return true;
	}
	
	private List< List<String> > preprocess(List<Tweet> tweets) {
		TwitterTokenizer st = new TwitterTokenizer();
		StopWordAnalyzer swAnalyzer = new StopWordAnalyzer();
		List< List<String> > tokenizedSentences = new ArrayList< List<String> >();
		for(Tweet t : tweets) {
			String sentence = "";
			Set<String> candidateWords = new HashSet<String>();
			String tText = t.getText();
			List<String> lines = st.tokenize(tText);
			for(String line : lines) {
				sentence = sentence + line + " ";
			}
			List<String> words = WordTokenizer.tokenize(sentence);
			for(String word : words) {
				if(!swAnalyzer.STOP_WORD_MAP.contains(word)) {
					candidateWords.add(word.trim());
				}
			}
			tokenizedSentences.add(new ArrayList<String>(candidateWords));
		}
		return tokenizedSentences;
	}

	private List< List<String> > getConcepts(List< List<String> > tokenizedSentences) throws IOException, ParseException {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_29);
		Directory index = FSDirectory.getDirectory("/home/sudheer/workspace/Freebase-index");
		List< List<String> > documentConcepts = new ArrayList< List<String> >();
		for(List<String> tokenizedSentence : tokenizedSentences) {
			Set<String> sentenceConcepts = new HashSet<String>();
			for(String word : tokenizedSentence) {
				sentenceConcepts.addAll(searchFreebase(index, analyzer, word.trim()));
			}
			documentConcepts.add(new ArrayList<String>(sentenceConcepts));
		}
		return documentConcepts;
	}
	
	private Set<String> searchFreebase(Directory index, StandardAnalyzer analyzer, String querystr) throws ParseException, IOException {
		Set<String> concepts = new HashSet<String>();
		Query q = new QueryParser(Version.LUCENE_29, IndexerFieldType.NAME.name(), analyzer).parse(querystr);
	    int hitsPerPage = 100;
	    IndexSearcher searcher = new IndexSearcher(index, true);
	    TopScoreDocCollector collector = 
	    TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    for(int i=0;i<hits.length;++i) {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      if(!StringUtils.isEmpty(d.get(IndexerFieldType.PROPERTIES.name()))) {
		    	  concepts.add(d.get(IndexerFieldType.DOMAIN.name()));
		    	  concepts.add(d.get(IndexerFieldType.TYPE.name()));  
		      }      
		}
	    return concepts;
	}
}
