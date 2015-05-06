package com.dr.bounds.screens;

import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.dTweener;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dr.bounds.AssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.animations.CameraShakeAnimation;
import com.dr.bounds.animations.TranslateCameraAnimation;
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
	// keep track of players score this round
	private int playerScore = 0;
	// keep track of players combo this round
	private int playerCombo = 0;
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
	// Animation to move the camera so the player is vertically centered when on a combo >= 3.
	private TranslateCameraAnimation camAnim;

	public GameScreen(float x, float y, Texture texture) {
		super(x, y, texture);
		
		player = new Player(MainGame.VIRTUAL_WIDTH/2f-32f,MainGame.VIRTUAL_HEIGHT/2f-32f, 6);
		//deathAnim = new PlayerDeathAnimation(.75f,player);
		deathAnim = new CameraShakeAnimation(0.32f, this, DEATH_ANIM_ID);
		
		mapGen = new MapGenerator(player);
		mapGen.generateSeed();
		// TODO: might remove
		mapGen.generateFirstSet();
		
		gameOverScreen = new GameOverScreen(getX(), getY(), texture, player);
		gameOverScreen.hide();
		
		comboText = new dText(0,0,64f,"COMBO: 0");
		comboText.setColor(Color.WHITE);
		comboText.setShadow(true);
		
		scoreText = new dText(0,0,192f,"0");
		scoreText.setColor(1,1,1,1);
		scoreText.setShadow(true);
		
		coinInfo = new dUICard(16,0,AssetManager.getTexture("card"));
		coinInfo.setClipping(false);
		coinInfo.setAlpha(0);
		coinInfo.setHasShadow(false);
		coinInfo.setDimensions(128f, 64f);
		dImage coinImage = new dImage(0,0,AssetManager.getTexture("coin.png"));
		coinImage.setHasShadow(true);
		dText coinText = new dText(0,0,55f,"x0");
		coinText.setColor(Color.WHITE);
		coinText.setShadow(true);
		coinInfo.addObject(coinText, dUICard.RIGHT, dUICard.CENTER);
		coinInfo.addObject(coinImage, dUICard.LEFT, dUICard.CENTER);	
		
		addObject(scoreText,dUICard.CENTER, dUICard.TOP);
		comboText.setPos(scoreText.getX(), scoreText.getY() + scoreText.getHeight() + 4f);
	}
	
	@Override
	public void update(float delta)
	{
		if(isPaused() == false)
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
				comboText.setText("COMBO: " + Integer.toString(playerCombo));
				scoreText.setY(MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f + 16f);
				coinInfo.setY(MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f + 16f);
				((dText)coinInfo.getObject(0)).setText("x" + playerCoins);
				comboText.setPos(MainGame.VIRTUAL_WIDTH / 2f - comboText.getWidth() / 2f, scoreText.getY() + scoreText.getHeight() + 4f);
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
						MainGame.setCameraPos(MainGame.camera.position.x, MainGame.camera.position.y + 1250f * delta);
					}
					else if((MainGame.camera.position.y + 60) >= (player.getY() + player.getWidth()))
					{
						MainGame.setCameraPos(MainGame.camera.position.x, MainGame.camera.position.y - 1250f * delta);
					}
				}
			}
		}
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		mapGen.render(batch);
		player.render(batch);
		scoreText.render(batch);
		coinInfo.render(batch);
		gameOverScreen.render(batch);
		if(playerCombo >= 2)
		{
			comboText.render(batch);
		}
	}
	
	@Override
	public void resume()
	{
		super.resume();
	}
	
	/**
	 * Reset the game state
	 */
	private void reset()
	{
		deathAnim.stop();
		player.reset();
		mapGen.setHadCollision(false);
		scoreText.setText(Integer.toString(0));
		playerCoins = 0;
		scoreText.setX(getX() + getWidth()/2f - scoreText.getWidth()/2f);
	}
	
	public long getSeed()
	{
		return mapGen.getSeed();
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
		if(MainGame.previousScreen != null)
		{
			switchScreen(MainGame.previousScreen);
		}
		MainGame.camera.position.y = MainGame.VIRTUAL_HEIGHT / 2f;
		reset();
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
			gameOverScreen.show(playerScore, playerCombo, playerCoins);
		}
	}
}