package apps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import index.Index;
import index.InvertedIndex;
import retrieval.AndNode;
import retrieval.Dirichlet;
import retrieval.InferenceNetwork;
import retrieval.NotNode;
import retrieval.OrNode;
import retrieval.OrderedWindow;
import retrieval.ProximityNode;
import retrieval.QueryNode;
import retrieval.RetrievalModel;
import retrieval.TermNode;
import retrieval.UnorderedWindow;
/**
 * Usage java apps.TestInferenceNetwork
 * 
 * Must be run in the working directory that contains the index to search.
 * 
 * we will test the following queries, retrieving the top 10 documents:
Q1: fool jester player
Q2: to be or not to be
Q3: alas poor yorick
Q4: antony strumpet
 */
public class TestInferenceNetwork {

	public static void main(String[] args) {
		int k = 10;
		Index index = new InvertedIndex();
		index.load(); // we assume that the index files are in the current directory.

		RetrievalModel model = new Dirichlet(index, 1500);
		List<Map.Entry<Integer, Double>> results;
		InferenceNetwork network = new InferenceNetwork();
		QueryNode queryNode;
		ArrayList<? extends ProximityNode> children;
		String [] queries = {"fool jester player", "to be or not to be",
				"alas poor yorick", "antony strumpet"};
		String qId, runId = "hack(h)er2020-";
		int qNum;
		qNum = 0;
		// ordered window
		for (String query : queries) {
			qNum++;
			// make each of the required query nodes and run the queries
			children = genTermNodes(query, index, model);
			queryNode = new OrderedWindow(1, children, index, model);
			results = network.runQuery(queryNode, k);
			qId = "Q" + qNum;
			printResults(results, index, runId + "od1", qId);
		}
		qNum = 0;
		//unordered window
		for (String query : queries) {
			qNum++;
			// make each of the required query nodes and run the queries
			children = genTermNodes(query, index, model);
			int winSize = 3 * children.size();
			queryNode = new UnorderedWindow(winSize, children, index, model);
			results = network.runQuery(queryNode, k);
			qId = "Q" + qNum;
			printResults(results, index, runId + "uw" + winSize, qId);
		}
		qNum = 0;
		//and
		for (String query : queries) {
			qNum++;
			// make each of the required query nodes and run the queries
			children = genTermNodes(query, index, model);
			queryNode = new AndNode(children);
			results = network.runQuery(queryNode, k);
			qId = "Q" + qNum;
			printResults(results, index, runId + "and", qId);
		}
		qNum = 0;
		//or
		for (String query : queries) {
			qNum++;
			// make each of the required query nodes and run the queries
			children = genTermNodes(query, index, model);
			queryNode = new OrNode(children);
			results = network.runQuery(queryNode, k);
			qId = "Q" + qNum;
			printResults(results, index, runId + "or", qId);
		}
		// not using a single complex query
		// #and (alas poor #not (yorick) )
		qNum++;
		children = genTermNodes("yorick", index, model);
		NotNode not = new NotNode(children);
		children = genTermNodes("alas poor", index, model);
		ArrayList<? super QueryNode> combined = new ArrayList<>();
		combined.addAll(children);
		combined.add(not);
		queryNode = new AndNode(children);
		results = network.runQuery(queryNode, k);
		qId = "Q" + qNum;
		printResults(results, index, runId + "and-not", qId);
	}

	private static void printResults(List<Entry<Integer, Double>> results, 
			Index index, String runId, String qId) {
		int rank = 1;
		for (Map.Entry<Integer, Double> entry : results) {
			String sceneId = index.getDocName(entry.getKey());
			String resultLine = qId + " skip " + sceneId + " " + rank + " " 
					+ String.format("%.7f", entry.getValue()) + " " + runId;

			System.out.println(resultLine);
			rank++;
		}
	}
	private static ArrayList<ProximityNode> genTermNodes(String query, Index index, RetrievalModel model) {
		String [] terms = query.split("\\s+");
		ArrayList<ProximityNode> children = new ArrayList<ProximityNode>();
		for (String term : terms) {
			ProximityNode node = new TermNode(term, index, model);
			children.add(node);
		}
		return children;
	}
}
