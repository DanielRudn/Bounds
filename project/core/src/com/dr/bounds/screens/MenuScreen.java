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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;

public class MenuScreen extends dScreen implements AnimationStatusListener {

	// animations
	private SlideInOrderAnimation showButtonsAnimation;
	private final int SHOW_BUTTONS_ID = 123;
	private dAnimation hideAnimation;
	private final int HIDE_ANIM_ID = 12345;
	// title
	private dImage titleImage;
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
	private OrthographicCamera test;
	private MapGenerator gen;
	
	public MenuScreen(float x, float y, Texture texture, Player p) {
		super(x, y, texture);
		test = new OrthographicCamera(MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT);
		test.setToOrtho(true, MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT);
		gen = new MapGenerator(p);
		gen.generateSeed();
		// TODO: might remove
		gen.generateFirstSet();
		
		this.setClipping(false);
		//setColor(52f/256f, 73f/256f, 94f/256f,1f);
		setColor(236f/256f, 239f/256f, 241f/256f, 0f);
		setPaddingTop(16f);
		hideAnimation = new SlideExponentialAnimation(1.5f, this, HIDE_ANIM_ID, 0, MainGame.VIRTUAL_HEIGHT, this);
		setHideAnimation(hideAnimation);
		player = p;
		
		titleImage = new dImage(0,0,BoundsAssetManager.getTexture("BoundsLogo.png"));
		titleImage.setColor(Color.WHITE);
		titleImage.setHasShadow(true);
		
		settingsScreen = new SettingsScreen(0, 0);
		
		//Color buttonColor = new Color(3f/256f, 169f/256f, 244f/256f, 1f);
		
		//fix
		Texture buttonTexture = BoundsAssetManager.getTexture("button");
		playButton = new dButton(0,0, new Sprite(buttonTexture), "Tap to begin");
		playButton.setTextSize(64f);
		playButton.setTextColor(Color.WHITE);
		playButton.setColor(253f/256f, 216f/256f, 53f/256f, 0f);
		
		skinsButton = new dButton(0,0, new Sprite(buttonTexture), "Shop");
		skinsButton.setTextSize(64f);
		skinsButton.setColor(1f, 193f/256f, 7f/256f, 1f);
		
		leaderboardsButton = new dButton(0,0, new Sprite(buttonTexture), "Leaderboards");
		leaderboardsButton.setTextSize(64f);
		leaderboardsButton.setColor(0, 188f/256f, 212f/256f, 1f);
		
		achievementsButton = new dButton(0,0, new Sprite(buttonTexture), "Achievements");
		achievementsButton.setTextSize(64f);
		achievementsButton.setColor(41f/256f, 182f/256f, 246f/256f, 1);
		
		addObject(titleImage, dUICard.CENTER, dUICard.CENTER);
		//titleImage.setY(titleImage.getY() + titleImage.getHeight()/2f);
		addObjectUnder(playButton, this.getIndexOf(titleImage));
	//	addObjectUnder(skinsButton, getIndexOf(playButton));
	//	addObjectUnder(leaderboardsButton, getIndexOf(skinsButton));
	//	addObjectUnder(achievementsButton, getIndexOf(leaderboardsButton));
	//	addObject(settingsScreen, dUICard.LEFT, dUICard.BOTTOM);
		
		showButtonsAnimation = new SlideInOrderAnimation(2f, this, SHOW_BUTTONS_ID, MainGame.VIRTUAL_WIDTH, new dButton[]{playButton, skinsButton, leaderboardsButton, achievementsButton});
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		if(!this.hideAnimation.isActive())
		{
			gen.render(batch);
			batch.setProjectionMatrix(test.combined);
			if(nextScreen != null && nextScreen instanceof GameScreen)
			{
				nextScreen.render(batch);
			}
			super.render(batch);
			if(nextScreen != null && nextScreen instanceof ShopScreen)
			{
				nextScreen.render(batch);
			}
			batch.setProjectionMatrix(MainGame.camera.combined);
		}
		else
		{
			batch.setProjectionMatrix(MainGame.camera.combined);
			gen.render(batch);
			if(nextScreen != null && nextScreen instanceof GameScreen)
			{
				nextScreen.render(batch);
			}
			super.render(batch);
		}
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(!hideAnimation.isActive())
		{
			gen.update(delta);
			// move camera up
			MainGame.setCameraPos(MainGame.camera.position.x, MainGame.camera.position.y - GameScreen.CAMERA_SPEED * delta);
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
		if(Gdx.input.justTouched() && !hideAnimation.isActive())
		{
			MainGame.gameScreen = new GameScreen(0, 0, BoundsAssetManager.getTexture("card.png"), gen.getMapType());
			switchScreen(MainGame.gameScreen);
			MainGame.setCameraPos(MainGame.camera.position.x, MainGame.VIRTUAL_HEIGHT / 2f);
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
		nextScreen.show();
		this.hide();
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
