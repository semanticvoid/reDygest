package com.redygest.commons.db;

import java.util.List;

import junit.framework.TestCase;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.POS;

public class WordNetTest extends TestCase {
	
	public void testGetInstance() {
		WordNet wn = WordNet.getSingleton();
		assertNotNull(wn);
	}
	
	public void testGetSysnets() {
		WordNet wn = WordNet.getSingleton();
		List<ISynsetID> ids = wn.getSynsets("run", POS.VERB);
		assertNotNull(ids);
		boolean flag = false;
		for(ISynsetID id : ids) {
			if(!flag && id.toString().equals("SID-02720904-V")) {
				flag = true;
			}
		}
		assertTrue(flag);
	}
	
	public void testGetSimilarity() {
		WordNet wn = WordNet.getSingleton();
		List<ISynsetID> syns1 = wn.getSynsets("run", POS.VERB);
		List<ISynsetID> syns2 = wn.getSynsets("run down", POS.VERB);
		double sim = 0;
		for (ISynsetID syn1 : syns1) {
			for (ISynsetID syn2 : syns2) {
				sim = wn.getSimilarity(syn1, syn2);
				if(syn1.toString().equals("SID-00549063-V") && syn2.toString().equals("SID-00099517-V")) {
					assertEquals(0.26151705652174295, sim, 0);
					return;
				}
			}
		}
		
		fail();
	}
}
