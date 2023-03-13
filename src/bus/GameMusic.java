package bus;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import data.ConnectionFile;

public class GameMusic{
	
	private AudioInputStream audioInputStream;
	private Clip clip;
	private String name;

	public GameMusic() {
		audioInputStream = null;
		clip = null;
		this.name = "";
	}
	public GameMusic(String file) {
		if (file.isEmpty()) {
			audioInputStream = null;
			clip = null;
			this.name = "";
		}else {
			this.setMusic(file);
		}

	}

	public void play() {
		if (clip != null) {
			clip.start();
		}
	}

	public void pause() {
		if (clip != null) {
			clip.stop();
		}
	}

	public void stop() {
		if (clip != null) {
			clip.stop();
			clip.close();
			this.name = "";
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static ArrayList<String> getMusicList() {
		return ConnectionFile.getMusicList();
	}

	public void setMusic(String file) {

		this.stop();

		this.name = file;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(ConnectionFile.getMusic(file).getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
		}
	}
}
