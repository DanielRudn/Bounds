package com.dr.bounds.maps;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class MapGenerator {

	// map generation type
	public static final int TYPE_DEFAULT = 1515, TYPE_SPACE = 3030, TYPE_FACTORY = 6060;
	// the current map generation type
	private MapType currentType; 
	// the next map generation type
	private MapType nextType = null;
	// Random number generator for positioning objects
	public static Random rng = new Random();
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
	
	/**
	 * Creates a new generator and sets the level type
	 * @param mapType Type of map to generate, use static attributes from this class as parameters
	 */
	public MapGenerator(int mapType, Texture obstacleTexture, Player player)
	{
		generateSeed();
		this.player = player;
		if(mapType == TYPE_FACTORY)
		{
			currentType = new FactoryMapType(TYPE_FACTORY, player, obstacleTexture, this);
		}
		else if(mapType == TYPE_SPACE)
		{
			currentType = new SpaceMapType(TYPE_SPACE, player, this);
		}
	}
	
	public void update(float delta)
	{
		currentType.update(delta);
		
		if(nextType != null && currentType.shouldSwitch())
		{
			currentType = nextType;
			currentType.generateFirstSet();
			nextType = null;
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
					nextType.generateDefault(x);
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
	}
	
	public void setSeed(long s)
	{
		seed = s;
		rng.setSeed(seed);
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
			MainGame.requestHandler.sendReliableMessage(new byte[]{'C', (byte) getScore()});
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
		int newType = rng.nextInt(2);
		if(newType == 0)
		{
			currentType = new FactoryMapType(TYPE_FACTORY, player, new Texture("girder.png"), this);
		}
		else if(newType == 1)
		{
			currentType = new SpaceMapType(TYPE_SPACE, player, this);
		}
		generateSeed();
		generateFirstSet();
		nextType = null;
		currentType.reset();
		score = 0;
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
		if(score % 10 == 0 && nextType == null)
		{
			//	determine which map type is set
			int newType = rng.nextInt(2);
			System.out.println("newType: " + newType);
			if(newType == 0)
			{
				if(currentType.getMapType() != TYPE_FACTORY)
				{
					nextType = new FactoryMapType(TYPE_FACTORY, player, new Texture("girder.png"), this);
				}
			}
			else if(newType == 1)
			{
				if(currentType.getMapType() != TYPE_SPACE)
				{
					nextType = new SpaceMapType(TYPE_SPACE, player, this);
				}
			}
			currentType.setNextMapType(nextType);
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
	
}
