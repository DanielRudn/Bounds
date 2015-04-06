package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.dr.bounds.AssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.obstacles.ForestObstacle;

public class ForestMapType extends MapType {

	public ForestMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator);
		MIN_WIDTH = 64;
		typeName = "Forest";
		bgColor = new Color(135f/256f, 211f/256f, 124f/256f, 1f);
		gen.setScoreIncrementAmount(1);
		
		// make 8 obstacles
		for(int x = 0; x < 8; x++)
		{
			obstacles.add(new ForestObstacle(0,0, AssetManager.getTexture("circle.png"), player));
			obstacles.get(x).setRegenerate(false);
		}
	}
	
/*	@Override
	protected void checkCollision(int index)
	{
		if(gen.hadCollision() == false && Intersector.intersectRectangles(player.getBoundingRectangle(), obstacles.get(index).getBoundingRectangle(), useless)) // FIX
		{
			gen.setHadCollision(true);
		}
		if(gen.hadCollision() == false && Intersector.intersectRectangles(player.getBoundingRectangle(), ((ForestObstacle)obstacles.get(index)).getTrunk().getBoundingRectangle(), useless))
		{
			gen.setHadCollision(true);
		}
	}*/

	@Override
	protected void generateBlock(int index)
	{
		int dimensions = MIN_WIDTH + MapGenerator.rng.nextInt(64);
		obstacles.get(index).setDimensions(dimensions, dimensions);
		// set trunk dimensions
		((ForestObstacle)obstacles.get(index)).getTrunk().setDimensions(16f + MapGenerator.rng.nextInt(8), 16f + MapGenerator.rng.nextInt(256));
		int side = MapGenerator.rng.nextInt(11); // 0,1,5,6,7 is LEFT, 2,3,8,9,10 is RIGHT, 4 is center
		if(side == 0 || side == 1 || side == 5 || side == 6 || side == 7)// left
		{
			obstacles.get(index).setX(MapGenerator.rng.nextInt(MIN_WIDTH));
		}
		else if(side == 2 || side == 3 || side == 8 || side == 9 || side == 10)// right
		{
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH - obstacles.get(index).getWidth());	
		}
		else if(side == 4)// center
		{
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH / 2f - obstacles.get(index).getWidth() / 2f + (-50 + MapGenerator.rng.nextInt(100)));
		}
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}

}
