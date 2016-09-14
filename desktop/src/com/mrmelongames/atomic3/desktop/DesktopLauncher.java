package com.mrmelongames.atomic3.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mrmelongames.atomic3.MainScene;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 332;
		config.height = 590;
		//config.useGL30 = true;
		//config.overrideDensity = 120;
		//config.samples = 4;
		new LwjglApplication(new MainScene(), config);
	}
}
