package utilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Basic compression interface for converting arrays of integers into
 * compressed arrays of bytes.
 * @author dfisher
 *
 */
public interface Compression {
	/**
	 * @param posts the Integers to compress
	 * @param output the compressed integers as bytes
	 */
	public void encode(Integer[] posts, ByteBuffer output);
	/**
	 * @param input the bytes to decompress
	 * @param output the decompressed bytes as Integers
	 */
	public void decode(byte[] input, IntBuffer output);
}