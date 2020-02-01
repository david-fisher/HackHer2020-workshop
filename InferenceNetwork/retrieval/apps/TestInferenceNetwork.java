package apps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import index.Index;
import index.InvertedIndex;
import retrieval.AndNode;
import retrieval.Dirichlet;
import retrieval.InferenceNetwork;
import retrieval.OrNode;
import retrieval.OrderedWindow;
import retrieval.ProximityNode;
import retrieval.QueryNode;
import retrieval.RetrievalModel;
import retrieval.TermNode;
import retrieval.UnorderedWindow;
/*
 * *
Q1: the king queen royalty
Q2: servant guard soldier
Q3: hope dream sleep
Q4: ghost spirit
Q5: fool jester player
Q6: to be or not to be
Q7: alas
Q8: alas poor
Q9: alas poor yorick
Q10: antony strumpet

Please run these queries using the two phrase operators, ordered window and unordered window.
For ordered, use a distance of 1 (exact phrase), for unordered, use a window width 
3*|Q| (three times the length of the query). Please run these queries with each of the 
operators: SUM, AND, OR, and MAX. Use dirichlet smoothing with μ=1500 for all runs.
 */
public class TestInferenceNetwork {

	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		Index index = new InvertedIndex();
		index.load();

		RetrievalModel model = new Dirichlet(index, 1500);
		List<Map.Entry<Integer, Double>> results;
		InferenceNetwork network = new InferenceNetwork();
		QueryNode queryNode;
		ArrayList<ProximityNode> children;
		
		// read in the queries
		String queryFile = args[2];
		List<String> queries = new ArrayList<String>();
		try {
			String query;

			BufferedReader reader = new BufferedReader(new FileReader(queryFile));
			while (( query = reader.readLine()) != null) {
				queries.add(query);
			}
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String outfile, runId, qId;
		int qNum = 0;
		
		outfile = "od1.trecrun";
		runId = "dafisher-od1-dir-1500";
		for (String query : queries) {
			qNum++;
			// make each of the required query nodes and run the queries
			children = genTermNodes(query, index, model);
			queryNode = new OrderedWindow(1, children, index, model);
			results = network.runQuery(queryNode, k);
			qId = "Q" + qNum;
			boolean append = qNum > 1;
			try {
				PrintWriter writer = new PrintWriter(new FileWriter(outfile, append));
				printResults(results, index, writer, runId, qId);
				writer.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//unordered
		outfile = "uw.trecrun";
		runId = "dafisher-uw-dir-1500";
		qNum = 0;
		for (String query : queries) {
			qNum++;
			// make each of the required query nodes and run the queries
			children = genTermNodes(query, index, model);
			int winSize = 3 * children.size();
			queryNode = new UnorderedWindow(winSize, children, index, model);
			results = network.runQuery(queryNode, k);
			qId = "Q" + qNum;
			boolean append = qNum > 1;
			try {
				PrintWriter writer = new PrintWriter(new FileWriter(outfile, append));
				printResults(results, index, writer, runId, qId);
				writer.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}


		// and
		outfile = "and.trecrun";
		runId = "dafisher-and-dir-1500";
		qNum = 0;
		for (String query : queries) {
			qNum++;
			// make each of the required query nodes and run the queries
			children = genTermNodes(query, index, model);
			queryNode = new AndNode(children);
			results = network.runQuery(queryNode, k);
			qId = "Q" + qNum;
			boolean append = qNum > 1;
			try {
				PrintWriter writer = new PrintWriter(new FileWriter(outfile, append));
				printResults(results, index, writer, runId, qId);
				writer.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		// or
		outfile = "or.trecrun";
		runId = "dafisher-or-dir-1500";
		qNum = 0;
		for (String query : queries) {
			qNum++;
			// make each of the required query nodes and run the queries
			children = genTermNodes(query, index, model);
			queryNode = new OrNode(children);
			results = network.runQuery(queryNode, k);
			qId = "Q" + qNum;
			boolean append = qNum > 1;
			try {
				PrintWriter writer = new PrintWriter(new FileWriter(outfile, append));
				printResults(results, index, writer, runId, qId);
				writer.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void printResults(List<Entry<Integer, Double>> results, 
			Index index, PrintWriter writer, String runId, String qId) {
		int rank = 1;
		for (Map.Entry<Integer, Double> entry : results) {
			String sceneId = index.getDocName(entry.getKey());
			String resultLine = qId + " skip " + sceneId + " " + rank + " " 
					+ String.format("%.7f", entry.getValue()) + " " + runId;

			writer.println(resultLine);
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
