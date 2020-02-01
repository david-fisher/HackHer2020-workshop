package retrieval;

import index.Index;

public class Dirichlet extends RetrievalModel {
	private double mu;
	
	public Dirichlet(Index ind, double mu) {
		this.index = ind;
		this.mu = mu;
		collSize = ind.getCollectionSize();
	}

	@Override
	public double scoreOccurrence(int tf, int ctf, int docLen) {
		return Math.log((tf + mu * ctf/collSize)/(mu + docLen));	
	}	
}	
