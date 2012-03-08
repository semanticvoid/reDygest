/**
 * 
 */
package com.redygest.grok.knowledge;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.extractor.FeatureExtractorFactory;
import com.redygest.grok.features.extractor.FeatureExtractorType;
import com.redygest.grok.features.extractor.IFeatureExtractor;
import com.redygest.grok.features.repository.FeaturesRepository;
import com.redygest.grok.knowledge.graph.IRepresentation;
import com.redygest.grok.knowledge.graph.Node;
import com.redygest.grok.knowledge.graph.Node.NodeType;
import com.redygest.grok.knowledge.graph.NodeProperty;
import com.redygest.grok.knowledge.graph.Relation;

/**
 * Unit test cases for Curator
 */
public class CuratorTest extends TestCase {

	private IFeatureExtractor extractor = FeatureExtractorFactory.getInstance()
			.getFeatureExtractor(FeatureExtractorType.SRL);
	private FeatureVectorCollection f = null;

	protected void setUp() {
		if (f == null) {
			Data d1 = new Tweet("{\"text\":\"John hit Tom with a bat.\"}", "1");
			List<Data> dataList = new ArrayList<Data>();
			dataList.add(d1);
			FeaturesRepository repository = FeaturesRepository.getInstance();
			f = extractor.extract(dataList, repository);
			repository.addFeatures(f);
		}
	}

	protected void tearDown() {
		// do nothing
	}

	public void testAddRepository() {
		Curator c = new Curator();
		boolean retVal = c.addRepository(FeaturesRepository.getInstance());
		assertEquals(true, retVal);
	}

	public void testKnowledgeRepresentationNodes() {
		Curator c = new Curator();
		boolean retVal = c.addRepository(FeaturesRepository.getInstance());
		if (retVal) {
			IRepresentation rep = c.getModel();
			Node n = rep.getNodeWithName("John");
			assertNotNull(n);
			n = rep.getNodeWithName("Tom");
			assertNotNull(n);
			n = rep.getNodeWithName("hit");
			assertNotNull(n);
			return;
		}

		fail();
	}

	public void testKnowledgeRepresentationRelations() {
		Curator c = new Curator();
		boolean retVal = c.addRepository(FeaturesRepository.getInstance());
		if (retVal) {
			IRepresentation rep = c.getModel();
			Node n1 = rep.getNodeWithName("John");
			Relation r = null;
			List<Relation> relations = rep.getRelations("start n=("
					+ n1.get(NodeProperty.ID)
					+ ") match (n)-[q, :A0]-() return q");
			if (relations != null && relations.size() >= 1) {
				r = relations.get(0);
			}
			assertNotNull(r);
			Node node1 = r.getNode1();
			assertNotNull(node1);
			assertEquals(NodeType.EVENT.toString(),
					node1.get(NodeProperty.TYPE));

			n1 = rep.getNodeWithName("Tom");
			relations = rep.getRelations("start n=(" + n1.get(NodeProperty.ID)
					+ ") match (n)-[q, :A1]-() return q");
			if (relations != null && relations.size() >= 1) {
				r = relations.get(0);
			}
			assertNotNull(r);
			node1 = r.getNode1();
			assertNotNull(node1);
			assertEquals(NodeType.EVENT.toString(),
					node1.get(NodeProperty.TYPE));
			return;
		}

		fail();
	}

}
