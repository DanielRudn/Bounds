package com.dr.bounds.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dr.bounds.MainGame;
import com.dr.bounds.MultiplayerListener;
import com.dr.bounds.RequestHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.plus.Plus;

public class AndroidLauncher extends AndroidApplication implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
	RequestHandler, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener, OnInvitationReceivedListener{
	
	private GoogleApiClient apiClient;
	
	private final int RC_INVITE = 10000;
	private final int RC_CONNECTION_RESOLUTION = 1361;
	private final int RC_WAITING_ROOM = 10001;
	private final int RC_INBOX = 10002;
	
	private Room currentRoom = null;
	private String currentRoomID = null;
	private Invitation cInv = null; // current invitation
	private Participant opponent = null;
	
	private boolean isHost = false;
	
	// multiplayer listener called when opponent joins,  leaves, message received etc...
	private MultiplayerListener multiplayerListener;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// create API client with access to google plus and games services.
		apiClient = new GoogleApiClient.Builder(this)
	     .addConnectionCallbacks(this)
	     .addOnConnectionFailedListener(this)
	     .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
	     .addApi(Games.API).addScope(Games.SCOPE_GAMES)
	     .build();
		
		Log.d("BOUNDS","apiCient built, isNull: " + (apiClient == null));
		
		MainGame game = new MainGame(this);
		multiplayerListener = game;
		initialize(game, config);
		
		Log.d("BOUNDS","initialized");
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		Log.d("BOUNDS","onStart() called");
		// try to connect
		if(apiClient != null && apiClient.isConnected())
		{
			Log.d("BOUNDS","already connected on onStart()");
		}
		else
		{
			Log.d("BOUNDS","Connecting client..");
			apiClient.connect();
		}
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		if(apiClient.isConnected())
		{
			Log.d("BOUNDS","Disconnecting!");
			apiClient.disconnect();
		}
		else
		{
			Log.d("BOUNDS","Not connected");
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == RC_CONNECTION_RESOLUTION)
		{
			Log.d("BOUNDS","Request Code RC_CONNECTION_RESOLUTION returned! resultCode: " + resultCode);
			if(resultCode == RESULT_OK)
			{
				if(apiClient.isConnecting() == false)
				{
					apiClient.connect();
					Log.d("BOUNDS","Result is ok, attempting to reconnect");
				}
			}
		}
		else if(requestCode == RC_INVITE)
		{
			if(resultCode != RESULT_OK)
			{
				// user cancelled
				return;
			}
			
			// get list of invited people
			Bundle extras = data.getExtras();
			final ArrayList<String> invited = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
			
			// get auto-match criteria
			// none atm
			
			// create room
			RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
			roomConfigBuilder.addPlayersToInvite(invited);
			RoomConfig roomConfig = roomConfigBuilder.build();
			Games.RealTimeMultiplayer.create(apiClient, roomConfig);
			
			// set this player as host
			isHost = true;
			
			// show waiting room (test, might have to remove)
		  // 	shouldShowWaitingRoom = true;
		   	
			
			// prevent sleep
			preventSleep();
		}
		else if(requestCode == RC_WAITING_ROOM)
		{
			if(resultCode == RESULT_OK)
			{
				// all players connected successfully
			}
			else if(resultCode == RESULT_CANCELED)
			{
				// player closed waiting room with back button, leave room
				Games.RealTimeMultiplayer.leave(apiClient, this, currentRoomID);
				enableSleep();
				isHost = false;
			}
			else if(resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM)
			{
				// player clicked leave room
				Games.RealTimeMultiplayer.leave(apiClient, this, currentRoomID);
				enableSleep();
				isHost = false;
			}
		}
		else if(requestCode == RC_INBOX)
		{
			if(resultCode != RESULT_OK)
			{
				// user canclled
				return;
			}
			
			// get selected invitation
			Bundle extras = data.getExtras();
			cInv = extras.getParcelable(Multiplayer.EXTRA_INVITATION);
			
			RoomConfig roomConfig = makeBasicRoomConfigBuilder()
					.setInvitationIdToAccept(cInv.getInvitationId())
					.build();
			
			// join room
			Games.RealTimeMultiplayer.join(apiClient, roomConfig);
			
			// show waiting room (test, might have to remove)
		 //  	shouldShowWaitingRoom = true;
			
			// prevent sleep
			preventSleep();
		}
	}
	
	private RoomConfig.Builder makeBasicRoomConfigBuilder()
	{
		return RoomConfig.builder(this)
				.setMessageReceivedListener(this)
				.setRoomStatusUpdateListener(this);
	}
	
	private void searchQuickGame()
	{
		// auto match criteria
		Bundle am = RoomConfig.createAutoMatchCriteria(1, 1, 0);
		
		// build room config
		RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
		roomConfigBuilder.setAutoMatchCriteria(am);
		RoomConfig roomConfig = roomConfigBuilder.build();
		
		// create room
		Games.RealTimeMultiplayer.create(apiClient, roomConfig);
		
		// prevent screen from sleeping
		preventSleep();
		
		// show different screen
		
	}
	
	private void preventSleep()
	{
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	private void enableSleep()
	{
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0)
	{
		// TODO Auto-generated method stub
		Log.d("BOUNDS", "Failed to connect\n" + arg0.toString());
		try {
			arg0.startResolutionForResult(this, RC_CONNECTION_RESOLUTION);
		} catch (SendIntentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			apiClient.connect();
		}
	}
	
	@Override
	public void onConnected(Bundle connectionHint)
	{
		// TODO Auto-generated method stub
		Log.d("BOUNDS", "Connected");
		// initialize invitation listener
		Games.Invitations.registerInvitationListener(apiClient, this);
		
		// check if user has an invitation
		if(connectionHint != null)
		{
			 cInv = connectionHint.getParcelable(Multiplayer.EXTRA_INVITATION);

			if(cInv != null)
			{
				// accept invite if it exists
				RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
				roomConfigBuilder.setInvitationIdToAccept(cInv.getInvitationId());
				Games.RealTimeMultiplayer.join(apiClient, roomConfigBuilder.build());
				
				// prevent sleep
				preventSleep();
				
				// go to game screen
			}
		}
	}

	@Override
	public void onConnectionSuspended(int arg0)
	{
		// TODO Auto-generated method stub
		Log.d("BOUNDS","Connected suspended");
	}

	@Override
	public void requestSignIn() 
	{
		Log.d("BOUNDS","Requested sign in...\nSigning In...");
		apiClient.connect();
	}

	@Override
	public void requestSignOut()
	{
		// TODO Auto-generated method stub
		Log.d("BOUNDS","Requested sign out...\nSigning out...");
	//	apiClient.disconnect();
	}
	
	@Override
	public void requestInboxActivity()
	{
		Log.d("BOUNDS","Requested inbox");
		startActivityForResult(Games.Invitations.getInvitationInboxIntent(apiClient),RC_INBOX);
	}

	@Override
	public String getCurrentAccountName()
	{
		return Games.Players.getCurrentPlayer(apiClient).getDisplayName();
		// return Games.getCurrentAccountName(apiClient); <-- returns users email, not name
	}
	
	@Override
	public String getOpponentName()
	{
		if(opponent != null)
		{
			return opponent.getDisplayName();
		}
		return "opponent";
	}
	
	@Override
	public boolean isHost()
	{
		return isHost;
	}
	
	@Override
	public boolean isMultiplayer()
	{
		// currentRoom becomes null if the user leaves/disconnects from a room, or it is never assigned a room
		return (currentRoom != null);
	}
	
	@Override
	public int getOpponentStatus()
	{
	//	if(currentRoom != null)
		if(opponent != null)
		{
			// return currentRoom.getParticipants().get(0).getStatus();
			return opponent.getStatus();
		}
		return 1; // 1 = STATUS_INVITED
	}

	@Override
	public boolean isConnected()
	{
		return apiClient.isConnected();
	}

	@Override
	public boolean isConnecting()
	{
		return apiClient.isConnecting();
	}
	
	@Override
	public void leaveRoom()
	{
		Log.d("BOUNDS","Requested to leave room...");
		this.sendReliableMessage(new byte[]{'L'});
		Games.RealTimeMultiplayer.leave(apiClient, this, currentRoomID);
		isHost = false;
	}

	@Override
	public String getInviterName()
	{
		return cInv.getInviter().getDisplayName();
	}

	@Override
	public void requestInviteActivity() {
		Log.d("BOUNDS","Requested invite activity");
		startActivityForResult(Games.RealTimeMultiplayer.getSelectOpponentsIntent(apiClient, 1, 1), RC_INVITE);
	}
	
	@Override
	public void sendReliableMessage(byte[] message)
	{
		if(currentRoomID != null && opponent != null)
		{
			Games.RealTimeMultiplayer.sendReliableMessage(apiClient, null, message, currentRoomID, opponent.getParticipantId());
		}
	}
	
	@Override
	public void sendUnreliableMessage(byte[] message)
	{
		if(currentRoomID != null && opponent != null)
		{
			Games.RealTimeMultiplayer.sendUnreliableMessage(apiClient, message, currentRoomID, opponent.getParticipantId());
		}
	}

	@Override
	public void onJoinedRoom(int statusCode, Room room) {
	    if (statusCode != GamesStatusCodes.STATUS_OK) {
	        // let screen go to sleep
	        enableSleep();

	        // show error message, return to main screen.
	    }
	    else
	    {
	    	currentRoom = room;
	    	currentRoomID = room.getRoomId();
	    	// show waiting room UI
	    	// startActivityForResult(Games.RealTimeMultiplayer.getWaitingRoomIntent(apiClient, arg1, Integer.MAX_VALUE),RC_WAITING_ROOM);
	    	//shouldShowWaitingRoom = true;
	    	multiplayerListener.onJoinedRoom();
	    } 
	}

	@Override
	public void onLeftRoom(int statusCode, String roomID) {
		currentRoom = null;
		currentRoomID = null;
		Log.d("BOUNDS","Left room");
		multiplayerListener.onLeftRoom();
	}

	@Override
	public void onRoomConnected(int statusCode, Room room) {
		Log.d("BOUNDS","Connected to room");
	    if (statusCode != GamesStatusCodes.STATUS_OK) {
	        // let screen go to sleep
	        enableSleep();

	        // show error message, return to main screen.
	    }
	    else
	    {
	    	currentRoom = room;
	    	currentRoomID = room.getRoomId();
	    	multiplayerListener.onRoomConnected();
	    } 
	}

	@Override
	public void onRoomCreated(int statusCode, Room room) {
		Log.d("BOUNDS","Created room");
	    if (statusCode != GamesStatusCodes.STATUS_OK) {
	        // let screen go to sleep
	        enableSleep();

	        // show error message, return to main screen.
	    }
	    else
	    {
	    	currentRoom = room;
	    	currentRoomID = room.getRoomId();
	    	// show waiting room UI
	    	// startActivityForResult(Games.RealTimeMultiplayer.getWaitingRoomIntent(apiClient, arg1, Integer.MAX_VALUE),RC_WAITING_ROOM);
	    	//shouldShowWaitingRoom = true;
	    	multiplayerListener.onRoomCreated();
	    } 
	}

	@Override
	public void onRealTimeMessageReceived(RealTimeMessage msg) {
		byte[] data = msg.getMessageData();
		multiplayerListener.onRealTimeMessageRecieved(data);
	}

	@Override
	public void onConnectedToRoom(Room arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnectedFromRoom(Room arg0) {
		Log.d("BOUNDS","Disconnected from room... leaving");
		Games.RealTimeMultiplayer.leave(apiClient, this, currentRoomID);
		isHost = false;
	}

	@Override
	public void onP2PConnected(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onP2PDisconnected(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeerDeclined(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeerInvitedToRoom(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeerJoined(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeerLeft(Room arg0, List<String> arg1) {
		Log.d("BOUNDS",opponent.getDisplayName() + " has left..leaving room");
		Games.RealTimeMultiplayer.leave(apiClient, this, currentRoomID);	
		isHost = false;
		multiplayerListener.onPeerLeft();
	}

	@Override
	public void onPeersConnected(Room arg0, List<String> peers) {
		Log.d("BOUNDS","Player Connected: " +peers.get(0));
		opponent = arg0.getParticipant(peers.get(0));
		Log.d("BOUNDS","Opponent: " + opponent.getDisplayName());
		multiplayerListener.onPeersConnected();
	}

	@Override
	public void onPeersDisconnected(Room arg0, List<String> arg1) {
		// opponent left
		Log.d("BOUNDS",opponent.getDisplayName() + " has left..leaving room");
		Games.RealTimeMultiplayer.leave(apiClient, this, currentRoomID);
		isHost = false;
		multiplayerListener.onPeersDisconnected();
	}

	@Override
	public void onRoomAutoMatching(Room arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomConnecting(Room arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInvitationReceived(Invitation inv) {	
		Log.d("BOUNDS","invitation recieved");
		// alert user that they just got an invitation to play
		// save current invitation
		cInv = inv;
		multiplayerListener.onInvitationReceived();
	}

	@Override
	public void onInvitationRemoved(String arg0) {
		cInv = null;
		multiplayerListener.onInvitationRemoved();
	}
}