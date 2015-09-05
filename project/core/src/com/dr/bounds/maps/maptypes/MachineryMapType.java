package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.MapType;
import com.dr.bounds.maps.obstacles.MovingObstacle;

public class MachineryMapType extends MapType {
	
	public MachineryMapType(int type, Player p, MapGenerator generator) {
		super(type, p, generator);
		// this map type awards 1 point per obstacle
		super.setScoreIncrementAmount(1);
		typeName = "Machinery";
		bgColor = new Color(93f/256f, 95f/256f, 94f/256f, 1f);
		// add 12 obstacles to start with
		for(int x = 0; x < 12; x++)
		{
			obstacles.add(new MovingObstacle(0,0, BoundsAssetManager.getTexture("obstacle.png"), p));
			obstacles.get(x).setRegenerate(false);
			obstacles.get(x).setColor(247f/256f, 182f/256f, 22f/256f, 1f);
			obstacles.get(x).setHeight(32f);
		}
		super.particleEffect.load(Gdx.files.internal("machinery.p"), Gdx.files.internal(""));
	} 
	
	@Override
	public void dispose()
	{
		super.dispose();
		BoundsAssetManager.dispose("obstacle.png");
	}
	
}
