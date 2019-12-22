package utilities;

public class CompressionFactory {
	
public static Compression getCompressor(Compressors c) {
		switch (c) {
		case EMPTY:
			return new EmptyCompression();
		case VBYTE:
			return new VByteCompression();
		}
		return null;
	}
}
