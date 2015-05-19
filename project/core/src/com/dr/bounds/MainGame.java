package com.dr.bounds;

import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.dValues;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.dr.bounds.screens.GameScreen;
import com.dr.bounds.screens.MenuScreen;

public class MainGame extends ApplicationAdapter {
	
	/*==================================================
	 *					VARIABLES					   |
	 *=================================================*/
	
	// RequestHandler, Batch, Camera
	public static OrthographicCamera camera;
	private SpriteBatch batch;
	public static RequestHandler requestHandler;
	
	// CONSTANTS
	public static final float VIRTUAL_WIDTH = 720f, VIRTUAL_HEIGHT = 1280f, ASPECT_RATIO = VIRTUAL_WIDTH / VIRTUAL_HEIGHT;
	public static final int PLACEHOLDER_SKIN_ID = 0;
	public static final int GAME_VERSION = 1;
	public static final String COMBO_LEADERBOARD_ID = "CggI-byO8BkQAhAC", SCORE_LEADERBOARD_ID = "CggI-byO8BkQAhAB";
	
	// SCREENS
	public static dScreen currentScreen;
	public static dScreen previousScreen = null;
	public static MenuScreen menuScreen;
	public static GameScreen gameScreen;
	
	public static boolean isPlaying = false;
	public static boolean isSoundEnabled = false;
	public static boolean isVibrationEnabled = true;
	
	// the time difference between frames
	private final float DELTA = 1f/60f;
	// the time each update call takes ?
	private float accumulator = 0f;
	
	// test, remove
	private static Sound scoreSound, scoreSound2, deathSound;
	private dText fpsText;

	public MainGame(RequestHandler h)
	{
		requestHandler = h;
	}
	
	@Override
	public void create () {
		Gdx.input.setCatchBackKey(true);
		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		camera.setToOrtho(true,VIRTUAL_WIDTH,VIRTUAL_HEIGHT);
		dValues.init(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
		BoundsAssetManager.loadAll();
		
		gameScreen = new GameScreen(0,0,BoundsAssetManager.getTexture("card.png"));
		
		menuScreen = new MenuScreen(0,0,BoundsAssetManager.getTexture("card"), gameScreen.getPlayer());
		
		batch = new SpriteBatch();
		
		scoreSound = Gdx.audio.newSound(Gdx.files.internal("score.wav"));
		scoreSound2 = Gdx.audio.newSound(Gdx.files.internal("score2.wav"));
		deathSound = Gdx.audio.newSound(Gdx.files.internal("death.wav"));
		
		fpsText = new dText(5,5,24,"FPS: 60");
		fpsText.setColor(Color.WHITE);
		currentScreen = menuScreen;
		currentScreen.show();
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0,0, (int)Gdx.graphics.getWidth(), (int)Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(189f/256f, 195f/256f, 199f/256f,.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		if(Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.SPACE))
		{	
			currentScreen.goBack();
		}
		
		// UPDATE
		accumulator += Gdx.graphics.getDeltaTime();
		while(accumulator >= DELTA)
		{
			update(DELTA);
			fpsText.setPos(camera.position.x - MainGame.VIRTUAL_WIDTH / 2f + 5, camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f + 5f);
			fpsText.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
			accumulator -= DELTA;
		}
		batch.setProjectionMatrix(camera.combined);
		batch.begin();	
		currentScreen.render(batch);
		fpsText.render(batch);
		batch.end();
	}
	
	public void update(float delta)
	{
		currentScreen.update(delta);
		if(previousScreen != null && currentScreen != previousScreen)
		{
		//	previousScreen.update(delta);
		}
		//update camera
		camera.update();
	}
	
	public static int getVirtualMouseX()
	{
		return (int) (camera.position.x - VIRTUAL_WIDTH / 2f + (Gdx.input.getX() / (Gdx.graphics.getWidth() / VIRTUAL_WIDTH)));
	}
	
	public static int getVirtualMouseY()
	{
		return (int) (camera.position.y - VIRTUAL_HEIGHT / 2f + Gdx.input.getY() / (Gdx.graphics.getHeight() / VIRTUAL_HEIGHT));
	}
	
	public static void setCameraPos(float x, float y)
	{
		camera.position.set(x,y, camera.position.z);
	}
	
	public static void playSound(String sound)
	{
		if(isSoundEnabled)
		{
			if(sound.equalsIgnoreCase("score"))
			{
				if(MathUtils.randomBoolean())
				{
					scoreSound.play(0.75f);
				}
				else
				{
					scoreSound2.play(0.75f);
				}
			}
			else if(sound.equalsIgnoreCase("death"))
			{
				deathSound.play();
			}
			else if(sound.equalsIgnoreCase("coin"))
			{
				deathSound.play();
			}
		}
	}

	public static int getPlayerSkinID()
	{
		return gameScreen.getPlayer().getSkinID();
	}
	
	public static void setPlayerSkin(byte id) {
		gameScreen.setPlayerSkin(id);
	}
	
	@Override
	public void dispose()
	{
		BoundsAssetManager.disposeAll();
	}
}
