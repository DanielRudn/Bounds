package com.dr.bounds.screens;

import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.SlideElasticAnimation;
import com.DR.dLib.animations.SlideInOrderAnimation;
import com.DR.dLib.ui.dButton;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.AssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class MenuScreen extends dScreen implements AnimationStatusListener {

	// animations
	private SlideInOrderAnimation showButtonsAnimation;
	private final int SHOW_BUTTONS_ID = 123;
	private SlideElasticAnimation hideAnimation;
	private final int HIDE_ANIM_ID = 12345;
	// title
	private dText titleText;
	private float titleTime = 0f, titleDuration = (float)Math.PI/2f;
	// BUTTONS
	private dButton playButton;
	private dButton skinsButton;
	private dButton leaderboardsButton;
	private dButton achievementsButton;
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
		//setColor(236f/256f, 240f/256f, 241f/256f, 1f);
		hideAnimation = new SlideElasticAnimation(1f, this, HIDE_ANIM_ID,0, 0, this);
		setHideAnimation(hideAnimation);
		player = p;
		
		titleText = new dText(0,0,192f,"Bounds");
		titleText.setColor(Color.WHITE);
		
		Color buttonColor = new Color(210f/256f, 82f/256f, 127f/256f, 1f);
		
		//fix
		Texture buttonTexture = AssetManager.getTexture("button");
		playButton = new dButton(0,0, new Sprite(buttonTexture), "play");
		playButton.setTextSize(92f);
		playButton.setColor(buttonColor);
		
		skinsButton = new dButton(0,0, new Sprite(buttonTexture), "skins");
		skinsButton.setTextSize(92f);
		skinsButton.setColor(buttonColor);
		
		leaderboardsButton = new dButton(0,0, new Sprite(buttonTexture), "scores");
		leaderboardsButton.setTextSize(92f);
		leaderboardsButton.setColor(buttonColor);
		
		achievementsButton = new dButton(0,0, new Sprite(buttonTexture), "trophies");
		achievementsButton.setTextSize(92f);
		achievementsButton.setColor(buttonColor);
		
		addObject(titleText, dUICard.CENTER, dUICard.TOP);
		titleText.setY(titleText.getY() + titleText.getHeight());
		addObject(playButton, dUICard.CENTER, dUICard.TOP);
		playButton.setY(playButton.getY() + 364f);
		addObjectUnder(skinsButton, getIndexOf(playButton));
		addObjectUnder(leaderboardsButton, getIndexOf(skinsButton));
		addObjectUnder(achievementsButton, getIndexOf(leaderboardsButton));
		
		showButtonsAnimation = new SlideInOrderAnimation(2f, this, SHOW_BUTTONS_ID, MainGame.VIRTUAL_WIDTH, new dButton[]{playButton, skinsButton, leaderboardsButton, achievementsButton});
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		if(nextScreen != null)
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
			titleText.setY(titleText.getY() - (float)Math.sin(titleTime));
		}
		else
		{
			titleTime = (float)-Math.PI/2f;
		}
		if(nextScreen != null)
		{
			nextScreen.update(delta);
		}
		if(hideAnimation.isActive())
		{
			hideAnimation.update(delta);
		}
		if(showButtonsAnimation.isActive())
		{
			showButtonsAnimation.update(delta);
		}
		if(playButton.isClicked())
		{
			switchScreen(MainGame.gameScreen);
		}
		if(skinsButton.isClicked() && nextScreen == null)
		{
			if(shopScreen == null)
			{
				shopScreen = new ShopScreen(0,0, AssetManager.getTexture("card"), player);
			}
			switchScreen(shopScreen);
		}
	}
	
	@Override
	public void show()
	{
		super.show();
		playButton.setX(playButton.getX() - MainGame.VIRTUAL_WIDTH);
		skinsButton.setX(skinsButton.getX() - MainGame.VIRTUAL_WIDTH);
		leaderboardsButton.setX(leaderboardsButton.getX() - MainGame.VIRTUAL_WIDTH);
		achievementsButton.setX(achievementsButton.getX() - MainGame.VIRTUAL_WIDTH);
		showButtonsAnimation.start();
	}

	@Override
	public void goBack() {
		if(MainGame.previousScreen != null)
		{
			//switchScreen(MainGame.previousScreen);
		}
	}

	@Override
	public void switchScreen(dScreen newScreen) {
		//this.hide();
		nextScreen = newScreen;
		nextScreen.show();
		this.hide();
	}

	@Override
	public void onAnimationStart(int ID, float duration) {
		if(ID == HIDE_ANIM_ID)
		{
			setVisible(true);
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
			nextScreen = null;
		}
	}

}
