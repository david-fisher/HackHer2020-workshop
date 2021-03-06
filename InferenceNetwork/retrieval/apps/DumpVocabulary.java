package apps;

import java.util.SortedSet;
import java.util.TreeSet;

import index.Index;
import index.InvertedIndex;

/**
 * Dumps an index's vocabulary to the standard output.
 * Must be run in the directory containing the index files.
 * @author dfisher
 *
 */
public class DumpVocabulary {
	public static void main(String[] args) {    	
		Index index = new InvertedIndex();
		index.load();
		//Get it sorted sorta almost for free... NB: Index could return SortedSet
		SortedSet<String> vocabulary = new TreeSet<String>(index.getVocabulary());
		for (String term : vocabulary) {
			int freq = index.getTermFreq(term);
			int docFreq = index.getDocFreq(term);
	        System.out.println(term + "  " + freq + " " + docFreq);	
		}
	}
}
