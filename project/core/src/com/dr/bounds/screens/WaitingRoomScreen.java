package com.dr.bounds.screens;

import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.dTweener;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.MainGame;
import com.dr.bounds.RequestHandler;
import com.dr.bounds.SkinLoader;
import com.dr.bounds.ui.LoadingIcon;

public class WaitingRoomScreen extends dScreen {

	private RequestHandler requestHandler;
	private dImage playerIcon, opponentIcon, playerBall, opponentBall;
	private dUICard divider;
	private dText playerName, opponentName, versusText, opponentStatus;
	private boolean showElements = false, showOpponent = false, switchStatus = false, changedText = false, opponentReady = false, hide = false;
	private float currentTime = 0, extraTime = 0, opponentCurrentTime = 0, opponentExtraTime = 0, hideTime = 0;
	private int playerSkinID = MainGame.PLACEHOLDER_SKIN_ID, opponentSkinID = MainGame.PLACEHOLDER_SKIN_ID;
	public static final int STATUS_NOT_YET_INVITED = 0, STATUS_INVITED = 1, STATUS_JOINED = 2, STATUS_DECLINED = 3, STATUS_LEFT  = 4, STATUS_UNRESPONSIVE = 6;
	private int currentStatus = 0;
	// loading icon to show user that something is happening
	private LoadingIcon loadingIcon;
	// time for room to show
	private float showTime = 0;
	private final float SHOW_DURATION = 2.5f;
	// whether to play the show animation
	private boolean showScreen = false;
	// maximum name of a length before the size changes to less than 64
	private final int MAX_NAME_LENGTH = 15;
	
	public WaitingRoomScreen(float x, float y, Texture texture, Texture icon, Texture loadingIconTexture) {
		super(x, y, texture);
		requestHandler = MainGame.requestHandler;
		setColor(52f/256f, 73f/256f, 94f/256f,1f);
		setPadding(32f);
		
		loadingIcon = new LoadingIcon(getWidth()/2f - 128f,getHeight() / 2f - 128f,loadingIconTexture);
		
		playerIcon = new dImage(0,0,icon);
		playerIcon.setHasShadow(true);
		playerIcon.setDimensions(128f, 128f);
		playerIcon.setColor(46f/256f, 204f/256f, 113f/256f,1f);
		opponentIcon = new dImage(0,0,icon);
		opponentIcon.setHasShadow(true);
		opponentIcon.setDimensions(128f, 128f);
		opponentIcon.setColor(231f/256f, 76f/256f, 60f/256f, 1f);
		
		divider = new dUICard(x,y,texture);
		divider.setDimensions(0.1f, 8f);
		
		playerBall = new dImage(0,0,new Sprite(SkinLoader.getTextureForSkinID(playerSkinID)));
		playerBall.setHasShadow(true);
		playerBall.setDimensions(96f, 96f);
		
		opponentBall = new dImage(0,0,new Sprite(SkinLoader.getTextureForSkinID(opponentSkinID)));
		opponentBall.setHasShadow(true);
		opponentBall.setDimensions(96f, 96f);
		
		versusText = new dText(0,0,92f,"VERSUS");
		versusText.setShadow(true);
		versusText.setColor(Color.WHITE);
		
		playerName = new dText(0,0,64f,"name");
		playerName.setColor(Color.WHITE);
		playerName.setShadow(true);
		
		opponentName = new dText(0,0,64f,"opponent");
		opponentName.setColor(Color.WHITE);
		opponentName.setShadow(true);
		
		opponentStatus = new dText(0,0,96f,"status");
		opponentStatus.setColor(Color.WHITE);
		opponentStatus.setShadow(true);
		
		addObject(playerIcon,dUICard.CENTER,dUICard.TOP);
		playerIcon.setPos(playerIcon.getX() + 192f,playerIcon.getY() + 192f);
		addObjectToRightOf(playerBall,getIndexOf(playerIcon));
		playerBall.setPos(playerIcon.getX() + 32f, playerIcon.getY() + 32f);
		addObjectToLeftOf(playerName,getIndexOf(playerIcon));
		playerName.setPos(-MainGame.VIRTUAL_WIDTH, playerName.getY() - 80f);
		addObject(divider,dUICard.LEFT_NO_PADDING,dUICard.CENTER);
		// addObject(versusText,dUICard.CENTER,dUICard.CENTER);
		// addObject(loadingIcon, dUICard.CENTER, dUICard.CENTER);
		addObjectUnder(opponentIcon, dUICard.CENTER, getIndexOf(divider));
		opponentIcon.setPos(opponentIcon.getX() - 192f, opponentIcon.getY() + 192f);
		addObjectToRightOf(opponentBall, getIndexOf(opponentIcon));
		opponentBall.setPos(opponentIcon.getX() + 32f, opponentIcon.getY()+32f);
		// addObjectUnder(opponentName, dUICard.CENTER,getIndexOf(divider));
		addObjectToRightOf(opponentName, getIndexOf(opponentIcon));
		opponentName.setPos(MainGame.VIRTUAL_WIDTH * 1.5f, opponentName.getY() - 80f);
		addObject(opponentStatus,dUICard.CENTER,dUICard.BOTTOM);
		opponentIcon.setDimensions(0.1f, 0.1f);
		playerIcon.setDimensions(0.1f, 0.1f);
		playerBall.setDimensions(0.1f,0.1f);
		opponentBall.setDimensions(0.1f, 0.1f);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		loadingIcon.update(delta);
		divider.setColor(loadingIcon.getColor());
		// bring in room from the top
		if(showScreen && showTime < SHOW_DURATION)
		{
			showTime+=delta;
			//camera.position.set(dTweener.ElasticOut(cameraTime, VIRTUAL_WIDTH/2f, VIRTUAL_WIDTH, SHOW_DURATION,4f),dTweener.ElasticOut(cameraTime, VIRTUAL_HEIGHT/2f, 0, SHOW_DURATION,6f), camera.position.z);
			//camera.position.set(camera.position.x,dTweener.ElasticOut(cameraTime, VIRTUAL_HEIGHT/2f, VIRTUAL_HEIGHT, SHOW_DURATION,6f), camera.position.z);
			setX(dTweener.ElasticOut(showTime, -MainGame.VIRTUAL_WIDTH - getPadding(), MainGame.VIRTUAL_WIDTH + getPadding(), SHOW_DURATION, 6f));
		//	currentTime = 0;
			if(showTime >= SHOW_DURATION/4f && showTime <= SHOW_DURATION/3.5f)
			{
				showPlayerElements(MainGame.getPlayerSkinID()); // show info regarding player
			}
		}
		
		// waiting room started, show animation and own info
		if(showElements)
		{
			if(currentTime < 2f)
			{
				currentTime+=delta;
				playerIcon.setDimensions(dTweener.ElasticOut(currentTime, 0.1f, 128f, 2f), dTweener.ElasticOut(currentTime, 0.1f, 128f, 2f));
				playerBall.setDimensions(dTweener.ElasticOut(currentTime, 0f, 96f, 2f), dTweener.ElasticOut(currentTime, 0f, 96f, 2f));
		
				playerIcon.setOriginCenter();

				playerBall.setOriginCenter();
				divider.setWidth(dTweener.BounceOut(currentTime, 0.1f, MainGame.VIRTUAL_WIDTH, 2f));
			}
			else
			{
				playerIcon.setDimensions(128f, 128f);
				MainGame.previousScreen = null;
			}
			if(currentTime > 1.1f)
			{
				if(extraTime < 2f)
				{
					extraTime+=delta;
					playerName.setX(dTweener.ElasticOut(extraTime,getX() - playerName.getWidth(), (playerIcon.getX() - getPadding() - playerName.getWidth()) - (getX() - playerName.getWidth()), 2f,6f));
					//opponentNameCard.setWidth(dTweener.ElasticOut(extraTime, 0.1f, MainGame.VIRTUAL_WIDTH - 128f, 2f,6f));
					loadingIcon.start();
				}
			}
		}
		// opponent just joined, show animations
		if(showOpponent)
		{
			if(opponentCurrentTime < 2f)
			{
				opponentCurrentTime+=delta;
				opponentIcon.setDimensions(dTweener.ElasticOut(opponentCurrentTime, 0.1f, 128f, 2f), dTweener.ElasticOut(opponentCurrentTime, 0.1f, 128f, 2f));
				opponentBall.setDimensions(dTweener.ElasticOut(opponentCurrentTime, 0f, 96f, 2f), dTweener.ElasticOut(opponentCurrentTime, 0f, 96f, 2f));
				opponentIcon.setOriginCenter();
				opponentBall.setOriginCenter();
			}
			else
			{
				opponentIcon.setDimensions(128f, 128f);
			}
			if(opponentCurrentTime > 1f)
			{
				if(opponentExtraTime < 2f)
				{
					opponentExtraTime+=delta;
					opponentName.setX(dTweener.ElasticOut(opponentExtraTime, 1000f, opponentIcon.getX() + opponentIcon.getWidth() + getPadding()/2f - 1000f, 2f,6f));
				}
			}
		}
		
		// check opponent status
		checkOpponentStatus();
		if(switchStatus)
		{
			switchOpponentStatusText(delta);
		}
		
		// check whether opponent is ready
		if(opponentReady && opponentExtraTime >= 2f && extraTime >= 2f )
		{
			loadingIcon.stop();
			this.animateHide();
		}
		
		if(hide)
		{
			if(hideTime < 2f)
			{
				hideTime+=delta;
				divider.setHeight(dTweener.LinearEase(hideTime, 8f, -8f, 2f));
				setY(dTweener.ElasticOut(hideTime,0, -MainGame.VIRTUAL_HEIGHT*1.25f, 2f));
				
			}
			else{
				hide();
			}
		}
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		loadingIcon.render(batch);
	}
	
	@Override
	public void show()
	{
		super.show();
		showScreen = true;
	}
	
	public void animateHide()
	{
		hide = true;
	}
	
	@Override
	public void hide()
	{
		super.hide();
		// reset values
		reinit();
	}

	private void reinit()
	{
		setPosition(-MainGame.VIRTUAL_WIDTH - getPadding(), 0);
		showTime = 0;
		showScreen = false;
		showElements = false;
		showOpponent = false;
		currentTime = 0;
		extraTime = 0;
		playerName.setText("name");
		playerIcon.setDimensions(0.1f, 0.1f);
		playerBall.setDimensions(0.1f,0.1f);
		playerSkinID = MainGame.PLACEHOLDER_SKIN_ID;
		divider.setDimensions(0.1f, 8f);
		opponentCurrentTime = 0;
		opponentIcon.setDimensions(0.1f, 0.1f);
		opponentExtraTime = 0;
		opponentBall.setDimensions(0.1f, 0.1f);
		opponentName.setText("opponent");
		opponentSkinID = MainGame.PLACEHOLDER_SKIN_ID;
		switchStatus = false;
		currentStatus = 0;
		changedText = false;
		hide = false;
		hideTime = 0;
		opponentReady = false;
		opponentStatus.setText("INVITED");
	}
	
	private void checkOpponentStatus()
	{
	//	if(requestHandler.getOpponentStatus() != currentStatus)
		{
		//	currentStatus = requestHandler.getOpponentStatus();
			switchStatus = true;
		}
	}
	
	private void switchOpponentStatusText(float delta)
	{
		if(opponentStatus.getX() < MainGame.VIRTUAL_WIDTH+5f && changedText == false)
		{
			opponentStatus.setX(dTweener.MoveToAndSlow(opponentStatus.getX(), MainGame.VIRTUAL_WIDTH+10f, 5f*delta));
		}
		else if(opponentStatus.getX() >= MainGame.VIRTUAL_WIDTH)
		{
			if(currentStatus == WaitingRoomScreen.STATUS_INVITED)
			{
				opponentStatus.setText("INVITED");
			}
			else if(currentStatus == WaitingRoomScreen.STATUS_JOINED)
			{
				opponentStatus.setText("JOINED");
			}
			else if(currentStatus == WaitingRoomScreen.STATUS_DECLINED)
			{
				opponentStatus.setText("DECLINED");
			}
			else if(currentStatus == WaitingRoomScreen.STATUS_LEFT)
			{
				opponentStatus.setText("LEFT");
			}
			opponentStatus.setX(-5f - opponentStatus.getWidth());
			changedText = true;
		}
		else
		{
			opponentStatus.setX(dTweener.MoveToAndSlow(opponentStatus.getX(), MainGame.VIRTUAL_WIDTH/2f - opponentStatus.getWidth()/2f, 5f*delta));
			if(opponentStatus.getX() >= MainGame.VIRTUAL_WIDTH/2f - opponentStatus.getWidth()/2f - 2f)
			{
				switchStatus = false;
				changedText = false;
				
				// test
			//	requestHandler.sendReliableMessage(new byte[]{'R'});
			}
		}
	}

	private void showPlayerElements(int pSkinID)
	{
		showElements = true;
		playerSkinID = pSkinID;
		playerBall.getSprite().setRegion(SkinLoader.getTextureForSkinID(playerSkinID));
		playerName.setText(requestHandler.getCurrentAccountName());
		playerName.setSize(this.getFontSize(playerName.getText()));
	}
	
	public void showOpponentElements(int oSkinID, String name)
	{
		showOpponent = true;
		opponentSkinID = oSkinID;
		opponentBall.getSprite().setRegion(SkinLoader.getTextureForSkinID(opponentSkinID));
		opponentName.setText(name);
		opponentName.setSize(this.getFontSize(opponentName.getText()));
	}
	
	private float getFontSize(String input)
	{
		if(input.length() >= MAX_NAME_LENGTH)
		{
			return 64f / input.length() * MAX_NAME_LENGTH;
		}
		return 64f;
	}
	
	public void setOpponentReady()
	{
		opponentReady = true;
	}
	
	// used to know when to resume gameScreen
	public float getHideTime()
	{
		return hideTime;
	}

	@Override
	public void goBack() {
		if(MainGame.previousScreen != null)
		{
			switchScreen(MainGame.previousScreen);
		}
	}

	@Override
	public void switchScreen(dScreen newScreen) {
		this.hide();
		newScreen.show();
		MainGame.currentScreen = newScreen;
		MainGame.previousScreen = this;
	}

}