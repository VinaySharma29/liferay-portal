package com.liferay.portlet.journal.model.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/*
 * ObjectInputCharsetReader implements a CharsetDecoder
 * for a specified number of byte[] read from an underlying
 * ObjectInput.
 */
public class ObjectInputCharsetReader {
	private ObjectInput in = null;
	private Charset cs = null;
	private CharsetDecoder dec = null;

	private int bufSize = 0;
	private int remaining = 0;

	private byte[] tmpBuf = null;
	private ByteBuffer byteBuf = null;
	private CharBuffer charBuf = null;
	
	private boolean eof = false;

	/*
	 * Initialize a new ObjectInputCharsetReader, which will read up to
	 * the limit of byte[] from the provided ObjectInput using three buffers
	 * (byte[], ByteBuffer, and CharBuffer) of bufSize to translate the bytes
	 * to the specified Charset.
	 *
	 * @param in ObjectInput to read byte[] from
	 * @param limit number of bytes to read
	 * @param bufSize size of internal buffers to use (3x)
	 * @param cs Charset to decode the byte[] from.
	 * @return an initialized ObjectInputCharsetReader
	 * @throws IllegalArgumentException if an invalid argument is provided
	 */
	public ObjectInputCharsetReader(ObjectInput in,  int limit, int bufSize, Charset cs) {
		if (in == null) {
			throw new IllegalArgumentException("ObjectInput may not be null");
		}
		if (limit < 1) {
			throw new IllegalArgumentException("limit must be > 0");
		}
		if (bufSize < 1) {
			throw new IllegalArgumentException("bufSize must be > 0");
		}
		if (cs == null) {
			throw new IllegalArgumentException("Charset may not be null");
		}

		this.in = in;
		this.cs = cs;
		this.dec = cs.newDecoder();

		this.bufSize = bufSize;
		this.remaining = limit;

		this.tmpBuf = new byte[bufSize];
		this.byteBuf = ByteBuffer.allocate(bufSize);
		this.charBuf = CharBuffer.allocate(bufSize);
		this.charBuf.position(this.charBuf.capacity());
	}

	/**
	 * read copies up to length characters from the underlying ObjectInput 
	 *  into the given destination array.
	 * If an EOF is encountered because the underlying limit has been
	 * reached and no bytes have been transfered then -1 will be returned,
	 * otherwise the number of bytes written are returned.
	 *
	 * @param cbuf The array into which the characters are to be copied
	 * @param offset The offset within the array of the first character to be written; must be non-negative and no larger than cbuf.length
	 * @param length The number of bytes to copy
	 * @return The number of bytes written, if any, or -1 on EOF
	 * @throws IOException
	 */
	public int read(char[] cbuf, int offset, int length) throws IOException {
		int n = 0;
		while(true) {
			int remaining = charBuf.remaining();
			if (remaining > length) {
				remaining = length;
			}

			charBuf.get(cbuf, offset, remaining);
			n += remaining;
			length -= remaining;
			offset += remaining;

			if (length == 0) {
				break;
			}

			final int c = read();
			if (c == -1) {
				if (n == 0) {
					return -1;
				}
				break;
			}
			n++;
			length--;
			cbuf[offset++] = (char) c;
		}
		return n;
	}

	/**
	 * read returns one character read from the underlying ObjectInput,
	 * or -1 if the underlying limit has been reached.
	 *
	 * @return a char read from the ObjectInput, or -1 on EOF
	 * @ throws IOException
	 */
	public int read() throws IOException {
		while(true) {
			if (charBuf.hasRemaining()) {
				return (int) charBuf.get();
			}
			if (eof) {
				return -1;
			}
			charBuf.clear();
			
			int n = bufSize - byteBuf.position();
			if (n > remaining) {
				n = remaining;
			}

			in.readFully(tmpBuf, 0, n);
			if ((remaining -= n) == 0) {
				eof = true;
			}
			byteBuf.put(tmpBuf, 0, n);

			byteBuf.flip();

			final CoderResult result = dec.decode(byteBuf, charBuf, false);
			if (result.isError()) {
				result.throwException();
			}

			dec.decode(byteBuf, charBuf, true);
			dec.flush(charBuf);
			dec.reset();

			if (byteBuf.hasRemaining()) {
				byteBuf.compact();
			} else {
				byteBuf.clear();
			}	

			charBuf.flip();
		}
	}
}
