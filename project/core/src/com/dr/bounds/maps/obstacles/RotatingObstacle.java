package com.dr.bounds.maps.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
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
<<<<<<< HEAD

	// TODO: redo this collision.
	@Override
	protected boolean checkCollision()
	{
		if(Intersector.distanceSegmentPoint(this.getVertices()[0], this.getVertices()[1],
				this.getVertices()[15],this.getVertices()[16],
				player.getX() + player.getWidth()/2f,
				player.getY() + player.getHeight()/2f) <= player.getWidth() / 2f)
		{
			return true;
		}
		if(Intersector.distanceSegmentPoint(this.getVertices()[5], this.getVertices()[6],
			this.getVertices()[10], this.getSprite().getVertices()[11],
				player.getX() + player.getWidth()/2f,
				player.getY() + player.getHeight()/2f) <= player.getWidth() / 2f)
		{
			return true;
		}
		return false;
	}
=======
>>>>>>> parent of 713cce7... * Refactored collision method. Now in dObstacle rather than MapType.
	
	public void setRotation(float r)
	{
		rotation = r;
	}
	
	public float[] getVertices()
	{
		//0,1, 5,6, 10,11, 15,16
		return getSprite().getVertices();
	}

}
