package edu.cmu.cs.cs214.rec06.alarmclock;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Plays a beeping sound when the alarm goes off.
 */
public class Beeper implements AlarmListener {

	private final Clip alarmSound;

	public Beeper() {
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(
				new File("src/main/resources/alarm.wav"));
			alarmSound = AudioSystem.getClip();
			alarmSound.open(stream);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void onAlarmEvent(AlarmClock source, Date time) {
		alarmSound.start();
	}

}
