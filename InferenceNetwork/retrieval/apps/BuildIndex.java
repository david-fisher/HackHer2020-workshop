package apps;

import index.IndexBuilder;

/**
 * Constructs an InvertedIndex from the specified source file.
 * 
 * Usage apps.BuildIndex pathtoinputdatafile
 * @author dfisher
 *
 */
public class BuildIndex {
	public static void main(String[] args) {
		// usage inputFile
		String sourcefile = args[0];
		IndexBuilder builder = new IndexBuilder();
		builder.buildIndex(sourcefile);
	}
}
