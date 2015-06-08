package com.dr.bounds.screens;

import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.SlideExponentialAnimation;
import com.DR.dLib.animations.SlideInOrderAnimation;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dButton;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class MenuScreen extends dScreen implements AnimationStatusListener {

	// animations
	private SlideInOrderAnimation showButtonsAnimation;
	private final int SHOW_BUTTONS_ID = 123;
	private dAnimation hideAnimation;
	private final int HIDE_ANIM_ID = 12345;
	// title
	private dImage titleImage;
	private float titleTime = 0f, titleDuration = (float)Math.PI/2f;
	// BUTTONS
	private dButton playButton;
	private dButton skinsButton;
	private dButton leaderboardsButton;
	private dButton achievementsButton;
	private SettingsScreen settingsScreen;
	// shop screen
	private ShopScreen shopScreen = null;
	// next screen
	private dScreen nextScreen = null;
	// Player instance
	private Player player;
	
	public MenuScreen(float x, float y, Texture texture, Player p) {
		super(x, y, texture);
		setColor(52f/256f, 73f/256f, 94f/256f,1f);
		setPaddingTop(16f);
		hideAnimation = new SlideExponentialAnimation(1.5f, this, HIDE_ANIM_ID, 0, MainGame.VIRTUAL_HEIGHT, this);
		setHideAnimation(hideAnimation);
		player = p;
		
		titleImage = new dImage(0,0,BoundsAssetManager.getTexture("BoundsLogo.png"));
		titleImage.setColor(Color.WHITE);
		
		settingsScreen = new SettingsScreen(0, 0);
		
		Color buttonColor = new Color(68f/256f,108f/256f,179f/256f, 1f);
		
		//fix
		Texture buttonTexture = BoundsAssetManager.getTexture("button");
		playButton = new dButton(0,0, new Sprite(buttonTexture), "PLAY");
		playButton.setTextSize(92f);
		playButton.setColor(buttonColor);
		
		skinsButton = new dButton(0,0, new Sprite(buttonTexture), "SHOP");
		skinsButton.setTextSize(92f);
		skinsButton.setColor(buttonColor);
		
		leaderboardsButton = new dButton(0,0, new Sprite(buttonTexture), "SCORES");
		leaderboardsButton.setTextSize(92f);
		leaderboardsButton.setColor(Color.GRAY);
		
		achievementsButton = new dButton(0,0, new Sprite(buttonTexture), "TROPHIES");
		achievementsButton.setTextSize(92f);
		achievementsButton.setColor(Color.GRAY);
		
		addObject(titleImage, dUICard.CENTER, dUICard.TOP);
		titleImage.setY(titleImage.getY() + titleImage.getHeight()/2f);
		addObject(playButton, dUICard.CENTER, dUICard.TOP);
		playButton.setY(playButton.getY() + 364f);
		addObjectUnder(skinsButton, getIndexOf(playButton));
		addObjectUnder(leaderboardsButton, getIndexOf(skinsButton));
		addObjectUnder(achievementsButton, getIndexOf(leaderboardsButton));
		addObject(settingsScreen, dUICard.LEFT, dUICard.BOTTOM);
		
		showButtonsAnimation = new SlideInOrderAnimation(2f, this, SHOW_BUTTONS_ID, MainGame.VIRTUAL_WIDTH, new dButton[]{playButton, skinsButton, leaderboardsButton, achievementsButton});
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		if(nextScreen != null && nextScreen instanceof GameScreen)
		{
			nextScreen.render(batch);
		}
		super.render(batch);
		if(nextScreen != null && nextScreen instanceof ShopScreen)
		{
			nextScreen.render(batch);
		}
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		// move the title text up and down
		if(titleTime < titleDuration)
		{
			titleTime+=delta;
			titleImage.setY(titleImage.getY() - (float)Math.sin(titleTime));
		}
		else
		{
			titleTime = (float)-Math.PI/2f;
		}
		if(hideAnimation.isActive())
		{
			hideAnimation.update(delta);
		}
		if(showButtonsAnimation.isActive())
		{
			showButtonsAnimation.update(delta);
		}
		
		if(nextScreen != null)
		{
			nextScreen.update(delta);
		}
		else
		{
			if(playButton.isClicked())
			{
				switchScreen(MainGame.gameScreen);
			}
			if(skinsButton.isClicked() && nextScreen == null)
			{
				if(shopScreen == null)
				{
					shopScreen = new ShopScreen(0,0, BoundsAssetManager.getTexture("card"), player);
				}
				switchScreen(shopScreen, false);
			}
		}
	}
	
	@Override
	public void show()
	{
		super.show();
		setPos(0,0);
		playButton.setX(playButton.getX() - MainGame.VIRTUAL_WIDTH);
		skinsButton.setX(skinsButton.getX() - MainGame.VIRTUAL_WIDTH);
		leaderboardsButton.setX(leaderboardsButton.getX() - MainGame.VIRTUAL_WIDTH);
		achievementsButton.setX(achievementsButton.getX() - MainGame.VIRTUAL_WIDTH);
		showButtonsAnimation.start();
		nextScreen = null;
	}
	
	@Override
	public void hide()
	{
		hideAnimation.start();
		setVisible(true);
	}

	@Override
	public void goBack() {
		if(settingsScreen.isOpen())
		{
			settingsScreen.goBack();
		}
		else if(nextScreen != null && nextScreen == shopScreen)
		{
			nextScreen.hide();
			this.show();
			nextScreen = null;
		}
		else
		{
			Gdx.app.exit();
		}
	}
	
	public void switchScreen(dScreen newScreen, boolean hideThis)
	{
		if(hideThis)
		{
			switchScreen(newScreen);
		}
		else
		{
			nextScreen = newScreen;
			nextScreen.show();
		//	MainGame.currentScreen = nextScreen;
		}
	}

	@Override
	public void switchScreen(dScreen newScreen) {
		this.hide();
		nextScreen = newScreen;
		//nextScreen.show();
		//this.hide();
	}

	@Override
	public void onAnimationStart(int ID, float duration) {
		if(ID == HIDE_ANIM_ID)
		{
		}
	}

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta) {
	}
	
	@Override
	public void onAnimationFinish(int ID) {
		if(ID == HIDE_ANIM_ID)
		{
			setVisible(false);
			MainGame.currentScreen = nextScreen;
			MainGame.previousScreen = this;
			nextScreen.show();
			nextScreen = null;
		}
	}

}
