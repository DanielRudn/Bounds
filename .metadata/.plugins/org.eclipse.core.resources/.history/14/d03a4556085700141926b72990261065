package com.dr.bounds.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dr.bounds.MainGame;
import com.dr.bounds.RequestHandler;

public class DesktopLauncher implements RequestHandler {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		DesktopLauncher l = new DesktopLauncher();
		new LwjglApplication(new MainGame(l), config);
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
		// TODO Auto-generated method stub
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
	public void requestInviteActivity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestInboxActivity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveRoom() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] getRecievedMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearRecievedMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendReliableMessage(byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendUnreliableMessage(byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasNewInvite() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getInviterName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean justJoined() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setJoined(boolean j) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getOpponentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean shouldShowWaitingRoom() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setShowWaitingRoom(boolean w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getOpponentStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isHost() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isMultiplayer() {
		// TODO Auto-generated method stub
		return false;
	}
}
