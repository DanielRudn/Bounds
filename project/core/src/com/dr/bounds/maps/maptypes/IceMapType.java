package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.graphics.Color;
import com.dr.bounds.AssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.obstacles.IceObstacle;

public class IceMapType extends MapType {

	public IceMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator, AssetManager.getBackground("SPACE_BG.png"));
		typeName = "Falling Ice";
		bgColor = new Color(197f/256f, 239f/256f, 247f/256f,1f);
		
		for(int x = 0; x < 10; x++)
		{
			obstacles.add(new IceObstacle(0,0, AssetManager.getTexture("ice.png"), player));
			obstacles.get(x).setRegenerate(false);
		}
	}
	
	@Override
	protected void generateBlock(int index)
	{
		obstacles.get(index).setX(16 + MapGenerator.rng.nextInt((int) (MainGame.VIRTUAL_WIDTH - obstacles.get(index).getWidth())));
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}

}
