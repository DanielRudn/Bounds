package com.dr.bounds.maps.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.dObstacle;

public class IceObstacle extends dObstacle {

	public IceObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		setColor(129f/256f, 207f/256f, 224f/256f,1f);
	}

	@Override
	public void update(float delta)
	{
		super.update(delta);
		setY(getY() + MapGenerator.rng.nextInt(250)*delta);
	}
}
