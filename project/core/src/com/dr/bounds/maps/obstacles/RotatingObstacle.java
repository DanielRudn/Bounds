package com.dr.bounds.maps.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.Player;
import com.dr.bounds.maps.maptypes.MapGenerator;

public class RotatingObstacle extends dObstacle {
	
	private float rotation = 0;
	private float DEGREES_PER_SECOND = 45f;

	public RotatingObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		DEGREES_PER_SECOND += MapGenerator.rng.nextInt(20);
		setOriginCenter();
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(rotation < 360f)
		{
			rotation += DEGREES_PER_SECOND * delta;
		}
		else
		{
			rotation = 0;
		}
		setOriginCenter();
		getSprite().setRotation(rotation);
	}

	// TODO: redo this collision.
	@Override
	protected boolean checkCollision()
	{
		if(Intersector.distanceSegmentPoint(getObstacles().get(index).getSprite().getVertices()[0], getObstacles().get(index).getSprite().getVertices()[1],
				getObstacles().get(index).getSprite().getVertices()[15], getObstacles().get(index).getSprite().getVertices()[16],
				player.getX() + player.getWidth()/2f,
				player.getY() + player.getHeight()/2f) <= player.getWidth() / 2f)
		{
			return true;
		}
		if(Intersector.distanceSegmentPoint(getObstacles().get(index).getSprite().getVertices()[5], getObstacles().get(index).getSprite().getVertices()[6],
				getObstacles().get(index).getSprite().getVertices()[10], getObstacles().get(index).getSprite().getVertices()[11],
				player.getX() + player.getWidth()/2f,
				player.getY() + player.getHeight()/2f) <= player.getWidth() / 2f)
		{
			return true;
		}
		return false;
	}
	
	public void setRotation(float r)
	{
		rotation = r;
	}
	
	private float[] getVertices()
	{
		//0,1, 5,6, 10,11, 15,16
		return getSprite().getVertices();
	}

}
