package retrieval;

import java.util.ArrayList;
import java.util.Arrays;

import index.Index;
import index.Posting;

public class OrderedWindow extends Window{

	private Integer distance;

	public OrderedWindow(int d, ArrayList<? extends ProximityNode> children, 
			Index ind, RetrievalModel mod) {
		super(ind, mod);
		this.children = children;
		this.distance = d;
		generatePostings();
	}

	@Override
	protected Posting calculateWindows(ArrayList<Posting> postings) {
		int prev;
		boolean found = false;
		Posting p_result = null; 
		if (postings.size() == 1) {
			return postings.get(0);
		}
		Integer[] p0 = postings.get(0).getPositionsArray();

		for(int i = 0; i < p0.length; i++){
			prev = p0[i];
			for (int j = 1; j < postings.size(); j++){
				ArrayList<Integer> p = new ArrayList<>(Arrays.asList( postings.get(j).getPositionsArray()));
				found = false;
				for (int k = 0; k < p.size(); k++) {
					int cur = p.get(k);
					if (prev < cur && cur <= prev + distance) {
						found = true;
						prev = cur;
						break;
					}
				}
				if (!found) {
					break;
				}
			}
			if (found) {
				if( p_result == null){
					p_result =  new Posting(postings.get(0).getDocId(), p0[i] );
				}
				else{
					p_result.add(p0[i]);
				}
			}
		}
		return p_result;
	}
}
