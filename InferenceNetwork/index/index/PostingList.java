package index;

import java.util.ArrayList;
import java.util.List;

/**
 * Inverted List interface
 * NB: the interface is minimalist and could present more support for iteration
 * @author dfisher
 *
 */
public class PostingList {
	private List<Posting> postings;
	private int postingsIndex;
	public PostingList () {
		postings = new ArrayList<Posting>();
		postingsIndex = -1;
	}
	/**
	 * reset the list pointer to the first element
	 */
	public void startIteration () {
		postingsIndex = 0;
	}
	/**
	 * are there any more?
	 * @return true if there are remaining elements in the list, otherwise false
	 */
	public boolean hasMore() {
		return (postingsIndex >= 0 && postingsIndex < postings.size());
	}
	/**
	 * skip to or past the specified document id
	 * @param docid the id to skip to
	 * 
	 */
	public void skipTo (int docid) {
		while (postingsIndex < postings.size() &&
				getCurrentPosting().getDocId() < docid) {
			postingsIndex++;
		}
	}
	/**
	 * 
	 * @return the current posting in the list or null if the list is empty
	 * or consumed
	 */
	public Posting getCurrentPosting() {
		Posting retval = null;
		try {
			retval = postings.get(postingsIndex);
		} catch (IndexOutOfBoundsException ex) {
			// ignore
		}
		return retval;
	}
	/**
	 * @param index the index of the Posting to return
	 * @return the index'th Posting of the list
	 */
	public Posting get(int index) {
		return postings.get(index);
	}
	/**
	 * @return the number of Postings in the list
	 */
	public int documentCount() {
		return postings.size();
	}
	/**
	 * @param posting the Posting to add
	 */
	public void add(Posting posting) {
		postings.add(posting);
		postingsIndex++;
	}

	/**
	 * @param docid the document to add a position to
	 * @param position the position to add
	 */
	public void add(Integer docid, Integer position) {
		Posting current = getCurrentPosting();
		if (current != null && current.getDocId().equals(docid) ) {
			current.add(position);
		} else { 
			Posting posting = new Posting(docid, position);
			add(posting);
		}
	}
	/**
	 * Transform to an array of integers for encoding
	 * @return the PostingList represented as a flat array of Integers
	 */
	public Integer[] toIntegerArray () {
		ArrayList <Integer> retval = new ArrayList<Integer>();
		// format is (docid count positions)+
		for (Posting p : postings) {
			retval.addAll(p.toIntegerArray());
		}
		return retval.toArray(new Integer[retval.size()]);

	}
	/**
	 * Construct a PostingList from an array of int
	 * @param input the array of int values constituting the PostingList.
	 */
	public void fromIntegerArray(int[] input) {
		// format is (docid count positions)+
		int idx = 0;
		while (idx < input.length) {
			int docid = input[idx++];
			int count = input[idx++];
			for (int j = 0; j < count; j++) {
				int position = input[idx++];
				add(docid, position);
			}
		}
		postingsIndex = 0; // reset the list pointer
	}

	/**
	 * @return The frequency of the term in the collection
	 */
	public int termFrequency() {
		return postings.stream().mapToInt(p -> p.getTermFreq()).sum();
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		int savedIdx = postingsIndex;
		startIteration();
		while (hasMore()) {
			Posting p = getCurrentPosting();
			int doc = p.getDocId();
			Integer [] positions = p.getPositionsArray();
			buf.append("{").append(doc).append(", ");
			buf.append(positions.length).append(" [");

			for (int i : positions) {
				buf.append(i).append(" ");
			}
			buf.append(" ]} ");
			skipTo(doc  + 1);
		}
		postingsIndex = savedIdx;
		return buf.toString();
	}
}
