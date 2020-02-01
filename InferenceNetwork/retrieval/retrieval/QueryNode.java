package retrieval;

/**
 * Top level query node interface.
 *
 */
public interface QueryNode {

	/**
	 * @return the next scoreable document id
	 */
	public Integer nextCandidate();
	
	/**
	 * @param docId the document to score
	 * @return the score
	 */
	public Double score(Integer docId);

	/**
	 * @return true if there are still candidate documents to score
	 */
	public boolean hasMore();

	/**
	 * Advances the query node to or past the given document id. Used to synchronize
	 * the nodes in a query tree before scoring a candidate.
	 * @param docId the document to advance to or past
	 */
	public void skipTo(int docId);

}
