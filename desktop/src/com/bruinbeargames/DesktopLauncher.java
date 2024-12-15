package com.bruinbeargames;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import brunibeargames.Borodino;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		Graphics.DisplayMode mode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		config.setWindowedMode(1, 1);
//		config.setWindowedMode(mode.width, mode.height);
		config.setWindowPosition(1, 1);
		config.setResizable(false);
		config.useVsync(true);
		config.setDecorated(false);
//		config.setWindowIcon(Files.FileType.Internal, "effects/desktopicon.png");
		config.setForegroundFPS(60);
		config.setTitle("Borodino");
		new Lwjgl3Application(new Borodino(), config);
	}
}
