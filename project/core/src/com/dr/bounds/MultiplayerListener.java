package com.dr.bounds;

public interface MultiplayerListener {

	/*
	 * Room updates
	 */
	public void onJoinedRoom();
	public void onLeftRoom();
	public void onRoomConnected();
	public void onRoomCreated();
	
	/*
	 * RealTimeMessages
	 */
	public void onRealTimeMessageRecieved(byte[] msg);
	
	/*
	 * opponent status
	 */
	public void onPeerLeft();
	public void onPeersConnected();
	public void onPeersDisconnected();
	
	/*
	 * Invitations
	 */
	public void onInvitationReceived();
	public void onInvitationRemoved();
}
