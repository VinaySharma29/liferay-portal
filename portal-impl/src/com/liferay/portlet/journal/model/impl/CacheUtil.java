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
		
		final char[] cbuf = new char[1024*8];
		final ObjectInputCharsetReader reader =
			new ObjectInputCharsetReader(in, remaining, cbuf.length, UTF8);
		final StringBuilder sbuf = new StringBuilder();

		int n = 0;
		while((n = reader.read(cbuf, 0, cbuf.length)) != -1) {
			sbuf.append(cbuf, 0, n);
		}

		return sbuf.toString();
	}
}
