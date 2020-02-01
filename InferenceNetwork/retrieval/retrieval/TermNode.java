package retrieval;

import index.Index;

public class TermNode extends ProximityNode {
	private String term;
	public TermNode(String t, Index ind, RetrievalModel mod){
		super(ind, mod);
		term = t;
		generatePostings();
	}

	@Override
	protected void generatePostings() {
		postingList = index.getPostings(term);
		ctf = index.getTermFreq(term);
	}
}
