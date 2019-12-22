package utilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class VByteCompression implements Compression {
	
	private void deltaEncode(Integer [] input) {
		// presumes (docid count positions)+ format
		int idx = 0;
		int savedDocid = 0;
		int savedPos = 0;
		int firstPos = 0;
		int firstDocid = input[idx++];
		while (idx < input.length) {
			int count = input[idx++];
			try {
				// do the deltas on the real doc ids, not the deltas
				savedDocid = input[idx+count];
				input[idx+count] -= firstDocid;
				firstDocid = savedDocid;
			} catch (ArrayIndexOutOfBoundsException ex) {
				// trying to do last + 1 docid, ignore the exception
			}
			firstPos = input[idx];
			for (int j = 1; j < count; j++) {
				// do the deltas on the real positions, not the deltas
				savedPos = input[idx + j];
				input[idx + j] -= firstPos;
				firstPos = savedPos;
			}
			idx += count + 1;
		}
	}
	
	private void deltaDecode(IntBuffer output) {
		// presumes (docid count positions)+ format
		int [] ints = output.array();
		int idx = 0;
		while (idx < output.position()) {
			//first decode docids
			int firstDocid = ints[idx++];
			int count = ints[idx++];
			try {
				ints[idx+count] += firstDocid;
			} catch (ArrayIndexOutOfBoundsException ex) {
				// no more elements
			}
			// next decode the positions
			int firstPos = ints[idx];
			for (int j = 1; j < count; j++) {
				ints[idx + j] += firstPos;
				firstPos = ints[idx + j];
			}
			idx += count;
		}
	}

    @Override
	public void encode(Integer[] input, ByteBuffer output) {
    	deltaEncode(input);
        for (int i : input) {
            while (i >= 128) {
                output.put((byte)(i & 0x7f));
                i >>>= 7;
            }
            output.put((byte)(i | 0x80));
        }
    }
    @Override
	public void decode(byte[] input, IntBuffer output) {
         for (int i = 0; i < input.length; i++) {
            int position = 0;
            int result = (input[i] & 0x7F);

            while ((input[i] & 0x80) == 0) {
                i++;
                position++;
                int unsignedByte = input[i] & 0x7F;
                result |= (unsignedByte << (7*position));
            }
            output.put(result);
        }
        deltaDecode(output);
    }
    
}
