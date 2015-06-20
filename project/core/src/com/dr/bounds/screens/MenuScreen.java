package com.dr.bounds.screens;

import com.DR.dLib.dObject;
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
import com.dr.bounds.ui.CircleImageButton;

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
	/*private dButton skinsButton;
	private dButton leaderboardsButton;
	private dButton achievementsButton;*/
	private CircleImageButton shopButton, inventoryButton, leaderboardsButton, achievementsButton;
	private SettingsScreen settingsScreen;
	// shop screen
	private ShopScreen shopScreen = null;
	// inventory screen
	private InventoryScreen inventoryScreen = null;
	// next screen
	private dScreen nextScreen = null;
	// Player instance
	private Player player;
	private OrthographicCamera test;
	private MapGenerator gen;
	// whether the player can tap to play since the menu has various other screens.
	private boolean canPlay = true;
	
	public MenuScreen(float x, float y, Texture texture, Player p) {
		super(x, y, texture);
		this.setPadding(48f);
	//	test = new OrthographicCamera(MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT);
	//	test.setToOrtho(true, MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT);
		gen = new MapGenerator(p);
		gen.generateSeed();
		// TODO: might remove
		gen.generateFirstSet();
		
		this.setClipping(false);
		//setColor(52f/256f, 73f/256f, 94f/256f,1f);
		setColor(236f/256f, 239f/256f, 241f/256f, 0f);
		hideAnimation = new SlideExponentialAnimation(1.5f, this, HIDE_ANIM_ID, 0, MainGame.VIRTUAL_HEIGHT, this);
		setHideAnimation(hideAnimation);
		player = p;
		
		titleImage = new dImage(0,0,BoundsAssetManager.getTexture("BoundsLogo.png"));
		titleImage.setColor(Color.WHITE);
		titleImage.setHasShadow(true);
		
		settingsScreen = new SettingsScreen(0, 0);
		
		Color buttonColor = new Color(0f, 188f/256f, 212f/256f, 1f);
		
		//fix
		Texture buttonTexture = BoundsAssetManager.getTexture("button");
		playButton = new dButton(0,0, buttonTexture, "Tap to play");
		playButton.setTextSize(64f);
		playButton.setTextColor(Color.WHITE);
		playButton.setColor(253f/256f, 216f/256f, 53f/256f, 0f);
		
		shopButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("replay.png"), Color.WHITE);
		shopButton.setColor(buttonColor);
		inventoryButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("homeIcon.png"), Color.WHITE);
		inventoryButton.setColor(buttonColor);
		leaderboardsButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("leaderboardsIcon.png"), Color.WHITE);
		leaderboardsButton.setColor(buttonColor);
		achievementsButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("achievementsIcon.png"), Color.WHITE);
		achievementsButton.setColor(buttonColor);
		
	/*	skinsButton = new dButton(0,0, new Sprite(buttonTexture), "Shop");
		skinsButton.setTextSize(64f);
		skinsButton.setColor(1f, 193f/256f, 7f/256f, 1f);
		
		leaderboardsButton = new dButton(0,0, new Sprite(buttonTexture), "Leaderboards");
		leaderboardsButton.setTextSize(64f);
		leaderboardsButton.setColor(0, 188f/256f, 212f/256f, 1f);
		
		achievementsButton = new dButton(0,0, new Sprite(buttonTexture), "Achievements");
		achievementsButton.setTextSize(64f);
		achievementsButton.setColor(41f/256f, 182f/256f, 246f/256f, 1);*/
		
		addObject(titleImage, dUICard.CENTER, dUICard.CENTER);
		//titleImage.setY(titleImage.getY() + titleImage.getHeight()/2f);
		addObjectUnder(playButton, this.getIndexOf(titleImage));
		addObject(shopButton, dUICard.RIGHT, dUICard.BOTTOM);
		addObjectToLeftOf(leaderboardsButton, this.getIndexOf(shopButton));
		addObjectToLeftOf(achievementsButton, this.getIndexOf(leaderboardsButton));
		addObject(settingsScreen, dUICard.LEFT, dUICard.BOTTOM);
		addObjectToRightOf(inventoryButton, this.getIndexOf(settingsScreen));
		inventoryButton.setX(inventoryButton.getX() - 16f);
		
		showButtonsAnimation = new SlideInOrderAnimation(2f, this, SHOW_BUTTONS_ID, 0, -256f, new dObject[]{playButton, settingsScreen, inventoryButton, shopButton, leaderboardsButton, achievementsButton});
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		gen.render(batch);
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
		if(!hideAnimation.isActive() && nextScreen == null)
		{
			gen.update(delta);
			if(showButtonsAnimation.isFinished())
			{
				// move camera up
				MainGame.setCameraPos(MainGame.camera.position.x, MainGame.camera.position.y - GameScreen.CAMERA_SPEED * delta);
				this.setY(MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f);
			}
		}
		if(showButtonsAnimation.isActive())
		{
			showButtonsAnimation.update(delta);
		}

		if(settingsScreen.isOpen())
		{
			canPlay = false;
		}
		
		if(nextScreen != null)
		{
			nextScreen.update(delta);
		}
		else
		{
			if(Gdx.input.justTouched() && !hideAnimation.isActive())
			{
				if(shopButton.isClicked() && nextScreen == null)
				{
					if(shopScreen == null)
					{
						shopScreen = new ShopScreen(0,0, BoundsAssetManager.getTexture("card"), player);
					}
					switchScreen(shopScreen, false);
					canPlay = false;
				}
				else if(inventoryButton.isClicked())
				{
					// Remove the inventory screen from the list if we already had one.
					if(inventoryScreen != null)
					{
						this.removeObject(this.getIndexOf(inventoryScreen));
					}
					inventoryScreen = new InventoryScreen(player);
					this.addObject(inventoryScreen, dUICard.CENTER, dUICard.CENTER);
					inventoryScreen.show();
					canPlay = false;
				}
				else if(MainGame.getVirtualMouseY() <= shopButton.getY() && canPlay) // only start if the players tap is above the button row
				{
					MainGame.gameScreen = new GameScreen(0, 0, BoundsAssetManager.getTexture("card.png"), gen.getMapType());
					switchScreen(MainGame.gameScreen);
					MainGame.setCameraPos(MainGame.camera.position.x, MainGame.VIRTUAL_HEIGHT / 2f);
				}
			}
		}
	}
	
	@Override
	public void show()
	{
		super.show();
		setPos(0, MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f);
		playButton.setY(playButton.getY() + 256f);
		settingsScreen.setY(settingsScreen.getY() + 256f);
		shopButton.setY(shopButton.getY() + 256f);
		leaderboardsButton.setY(leaderboardsButton.getY() + 256f);
		inventoryButton.setY(inventoryButton.getY() + 256f);
		achievementsButton.setY(achievementsButton.getY() + 256f);
		showButtonsAnimation.start();
		nextScreen = null;
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
		else if(inventoryScreen != null)
		{
			inventoryScreen.hide();
		}
		else
		{
			Gdx.app.exit();
		}
		canPlay = true;
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
