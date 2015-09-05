package com.dr.bounds.screens;

import com.DR.dLib.dObject;
import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.SlideExponentialAnimation;
import com.DR.dLib.animations.SlideInOrderAnimation;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dToggleCard;
import com.DR.dLib.ui.dUICard;
import com.DR.dLib.utils.dUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.MapTypeFactory;
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
	private dText playText;
	private CircleImageButton shopButton, settingsButton, inventoryButton, leaderboardsButton, achievementsButton;
	private SettingsScreen settingsScreen;
	// shop screen
	private ShopScreen shopScreen = null;
	// inventory screen
	private InventoryScreen inventoryScreen = null;
	// next screen
	private dScreen nextScreen = null;
	// Player instance
	private Player player;
	private MapGenerator gen;
	// whether the player can tap to play since the menu has various other screens.
	private boolean canPlay = true;
	
	public MenuScreen(float x, float y, Texture texture, Player p) {
		super(x, y, texture);
		this.setPadding(42f);
		gen = new MapGenerator(p);
		gen.generateSeed();
		// TODO: might remove
		gen.generateFirstSet();
		gen.update(1f/60f); // TODO: FIX.
		
		this.setClipping(false);
		//setColor(52f/256f, 73f/256f, 94f/256f,1f);
		setColor(236f/256f, 239f/256f, 241f/256f, 0f);
		// hide anim only waits 1.5f and switches MainGame.currentScreen to gameScreen.
		hideAnimation = new SlideExponentialAnimation(1.5f, this, HIDE_ANIM_ID, 0, 0, this);
		this.setHideAnimation(hideAnimation);
		this.player = p;
		
		titleImage = new dImage(0,0,BoundsAssetManager.getTexture("BoundsLogo.png"));
		titleImage.setColor(Color.WHITE);
		titleImage.setHasShadow(true);
		
		settingsScreen = new SettingsScreen(0, 0);
		
		Color buttonColor = new Color(0f, 188f/256f, 212f/256f, 1f);

		playText = new dText(0, 0, 64f, "Tap to Play");
		playText.setShadow(true);
		playText.setColor(Color.WHITE);
		
		shopButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("replay.png"), Color.WHITE);
		shopButton.setColor(buttonColor);
		settingsButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("gear.png"), Color.WHITE);
		settingsButton.setColor(buttonColor);
		inventoryButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("homeIcon.png"), Color.WHITE);
		inventoryButton.setColor(buttonColor);
		leaderboardsButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("leaderboardsIcon.png"), Color.WHITE);
		leaderboardsButton.setColor(buttonColor);
		achievementsButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("achievementsIcon.png"), Color.WHITE);
		achievementsButton.setColor(buttonColor);
		
		addObject(titleImage, dUICard.CENTER, dUICard.CENTER);
		addObjectUnder(playText, dUICard.CENTER, this.getIndexOf(titleImage));
		addObject(settingsButton, dUICard.LEFT, dUICard.BOTTOM);
		addObjectToRightOf(inventoryButton, this.getIndexOf(settingsButton));
		addObjectToRightOf(achievementsButton, this.getIndexOf(inventoryButton));
		addObjectToRightOf(leaderboardsButton, this.getIndexOf(achievementsButton));
		addObjectToRightOf(shopButton, this.getIndexOf(leaderboardsButton));
		
		showButtonsAnimation = new SlideInOrderAnimation(2f, this, SHOW_BUTTONS_ID, 0, -256f, new dObject[]{playText, settingsButton, inventoryButton, achievementsButton, leaderboardsButton, shopButton});
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
			if(showButtonsAnimation.isFinished())
			{
				gen.update(delta);
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
				if(shopButton.isClicked() && nextScreen == null && canPlay)
				{
					if(shopScreen == null)
					{
						shopScreen = new ShopScreen(getX(), getY(), BoundsAssetManager.getTexture("card"), player);
					}
					switchScreen(shopScreen, false);
					canPlay = false;
				}
				else if(settingsButton.isClicked() && canPlay)
				{
					this.addObject(settingsScreen, dUICard.LEFT_NO_PADDING, dUICard.BOTTOM_NO_PADDING);
					settingsScreen.show();
				}
				else if(inventoryButton.isClicked() && canPlay)
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
				else if(dUtils.getVirtualMouseY() <= shopButton.getY() && canPlay) // only start if the players tap is above the button row
				{
					MainGame.gameScreen = new GameScreen(0, 0, BoundsAssetManager.getTexture("card.png"), player, gen.getMapType());
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
		playText.setY(playText.getY() + 256f);
		settingsButton.setY(settingsButton.getY() + 256f);
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
		this.setPos(this.getX(), 0);
		nextScreen = newScreen;
		nextScreen.show();
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
