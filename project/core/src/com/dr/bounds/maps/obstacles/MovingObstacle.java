package com.dr.bounds.maps.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.Player;
import com.dr.bounds.maps.maptypes.MapGenerator;

public class MovingObstacle extends dObstacle {
	
	private float moveTime = 0;
	private final float moveDuration = (float) (Math.PI * 2f);
	private float amplitude = 1f;
	
	public MovingObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		amplitude += MapGenerator.rng.nextInt(3);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(moveTime < moveDuration)
		{
			moveTime+=delta;
			setX((float) (getX() - amplitude * Math.sin(moveTime)));
		}
		else
		{
			moveTime = 0;
		}
	}
	
	protected float getAmplitude()
	{
		return amplitude;
	}

}
