package com.dr.bounds;

public interface RequestHandler {

	public void requestSignIn();
	
	public void requestSignOut();
	
	public void requestInviteActivity();
	
	public void requestInboxActivity();
	
	public void leaveRoom();
	
	public String getCurrentAccountName();
	
	public String getOpponentName();
	
	public boolean isMultiplayer();
	
	public boolean isHost();
	
	public int getOpponentStatus();
	
	public boolean isConnected();
	
	public boolean isConnecting();
	
	public String getInviterName();
	
	public void sendReliableMessage(byte[] message);
	
	public void sendUnreliableMessage(byte[] message);
}
