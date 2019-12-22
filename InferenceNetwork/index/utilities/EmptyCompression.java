package utilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Vacuous compression implementation, copies the input to the output.
 * @author dfisher
 *
 */
public class EmptyCompression implements Compression {
	@Override
	public void encode(Integer[] input, ByteBuffer output) {
		for (int i : input) {
			output.putInt(i);
		}
	}
	@Override
	public void decode(byte[] input, IntBuffer output) {
		ByteBuffer bytes = ByteBuffer.wrap(input);
		bytes.rewind();
		output.put(bytes.asIntBuffer());
	}
}