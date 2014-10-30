package com.dr.bounds.screens;

import com.DR.dLib.dButton;
import com.DR.dLib.dImage;
import com.DR.dLib.dScreen;
import com.DR.dLib.dText;
import com.DR.dLib.dTweener;
import com.DR.dLib.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.MainGame;
import com.dr.bounds.SkinLoader;

public class GameOverScreen extends dScreen {

	// two darker panels containing info about users score and money won
	private dUICard scoreCard, moneyCard;
	// the counters that will be displayed on screen
	private float currentScore = 0, currentMoney = 0, currentOpponentScore = 0;
	// players final score for score counter
	private float playerScore = 0;
	// opponents final score for score counter
	private float opponentScore = 0;
	// image of the player's skin
	private dImage playerImage;
	// name of local player
	private dText playerName;
	// image of opponents skin
	private dImage opponentImage;
	// name of opponent
	private dText opponentName;
	// replay button
	private dButton replayButton;
	// when user clicks replay, this turns false and resets the game
	private boolean wantsReplay = false;
	// opponent wants a rematch
	private boolean opponentRematch = false;
	// text to let user know opponent wants to play again
	private dText rematchText;
	// Text at the top E.G (Game Over, You Win, You lose...)
	private dText topText;
	// Timer for sliding card in
	private float showTime = 0;
	// duration for slide in
	private final float SHOW_DURATION = 4f;
	
	public GameOverScreen(float x, float y, Texture texture, int playerSkinID) {
		super(x, y, texture);
		setColor(37f/256f, 116f/256f, 169f/256f,1f);
		setPadding(32f);
		setPaddingLeft(64f);
		
		playerName = new dText(0,0,getFontSize(MainGame.requestHandler.getCurrentAccountName()),MainGame.requestHandler.getCurrentAccountName());
		playerName.setColor(Color.WHITE);
		
		playerImage = new dImage(0,0, SkinLoader.getTextureForSkinID(playerSkinID));
		playerImage.setDimensions(128f, 128f);
		
		opponentImage = new dImage(0,0, SkinLoader.getTextureForSkinID(10));
		opponentImage.setDimensions(128f, 128f);
		
		opponentName = new dText(0,0,getFontSize(MainGame.requestHandler.getOpponentName()),MainGame.requestHandler.getOpponentName());
		opponentName.setColor(Color.WHITE);
		
		topText = new dText(0,0,64f,"GAME OVER");
		topText.setColor(Color.WHITE);
		
		scoreCard = new dUICard(0,0,texture);
		scoreCard.setColor(41f/256f, 128f/256f, 185f/256f, 1f);
		scoreCard.setHasShadow(false);
		scoreCard.setDimensions(MainGame.VIRTUAL_WIDTH, 128f);
		scoreCard.setPaddingLeft(getWidth() / 4f - 92f / 4f);
		dText scoreIdentifierText = new dText(0,0,32f,"score");
		scoreIdentifierText.setColor(Color.WHITE);
		dText playerScore = new dText(0,0, 92f, "0");
		playerScore.setColor(Color.WHITE);
		dText opponentScore = new dText(0,0,92f,"0");
		opponentScore.setColor(Color.WHITE);
		scoreCard.addObject(playerScore, dUICard.LEFT, dUICard.CENTER);
		scoreCard.addObject(opponentScore, dUICard.RIGHT, dUICard.CENTER);
		scoreCard.addObject(scoreIdentifierText, dUICard.CENTER, dUICard.CENTER);
		
		moneyCard = new dUICard(0,0,texture);
		moneyCard.setColor(scoreCard.getColor());
		moneyCard.setHasShadow(false);
		moneyCard.setDimensions(MainGame.VIRTUAL_WIDTH, 128f);
		moneyCard.setPaddingLeft(32f);
		dText moneyCardText = new dText(0,0,72f, "Dots: 0");
		moneyCardText.setColor(Color.WHITE);
		moneyCard.addObject(moneyCardText, dUICard.CENTER, dUICard.CENTER);
		
		// fix
		Texture replayTexture = new Texture("replay.png");
		replayTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		replayButton = new dButton(0,0, new Sprite(replayTexture), "");
		replayButton.setDimensions(192f, 192f);
		replayButton.setColor(moneyCard.getColor());
		
		rematchText = new dText(0,0,48f,"");
		rematchText.setColor(Color.WHITE);
		
		addObject(topText,dUICard.CENTER,dUICard.TOP);
		addObject(playerImage,dUICard.CENTER, dUICard.TOP);
		playerImage.setPos(getX() + getWidth()/4f - playerImage.getWidth()/2f, getY() + 256f - playerImage.getHeight()/2f);
		addObject(playerName, dUICard.CENTER, dUICard.TOP);
		playerName.setPos(getX() + getWidth() / 4f - playerName.getWidth() / 2f, playerImage.getY() - getPadding() / 2f - playerName.getHeight() / 2f);
		addObject(opponentImage,dUICard.CENTER, dUICard.TOP);
		opponentImage.setPos(getX() + getWidth() - getWidth() / 4f - opponentImage.getWidth() / 2f, playerImage.getY());
		addObject(opponentName, dUICard.CENTER, dUICard.TOP);
		opponentName.setPos(getX() + getWidth() - getWidth() / 4f - opponentName.getWidth() / 2f, playerImage.getY() - getPadding() / 2f - opponentName.getHeight() / 2f);
		addObjectUnder(scoreCard,getIndexOf(playerImage));
		scoreCard.setX(getX());
		addObject(replayButton, dUICard.RIGHT, dUICard.BOTTOM);
		replayButton.setOriginCenter();
		addObject(rematchText,dUICard.CENTER,dUICard.CENTER);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		// spin replay button on click
		if(isVisible())
		{
			if(showTime <= SHOW_DURATION)
			{
				showTime+=delta;
				setY(dTweener.ElasticOut(showTime, MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f, -MainGame.VIRTUAL_HEIGHT, SHOW_DURATION, 8f));
			}
			if(showTime >= SHOW_DURATION / 6f)
			{
				currentScore = dTweener.MoveToAndSlow(currentScore, playerScore, 2f* delta);
				((dText)scoreCard.getObject(0)).setText(Float.toString((int)currentScore+1).substring(0,Float.toString((int)currentScore+1).length()-2));
				currentOpponentScore = dTweener.MoveToAndSlow(currentOpponentScore, opponentScore, 2f * delta);
				((dText)scoreCard.getObject(1)).setText(Float.toString((int)currentOpponentScore+1).substring(0, Float.toString((int)currentOpponentScore+1).length()-2));
				currentMoney = dTweener.MoveToAndSlow(currentMoney, 30, 2f * delta);
				((dText)moneyCard.getObject(0)).setText("$$$: " + Float.toString((int)currentMoney+1).substring(0,Float.toString((int)currentMoney+1).length()-2));
			}

			if(replayButton.isClicked())
			{
				setY(0);
				MainGame.camera.position.y = MainGame.VIRTUAL_HEIGHT / 2f;
				if(wantsReplay == false)
				{
					wantsReplay = true;
					showTime = SHOW_DURATION + 1f;
				}
				// send message to oppenent requesting a rematch
				if(MainGame.requestHandler.isMultiplayer())
				{
					MainGame.requestHandler.sendReliableMessage(new byte[]{'P'});
				}
			}
			
			// single player reset
			if(wantsReplay && MainGame.requestHandler.isMultiplayer() == false)
			{
				replayButton.getSprite().setRotation(dTweener.MoveToAndSlow(replayButton.getSprite().getRotation(), 360f, 5f*delta));
				setX(dTweener.MoveToAndSlow(getX(), 0 + MainGame.VIRTUAL_WIDTH * 2f,3f*delta));
				if(getX() >= MainGame.VIRTUAL_WIDTH)
				{
					reset();
				}
			}
			// multiplayer reset
			else if(wantsReplay && opponentRematch && MainGame.requestHandler.isMultiplayer())
			{
				replayButton.getSprite().setRotation(dTweener.MoveToAndSlow(replayButton.getSprite().getRotation(), 360f, 5f*delta));
				setX(dTweener.MoveToAndSlow(getX(), 0 + MainGame.VIRTUAL_WIDTH * 2f,3f*delta));
				if(getX() >= MainGame.VIRTUAL_WIDTH)
				{
					reset();
				}
			}
			
		}
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
	}

	@Override
	public void show()
	{
		super.show();
		setPos(MainGame.camera.position.x - MainGame.VIRTUAL_WIDTH / 2f,MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f);
		if(MainGame.requestHandler.isMultiplayer())
		{
			((dImage)getObject(3)).setImage(SkinLoader.getTextureForSkinID(MainGame.gameScreen.getOpponentSkinID()));
			opponentName.setText(MainGame.requestHandler.getOpponentName());
			opponentName.setColor(Color.WHITE);
		}
		showTime = 0;
	}
	
	private float getFontSize(String text)
	{
		if(text.length() >= 15)
		{
			return 48f / text.length() * 15;
		}
		return 48f;
	}
	
	
	public void reset()
	{
		wantsReplay = false;
		opponentRematch = false;
		hide();
		replayButton.getSprite().setRotation(0);
		currentScore = 0;
		currentMoney = 0;
		currentOpponentScore = 0;
		playerScore = 0;
		opponentScore = 0;
		rematchText.setText("");
	}
	
	public void setTitleMessage(String title)
	{
		topText.setText(title);
		updateObjectPosition();
	}
	
	public void setScore(int score)
	{
		playerScore = score;
	}

	public void setOpponentScore(int score)
	{
		opponentScore = score;
	}
	
	public void setOpponentWantsRematch(boolean rematch)
	{
		opponentRematch = rematch;
	}
	
	public boolean wantsReplay()
	{
		return wantsReplay;
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
