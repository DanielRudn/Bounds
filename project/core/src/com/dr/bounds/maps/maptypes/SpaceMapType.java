package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.MapType;
import com.dr.bounds.maps.obstacles.PlanetObstacle;

public class SpaceMapType extends MapType {

	public SpaceMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator);
		MIN_DISTANCE = MAX_DISTANCE;
		MAX_DISTANCE *= 2;
		MIN_WIDTH = 192;
		MAX_WIDTH = 333 - MIN_WIDTH;
	//	bgColor = new Color(82f/256f, 74f/256f, 115f/256f, 1f);
		bgColor = new Color(39f/256f, 15f/256f, 48f/256f, 1f);
		// this map type awards 2 points per obstacle
		super.setScoreIncrementAmount(2);
		
		typeName = "Space";
		// add 4 obstacles to start with
		for(int x = 0; x < 4; x++)
		{
			obstacles.add(new PlanetObstacle(0,0, BoundsAssetManager.getTexture("planet.png"), player, MapGenerator.rng));
			obstacles.get(x).setRegenerate(false);
		}
		super.particleEffect.load(Gdx.files.internal("stars.p"), Gdx.files.internal(""));
	}
	
	@Override
	protected void generateObstacle(int index)
	{
		int side = MapGenerator.rng.nextInt(11); // 0,1,5,6,7 is LEFT, 2,3,8,9,10 is RIGHT, 4 is center
		if(side == 0 || side == 1 || side == 5 || side == 6 || side == 7)// left
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH));
			obstacles.get(index).setHeight(obstacles.get(index).getWidth());
			obstacles.get(index).setX(32f);
		}
		else if(side == 2 || side == 3 || side == 8 || side == 9 || side == 10)// right
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH));
			obstacles.get(index).setHeight(obstacles.get(index).getWidth());
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH - obstacles.get(index).getWidth() - 32f);
		}
		else if(side == 4)// center		
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH) - 32f);
			obstacles.get(index).setHeight(obstacles.get(index).getWidth());
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH / 2f - obstacles.get(index).getWidth() / 2f + (-50 + MapGenerator.rng.nextInt(100)));
		}
		((PlanetObstacle)obstacles.get(index)).generate();
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		BoundsAssetManager.dispose("planet.png");
		BoundsAssetManager.dispose("planetRing.png");
		BoundsAssetManager.dispose("moon.png");
	}

}
