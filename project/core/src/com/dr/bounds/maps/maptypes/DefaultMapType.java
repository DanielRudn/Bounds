package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.graphics.Color;
import com.dr.bounds.AssetManager;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.MapType;
import com.dr.bounds.maps.dObstacle;

public class DefaultMapType extends MapType {
	
	public DefaultMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator);
		typeName = "Void/Default";
		gen.setScoreIncrementAmount(1);
		bgColor = new Color(44f/256f, 62f/256f, 80f/256f,1f);
		// add 12 obstacles to start with
		for(int x = 0; x < 12; x++)
		{
			obstacles.add(new dObstacle(0,0, AssetManager.getTexture("obstacle.png"), player));
			obstacles.get(x).setHeight(32f);
			obstacles.get(x).setRegenerate(false);
			obstacles.get(x).setColor(210f/256f, 82f/256f, 127f/256f,1f);
		}
	}

}
