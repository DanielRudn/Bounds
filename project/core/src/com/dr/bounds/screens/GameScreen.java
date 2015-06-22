package com.dr.bounds.screens;

import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.dTweener;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.animations.CameraShakeAnimation;
import com.dr.bounds.maps.MapGenerator;

public class GameScreen extends dScreen implements AnimationStatusListener {
	
	// current clients player
	private Player player;
	// camera's speed in pixels per second (camera moves upward)
	public static float CAMERA_SPEED = 500f;
	// Generate the map
	private MapGenerator mapGen;
	// screen that will show when game ends
	private GameOverScreen gameOverScreen;
	// screen that will show when game is paused
	private PauseScreen pauseScreen;
	// keep track of players score this round
	private int playerScore = 0;
	// keep track of players combo this round
	private int playerCombo = 0;
	// keep track of players HIGHEST combo this round
	private int highestCombo = 0;
	// keep track of players coins this round
	private static int playerCoins = 0;
	// used to display player score
	private dText scoreText;
	// animate score changing
	private float scoreTime = 0;
	// used to display player combo
	private dText comboText;
	// used to display current coins
	private dUICard coinInfo;
	// death animation for the player
	private dAnimation deathAnim;
	private final static int DEATH_ANIM_ID = 321;
	// camera to display score and coin count so they don't move with the game camera
	private OrthographicCamera uiCam;

	public GameScreen(float x, float y, Texture texture, Player p) {
		super(x, y, texture);
		uiCam = new OrthographicCamera(MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT);
		uiCam.setToOrtho(true, MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT);
		this.player = p;
		player.reset();
		deathAnim = new CameraShakeAnimation(0.48f, this, DEATH_ANIM_ID);

		mapGen = new MapGenerator(player);
		mapGen.generateSeed();
		// TODO: might remove
		mapGen.generateFirstSet();
		
		gameOverScreen = new GameOverScreen(getX(), getY(), texture, player);
		gameOverScreen.hide();
		
		pauseScreen = new PauseScreen(getX(), getY(), texture, this);
		
		comboText = new dText(0,0,64f,"COMBO: 0");
		comboText.setColor(Color.WHITE);
		comboText.setShadow(true);
		
		scoreText = new dText(0,0,192f,"0");
		scoreText.setColor(1,1,1,1);
		scoreText.setShadow(true);
		
		coinInfo = new dUICard(16,0,BoundsAssetManager.getTexture("card"));
		coinInfo.setClipping(false);
		coinInfo.setAlpha(0);
		coinInfo.setHasShadow(false);
		coinInfo.setDimensions(128f, 64f);
		coinInfo.setY(uiCam.position.y - MainGame.VIRTUAL_HEIGHT / 2f + 32f);
		dImage coinImage = new dImage(0,0,BoundsAssetManager.getTexture("coin.png"));
		coinImage.setHasShadow(true);
		dText coinText = new dText(0,0,55f,"x0");
		coinText.setColor(Color.WHITE);
		coinText.setShadow(true);
		coinInfo.addObject(coinText, dUICard.RIGHT, dUICard.CENTER);
		coinInfo.addObject(coinImage, dUICard.LEFT, dUICard.CENTER);	
		
		addObject(scoreText,dUICard.CENTER, dUICard.TOP);
		comboText.setPos(scoreText.getX(), scoreText.getY() + scoreText.getHeight() + 4f);
		scoreText.setY(uiCam.position.y - MainGame.VIRTUAL_HEIGHT / 2f + 32f);
	}
	
	public GameScreen(float x, float y, Texture texture, Player p, int mapType)
	{
		this(x, y, texture, p);
		mapGen = new MapGenerator(mapType, player);
		mapGen.generateSeed();
		// TODO: might remove
		mapGen.generateFirstSet();
	}
	
	@Override
	public void update(float delta)
	{
		if(pauseScreen.isPaused() == false)
		{
			super.update(delta);
			gameOverScreen.update(delta);
			if(deathAnim.isActive())
			{
				deathAnim.update(delta);
			}
			if(mapGen.hadCollision() && gameOverScreen.isVisible() == false)
			{
				gameOverScreen.setScore(playerScore);
				if(deathAnim.isActive() == false)
				{
					deathAnim.start();
				}
			}
			// single player game over screen
			else if(mapGen.hadCollision() && gameOverScreen.wantsReplay())
			{
				// player wants a replay
				reset();
				mapGen.update(delta); // TODO: FIX, this is here to set the bgColor after switching map types on start
			}
			else if(!mapGen.hadCollision() && gameOverScreen.isVisible() == false)
			{
				// only update player if game is running
				player.update(delta);
				mapGen.update(delta);
				// get players score from the map gen
				playerScore = mapGen.getScore();
				playerCombo = mapGen.getCombo();
				// keep track of the highest combo this round.
				if(playerCombo > highestCombo) 
				{
					highestCombo = playerCombo;
				}
				comboText.setText("COMBO: " + Integer.toString(playerCombo));
				comboText.setPos(MainGame.VIRTUAL_WIDTH / 2f - comboText.getWidth() / 2f, scoreText.getY() + scoreText.getHeight() + 4f);
				((dText)coinInfo.getObject(0)).setText("x" + playerCoins);

				if(mapGen.hasScoreChanged())
				{
					scoreText.setText(Integer.toString(playerScore));
					scoreText.setX(getX() + getWidth()/2f - scoreText.getWidth()/2f);
					scoreTime = 0;
					mapGen.setScoreChanged(false);
				}
				if(scoreTime <= 2f)
				{
					scoreTime+=delta;
					scoreText.setSize(dTweener.ElasticOut(scoreTime, 300f, 192f - 300f, 1.5f));
				}
				// move camera upward
				if(playerCombo < 3)
				{
					MainGame.setCameraPos(MainGame.camera.position.x, MainGame.camera.position.y - CAMERA_SPEED * delta);
				}
				else
				{ 
					// follow player vertically as long as they have a combo going.
					if(player.getY() >= MainGame.camera.position.y - 50 && player.getY() <= MainGame.camera.position.y + 50)
					{
						MainGame.setCameraPos(MainGame.camera.position.x, player.getY() + player.getWidth() / 2f); 
					}
					else if((MainGame.camera.position.y + 60) <= (player.getY() + player.getWidth()))
					{
						MainGame.camera.position.y = dTweener.MoveToAndSlow(MainGame.camera.position.y, MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT, 1f*delta);
					}
					else if((MainGame.camera.position.y + 60) >= (player.getY() + player.getWidth()))
					{
						MainGame.camera.position.y = dTweener.MoveToAndSlow(MainGame.camera.position.y, MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT, 1f*delta);
					}
				}
			}
		}
		else
		{
			pauseScreen.update(delta);
		}
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		mapGen.render(batch);
		player.render(batch);
		if(pauseScreen.isPaused())
		{
			pauseScreen.render(batch);
		}
		batch.setProjectionMatrix(uiCam.combined);
		scoreText.render(batch);
		coinInfo.render(batch);
		if(playerCombo >= 2)
		{
			comboText.render(batch);
		}
		batch.setProjectionMatrix(MainGame.camera.combined);	
		gameOverScreen.render(batch);
	}
	
	/**
	 * Reset the game state
	 */
	public void reset()
	{
		deathAnim.stop();
		player.reset();
		mapGen.setHadCollision(false);
		scoreText.setText(Integer.toString(0));
		playerCoins = 0;
		highestCombo = 0;
		scoreText.setX(getX() + getWidth()/2f - scoreText.getWidth()/2f);
		MainGame.camera.position.y = MainGame.VIRTUAL_HEIGHT/2f;
	}
	
	/**
	 * Called by the MapType containing the coin set when a coin is picked up
	 */
	public static void incrementPlayerCoins()
	{
		playerCoins++;
	}
	
	public void setPlayerSkin(int id)
	{
		player.setSkinID(id);
	}
	
	public int getPlayerSkinID()
	{
		return player.getSkinID();
	}
	
	public Player getPlayer()
	{
		return player;
	}

	public int getScore()
	{
		return mapGen.getScore();
	}
	
	@Override
	public void goBack() {
	/*	if(MainGame.previousScreen != null)
		{
			switchScreen(MainGame.previousScreen);
		}
		MainGame.camera.position.y = MainGame.VIRTUAL_HEIGHT / 2f;
		reset();*/
		pauseScreen.show();
	}

	@Override
	public void switchScreen(dScreen newScreen) {
		newScreen.show();
		MainGame.currentScreen = newScreen;
	}


	@Override
	public void onAnimationStart(int ID, float duration)
	{
		if(ID == DEATH_ANIM_ID)
		{
			
		}
	}

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta)
	{
		if(ID == DEATH_ANIM_ID)
		{
			player.setAlpha(dTweener.LinearEase(time, 1f, -1f, duration));
		}
	}
	
	@Override
	public void onAnimationFinish(int ID) {
		if(ID == DEATH_ANIM_ID)
		{
			MainGame.camera.position.set(((CameraShakeAnimation)deathAnim).getCameraStartPosition());
			MainGame.camera.rotate(((CameraShakeAnimation)deathAnim).getRotation());
			MainGame.camera.update();
			gameOverScreen.show(playerScore, highestCombo, playerCoins);
		}
	}
}