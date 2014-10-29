package com.dr.bounds.maps;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.screens.GameScreen;

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
	
	public MapType(int type, Player player, MapGenerator generator)
	{
		this.type = type;
		obstacles = new ArrayList<dObstacle>();
		gen = generator;
		this.player = player;
	}
	
	public void render(SpriteBatch batch)
	{
		for(int x = 0; x < obstacles.size(); x++)
		{
			obstacles.get(x).render(batch);
		}
	}
	
	public abstract void update(float delta);
	
	protected void generateFirstSet()
	{
	//	obstacles.get(0).setY(MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT/2f - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	//	obstacles.get(0).setRegenerate(false);
	//	obstacles.get(0).setPassed(false);
		for(int x = 0; x < obstacles.size(); x++)
		{
			generateDefault(x);
			obstacles.get(x).setRegenerate(false);
		}
	}
	
	protected void generateDefault(int index)
	{
		//reset passed for this obstacles
		obstacles.get(index).setPassed(false);
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
	
	public abstract void reset();
	
	public int getMapType()
	{
		return type;
	}
	
	public String getTypeName()
	{
		return typeName;
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