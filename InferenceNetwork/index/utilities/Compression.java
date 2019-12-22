package utilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface Compression {
	public void encode(Integer[] posts, ByteBuffer output);
	public void decode(byte[] input, IntBuffer output);
}