package index;

/**
 * Constants used by the Index interface implementers.
 * @author dfisher
 *
 */
public class Constants {

	/**
	 * The term to PostingList data lookup table as a text file
	 */
	static final String LOOKUP_TXT = "lookup.txt";	
    /**
     * The document lengths lookup table, as a text file.
     */
    static final String DOC_LENGTH_TXT = "docLength.txt";
	/**
	 * The internal document id to play id lookup table, as a text file
	 */
	static final String PLAY_IDS_TXT = "playIds.txt";
	/**
	 * The internal document id to scene id lookup table, as a text file
	 */
	static final String SCENE_ID_TXT = "sceneId.txt";
	/**
	 * The PostingLists as a binary file.
	 */
	static final String INVLIST = "invlist";
}
