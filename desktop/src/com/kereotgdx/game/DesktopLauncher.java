package com.kereotgdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1366, 768);
		config.setAudioConfig(32,512,9);
		config.setTitle("KereotGDX");
//		new Lwjgl3Application(new KereotGdxGame(), config);
		new Lwjgl3Application(new Main(), config);
	}
}
