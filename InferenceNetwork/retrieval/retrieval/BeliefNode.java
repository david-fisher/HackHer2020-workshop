package retrieval;

import java.util.ArrayList;

/**
 * Evidence combination (belief) nodes
 *
 */
public abstract class BeliefNode implements QueryNode {
	protected ArrayList<? extends QueryNode> children;

	public BeliefNode(ArrayList<? extends QueryNode> c) {
		children = c;
	}

	@Override
	public Integer nextCandidate() {
		int min = Integer.MAX_VALUE; 
		for (QueryNode q : children) {
			if (q.hasMore()) {
				min = Math.min(min, q.nextCandidate());
			}
		}
		return min != Integer.MAX_VALUE ? min : null;
	}

	@Override
	public boolean hasMore() {
		for (QueryNode child : children) {
			if (child.hasMore()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void skipTo(int docId) {
		children.forEach(c -> c.skipTo(docId));
	}
}

