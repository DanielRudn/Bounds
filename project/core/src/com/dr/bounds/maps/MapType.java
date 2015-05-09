package com.dr.bounds.maps;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.AssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.obstacles.CoinSet;

public abstract class MapType {

	// the values used when generating obstacles on the map
	// Minimum  and maximum vertical(y) distance between obstacles
	protected int MIN_DISTANCE = 192, MAX_DISTANCE = (int) MainGame.VIRTUAL_HEIGHT / 2 - 128;
	// minimum / maximum width of blocks
	protected int MIN_WIDTH = 192, MAX_WIDTH = (int)MainGame.VIRTUAL_WIDTH - 475;
	// the id of the map type
	private int type;
	// name of the map
	protected String typeName;
	// list of obstacles
	protected ArrayList<dObstacle> obstacles;
	// the MapGenerator object that this MapType is being used in
	protected MapGenerator gen;
	// Player object to determine collisions and score
	protected Player player;
	// the background images for this maptype
	//protected dImage firstBG, secondBG;
	// the next map type
	protected MapType nextType = null;
	// whether the currentType should be switched with the nextType
	protected boolean shouldSwitch = false;
	// whether the maptype should keep regenerating its obstacles
	protected boolean isTransitioning = false;
	// whether the map type should start showing the new bg types
	protected boolean switchBG = false;
	// whether to show the transition image between map types
	private boolean showTransitionImage = false;
	// whether to change position for the transition image
	private boolean moveTransitionImage = true;
	// whether the obstacles for the new type have been generated
	private boolean newTypeGenerated = false;
	// coin set for the maps
	private static CoinSet coinSet;
	// ShapeRenderer for rectangles nad backgrounds
	private static ShapeRenderer sr = new ShapeRenderer();
	// color for backgrounds of each individual maptype.
	protected Color bgColor;
	// colors for background rectangles, gradient from top to bottom.
	private static Color firstTop, firstBottom, secondTop, secondBottom;
	// Rectangles for background colors and changing
	protected Rectangle firstBG, secondBG;
	// rectangle for collision
	protected static final Rectangle useless = new Rectangle();
	// test
	private static boolean switchColors = false;
	// particle effect in the background
	protected ParticleEffect particleEffect = new ParticleEffect();
	
	public MapType(int type, Player player, MapGenerator generator)
	{
		this.type = type;
		obstacles = new ArrayList<dObstacle>();
		gen = generator;
		this.player = player;
		firstBG = new Rectangle(0,0,MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT);
		secondBG = new Rectangle(0, -MainGame.VIRTUAL_HEIGHT, MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT);
		switchColors = true;
		// TODO: FIND A BETTER SOLUTION
		if(firstTop == null)
		{
			firstTop = Color.WHITE;
			firstBottom = Color.WHITE;
			secondTop = Color.WHITE;
			secondBottom = Color.WHITE;
		}
	}
	
	public void render(SpriteBatch batch)
	{
		batch.end();
		sr.setProjectionMatrix(MainGame.camera.combined);
		sr.begin(ShapeType.Filled);
		sr.rect(firstBG.getX(), firstBG.getY(), firstBG.getWidth(), firstBG.getHeight(), firstTop,firstTop,firstBottom,firstBottom);
		sr.rect(secondBG.getX(), secondBG.getY(), secondBG.getWidth(), secondBG.getHeight(), secondTop,secondTop,secondBottom,secondBottom);
		sr.end();
		batch.begin();
		particleEffect.draw(batch);
		if(coinSet != null)
		{
			coinSet.render(batch);
		}
		renderObstacles(batch);
		if(nextType != null && showTransitionImage)
		{
			nextType.renderObstacles(batch);
		}
	}
	
	protected void renderObstacles(SpriteBatch batch)
	{
		for(int x = 0; x < obstacles.size(); x++)
		{
			obstacles.get(x).render(batch);
		}
	}
	
	public void update(float delta)
	{
		if(switchBG == false)
		{
			particleEffect.update(delta);
			particleEffect.setPosition(0, MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f);
		}
		// update backgrounds
		if(switchColors)
		{
			firstTop = bgColor;
			firstBottom = bgColor;
			secondTop = bgColor;
			secondBottom = bgColor;
			switchColors = false;
		}
		if(firstBG.getY() >= MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f)
		{
			if(nextType != null && switchBG && secondBG == nextType.secondBG)
			{
				firstBG = nextType.firstBG;
				firstTop = nextType.bgColor;
				firstBottom = nextType.bgColor;
				shouldSwitch = true;
			}
			else if(nextType != null && switchBG)
			{
				firstBG = nextType.firstBG;
				firstTop = nextType.bgColor;
				firstBottom = bgColor;
				showTransitionImage = true;
			}
			if(firstBottom != firstTop)
			{
				firstBottom = bgColor;
			}
			firstBG.setY(secondBG.getY() - firstBG.getHeight());
			if(showTransitionImage && moveTransitionImage)
			{
				MapGenerator.transitionImage.setPos(firstBG.getX(), firstBG.getY() + firstBG.getHeight() - MapGenerator.transitionImage.getHeight() / 2f);
				moveTransitionImage = false;
				nextType.generateNewType();
			}
		}
		if(secondBG.getY() >= MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f)
		{
			if(nextType != null && switchBG && firstBG == nextType.firstBG)
			{
				secondBG = nextType.secondBG;
				secondTop = nextType.bgColor;
				secondBottom = nextType.bgColor;
				shouldSwitch = true;
			}
			else if(nextType != null && switchBG)
			{
				secondBG = nextType.secondBG;
				secondTop = nextType.bgColor;
				secondBottom = bgColor;
				showTransitionImage = true;
			}
			if(secondBottom != secondTop)
			{
				secondBottom = bgColor;
			}
			secondBG.setY(firstBG.getY() - secondBG.getHeight());
			if(showTransitionImage && moveTransitionImage)
			{
				MapGenerator.transitionImage.setPos(secondBG.getX(), secondBG.getY() + secondBG.getHeight() - MapGenerator.transitionImage.getHeight() / 2f);
				moveTransitionImage = false;
				nextType.generateNewType();
			}
		}
		updateBlocks(delta);
		if(coinSet != null)
		{
			coinSet.update(delta);
		}
	}
	
	private void updateBlocks(float delta)
	{
		for(int x = 0; x < obstacles.size(); x++)
		{
			obstacles.get(x).update(delta);
			if(obstacles.get(x).shouldRegenerate())
			{
				generate(x);
				obstacles.get(x).setRegenerate(false);
			}
			// check if player had collision
			checkCollision(x);
		}
	}
	
	/**
	 * Checks collision using obstacles bounding rectangle by default
	 * @param index index of current obstacle
	 */
	protected void checkCollision(int index)
	{
		if(gen.hadCollision() == false && Intersector.intersectRectangles(player.getBoundingRectangle(), obstacles.get(index).getBoundingRectangle(), useless)) // FIX
		{
			gen.setHadCollision(true);
		}
	}
	
	protected void generateFirstSet()
	{
		for(int x = 0; x < obstacles.size(); x++)
		{
			generate(x);
			obstacles.get(x).setRegenerate(false);
		}
	}
	
	/**
	 * @param index
	 */
	protected final void generate(int index)
	{
		// only regenerate if the map type is not changing yet
		if(isTransitioning == false)
		{
			//reset passed for this obstacles
			obstacles.get(index).setPassed(false);
			generateBlock(index);
			// 30% chance to generate a set of coins
			// TODO: make it so there's a boolean to prevent two coin sets generating in a row
			//int genCoins = MapGenerator.rng.nextInt(10);
		//	if(genCoins == 5 || genCoins == 7 || genCoins == 1)
			//{
				generateCoinSet(index);
			//}
		}
		else
		{
			// check if we can switch the backgrounds by running through the obstacles and seeing if they are all under the camera.
			boolean canSwitch = true;
			for(int x = 0; x < obstacles.size(); x++)
			{
				if(obstacles.get(x).shouldRegenerate() == false)
				{
					canSwitch = false;
				}
			}
			if(canSwitch)
			{
				switchBG = true;
			}
		}
	}
	
	protected void generateBlock(int index)
	{
		int side = MapGenerator.rng.nextInt(11); // 0,1,5,6,7 is LEFT, 2,3,8,9,10 is RIGHT, 4 is center
		if(side == 0 || side == 1 || side == 5 || side == 6 || side == 7)// left
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH));
			obstacles.get(index).setX(0);
		}
		else if(side == 2 || side == 3 || side == 8 || side == 9 || side == 10)// right
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH));
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH - obstacles.get(index).getWidth());	
		}
		else if(side == 4)// center
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH) - 32f);
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH / 2f - obstacles.get(index).getWidth() / 2f + (-50 + MapGenerator.rng.nextInt(100)));
		}
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}
	
	private void generateCoinSet(int index)
	{
		if(coinSet != null && coinSet.canRegenerate())
		{
			coinSet.generate();
			/*int side = MapGenerator.rng.nextInt(11); // 0,1,5,6,7 is LEFT, 2,3,8,9,10 is RIGHT, 4 is center
			if(side == 0 || side == 1 || side == 6 || side == 7)// left
			{
				coinSet.setX(64);
			}
			else if(side == 2 || side == 3 || side == 8 || side == 10)// right
			{
				// 64 is the width of the coins
				coinSet.setX(MainGame.VIRTUAL_WIDTH - (64 + 16) * coinSet.getNumberOfCoins() - 64);	
			}
			else if(side == 4 || side == 5 || side == 9)// center
			{
				coinSet.setX(MainGame.VIRTUAL_WIDTH / 2f - obstacles.get(index).getWidth() / 2f + (-50 + MapGenerator.rng.nextInt(100)));
			}*/
			coinSet.setX(MainGame.VIRTUAL_WIDTH / 2f - obstacles.get(index).getWidth() / 2f + (-50 + MapGenerator.rng.nextInt(100)));
			coinSet.setY(obstacles.get(index).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
			//System.out.println("generated at x: " + coinSet.getX() + " y: " + coinSet.getY());
		}
		else if(coinSet == null)
		{
			// make a new CoinSet and then generate it
			coinSet = new CoinSet(0,0,AssetManager.getTexture("coin.png"), player);
			generateCoinSet(index);
		}
	}
	
	/**
	 * Generates the obstacles from scratch when transitioning to a new map type
	 */
	protected void generateNewType()
	{
		if(newTypeGenerated == false)
		{
			obstacles.get(0).setY(MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT/2f - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
			obstacles.get(0).setRegenerate(false);
			obstacles.get(0).setPassed(false);
			for(int x = 1; x < obstacles.size(); x++)
			{
				generate(x);
				obstacles.get(x).setRegenerate(false);
			}
			newTypeGenerated = true;
		}
	}
	
	public void reset()
	{
		// reset backgrounds
		firstBG.setPosition(0,0);
		secondBG.setPosition(0,-MainGame.VIRTUAL_HEIGHT);
		if(coinSet != null)
		{
			coinSet.reset();
		}
	}
	
	/**
	 * If this method has been called, the current map type will begin transitioning to the one provided
	 * @param next the next map type
	 */
	public void setNextMapType(MapType next)
	{
		nextType = next;
		if(next != null)
		{
			isTransitioning = true;
		}
	}
	
	public boolean shouldSwitch()
	{
		return shouldSwitch;
	}
	
	public int getMapType()
	{
		return type;
	}
	
	public String getTypeName()
	{
		return typeName;
	}
	
	public Color getBackgroundColor()
	{
		return bgColor;
	}
	
	public ArrayList<dObstacle> getObstacles()
	{
		return obstacles;
	}

	protected int getPreviousIndex(int i)
	{
		if(i == 0)
		{
			return obstacles.size() - 1;
		}
		return i-1;
	}	
}