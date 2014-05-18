/*
 * ﻿Copyright 2004-2007, Christian BREL, Hélène COLLAVIZZA, Sébastien MOSSER, Jean-Paul STROMBONI,
 * This file is part of project 'VocalyzeSIVOX'
 * 'VocalyzeSIVOX' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 'VocalyzeSIVOX'is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with 'VocalyzeSIVOX'. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * SequenceAudioInputStream.java
 * Copyright (c) 1999 - 2001 by Matthias Pfisterer
 */
package t2s.son;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class SequenceAudioInputStream extends AudioInputStream {
	
	private final List<AudioInputStream>	m_audioInputStreamList;
	private int	       m_nCurrentStream;
	
	public SequenceAudioInputStream(final AudioFormat audioFormat, final Collection<AudioInputStream> audioInputStreams) {
		super(new ByteArrayInputStream(new byte[0]), audioFormat, AudioSystem.NOT_SPECIFIED);
		this.m_audioInputStreamList = new ArrayList<AudioInputStream>(audioInputStreams);
		this.m_nCurrentStream = 0;
	}
	
	private AudioInputStream getCurrentStream() {
		return (AudioInputStream) this.m_audioInputStreamList.get(this.m_nCurrentStream);
	}
	
	private boolean advanceStream() {
		this.m_nCurrentStream++;
		final boolean bAnotherStreamAvailable = (this.m_nCurrentStream < this.m_audioInputStreamList.size());
		return bAnotherStreamAvailable;
	}
	
	@Override
	public long getFrameLength() {
		long lLengthInFrames = 0;
		final Iterator<AudioInputStream> streamIterator = this.m_audioInputStreamList.iterator();
		while (streamIterator.hasNext()) {
			final AudioInputStream stream = streamIterator.next();
			final long lLength = stream.getFrameLength();
			if (lLength == AudioSystem.NOT_SPECIFIED) {
				return AudioSystem.NOT_SPECIFIED;
			} else {
				lLengthInFrames += lLength;
			}
		}
		return lLengthInFrames;
	}
	
	@Override
	public int read() throws IOException {
		final AudioInputStream stream = getCurrentStream();
		final int nByte = stream.read();
		if (nByte == -1) { /* The end of the current stream has been signaled.We try to advance to the next stream. */
			final boolean bAnotherStreamAvailable = advanceStream();
			if (bAnotherStreamAvailable) { /* There is another stream. We recurse into this method to read from it. */
				return read();
			} else { /* No more data. We signal EOF. */
				return -1;
			}
		} else {/* The most common case: We return the byte. */
			return nByte;
		}
	}
	
	@Override
	public int read(final byte[] abData, final int nOffset, final int nLength) throws IOException {
		final AudioInputStream stream = getCurrentStream();
		final int nBytesRead = stream.read(abData, nOffset, nLength);
		if (nBytesRead == -1) { /* The end of the current stream has been signaled. We try to advance to the next stream. */
			final boolean bAnotherStreamAvailable = advanceStream();
			if (bAnotherStreamAvailable) { /* There is another stream. We recurse into this method to read from it. */
				return read(abData, nOffset, nLength);
			} else { /* No more data. We signal EOF. */
				return -1;
			}
		} else { /* The most common case: We return the length. */
			return nBytesRead;
		}
	}
	
	@Override
	public long skip(final long lLength) throws IOException {
		throw new IOException("skip() is not implemented.");
	}
	
	@Override
	public int available() throws IOException {
		return getCurrentStream().available();
	}
	
	@Override
	public void close() throws IOException {
	}
	
	@Override
	public void mark(final int nReadLimit) {
		throw new RuntimeException("mark() is not implemented.");
	}
	
	@Override
	public void reset() throws IOException {
		throw new IOException("reset() is not implemented.");
	}
	
	@Override
	public boolean markSupported() {
		return false;
	}
	
	@SuppressWarnings("unused")
	private static void out(final String strMessage) {
		System.out.println(strMessage);
	}
}

/*** SequenceAudioInputStream.java ***/
