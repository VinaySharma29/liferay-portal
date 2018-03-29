package com.liferay.portlet.journal.model.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.charset.Charset;

import com.liferay.portal.kernel.util.StringPool;

/**
 * CacheUtil provides wrapper functions to aid in the serialization
 * and deserialization of cache objects.
 */
public class CacheUtil {

	private static final Charset UTF8 = Charset.forName("UTF-8");

	/**
	 * writeUTF writes String s to ObjectOutput out using an int and a
	 * byte[].  The int specifies the size of the byte[], and the byte[], if
	 * the int is > 0, contains a UTF-8 encoded String.
	 */
	public static void writeUTF(ObjectOutput out, String s) throws IOException {
		final byte[] buf = s.getBytes(UTF8);
		out.writeInt(buf.length);
		if (buf.length > 0) {
			out.write(buf);
		}
	}

	/**
	 * readUTF reads a String from ObjectInput in.  It assumes the String was
	 * written using writeUTF.  It expects an int indicating the size of the following
	 * byte[].  When int is < 1 it returns StringPool.BLANK, otherwise it will read the
	 * specified number of bytes and convert the resulting byte[] array into a String
	 * using the UTF-8 charset decoder.
	 */
	public static String readUTF(ObjectInput in) throws IOException {
		int remaining = in.readInt();
		if (remaining < 1) {
			return StringPool.BLANK;
		}

		// note that we're not being very memory efficent here, we're
		// ultimately using 3x the size of the byte[] to (a) build up the
		// ByteArrayOutputStream then and (b) convert that into a String.
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(remaining);
		final byte[] tmp = new byte[remaining];
		int n = 0;
		while ( (n = in.read(tmp, 0, remaining)) != -1 && remaining > 0 ) {
			remaining -= n;
			bos.write(tmp, 0, n);
		}

                if (remaining > 0) {
                       throw new IOException("truncated read of UTF from ObjectInput:  " + remaining + " bytes still expected...");
                }

                return new String(bos.toByteArray(), UTF8);
	}
}
