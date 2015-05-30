package com.dr.bounds.maps.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.dObstacle;

public class RotatingObstacle extends dObstacle {
	
	private float rotation = 0;
	private float DEGREES_PER_SECOND = 45f;

	public RotatingObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		DEGREES_PER_SECOND += MapGenerator.rng.nextInt(20);
		setOriginCenter();
	}
	
	@Override
	protected void renderDebug(SpriteBatch batch)
	{
		batch.end();
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.begin(ShapeType.Line);
		sr.line(getVertices()[0], getVertices()[1], getVertices()[15], getVertices()[16]);
		sr.line(getVertices()[5], getVertices()[6], getVertices()[10], getVertices()[11]);
		sr.line(getVertices()[0], getVertices()[1], getVertices()[5], getVertices()[6]);
		sr.line(getVertices()[10], getVertices()[11], getVertices()[15], getVertices()[16]);
		sr.end();
		batch.begin();
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
