package com.mygdx.game.helpers;

public class SoundPaths
{
	
	public static final String SHOTGUN_PATH = "sound/shotgunFire.wav";
	public static final String REDGUN_PATH = "sound/redgunFire.wav";
    //might add different ones for this. I don't know. Sounds fine as it is
	public static final String PLAYERHIT_PATH = "sound/hit.wav";
	public static final String ENEMYHIT_PATH = "sound/hit.wav";
	public static final String PLAYERDEAD_PATH = "sound/dead.wav";
	public static final String ENEMYDEAD_PATH = "sound/enemyDies.wav";
	public static final String ENEMYGUN_PATH = "sound/enemyShoots.wav";
	public static final String BUTTON_PATH = "sound/buttonPress.wav";
	public static final String BUYUPGRADE_PATH = "sound/upgrade.wav";
	
    //this one uses .ogg because there is a slight delay at the start for other formats and it was making the looping sound bad
	public static final String MUSIC_PATH = "sound/music.ogg";
	private static float volume;
	private static float musicVolume;
	private static SoundPaths instance;

	// Singleton
    public static SoundPaths getInstance() {
        if(instance == null) instance = new SoundPaths();
        return instance;
    }

	public SoundPaths() {
		volume = 0.1f;
		musicVolume = 0.01f;
	}

	public void setVolume(float newVol) {
		volume = newVol;
	}

	public float getVolume() {
		return volume;
	}

	public void setMusicVolume(float newVol) {
		volume = newVol;
	}

	public float getMusicVolume() {
		return musicVolume;
	}
}