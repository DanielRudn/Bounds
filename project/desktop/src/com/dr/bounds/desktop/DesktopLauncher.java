package com.dr.bounds.desktop;

import java.util.Random;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dr.bounds.MainGame;
import com.dr.bounds.RequestHandler;

public class DesktopLauncher implements RequestHandler {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		new Random();
		DesktopLauncher l = new DesktopLauncher();
		MainGame game = new MainGame(l);
		new LwjglApplication(game, config);
	}

	@Override
	public void requestSignIn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestSignOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCurrentAccountName() {
		return "Daniel Rudn";
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isConnecting() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void submitToLeaderboard(int data, String leaderboardID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLeaderboard(String leaderboardID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showShareIntent(String text) {
		// TODO Auto-generated method stub
		
	}
}
