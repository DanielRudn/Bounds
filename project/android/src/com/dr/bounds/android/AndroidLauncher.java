package com.dr.bounds.android;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dr.bounds.MainGame;
import com.dr.bounds.RequestHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;

public class AndroidLauncher extends AndroidApplication implements RequestHandler, ConnectionCallbacks, OnConnectionFailedListener {
	
	private GoogleApiClient apiClient;
	
	private static final int SHARE_INTENT_ID = 13513;
	
	private final int RC_CONNECTION_RESOLUTION = 1361;
	
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
			//apiClient.connect();
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
		apiClient.disconnect();
	}
	
	@Override
	public String getCurrentAccountName()
	{
		if(apiClient.isConnected())
		{
			return Games.Players.getCurrentPlayer(apiClient).getDisplayName();
		}
		return "Player";
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
	public void submitToLeaderboard(int data, String leaderboardID)
	{
		if(isConnected())
		{
			Games.Leaderboards.submitScore(apiClient, leaderboardID, data);
		}
	}
	
	@Override
	public void showLeaderboard(String leaderboardID)
	{
		if(isConnected())
		{
		//	startActivityForResult(Games.Leaderboards.getLeaderboardIntent(apiClient, leaderboardID), 20001);
			startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(apiClient), 20001);
		}
		else
		{
			apiClient.connect();
		}
	}
	
	@Override
	public void showShareIntent(String text)
	{
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		shareIntent.setType("text/plain");
		startActivityForResult(Intent.createChooser(shareIntent, "Share to.."), SHARE_INTENT_ID);
	}

}