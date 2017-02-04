package com.powerpong.game;

import com.badlogic.gdx.ApplicationAdapter;
import states.*;

public class PowerPong extends ApplicationAdapter {
	public static final float PPM = 100f; //The number of pixels in one meter
	//basically, libgdx drawing works in pixels, but box2d works in meters. Using 1 pixel = 1 meter results in very bad
	//physics simulation. So whenever you draw, you have to divide both the coordinates and the dimensions by PPM
	//tbh it is pretty confusing

	public static int NATIVE_WIDTH = 1440;
	public static int NATIVE_HEIGHT = 2560;
	private GameStateManager gsm;
	
	@Override
	public void create () {
		gsm = new GameStateManager();
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		gsm.render();
	}
	
	@Override
	public void dispose () {
		gsm.dispose();
	}
}
