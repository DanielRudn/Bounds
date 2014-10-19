package com.dr.bounds;

public interface MultiplayerListener {

	/*
	 * Connection updates
	 */
	public void onConnected();
	
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
	
	/*
	 * loading invitable/recent players
	 */
	public void onRecentPlayersLoaded(int numLoaded);
	public void onInvitablePlayersLoaded(int numLoaded);
}
