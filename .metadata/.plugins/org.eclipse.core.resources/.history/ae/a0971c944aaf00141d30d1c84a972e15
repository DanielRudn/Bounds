package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.graphics.Color;
import com.dr.bounds.AssetManager;
import com.dr.bounds.Player;
import com.dr.bounds.maps.obstacles.dObstacle;

public class DefaultMapType extends MapType {
	
	public DefaultMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator,AssetManager.getBackground("DEFAULT_BG.png"));
		typeName = "Void/Default";
		bgColor = new Color(20f/256f,20f/256f,20f/256f,1f);
		gen.setScoreIncrementAmount(1);
		// add 12 obstacles to start with
		for(int x = 0; x < 12; x++)
		{
			obstacles.add(new dObstacle(0,0, AssetManager.getTexture("obstacle.png"), player));
			obstacles.get(x).setRegenerate(false);
			obstacles.get(x).setColor(65f/256f,177f/256f,202f/256f,1f);
		}
	}

}
