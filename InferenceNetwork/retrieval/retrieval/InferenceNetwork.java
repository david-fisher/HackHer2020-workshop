package retrieval;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class InferenceNetwork {
	/**
	 * Evaluates a query tree
	 * @param qnode Query tree to evaluate
	 * @param K number of results to return
	 * @return the list of document score pairs
	 */
	public List <Map.Entry<Integer, Double>> runQuery(QueryNode qnode, int K) {
		PriorityQueue<Map.Entry<Integer, Double>> result = 
				new PriorityQueue<>(Map.Entry.<Integer,Double>comparingByValue());
		while (qnode.hasMore()) {
			// which doc can we score next?
			Integer d = qnode.nextCandidate();
			// advance all of the query nodes to the document
			qnode.skipTo(d);
			// score it
			Double curScore = qnode.score(d);
			// filter nodes may return a null score
			if (curScore != null) { 
				result.add(new AbstractMap.SimpleEntry<Integer, Double>(d, curScore));
				// trim the queue if necessary
				if (result.size() > K) {
					result.poll();
				}
			}
			// advance all of the query nodes past the scored document
			qnode.skipTo(d + 1);
		}
		// reverse the queue
		ArrayList<Map.Entry<Integer, Double>> scores = new ArrayList<Map.Entry<Integer, Double>>();
		scores.addAll(result);
		scores.sort(Map.Entry.<Integer, Double>comparingByValue(Comparator.reverseOrder()));
		return scores;
	}
}
