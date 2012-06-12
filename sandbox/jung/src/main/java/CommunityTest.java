import java.util.Set;

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class CommunityTest {

	public static void main(String[] args) {
		Graph<Integer, String> g2 = new SparseMultigraph<Integer, String>();

		g2.addVertex(1);
		g2.addVertex(2);
		g2.addVertex(3);
		g2.addVertex(4);
		g2.addVertex(5);
		g2.addVertex(6);
		g2.addVertex(7);
		g2.addVertex(8);
		g2.addVertex(9);
		g2.addEdge("Edge-1", 1, 3);
		g2.addEdge("Edge-2", 1, 2);
		g2.addEdge("Edge-3", 2, 2);
		g2.addEdge("Edge-4", 6, 2);
		g2.addEdge("Edge-5", 1, 6);
		g2.addEdge("Edge-6", 4, 2);
		g2.addEdge("Edge-7", 2, 3);
		g2.addEdge("Edge-8", 3, 4);
		g2.addEdge("Edge-9", 1, 6);
		g2.addEdge("Edge-10", 7, 9);
		g2.addEdge("Edge-11", 8, 2);
		System.out.println("The graph g2 = " + g2.toString());

		EdgeBetweennessClusterer<Integer, String> clust = new EdgeBetweennessClusterer<Integer, String>(
				2);
		Set<Set<Integer>> edgeSet = clust.transform(g2);
		System.out.println();
	}
}
