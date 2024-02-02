package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.Screens.Menu;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("The Chef's Curse");	
		config.setWindowedMode(1280, 720);
		config.setResizable(false);
		try {
			new Lwjgl3Application(new Menu(), config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


