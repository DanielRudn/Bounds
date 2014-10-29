package com.dr.bounds.desktop;

import java.util.Random;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dr.bounds.MainGame;
import com.dr.bounds.MultiplayerListener;
import com.dr.bounds.RequestHandler;

public class DesktopLauncher implements RequestHandler {
	
	private static MultiplayerListener listener;
	private static Random random;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		random = new Random();
		DesktopLauncher l = new DesktopLauncher();
		MainGame game = new MainGame(l);
		listener = game;
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
	public void sendReliableMessage(byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendUnreliableMessage(byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInviterName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOpponentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOpponentStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isHost() {
		return true;
	}

	@Override
	public boolean isMultiplayer() {
		return false;
	}

	@Override
	public void loadRecentlyPlayedWithPlayers() {
		listener.onRecentPlayersLoaded(2);
	}

	@Override
	public String getRecentPlayerName(int index) {
		return "Recents " + (index+1);
	}

	@Override
	public void loadInvitablePlayers() {
		listener.onInvitablePlayersLoaded(10);
	}

	@Override
	public String getInvitablePlayerName(int index) {
		if(index == 2)
		{
			return "Michael Roudnintski";
		}
		else if(index == 1)
		{
			return "Daniel Rudnitski";
		}
		else if(index == 3)
		{
			return "Adolf Hitler";
		}
		else if(index == 4)
		{
			return "Joseph Stalin";
		}
		else if(index == 5)
		{
			return "This is a very long name lol";
		}
		return "Invitable " + (index+1);
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
	public String getRecentPlayerID(int index) {
		// TODO Auto-generated method stub
		return Long.toString(random.nextLong());
	}

	@Override
	public String getInvitablePlayerID(int index) {
		// TODO Auto-generated method stub
		return Long.toString(random.nextLong());
	}

	@Override
	public void sendInvite(String idToInvite) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadInvitations() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInvitationID(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInviterName(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void acceptInvite(String idToAccept) {
		// TODO Auto-generated method stub
		
	}
}
