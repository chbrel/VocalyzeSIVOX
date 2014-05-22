/*
 * source : https://github.com/hamilton-lima/jaga/blob/master/jaga%20desktop/src-desktop/com/athanazio/jaga/desktop/sound/Sound.java
 */

package t2s.son;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class JukeBox {
	
	private Player	      player;
	
	private ReentrantLock	mutex;
	
	private String _fileToRead;
	
	private class Player extends Thread {
		
		public AudioInputStream		in;
		public AudioFormat		   decodedFormat;
		public AudioInputStream		din;
		public AudioFormat		   baseFormat;
		public SourceDataLine		line;
		public BufferedInputStream	stream;
		public Boolean		       running, loop, waitUntilAudioEnds, terminated;
		public Player		       next, previous;
		private boolean		       eraseAudioFileAfterPlayback;
		private String		       filename;
		
		public Player(String filename, boolean loop, boolean waitUntilAudioEnds) {
			super();
			
			if(filename != null) {
				this.setName(filename.substring(filename.length() - 10 >= 0 ? filename.length() - 10 : 0));	
				
				this.filename = filename;
				eraseAudioFileAfterPlayback = false;
				running = false;
				terminated = false;
				this.next = null;
				this.previous = null;
				this.loop = loop;
				this.waitUntilAudioEnds = waitUntilAudioEnds;
				try {
					in = AudioSystem.getAudioInputStream(new File(filename));
					stream = new BufferedInputStream(in);
					in = new AudioInputStream(stream, in.getFormat(), in.getFrameLength());
					din = null;
					
					if (in != null) {
						baseFormat = in.getFormat();
						
						decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2,
						        baseFormat.getSampleRate(), false);
						
						din = AudioSystem.getAudioInputStream(decodedFormat, in);
						line = getLine(decodedFormat);
					}
				} catch (final UnsupportedAudioFileException e) {
					e.printStackTrace();
				} catch (final IOException e) {
					e.printStackTrace();
				} catch (final LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
		
		public Player(String filename, boolean loop, boolean waitUntilAudioEnds, boolean eraseAudioFileAfterPlayback) {
			this(filename, loop, waitUntilAudioEnds);
			this.eraseAudioFileAfterPlayback = eraseAudioFileAfterPlayback;
		}
		
		public void stopPlayback(boolean force) {
			
			if (waitUntilAudioEnds && !force) {
				return;
			}
			
			if (next != null) {
				next.stopPlayback(force);
			}
			
			if (running) {
				running = false;
			} else {
				mutex.lock();
				unlink();
			}
			
		}
		
		public void run() {
			
			running = true;
			
			try {
				final byte[] data = new byte[4096];
				boolean firstRun = true;
				while (loop || firstRun) {
					firstRun = false;
					
					if (line != null) {
						
						line.start();
						int nBytesRead = 0;
						
						while (nBytesRead != -1) {
							if (!running) {
								break;
							}
							nBytesRead = din.read(data, 0, data.length);
							if (nBytesRead != -1) {
								line.write(data, 0, nBytesRead);
							}
						}
						
						line.drain();
						line.stop();
						line.close();
						din.close();
						in.close();
					}
				}
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				mutex.lock();
				unlink();
			}
		}
		
		private void unlink() {
			running = false;
			
			if (previous != null) {
				previous.next = next;
			}
			
			if (next != null) {
				next.previous = previous;
				
				if (!next.isAlive() && !next.terminated) {
					next.start();
				}
			}
			
			terminated = true;
			mutex.unlock();
			
			if (eraseAudioFileAfterPlayback) {
				File file = new File(filename);
				file.delete();
			}
			
		}
		
		synchronized public void addNext(Player p, Player parent) {
			
			previous = parent;
			
			if (next != null) {
				next.addNext(p, this);
			} else {
				mutex.lock();
				next = p;
				boolean startNext = true;
				Player ancestor = this;
				while (ancestor != null) {
					if (ancestor.waitUntilAudioEnds && !ancestor.terminated) {
						startNext = false;
						break;
					}
					ancestor = ancestor.previous;
				}
				if (startNext) {
					next.start();
				}
				mutex.unlock();
			}
			
		}
	}
	
	public JukeBox() {
		player = null;
		mutex = new ReentrantLock(true);
		this._fileToRead = null;
	}
	
	 /** 
     * Construit un JuxeBox pour un fichier donne
     * 
     * Method re-written for retrocompatibilty.
     * 
     * @author Ecole Polytechnique de Sophia Antipolis
     * @param s le chemin d'acces au fichier
     * @deprecated
     */
    public JukeBox(String s)
    {
    	this();
    	this._fileToRead = s;
    }
    
    /**
     * Methode pour lire le fichier contenu dans le jukebox
     * 
     * Method re-written for retrocompatibilty.
     * 
     * @author Ecole Polytechnique de Sophia Antipolis
     * @deprecated
     */
    public void playSound()
    {
    	if (this._fileToRead != null)
    	{
    		this.playSound(this._fileToRead, false, false);
    	}
    }
    
    /**
     * Methode qui lit en boucle
     * 
     * Method re-written for retrocompatibilty.
     * 
     * @author Ecole Polytechnique de Sophia Antipolis
     * @deprecated
     */
    public void loopSound()
    {
    	if (this._fileToRead != null)
    	{
    		this.playSound(this._fileToRead, true, false);
    	}
    }
    
    /**
     * Methode qui stoppe la lecture
     * 
     * Method re-written for retrocompatibilty.
     * 
     * @author Ecole Polytechnique de Sophia Antipolis
     * @deprecated
     */
    public void stop()
    {
    	this.stop(false);
    }
	
	private SourceDataLine getLine(final AudioFormat audioFormat) throws LineUnavailableException {
		SourceDataLine res = null;
		final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}
	
	public void playSound(String filename, boolean loop, final boolean waitUntilAudioEnds) {
		Player p = new Player(filename, loop, waitUntilAudioEnds);
		playSound(p);
	}
	
	public void playSound(String filename, boolean loop, final boolean waitUntilAudioEnds, boolean eraseAudioFileAfterPlayback) {
		Player p = new Player(filename, loop, waitUntilAudioEnds, eraseAudioFileAfterPlayback);
		playSound(p);
	}
	
	synchronized private void playSound(Player p) {
		if (player == null) {
			player = p;
			player.start();
		} else {
			player.addNext(p, null);
		}
	}
	
	synchronized public void stop(boolean force) {
		if( player == null ) return;
		Player p = player;
		if (force) {
			player = null;
		}
		p.stopPlayback(force);
	}
	
}
