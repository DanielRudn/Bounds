package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.MapType;
import com.dr.bounds.maps.obstacles.FishObstacle;

public class OceanMapType extends MapType { 

	public OceanMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator);
		MIN_WIDTH = 64;
		typeName = "Sky";
		bgColor = new Color(91f/256f, 213f/256f, 219f/256f, 1f);
		super.setScoreIncrementAmount(1);
		// add 8 obstacles 
		for(int x = 0; x < 8; x++)
		{
			obstacles.add(new FishObstacle(0,0, BoundsAssetManager.getTexture("fishObstacle.png"), player));
			obstacles.get(x).setRegenerate(false);
			obstacles.get(x).setColor(88f/256f, 179f/256f, 124f/256f, 1f);
		}
		super.particleEffect.load(Gdx.files.internal("ocean.p"), Gdx.files.internal(""));
	}
	
	@Override
	protected void checkCollision(int index)
	{
		super.checkCollision(index);
		if(gen.hadCollision() == false && Intersector.intersectRectangles(player.getBoundingRectangle(), ((FishObstacle)obstacles.get(index)).getBodyRectangle(), useless))
		{
			gen.setHadCollision(true);
		}
		else if(gen.hadCollision() == false && Intersector.intersectRectangles(player.getBoundingRectangle(), ((FishObstacle)obstacles.get(index)).getMouthRectangle(), useless))
		{
			gen.setHadCollision(true);
		}
	}

	@Override
	protected void generateBlock(int index)
	{
		// get direction, 0 = east, 1 = west
		int direction = MapGenerator.rng.nextInt(2);
		if(direction == 0)
		{
			((FishObstacle)obstacles.get(index)).setDirection(true);
			obstacles.get(index).setX(-obstacles.get(index).getWidth());
		}
		else if(direction == 1)
		{
			((FishObstacle)obstacles.get(index)).setDirection(false);
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH + obstacles.get(index).getWidth());
		}
		int dimensions = MIN_WIDTH + MapGenerator.rng.nextInt(64); // TODO: change maybe? put in a final variable
		obstacles.get(index).setDimensions((float)dimensions*2f, dimensions);
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		BoundsAssetManager.dispose("fishObstacle.png");
	}
}
