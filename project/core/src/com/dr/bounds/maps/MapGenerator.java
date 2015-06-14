package com.dr.bounds.maps;

import java.util.Random;

import com.DR.dLib.TimerListener;
import com.DR.dLib.dTimer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class MapGenerator implements TimerListener {

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
	// player combo
	private int combo = 0;
	// used to let gamescreen know that it should show the score change animation
	private boolean scoreChanged = false;
	// timer for switching map type
	private dTimer typeSwitchTimer;
	
	/**
	 * Creates a new generator and sets the level type
	 * @param mapType Type of map to generate, use static attributes from this class as parameters
	 */
	public MapGenerator(int mapType, Player player)
	{
		generateSeed();
		this.player = player;
		currentType = MapTypeFactory.getMapType(mapType, this.player, this);
		
		typeSwitchTimer = new dTimer(7.5f,7.5f,0,this);
		typeSwitchTimer.start();
	}
	
	public MapGenerator(Player player)
	{
		this(rng.nextInt(MapTypeFactory.NUMBER_MAPS), player);
	}
	
	public void update(float delta)
	{
		currentType.update(delta);
		typeSwitchTimer.update(delta);
		if(nextType != null && currentType.shouldSwitch())
		{
			currentType.dispose();
			nextType.coinSet = currentType.coinSet;
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

		// check if combo should reset
		if(player.hasHitWall())
		{
			// check if it's a new record for the combo
			checkComboRecord();
			combo = 0;
		}
		
	}

	public void render(SpriteBatch batch)
	{
		currentType.render(batch);
	//	transitionImage.render(batch);
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
		if(hadCollision == false && c == true)
		{
			MainGame.playSound("death");
			Gdx.input.vibrate(50);
		}
		hadCollision = c;
		if(c == false) // reset
		{
			reset();
		}
	}
	
	private void checkComboRecord()
	{
		if(combo > player.getBestCombo())
		{
			player.setBestCombo(combo);
		}
	}
	
	private void reset()
	{
		// set next maptype
		currentType = MapTypeFactory.getMapType(rng.nextInt(MapTypeFactory.NUMBER_MAPS), player, this);
		generateSeed();
		generateFirstSet();
		checkComboRecord();
		nextType = null;
		currentType.nextType = null;
		currentType.reset();
		score = 0;
		combo = 0;
		typeSwitchTimer.stop();
	}
	
	public void generateFirstSet()
	{
		currentType.generateFirstSet();
	}
	
	public void setScoreChanged(boolean scoreChanged)
	{
		this.scoreChanged = scoreChanged;
	}
	
	public void incrementScore()
	{
		score += currentType.getScoreIncrementAmount();
		// update combo
		combo++;
		// play sound
		MainGame.playSound("score");
	}
	
	private void setNextType(int mapType)
	{
		if(mapType != currentType.getMapType())
		{
			nextType = MapTypeFactory.getMapType(mapType, player, this);
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
	
	public int getCombo()
	{
		return combo;
	}
	
	// TEMP
	public MapType getCurrentMapType()
	{
		return currentType;
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
		int newType = rng.nextInt(MapTypeFactory.NUMBER_MAPS);
		setNextType(newType);
		if(nextType == null)
		{
			typeSwitchTimer.start();
		}
		currentType.setNextMapType(nextType);
	}
	
	@Override
	public void onTimerStart(int ID) {}

	@Override
	public void onTimerTick(int ID) {}
	
}