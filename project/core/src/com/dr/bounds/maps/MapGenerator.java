package com.dr.bounds.maps;

import java.util.Random;

import com.DR.dLib.TimerListener;
import com.DR.dLib.dTimer;
import com.DR.dLib.ui.dImage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.screens.GameScreen;

public class MapGenerator implements TimerListener {

	// map generation type
	public static final int TYPE_DEFAULT = 1515, TYPE_SPACE = 3030, TYPE_MACHINERY = 6060, TYPE_SKY = 9090;
	// the current map generation type
	private MapType currentType; 
	// the next map generation type
	private MapType nextType = null;
	// Random number generator for positioning objects
	public static Random rng = new Random();
	private Random test = new Random();
	// seed for random number generator
	private long seed = 123456789;
	// player object
	private Player player;
	// useless REMOVE PLEASE
	Rectangle useless = new Rectangle();
	// whether or not the player had a collision
	private boolean hadCollision = false;
	// player score to give to game screen
	private int score = 0;
	// used to let gamescreen know that it should show the score change animation
	private boolean scoreChanged = false;
	// amount to increment score by since the MapTypes can vary in difficulty and award different amounts of score
	private int scoreIncrementAmount = 1;
	// timer for switching map type
	private dTimer typeSwitchTimer;
	
	// test, remove
	public static dImage transitionImage;
	
	/**
	 * Creates a new generator and sets the level type
	 * @param mapType Type of map to generate, use static attributes from this class as parameters
	 */
	public MapGenerator(int mapType, Texture obstacleTexture, Player player)
	{
		generateSeed();
		this.player = player;
		if(mapType == TYPE_DEFAULT)
		{
			currentType = new DefaultMapType(TYPE_DEFAULT, player, this);
		}
		else if(mapType == TYPE_MACHINERY)
		{
			currentType = new MachineryMapType(TYPE_MACHINERY, player, obstacleTexture, this);
		}
		else if(mapType == TYPE_SPACE)
		{
			currentType = new SpaceMapType(TYPE_SPACE, player, this);
		}
		else if(mapType == TYPE_SKY)
		{
			currentType = new SkyMapType(TYPE_SKY, player, this);
		}
		
		transitionImage = new dImage(0,2000, new Texture("transitionDevice.png"));
		
		typeSwitchTimer = new dTimer(5f,5f,0,this);
		typeSwitchTimer.start();
	}
	
	public void update(float delta)
	{
		currentType.update(delta);
	//	if(typeSwitchTimer.isRunning() && (MainGame.requestHandler.isMultiplayer() == false || MainGame.requestHandler.isHost()))
	//	{
			typeSwitchTimer.update(delta);
	//	}
		if(nextType != null && currentType.shouldSwitch())
		{
			currentType = nextType;
			nextType = null;
			typeSwitchTimer.start();
		}
		else if(nextType != null)
		{
			// update the obstacle generation for the next type but not the collision so that they dont generate on the players
			// update backgrounds
			if(nextType.firstBG.getY() >= MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f)
			{
				nextType.firstBG.setY(nextType.secondBG.getY() - nextType.firstBG.getHeight());
			}
			if(nextType.secondBG.getY() >= MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f)
			{
				nextType.secondBG.setY(nextType.firstBG.getY() - nextType.secondBG.getHeight());
			}
			for(int x = 0; x < nextType.obstacles.size(); x++)
			{
				nextType.obstacles.get(x).update(delta);
				if(nextType.obstacles.get(x).shouldRegenerate())
				{
					nextType.generate(x);
					nextType.obstacles.get(x).setRegenerate(false);
				}
			}
		}
		
		// loop through all currentType.getObstacles()
		for(int x = 0; x < currentType.getObstacles().size(); x++)
		{
			currentType.getObstacles().get(x).update(delta);
			// check players score
			if(currentType.getObstacles().get(x).hasPassed() && currentType.getObstacles().get(x).hasIncrementedScore() == false)
			{
				incrementScore();
				currentType.getObstacles().get(x).setIncrementedScore(true);
				scoreChanged = true;
			}
		}

	}

	public void render(SpriteBatch batch)
	{
		currentType.render(batch);
		transitionImage.render(batch);
	}
	
	public void setSeed(long s)
	{
		seed = s;
		rng.setSeed(seed);
		test.setSeed(seed);
	}
	
	public void generateSeed()
	{
		setSeed(Math.abs(rng.nextLong()));
	}
	
	public void setHadCollision(boolean c)
	{
		hadCollision = c;
		if(c == false) // reset
		{
			reset();
		}
		else if(c)
		{
	//		MainGame.requestHandler.sendReliableMessage(new byte[]{'C', (byte) getScore()});
		}
	}
	
	private void reset()
	{
		/*
		// reset currentType.getObstacles()
		for(int x = 0; x < currentType.getObstacles().size(); x++)
		{
			currentType.getObstacles().get(x).setY(0);
			currentType.getObstacles().get(x).setRegenerate(true);
			//currentType.getObstacles().get(x).setColor(Color.RED);
			currentType.getObstacles().get(x).setPassed(false);
			currentType.getObstacles().get(x).setIncrementedScore(true);
		}
		*/
		//	determine which map type is set
		int newType = test.nextInt(4);
		//GameScreen.log("newType: " + newType);
		if(newType == 0)
		{
			currentType = new DefaultMapType(TYPE_DEFAULT, player, this);
		}
		else if(newType == 1)
		{
			currentType = new MachineryMapType(TYPE_MACHINERY, player, new Texture("girder.png"), this);
		}
		else if(newType == 2)
		{
			currentType = new SpaceMapType(TYPE_SPACE, player, this);
		}
		else if(newType == 3)
		{
			currentType = new SkyMapType(TYPE_SKY, player, this);
		}
	//	if(MainGame.requestHandler.isMultiplayer() == false)
	//	{	
			generateSeed();
			generateFirstSet();
			GameScreen.log("Called");
	//	}
		nextType = null;
		currentType.nextType = null;
		currentType.reset();
		score = 0;
		transitionImage.setY(MainGame.VIRTUAL_HEIGHT * 2f);
	}
	
	public void generateFirstSet()
	{
		currentType.generateFirstSet();
	}
	
	public void setScoreChanged(boolean scoreChanged)
	{
		this.scoreChanged = scoreChanged;
	}
	
	public void setScoreIncrementAmount(int amount)
	{
		scoreIncrementAmount = amount;
	}
	
	public void incrementScore()
	{
		score+=scoreIncrementAmount;
	}
	
	/**
	 * Used to set MapGenerators new map type when a message is received from opponent when not host
	 * @param msg 
	 */
	public void setMapType(byte[] msg)
	{
		int newType = msg[1];
		setNextType(newType);
		/*if(nextType == null)
		{
			typeSwitchTimer.start();
			MainGame.requestHandler.sendReliableMessage(new byte[]{'T',(byte) newType});// send message to opponent with new map type 
		}*/
		currentType.setNextMapType(nextType);
	}
	
	private void setNextType(int MAP_TYPE)
	{
		if(MAP_TYPE == 0)
		{
			if(currentType.getMapType() != TYPE_DEFAULT)
			{
				nextType = new DefaultMapType(TYPE_DEFAULT, player, this);
				}
		}
		else if(MAP_TYPE == 1)
		{
			if(currentType.getMapType() != TYPE_MACHINERY)
			{
				nextType = new MachineryMapType(TYPE_MACHINERY, player, new Texture("girder.png"), this);
			}
		}
		else if(MAP_TYPE == 2)
		{
			if(currentType.getMapType() != TYPE_SPACE)
			{
				nextType = new SpaceMapType(TYPE_SPACE, player, this);
			}
		}
		else if(MAP_TYPE == 3)
		{
			if(currentType.getMapType() != TYPE_SKY)
			{
				nextType = new SkyMapType(TYPE_SKY, player, this);
			}
		}
	}

	
	public boolean hasScoreChanged()
	{
		return scoreChanged;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public int getMapType()
	{
		return currentType.getMapType();
	}
	
	public long getSeed()
	{
		return seed;
	}
	
	public boolean hadCollision()
	{
		return hadCollision;
	}

	@Override
	public void onTimerFinish(int ID)
	{
		//	determine which map type is set
		int newType = rng.nextInt(4);
		setNextType(newType);
		if(nextType == null)
		{
		//	if(MainGame.requestHandler.isMultiplayer() && MainGame.requestHandler.isHost())
		//	{
				typeSwitchTimer.start();
		//		MainGame.requestHandler.sendReliableMessage(new byte[]{'T',(byte) newType});// send message to opponent with new map type 
		//	}
		//	else if(MainGame.requestHandler.isMultiplayer() == false)
		//	{
		//		typeSwitchTimer.start();
		//	}
		}
		currentType.setNextMapType(nextType);
	}
	
	@Override
	public void onTimerStart(int ID) {
		System.out.println("timer started");
	}

	@Override
	public void onTimerTick(int ID) {}
	
}