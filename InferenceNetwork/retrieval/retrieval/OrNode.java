package retrieval;

import java.util.ArrayList;

public class OrNode extends BeliefNode {

	public OrNode(ArrayList<? extends QueryNode> c) {
		super(c);
	}

	@Override
	public Double score(Integer docId) {
		Double score = children.stream().mapToDouble(c -> Math.log(1 - Math.exp(c.score(docId)))).sum();
		return Math.log(1 - Math.exp(score));
	}
}
