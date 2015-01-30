package com.dr.bounds.maps.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.Player;

public class SpikeObstacle extends MovingObstacle {

	private float angle = 0;
	
	public SpikeObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		setOriginCenter();
		if(angle <= 360f)
		{
			angle+=90f*delta;
		}
		else
		{
			angle = 0;
		}
		getSprite().setRotation(angle);
	}
	

}
