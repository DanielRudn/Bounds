package com.dr.bounds.maps.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.dr.bounds.Player;

public class SpikeObstacle extends MovingObstacle {

	private float angle = 0;
	
	public SpikeObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		this.setColor(199f/256f, 138f/256f, 79/256f,1f);
		this.setOriginCenter();
	}
	
	@Override
	protected void renderDebug(SpriteBatch batch)
	{
		batch.end();
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.begin(ShapeType.Line);
		sr.circle(this.getX() + this.getWidth()/2f, this.getY() + this.getHeight()/2f, this.getWidth()/2f);
		sr.end();
		batch.begin();
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		this.setOriginCenter();
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
	
	@Override
	public boolean hadCollision(Player player)
	{
		if(passed == false)
		{
			return hadCirclularCollision(this.getPos(), player.getPos(), this.getWidth(), player.getWidth());
		}
		return false;
	}
	
	private boolean hadCirclularCollision(Vector2 f, Vector2 i, float obstacleWidth, float playerWidth)
	{
		float radiusPlanet = obstacleWidth / 2f;
		float radiusPlayer = playerWidth / 2f;
		return Math.pow((f.x + radiusPlanet) - (i.x + radiusPlayer), 2) + Math.pow((f.y + radiusPlanet) - (i.y + radiusPlayer), 2) <= Math.pow(radiusPlanet + radiusPlayer, 2); 
	}
}
