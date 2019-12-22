package index;

import java.util.ArrayList;
import java.util.List;

/**
 * Fundamental inverted index data type. Contains a document id and list of positions.
 */
public class Posting {

	// TODO: This needs to be converted to use Extents, (begin, end) pairs, so that nested windows work.
	// for terms, this is (pos, pos+1), for windows it is (first.beg, last.end)
	private List<Integer> positions;
	private Integer docId;
	
	/**
	 * Create a new Posting for a term
	 * @param position the first position of the term
	 * @param docId the document id
	 */
	public Posting(Integer docId, Integer position) {
		// TODO: this needs to handle the use of Extents.
		this.positions = new ArrayList<Integer>();
		this.positions.add(position);
		this.docId = docId;
	}

	/**
	 * @param pos position to add to the list of positions
	 */
	public void add(Integer pos){
		// TODO: this needs to handle the use of Extents.
		this.positions.add(pos);
	}

	/**
	 * @return the list of positions as an array
	 */
	public Integer[] getPositionsArray() {
		// TODO: this needs to handle the use of Extents.
		return positions.stream().toArray(Integer[]::new);
	}

	/**
	 * @return the entire posting as an array
	 */
	public ArrayList<Integer> toIntegerArray() {
		// TODO: this needs to handle the use of Extents.
		ArrayList<Integer> ret = new ArrayList<Integer>();
		ret.add(docId);
		ret.add(positions.size());
		ret.addAll(positions);
		return ret;
	}
	/**
	 * @return the number of occurrences of the term within the document
	 */
	public Integer getTermFreq() {		
		return this.positions.size();
	}

	/**
	 * @return the document id of this Posting
	 */
	public Integer getDocId() {
		return docId;
	}
}