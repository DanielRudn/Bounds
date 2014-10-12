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
	
	public boolean justJoined();
	
	public boolean shouldShowWaitingRoom();
	
	public boolean isHost();
	
	public void setShowWaitingRoom(boolean w);
	
	public int getOpponentStatus();
	
	public void setJoined(boolean j);
	
	public boolean isConnected();
	
	public boolean isConnecting();
	
	public byte[] getRecievedMessage();
	
	public boolean hasNewInvite();
	
	public String getInviterName();
	
	public void clearRecievedMessage();
	
	public void sendReliableMessage(byte[] message);
	
	public void sendUnreliableMessage(byte[] message);
}
