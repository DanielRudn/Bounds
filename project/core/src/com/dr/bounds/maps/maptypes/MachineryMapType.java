package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.graphics.Color;
import com.dr.bounds.AssetManager;
import com.dr.bounds.Player;
import com.dr.bounds.maps.obstacles.MovingObstacle;

public class MachineryMapType extends MapType{
	
	public MachineryMapType(int type, Player p, MapGenerator generator) {
		super(type, p, generator);
		// this map type awards 1 point per obstacle
		gen.setScoreIncrementAmount(1);
		typeName = "Machinery";
		bgColor = Color.GRAY;
		// add 12 obstacles to start with
		for(int x = 0; x < 12; x++)
		{
			obstacles.add(new MovingObstacle(0,0, AssetManager.getTexture("girder.png"), p));
			obstacles.get(x).setRegenerate(false);
		}
	}
	
}
