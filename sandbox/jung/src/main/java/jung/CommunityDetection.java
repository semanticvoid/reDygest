package jung;

import java.util.Set;

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.graph.Graph;

public class CommunityDetection {

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
