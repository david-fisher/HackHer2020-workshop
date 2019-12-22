package apps;

import java.util.SortedSet;
import java.util.TreeSet;

import index.Index;
import index.InvertedIndex;
import index.PostingList;

public class DumpPostingLists {
	public static void main(String[] args) {    	
		Index index = new InvertedIndex();
		index.load();
		//Get it sorted sorta almost for free... NB: Index could return SortedSet
		SortedSet<String> vocabulary = new TreeSet<String>(index.getVocabulary());
		for (String term : vocabulary) {
			PostingList list = index.getPostings(term);
	        System.out.println(term + " -> " + list.toString());	
		}
	}
}
