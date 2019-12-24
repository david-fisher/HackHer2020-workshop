package index;

import java.util.Set;

/**
 * Abstract interface of an information retrieval inverted index
 */
public interface Index {
	/**
	 * @param term the term to lookup
	 * @return the posting list for the given term
	 */
	public PostingList getPostings(String term);
	/**
	 * @param term the term to lookup
	 * @return the frequency of the term in the collection
	 */
	public int getTermFreq(String term);
	/**
	 * Get the document frequency of a word
	 * @return number of documents containing the word
	 * @param term the word
	 */
	public int getDocFreq(String term);

	/**
	 * @return the size of the collection in terms
	 */
	public long getCollectionSize();
	/**
	 * @param docId the document id to look up
	 * @return the length of the document
	 */
	public int getDocLength(int docId);
	/**
	 * @return the size of the collection in documents
	 */
	public int getDocCount();

	/**
	 * 
	 * @return the average length of a document in the collection
	 */
	public double getAverageDocLength();

	/**
	 * 
	 * @return the set of terms in the vocabulary, unordered.
	 */
	public Set<String> getVocabulary();

	/**
	 *  Load an index into memory to use.
	 */
	public void load();
		/**
	 * @param key document id number
	 * @return the external document id associated with that number.
	 */
	public String getDocName(int key);
}
