package com.dr.bounds.screens;

import com.DR.dLib.dImage;
import com.DR.dLib.dText;
import com.DR.dLib.dTweener;
import com.DR.dLib.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.LoadingIcon;
import com.dr.bounds.MainGame;
import com.dr.bounds.RequestHandler;
import com.dr.bounds.SkinLoader;

public class WaitingRoomScreen extends dScreen {

	private RequestHandler requestHandler;
	private dImage playerIcon, opponentIcon, playerBall, opponentBall;
	private dUICard divider, playerNameCard, opponentNameCard;
	private dText playerName, opponentName, versusText, opponentStatus;
	private boolean showElements = false, showOpponent = false, switchStatus = false, changedText = false, opponentReady = false, hide = false;
	private float currentTime = 0, extraTime = 0, opponentCurrentTime = 0, opponentExtraTime = 0, hideTime = 0;
	private int playerSkinID = MainGame.PLACEHOLDER_SKIN_ID, opponentSkinID = MainGame.PLACEHOLDER_SKIN_ID;
	public static final int STATUS_NOT_YET_INVITED = 0, STATUS_INVITED = 1, STATUS_JOINED = 2, STATUS_DECLINED = 3, STATUS_LEFT  = 4, STATUS_UNRESPONSIVE = 6;
	private int currentStatus = 0;
	// loading icon to show user that something is happening
	private LoadingIcon loadingIcon;
	
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
		// divider.setColor(52f/256f, 152f/256f, 219f/256f,1f);

		playerNameCard = new dUICard(x,y,texture);
		playerNameCard.setDimensions(0.1f, 6f);
		playerNameCard.setHasShadow(false);
		playerNameCard.setColor(46f/256f, 204f/256f, 113f/256f,1f);
		opponentNameCard = new dUICard(x,y,texture);
		opponentNameCard.setDimensions(0.1f, 6f);
		opponentNameCard.setHasShadow(false);
		opponentNameCard.setColor(231f/256f, 76f/256f, 60f/256f, 1f);
		
		playerBall = new dImage(0,0,new Sprite(SkinLoader.getTextureForSkinID(playerSkinID)));
		playerBall.setHasShadow(true);
		playerBall.setDimensions(96f, 96f);
		
		opponentBall = new dImage(0,0,new Sprite(SkinLoader.getTextureForSkinID(opponentSkinID)));
		opponentBall.setHasShadow(true);
		opponentBall.setDimensions(96f, 96f);
		
		versusText = new dText(0,0,92f,"VERSUS");
		versusText.setShadow(true);
		versusText.setColor(Color.WHITE);
		
		playerName = new dText(0,0,96f,"name");
		playerName.setColor(Color.WHITE);
		playerName.setShadow(true);
		
		opponentName = new dText(0,0,96f,"opponent");
		opponentName.setColor(Color.WHITE);
		opponentName.setShadow(true);
		
		opponentStatus = new dText(0,0,96f,"status");
		opponentStatus.setColor(Color.WHITE);
		opponentStatus.setShadow(true);
		
		addObject(playerIcon,dUICard.RIGHT,dUICard.TOP);
		addObjectToRightOf(playerBall,getIndexOf(playerIcon));
		playerBall.setPos(playerIcon.getX() + 32f, playerIcon.getY() + 32f);
		addObjectUnder(playerNameCard,getIndexOf(playerIcon));
		playerNameCard.setPos(playerIcon.getX() + playerIcon.getWidth()/2f, playerIcon.getY() + playerIcon.getHeight() - playerNameCard.getHeight());
		addObject(playerName,dUICard.LEFT,dUICard.TOP);
		playerName.setPos(-1000f, playerNameCard.getY() - playerNameCard.getHeight() - getPadding()*2f);
		addObject(divider,dUICard.LEFT_NO_PADDING,dUICard.CENTER);
		// addObject(versusText,dUICard.CENTER,dUICard.CENTER);
		// addObject(loadingIcon, dUICard.CENTER, dUICard.CENTER);
		addObjectUnder(opponentIcon, dUICard.LEFT, getIndexOf(divider));
		addObjectToRightOf(opponentBall, getIndexOf(opponentIcon));
		opponentBall.setPos(opponentIcon.getX() + 32f, opponentIcon.getY()+32f);
		addObjectUnder(opponentNameCard, getIndexOf(opponentIcon));
		addObjectUnder(opponentName, dUICard.CENTER,getIndexOf(divider));
		addObject(opponentStatus,dUICard.CENTER,dUICard.BOTTOM);
		opponentNameCard.setPos(opponentIcon.getX() + opponentIcon.getWidth()/2f, opponentIcon.getY() + opponentIcon.getHeight() - opponentNameCard.getHeight());
		opponentName.setPos(1000f,opponentNameCard.getY() - opponentNameCard.getHeight() - getPadding()*2f);
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
			
			}
			if(currentTime > 1.1f)
			{
				if(extraTime < 2f)
				{
					extraTime+=delta;
					playerNameCard.setWidth(dTweener.ElasticOut(extraTime, 0.1f, -MainGame.VIRTUAL_WIDTH + 128f, 2f,6f));
					playerName.setX(dTweener.ElasticOut(extraTime,getX() - playerName.getWidth(), (playerIcon.getX() - playerIcon.getWidth() - getPadding() - playerName.getWidth()) - (getX() - playerName.getWidth()), 2f,6f));
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
					opponentNameCard.setWidth(dTweener.ElasticOut(opponentExtraTime, 0.1f, MainGame.VIRTUAL_WIDTH - 128f, 2f,6f));
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
		if(opponentReady && opponentExtraTime >= 2f && extraTime >= 2f)
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
		setPosition(0,-MainGame.VIRTUAL_HEIGHT);
		showElements = false;
		showOpponent = false;
		currentTime = 0;
		extraTime = 0;
		playerName.setText("name");
		playerIcon.setDimensions(0.1f, 0.1f);
		playerNameCard.setDimensions(0.1f, 6f);
		playerBall.setDimensions(0.1f,0.1f);
		playerSkinID = MainGame.PLACEHOLDER_SKIN_ID;
		divider.setDimensions(0.1f, 8f);
		opponentCurrentTime = 0;
		opponentIcon.setDimensions(0.1f, 0.1f);
		opponentExtraTime = 0;
		opponentBall.setDimensions(0.1f, 0.1f);
		opponentName.setText("opponent");
		opponentSkinID = MainGame.PLACEHOLDER_SKIN_ID;
		opponentNameCard.setDimensions(0.1f, 6f);
		opponentName.setPos(1000f,opponentNameCard.getY() - opponentNameCard.getHeight() - getPadding()*2f);
		playerName.setPos(-1000f, playerNameCard.getY() - playerNameCard.getHeight() - getPadding()*2f);
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
		if(requestHandler.getOpponentStatus() != currentStatus)
		{
			currentStatus = requestHandler.getOpponentStatus();
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
				requestHandler.sendReliableMessage(new byte[]{'R'});
			}
		}
	}

	public void showPlayerElements(int pSkinID)
	{
		showElements = true;
		playerSkinID = pSkinID;
		playerBall.getSprite().setRegion(SkinLoader.getTextureForSkinID(playerSkinID));
		if(requestHandler.getCurrentAccountName().contains(" "))
		{
			playerName.setText(requestHandler.getCurrentAccountName().substring(0,requestHandler.getCurrentAccountName().indexOf(' ')));
		}
		else
		{
			playerName.setText(requestHandler.getCurrentAccountName());
		}
	}
	
	public void showOpponentElements(int oSkinID, String name)
	{
		showOpponent = true;
		opponentSkinID = oSkinID;
		opponentBall.getSprite().setRegion(SkinLoader.getTextureForSkinID(opponentSkinID));
		if(name.contains(" "))
		{
			opponentName.setText(name.substring(0,name.indexOf(' ')));
		}
		else
		{
			opponentName.setText(name);
		}
	}
	
	public void setLoadingIconImage(Texture texture)
	{
		
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

}
