package jung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * An algorithm for computing clusters (community structure) in graphs based on
 * edge betweenness. The betweenness of an edge is defined as the extent to
 * which that edge lies along shortest paths between all pairs of nodes.
 * 
 * This algorithm works by iteratively following the 2 step process:
 * <ul>
 * <li>Compute edge betweenness for all edges in current graph
 * <li>Remove edge with highest betweenness
 * </ul>
 * <p>
 * Running time is: O(kmn) where k is the number of edges to remove, m is the
 * total number of edges, and n is the total number of vertices. For very sparse
 * graphs the running time is closer to O(kn^2) and for graphs with strong
 * community structure, the complexity is even lower.
 * <p>
 * This algorithm is a slight modification of the algorithm discussed below in
 * that the number of edges to be removed is parameterized.
 * 
 * @author Scott White
 * @author Tom Nelson (converted to jung2)
 * @see "Community structure in social and biological networks by Michelle Girvan and Mark Newman"
 */
public class EdgeBetweennessClusterer<V, E> implements
		Transformer<Graph<V, E>, Set<Set<V>>> {
	private int mNumEdgesToRemove;
	private Map<E, Pair<V>> edges_removed;

	/**
	 * Constructs a new clusterer for the specified graph.
	 * 
	 * @param numEdgesToRemove
	 *            the number of edges to be progressively removed from the graph
	 */
	public EdgeBetweennessClusterer(int numEdgesToRemove) {
		mNumEdgesToRemove = numEdgesToRemove;
		edges_removed = new LinkedHashMap<E, Pair<V>>();
	}

	/**
	 * calculate conductance for each cluster
	 * 
	 * @param graph
	 * @param clusterSet
	 * @return
	 */
	public HashMap<ArrayList<V>, Double> getConductance(Graph<V, E> graph,
			Set<Set<V>> clusterSet) {
		HashMap<ArrayList<V>, Double> conductanceScores = new HashMap<ArrayList<V>, Double>();

		// convert set to arraylist .. java ki gaand
		ArrayList<ArrayList<V>> clusterList = new ArrayList<ArrayList<V>>();
		for (Set<V> cluster : clusterSet) {
			clusterList.add(new ArrayList<V>(cluster));
		}
		for (int i = 0; i < clusterList.size(); i++) {
			// calculate the number of internal edges in this set
			double internalEdges = 0;
			ArrayList<V> outerList = clusterList.get(i);
			for (int k = 0; k < outerList.size(); k++) {
				for (int l = k + 1; l < outerList.size(); l++) {
					if (graph.findEdge(outerList.get(k), outerList.get(l)) != null) {
						internalEdges++;
					}
				}
			}
			double externalEdges = 0;
			for (int j = i + 1; j < clusterSet.size(); j++) {
				ArrayList<V> innerList = clusterList.get(j);
				for (int k = 0; k < outerList.size(); k++) {
					for (int l = k + 1; l < innerList.size(); l++) {
						if (graph.findEdge(outerList.get(k), innerList.get(l)) != null) {
							externalEdges++;
						}
					}
				}
			}
			double conductance = externalEdges / internalEdges;
			conductanceScores.put(outerList, conductance);
		}
		return conductanceScores;
	}

	/**
	 * Finds the set of clusters which have the strongest "community structure".
	 * The more edges removed the smaller and more cohesive the clusters.
	 * 
	 * @param graph
	 *            the graph
	 */
	public Set<Set<V>> transform(Graph<V, E> graph) {

		if (mNumEdgesToRemove < 0 || mNumEdgesToRemove > graph.getEdgeCount()) {
			throw new IllegalArgumentException(
					"Invalid number of edges passed in.");
		}

		edges_removed.clear();
		Set<Set<V>> clusterSet = null;
		for (int k = 0; k < mNumEdgesToRemove; k++) {
			BetweennessCentrality<V, E> bc = new BetweennessCentrality<V, E>(
					graph);
			E to_remove = null;
			double score = 0;
			for (E e : graph.getEdges())
				if (bc.getEdgeScore(e) > score) {
					to_remove = e;
					score = bc.getEdgeScore(e);
				}

			edges_removed.put(to_remove, graph.getEndpoints(to_remove));
			Pair<V> removedEndPoints = graph.getEndpoints(to_remove);
			System.out.println("Iteration: " + k + " Edge removed: "
					+ graph.getEndpoints(to_remove) + " score: " + score);
			graph.removeEdge(to_remove);
			E reversedEdge = graph.findEdge(removedEndPoints.getSecond(),
					removedEndPoints.getFirst());
			edges_removed.put(reversedEdge, graph.getEndpoints(reversedEdge));
			graph.removeEdge(reversedEdge);
			WeakComponentClusterer<V, E> wcSearch = new WeakComponentClusterer<V, E>();
			clusterSet = wcSearch.transform(graph);
			HashMap<ArrayList<V>, Double> conductanceScores = getConductance(graph, clusterSet);
			System.out.println("********Clusters**********");
			for(ArrayList<V> key : conductanceScores.keySet()){
				System.out.println("Cluster: "+key);
				System.out.println("conductance: "+conductanceScores.get(key));
			}
			System.out.println("\n");
		}

//		for (Map.Entry<E, Pair<V>> entry : edges_removed.entrySet()) {
//			Pair<V> endpoints = entry.getValue();
//			graph.addEdge(entry.getKey(), endpoints.getFirst(), endpoints
//					.getSecond());
//		}
		return clusterSet;
	}

	/**
	 * Retrieves the list of all edges that were removed (assuming extract(...)
	 * was previously called). The edges returned are stored in order in which
	 * they were removed.
	 * 
	 * @return the edges in the original graph
	 */
	public List<E> getEdgesRemoved() {
		return new ArrayList<E>(edges_removed.keySet());
	}
}
