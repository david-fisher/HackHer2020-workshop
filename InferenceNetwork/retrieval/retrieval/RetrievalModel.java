package retrieval;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import index.Index;
import index.Posting;
import index.PostingList;

public abstract class RetrievalModel {
	Index index;
	double collSize;

	/**
	 * @param tf -- term frequency
	 * @param ctf -- collection term frequency
	 * @param docLen -- document length
	 * @return score of the occurrences for the given model
	 */

	public abstract double scoreOccurrence(int tf, int ctf, int docLen);

	/**
	 * @return a list of the top k documents in descending order with respect to scores.
	 * key = sceneId, value = score
	 * Does document at a time retrieval using the model's smoothed probability estimates
	 * combined using AND (sum of log P(q_{i}|D)).
	 */
	public List<Map.Entry<Integer, Double>> retrieveQuery(String query, int k) {
		PriorityQueue<Map.Entry<Integer, Double>> result = 
				new PriorityQueue<>(Map.Entry.<Integer, Double>comparingByValue());
		String [] queryTerms = query.split("\\s+");
		PostingList[] lists = new PostingList[queryTerms.length];
		for (int i = 0; i < queryTerms.length; i++) {
			lists[i] = index.getPostings(queryTerms[i]);
		}
		for (int doc = 1; doc <= index.getDocCount(); doc++) {
			Double curScore = 0.0;
			for (int i = 0; i < lists.length; i++) {
				PostingList p = lists[i];
				p.skipTo(doc);
				Posting post = p.getCurrentPosting();
				int tf = 0;
				// have to do background score for docs that don't contain the terms...
				if (post != null && post.getDocId() == doc) tf = post.getTermFreq();
				int dlen = index.getDocLength(doc);
				int ctf = index.getTermFreq(queryTerms[i]);
				curScore += scoreOccurrence(tf, ctf, dlen);
			}
			result.add(new AbstractMap.SimpleEntry<Integer, Double>(doc, curScore));
			// trim the queue if necessary
			if (result.size() > k) {
				result.poll();
			}
		}
		// reverse the queue
		ArrayList<Map.Entry<Integer, Double>> scores = new ArrayList<Map.Entry<Integer, Double>>();
		scores.addAll(result);
		scores.sort(Map.Entry.<Integer, Double>comparingByValue(Comparator.reverseOrder()));
		return scores;
	}
}
