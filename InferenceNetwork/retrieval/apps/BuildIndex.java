package apps;

import index.IndexBuilder;

public class BuildIndex {
	public static void main(String[] args) {
		// usage inputFile true|false
		String sourcefile = args[0];
		IndexBuilder builder = new IndexBuilder();
		builder.buildIndex(sourcefile);
	}
}
