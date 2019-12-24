package index;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import utilities.EmptyCompression;


/**
 * Lookup table entry for accessing stored inverted lists. Provides file offset, number of bytes,
 * document term frequency and collection term frequency.
 * @author dfisher
 *
 */
class LookUp {
	public LookUp(long offset2, long numBytes2, int dtf2, int ctf2) {
		this.offset = offset2;
		this.numBytes = numBytes2;
		this.dtf = dtf2;
		this.ctf = ctf2;
	}
	long offset;
	long numBytes;
	int dtf;
	int ctf;
};

/**
 * Concrete implementation of an inverted index for document retrieval.
 * @author dfisher
 *
 */
public class InvertedIndex implements Index {
	private Map<Integer, String> sceneIdMap = new HashMap<Integer, String>();
	private Map<Integer, String> playIdMap = new HashMap<Integer, String>();
	private Map<Integer, Integer> docLengths = new HashMap<Integer, Integer>();
	private Map<String, LookUp> lookup = new HashMap<String, LookUp>(); // key = term
	private EmptyCompression compressor;
	private long collectionSize;
	private double aveDocLen;
	private int numOfDoc;
	private String termInvListFile;

	@Override
	public void load() {
		compressor = new EmptyCompression();
		termInvListFile = Constants.INVLIST;
		loadStringMap(Constants.SCENE_ID_TXT, sceneIdMap);
		loadStringMap(Constants.PLAY_IDS_TXT, playIdMap);
		loadDocLengths(Constants.DOC_LENGTH_TXT);
		loadLookUp(Constants.LOOKUP_TXT);

	}

	private void loadStringMap(String fileName, Map<Integer, String> map) {
		String line;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((line = bufferedReader.readLine()) != null) {
				String[] data = line.split("\\s+");
				map.put(Integer.parseInt(data[0]), data[1]);
			}
			bufferedReader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void loadDocLengths(String fileName) {
		String line;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			long totalLength = 0;

			while((line = bufferedReader.readLine()) != null) {
				String[] data = line.split("\\s+");
				int docLen = Integer.parseInt(data[1]);
				docLengths.put(Integer.parseInt(data[0]), docLen);
				totalLength += docLen;
			}
			collectionSize = totalLength;
			numOfDoc = docLengths.keySet().size();
			aveDocLen = 1.0 * collectionSize / numOfDoc;
			bufferedReader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void loadLookUp(String fileName) {
		String line;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			// termOffset bytesWritten docTermFreq collectionTermFreq);
			while((line = bufferedReader.readLine()) != null) {
				String[] data = line.split("\\s+");
				String term = data[0];
				long offset = Long.parseLong(data[1]);
				long numBytes = Long.parseLong(data[2]);
				int dtf = Integer.parseInt(data[3]);
				int ctf = Integer.parseInt(data[4]);
				LookUp look = new LookUp(offset, numBytes, dtf, ctf);

				lookup.put(term, look);
			}
			bufferedReader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public PostingList getPostings(String word) {
		PostingList invertedList = new PostingList();
		try {
			RandomAccessFile reader = new RandomAccessFile(termInvListFile, "rw");
			LookUp look = lookup.get(word);
			reader.seek(look.offset);
			int buffLength =(int)(look.numBytes);
			byte[] buffer = new byte[buffLength];
			int numRead = reader.read(buffer, 0, buffLength);
			assert numRead == look.numBytes;
			IntBuffer intBuffer = IntBuffer.allocate(buffer.length);
			compressor.decode(buffer, intBuffer);   
			int[] data = new int[intBuffer.position()];
			intBuffer.rewind();
			intBuffer.get(data);
			invertedList.fromIntegerArray(data);
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return invertedList;
	}

	@Override
	public Set<String> getVocabulary() {
		// could return this as a sorted set...
		return lookup.keySet();
	}


	@Override
	public int getDocFreq(String term) {
		int retval = 0;
		if (lookup.containsKey(term)) {
			retval = lookup.get(term).dtf;
		}
		return retval;
	}

	@Override
	public int getTermFreq(String term) {
		int retval = 0;
		if (lookup.containsKey(term)) {
			retval = lookup.get(term).ctf;
		}
		return retval;
	}

	@Override
	public int getDocCount() {
		return numOfDoc;
	}

	@Override
	public int getDocLength(int docId) {
		return docLengths.get(docId);
	}

	@Override
	public double getAverageDocLength() {
		return aveDocLen;
	}

	@Override
	public long getCollectionSize() {
		return collectionSize;
	}

	public String getPlay(int docId) {
		return playIdMap.get(docId);
	}

	public String getScene(int docId) {
		return sceneIdMap.get(docId);
	}

	@Override
	public String getDocName(int docId) {
		return getScene(docId);
	}
}
