package com.dr.bounds.maps.obstacles;

import com.DR.dLib.dTweener;
import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.maptypes.MapGenerator;

public class BirdObstacle extends dObstacle{

	private float moveTime = 0;
	private float moveSpeed = 0;
	private boolean goingEast = true;
	
	public BirdObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		moveSpeed = 5f + MapGenerator.rng.nextInt(5);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		// move code
		if(moveTime < moveSpeed)
		{
			moveTime += delta;
			if(goingEast)
			{
				setX(dTweener.LinearEase(moveTime, -getWidth(), MainGame.VIRTUAL_WIDTH + getWidth(), moveSpeed));
			}
			else
			{
				setX(dTweener.LinearEase(moveTime, MainGame.VIRTUAL_WIDTH, -MainGame.VIRTUAL_WIDTH - getWidth(), moveSpeed));
			}
		}
		else if(moveTime >= moveSpeed)
		{
			moveTime = 0;
		}
	}
	

	public void setDirection(boolean goingEast)
	{
		// flip sprite for direction
		if(this.goingEast == goingEast)// was previously going east, and is now going east, keep the same
		{
			this.getSprite().flip(false, false);
		}
		else if(this.goingEast == false && goingEast == true)
		{
			this.getSprite().flip(true,false);
		}
		else if(this.goingEast == true && goingEast == false)
		{
			this.getSprite().flip(true,false);
		}
		this.goingEast = goingEast;
	}
}
