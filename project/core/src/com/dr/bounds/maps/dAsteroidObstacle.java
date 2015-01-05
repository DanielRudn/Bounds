package com.dr.bounds.maps;

import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.Player;

public class dAsteroidObstacle extends dObstacle {

	private float fallSpeed = 0f;
	
	public dAsteroidObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		fallSpeed = 5f + MapGenerator.rng.nextFloat()*MapGenerator.rng.nextInt(5);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		setY(getY() + fallSpeed);
	}

}
