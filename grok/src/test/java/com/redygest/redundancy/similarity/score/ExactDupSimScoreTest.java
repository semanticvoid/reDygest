package test.java.com.redygest.redundancy.similarity.score;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.redundancy.similarity.score.ExactDupSimScore;

import junit.framework.TestCase;

public class ExactDupSimScoreTest extends TestCase {

	List<Data> data = new ArrayList<Data>();
	ExactDupSimScore ed = new ExactDupSimScore();

	@Override
	protected void setUp() {
		Data d1 = new Tweet("{\"text\":\"Bill Clinton went to Washington.\"}",
				"1");
		Data d2 = new Tweet("{\"text\":\"Bill Clinton went to Washington.\"}",
				"1");
		data.add(d1);
		data.add(d2);
	}

	public void testExactDup() {
		double score = ed.score(data.get(0), data.get(1));
		assertEquals(1.0, score, 0);
	}
}
