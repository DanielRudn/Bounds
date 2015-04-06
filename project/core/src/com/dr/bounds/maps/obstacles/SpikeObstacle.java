package com.dr.bounds.maps.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.Player;

public class SpikeObstacle extends MovingObstacle {

	private float angle = 0;
	
	public SpikeObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		this.setColor(96f/255f,125f/255f,139f/255f, 1f);
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

	@Override
	protected boolean checkCollision()
	{
		if(ghadCirclularCollision(this..getPos(), player.getPos(), index))
		{
			return true;
		}
		return false;
	}

	private boolean hadCirclularCollision(Vector2 f, Vector2 i, int index)
	{
		float radiusPlanet = obstacles.get(index).getWidth() / 2f;
		float radiusPlayer = player.getWidth() / 2f;
		return Math.pow((f.x + radiusPlanet) - (i.x + radiusPlayer), 2) + Math.pow((f.y + radiusPlanet) - (i.y + radiusPlayer), 2) <= Math.pow(radiusPlanet + radiusPlayer, 2); 
	} 
}
