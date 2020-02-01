package retrieval;

import java.util.ArrayList;

public class AndNode extends BeliefNode {

	public AndNode(ArrayList<? extends QueryNode> c) {
		super(c);
	}

	@Override
	public Double score(Integer docId) {
		return children.stream().mapToDouble(c -> c.score(docId)).sum();
	}
}
