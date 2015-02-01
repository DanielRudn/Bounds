package com.dr.bounds.screens;

import java.util.ArrayList;

import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.SlideExponentialAnimation;
import com.DR.dLib.ui.dButton;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICardList;
import com.DR.dLib.dTweener;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dr.bounds.AssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.ui.RecentGamesGraph;
import com.dr.bounds.ui.ScoreCard;

public class GameOverScreen extends dUICardList implements AnimationStatusListener {

	// two darker panels containing info about users score and money won
	private ScoreCard scoreCard, comboCard;
	// players final score for score counter
	private float playerScore = 0;
	// player instance
	private Player player;
	// replay button
	private dButton replayButton;
	// when user clicks replay, this turns false and resets the game
	private boolean wantsReplay = false;
	// Text at the top E.G (Game Over)
	private dText topText;
	// card containing above elements
	private dUICard topCard;
	// animations
	private SlideExponentialAnimation showAnimation;
	// test
	private RecentGamesGraph rgg;
	
	public GameOverScreen(float x, float y, Texture texture, Player player) {
		super(x, y, texture, new ArrayList<dUICard>());
	//	setColor(37f/256f, 116f/256f, 169f/256f,1f);
		setColor(52f/256f, 152f/256f, 219f/256f,1f);
		setPadding(32f);
		setPaddingLeft(64f);
		
		this.player = player;
		
		showAnimation = new SlideExponentialAnimation(1f, this, 123, -MainGame.VIRTUAL_WIDTH, 0, this);
		setShowAnimation(showAnimation);
		
		topCard = new dUICard(getX(), getY(), texture);
		topCard.setColor(getColor());
		topCard.setPadding(32f);
		topCard.setPaddingLeft(64f);
		topCard.setDimensions(MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT / 2f - 128f);
		topCard.setHasShadow(false);
		
		topText = new dText(0,0,64f,"GAME OVER");
		topText.setColor(Color.WHITE);
		
		scoreCard = new ScoreCard(0,0,texture,"SCORE");
		comboCard = new ScoreCard(0,0,texture,"COMBO");
		
		replayButton = new dButton(0,0, new Sprite(AssetManager.getTexture("replay.png")), "");
		replayButton.setDimensions(192f, 192f);
		replayButton.setColor(scoreCard.getColor());
		
	
		
		topCard.addObject(topText,dUICard.CENTER,dUICard.TOP);
		topCard.addObjectUnder(scoreCard, dUICard.LEFT, topCard.getIndexOf(topText));
		topCard.addObjectUnder(comboCard, dUICard.RIGHT, topCard.getIndexOf(topText));
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
	
	public void show(int score, int combo, int coinsCollected)
	{
		super.show();
		// set score, combo, and coins
		scoreCard.setMainText(Integer.toString(score));
		scoreCard.setBottomText(Integer.toString(Player.bestScore));
		comboCard.setMainText(Integer.toString(combo));
		comboCard.setBottomText(Integer.toString(Player.bestCombo));
	}
	
	public void reset()
	{
		wantsReplay = false;
		hide();
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
			rgg = new RecentGamesGraph(getWidth() + getWidth()/2f - 396f / 2f , topCard.getY() + topCard.getHeight() - 32f, AssetManager.getTexture("card"), 396,256, "Score Last 5 Games");
			Player.addRecentScore((int) playerScore);
			if(playerScore > Player.bestScore)
			{
				Player.bestScore = (int) playerScore;
			}
			ArrayList<Vector2> scores = new ArrayList<Vector2>();
			for(int x = 0; x < Player.recentScores.size(); x++)
			{
				scores.add(new Vector2(x,Player.recentScores.get(x)));
			}
			rgg.setPoints(scores);
			Player.savePlayerData();
		}
	}

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta) {
		if(ID == 123 && time > duration / 6f)
		{
			rgg.setX(dTweener.ExponentialEaseOut(time, getWidth() + getWidth() / 2f - 396f / 2f, -getWidth(), duration));
		}
	}
	
	@Override
	public void onAnimationFinish(int ID) {
	}
}
