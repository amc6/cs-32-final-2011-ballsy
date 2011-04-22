package ballsy;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioClip {
	private Clip _clip;
	public AudioClip(String aspath) {
		try {
			File mediafile = new File(aspath);
			AudioInputStream ais = AudioSystem.getAudioInputStream(mediafile);
			_clip = AudioSystem.getClip();
			_clip.open(ais);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		_clip.setFramePosition(0);
		_clip.start();
		_clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		_clip.stop();
	}
	
	
	public void close() {
		_clip.stop();
		_clip.close();
		_clip = null;		
	}
}
