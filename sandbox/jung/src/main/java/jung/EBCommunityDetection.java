package jung;

import java.util.Set;

import jung.EdgeBetweennessClusterer;
import edu.uci.ics.jung.graph.Graph;

public class EBCommunityDetection {
	
	/**
	 * edge between-nes community detection
	 * 
	 * @param graph
	 * @param numEdgesToRemove
	 * @return
	 */

	public Set<Set<String>> run(Graph<String, Integer> graph,
			int numEdgesToRemove) {
		if (graph == null) {
			return null;
		}
		EdgeBetweennessClusterer<String, Integer> clust = new EdgeBetweennessClusterer<String, Integer>(
				numEdgesToRemove);
		Set<Set<String>> edgeSet = clust.transform(graph);

		return edgeSet;

	}
}
