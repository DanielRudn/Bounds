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
	private float currentScore = 0, currentMoney = 0;
	// players final score for score counter
	private float playerScore = 0;
	// image of the player's skin (or winner if multiplayer)
	private dImage playerImage;
	// burst behind player image
	private dImage playerBurst;
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
		setColor(52f/256f, 152f/256f, 219f/256f,1f);
		setPadding(32f);
		setPaddingLeft(64f);
		
		playerImage = new dImage(0,0, SkinLoader.getTextureForSkinID(playerSkinID));
		playerImage.setDimensions(128f, 128f);
		
		// fix
		Texture burstTexture = new Texture("burst.png");
		burstTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		playerBurst = new dImage(0,0, burstTexture);
		playerBurst.setColor(Color.ORANGE);
		playerBurst.setDimensions(512f, 512f);
		playerBurst.setOriginCenter();
		
		topText = new dText(0,0,64f,"GAME OVER");
		topText.setColor(Color.WHITE);
		
		scoreCard = new dUICard(0,0,texture);
		scoreCard.setColor(41f/256f, 128f/256f, 185f/256f, 1f);
		scoreCard.setHasShadow(false);
		scoreCard.setDimensions(MainGame.VIRTUAL_WIDTH, 128f);
		scoreCard.setPaddingLeft(32f);
		dText scoreCardText = new dText(0,0,72f, "Score: 0");
		scoreCardText.setColor(Color.WHITE);
		scoreCard.addObject(scoreCardText, dUICard.CENTER, dUICard.CENTER);
		
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
		
		addObject(playerBurst,dUICard.CENTER, dUICard.TOP);
		addObject(topText,dUICard.CENTER,dUICard.TOP);
		addObject(playerImage,dUICard.CENTER, dUICard.TOP);
		playerImage.setPos(getX() + getWidth()/2f - playerImage.getWidth()/2f, getY() + 356f - playerImage.getHeight()/2f);
		playerBurst.setPos(playerImage.getX() - 64f, playerImage.getY() - 50f);
		addObject(moneyCard,dUICard.LEFT_NO_PADDING,dUICard.BOTTOM);
		moneyCard.setY(moneyCard.getY() - 256f);
		addObjectOnTopOf(scoreCard,getIndexOf(moneyCard));
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
				((dText)scoreCard.getObject(0)).setText("score: " + Float.toString((int)currentScore+1).substring(0,Float.toString((int)currentScore+1).length()-2));
				currentMoney = dTweener.MoveToAndSlow(currentMoney, 30, 2f * delta);
				((dText)moneyCard.getObject(0)).setText("$$$: " + Float.toString((int)currentMoney+1).substring(0,Float.toString((int)currentMoney+1).length()-2));
			}
			
			if(playerBurst.getSprite().getRotation() <= 359f)
			{
				playerBurst.getSprite().setRotation(dTweener.MoveToAndSlow(playerBurst.getSprite().getRotation(), 360f, delta/20f));
			}
			else
			{
				playerBurst.getSprite().setRotation(0);
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
		showTime = 0;
	}
	
	public void reset()
	{
		wantsReplay = false;
		opponentRematch = false;
		hide();
		replayButton.getSprite().setRotation(0);
		currentScore = 0;
		currentMoney = 0;
		playerScore = 0;
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
	
	public void setWinnerSkinID(int id)
	{
		playerImage.getSprite().setRegion(SkinLoader.getTextureForSkinID(id));
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
