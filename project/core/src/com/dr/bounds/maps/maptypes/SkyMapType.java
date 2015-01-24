package com.dr.bounds.maps.maptypes;

import com.dr.bounds.AssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.obstacles.dBirdObstacle;

public class SkyMapType extends MapType { 
	
	public SkyMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator, AssetManager.getBackground("SKY_BG.png"));
		MIN_WIDTH = 64;
		typeName = "Sky";
		gen.setScoreIncrementAmount(1);
		// add 8 obstacles 
		for(int x = 0; x < 8; x++)
		{
			obstacles.add(new dBirdObstacle(0,0, AssetManager.getTexture("birdObstacle.png"), player));
			obstacles.get(x).setRegenerate(false);
		}
	}
	
	@Override
	protected void generateBlock(int index)
	{
		// get direction, 0 = east, 1 = west
		int direction = MapGenerator.rng.nextInt(2);
		if(direction == 0)
		{
			((dBirdObstacle)obstacles.get(index)).setDirection(true);
			obstacles.get(index).setX(-obstacles.get(index).getWidth());
		}
		else if(direction == 1)
		{
			((dBirdObstacle)obstacles.get(index)).setDirection(false);
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH + obstacles.get(index).getWidth());
		}
		int dimensions = MIN_WIDTH + MapGenerator.rng.nextInt(64); // TODO: change maybe? put in a final variable
		obstacles.get(index).setDimensions((float)dimensions*1.25f, dimensions);
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}
}
