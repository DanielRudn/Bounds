package com.dr.bounds.screens;

import java.util.ArrayList;

import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.SlideExponentialAnimation;
import com.DR.dLib.ui.dButton;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICardList;
import com.DR.dLib.dTweener;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dr.bounds.MainGame;
import com.dr.bounds.SkinLoader;
import com.dr.bounds.ui.RecentGamesGraph;

public class GameOverScreen extends dUICardList implements AnimationStatusListener {

	// two darker panels containing info about users score and money won
	private dUICard scoreCard, moneyCard;
	// the counters that will be displayed on screen
	private float currentScore = 0;
	// players final score for score counter
	private float playerScore = 0;
	// image of the player's skin
	private dImage playerImage;
	// name of local player
	private dText playerName;
	// replay button
	private dButton replayButton;
	// when user clicks replay, this turns false and resets the game
	private boolean wantsReplay = false;
	// Text at the top E.G (Game Over, You Win, You lose...)
	private dText topText;
	// card containing above elements
	private dUICard topCard;
	// animations
	private SlideExponentialAnimation showAnimation;
	// test
	private RecentGamesGraph rgg;
	
	public GameOverScreen(float x, float y, Texture texture, int playerSkinID) {
		super(x, y, texture, new ArrayList<dUICard>());
		setColor(37f/256f, 116f/256f, 169f/256f,1f);
		setPadding(32f);
		setPaddingLeft(64f);
		
		showAnimation = new SlideExponentialAnimation(1f, this, 123, -MainGame.VIRTUAL_WIDTH, 0, this);
		setShowAnimation(showAnimation);
		
		topCard = new dUICard(getX(), getY(), texture);
		topCard.setColor(getColor());
		topCard.setPadding(32f);
		topCard.setPaddingLeft(64f);
		topCard.setDimensions(MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT / 2f - 64f);
		topCard.setHasShadow(false);
		
		playerName = new dText(0,0,getFontSize(MainGame.requestHandler.getCurrentAccountName()),MainGame.requestHandler.getCurrentAccountName());
		playerName.setColor(Color.WHITE);
		
		playerImage = new dImage(0,0, SkinLoader.getTextureForSkinID(playerSkinID));
		playerImage.setDimensions(128f, 128f);
		
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
		scoreCard.addObject(playerScore, dUICard.CENTER, dUICard.CENTER);
		scoreCard.addObject(scoreIdentifierText, dUICard.LEFT, dUICard.CENTER);
		
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
		
		topCard.addObject(topText,dUICard.CENTER,dUICard.TOP);
		topCard.addObject(playerImage,dUICard.CENTER, dUICard.TOP);
		playerImage.setY(getY() + 256f - playerImage.getHeight()/2f);
		topCard.addObject(playerName, dUICard.CENTER, dUICard.TOP);
		playerName.setY(playerImage.getY() - getPadding() / 2f - playerName.getHeight() / 2f);
		topCard.addObjectUnder(scoreCard, topCard.getIndexOf(playerImage));
		scoreCard.setX(getX());
		addCardAsObject(topCard);
		addObject(replayButton, dUICard.RIGHT, dUICard.BOTTOM);
		replayButton.setOriginCenter();
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(showAnimation.isActive())
		{
			showAnimation.update(delta);
		}
		// spin replay button on click
		if(isVisible())
		{
			if(replayButton.isClicked())
			{
				setY(0);
				MainGame.camera.position.y = MainGame.VIRTUAL_HEIGHT / 2f;
				if(wantsReplay == false)
				{
					wantsReplay = true;
					showAnimation.stop();
				}
			}
			
			// single player reset
			if(wantsReplay)
			{
				replayButton.getSprite().setRotation(dTweener.MoveToAndSlow(replayButton.getSprite().getRotation(), 360f, 5f*delta));
				setX(dTweener.MoveToAndSlow(getX(), 0 + MainGame.VIRTUAL_WIDTH * 2f,3f*delta));
				rgg.setX(rgg.getX() + getX());
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
		if(isVisible() && rgg != null)
		{
			rgg.render(batch);
		}
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
		hide();
		replayButton.getSprite().setRotation(0);
		currentScore = 0;
		playerScore = 0;
		rgg = null;
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
	//	this.hide();
		newScreen.show();
		MainGame.currentScreen = newScreen;
	//	MainGame.previousScreen = this;
	}

	@Override
	public void onAnimationStart(int ID, float duration) {
		if(ID == 123)
		{
			setPos(MainGame.camera.position.x + MainGame.VIRTUAL_WIDTH /2f,MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f);
			rgg = new RecentGamesGraph(getWidth() + getWidth()/2f - 396f / 2f , getY()  + MainGame.VIRTUAL_HEIGHT / 2f , new Texture("card.png"), 396,256, "Score Last 5 Games");
			ArrayList<Vector2> scores = new ArrayList<Vector2>();
			scores.add(new Vector2(0,15));
			scores.add(new Vector2(1,20));
			scores.add(new Vector2(2,32));
			scores.add(new Vector2(3,12));
			scores.add(new Vector2(4,playerScore));
			rgg.setPoints(scores);
		}
	}

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta) {
		if(ID == 123 && time > duration / 6f)
		{
			currentScore = dTweener.MoveToAndSlow(currentScore, playerScore, 2f* delta);
			((dText)scoreCard.getObject(0)).setText(Float.toString((int)currentScore+1).substring(0,Float.toString((int)currentScore+1).length()-2));
			rgg.setX(dTweener.ExponentialEaseOut(time, getWidth() + getWidth() / 2f - 396f / 2f, -getWidth(), duration));
		}
	}
	
	@Override
	public void onAnimationFinish(int ID) {
	}
}
