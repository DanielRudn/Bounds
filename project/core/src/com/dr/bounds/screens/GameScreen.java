package com.dr.bounds.screens;

import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.dTweener;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.AssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.animations.PlayerDeathAnimation;
import com.dr.bounds.maps.maptypes.MapGenerator;

public class GameScreen extends dScreen {
	
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
	private PlayerDeathAnimation deathAnim;

	public GameScreen(float x, float y, Texture texture) {
		super(x, y, texture);
		
		player = new Player(MainGame.VIRTUAL_WIDTH/2f-32f,MainGame.VIRTUAL_HEIGHT/2f-32f, 6);
		deathAnim = new PlayerDeathAnimation(.75f,player);
		
		//mapGen = new MapGenerator(MapGenerator.TYPE_ICE, player);
		mapGen = new MapGenerator(player);
		mapGen.generateSeed();
		// TODO: might remove
		mapGen.generateFirstSet();
		
		gameOverScreen = new GameOverScreen(getX(), getY(), texture, player);
		gameOverScreen.hide();
		
		comboText = new dText(0,0,64f,"COMBO: 0");
		comboText.setColor(Color.WHITE);
		
		scoreText = new dText(0,0,192f,"0");
		scoreText.setColor(1,1,1,1);
		
		coinInfo = new dUICard(16,0,AssetManager.getTexture("card"));
		coinInfo.setClipping(false);
		coinInfo.setAlpha(0);
		coinInfo.setHasShadow(false);
		coinInfo.setDimensions(128f, 64f);
		dImage coinImage = new dImage(0,0,AssetManager.getTexture("coin.png"));
		dText coinText = new dText(0,0,55f,"x0");
		coinText.setColor(Color.WHITE);
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
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gameOverScreen.show();
				//deathAnim.start();
				//dialog.show();'
			}
			// single player game over screen
			else if(mapGen.hadCollision() && gameOverScreen.wantsReplay())
			{
				// player wants a replay
				deathAnim.stop();
				player.reset();
				mapGen.setHadCollision(false);
				scoreText.setText(Integer.toString(0));
				playerCoins = 0;
				scoreText.setX(getX() + getWidth()/2f - scoreText.getWidth()/2f);
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
				MainGame.setCameraPos(MainGame.camera.position.x, MainGame.camera.position.y - CAMERA_SPEED * delta);
			
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
	}

	@Override
	public void switchScreen(dScreen newScreen) {
		newScreen.show();
		MainGame.currentScreen = newScreen;
	}
}