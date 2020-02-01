package retrieval;

import java.util.ArrayList;

public class NotNode extends BeliefNode {

	public NotNode(ArrayList<? extends QueryNode> c) {
		super(c);
	}
	
	@Override
	public Double score(Integer docId) {
		return Math.log(1 - Math.exp(children.get(0).score(docId)));
	}
}
