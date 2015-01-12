package com.dr.bounds.maps;

import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.Player;

public class dAsteroidObstacle extends dObstacle {

	private float fallSpeed = 0f;
	private final float ROTATION_DURATION = 360f;
	private float rotationTime = 0f;
	
	public dAsteroidObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		fallSpeed = 5f + MapGenerator.rng.nextFloat()*MapGenerator.rng.nextInt(5);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		setY(getY() + fallSpeed);
		if(rotationTime < ROTATION_DURATION)
		{
			rotationTime+=delta*100f;
			setOriginCenter();
			getSprite().setRotation(rotationTime);
		}
		else
		{
			rotationTime = 0;
		}
	}

}
