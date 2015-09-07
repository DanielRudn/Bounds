package com.dr.bounds.maps.obstacles;

import com.DR.dLib.dTweener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.dObstacle;

public class FishObstacle extends dObstacle{

	private float moveTime = 0;
	private float moveSpeed = 0;
	private boolean goingEast = true;
	private Rectangle mouthRect, bodyRect;
	
	public FishObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		moveSpeed = 5f + MapGenerator.rng.nextInt(5);
		mouthRect = new Rectangle();
		bodyRect = new Rectangle();
	}
	
	@Override
	protected void renderDebug(SpriteBatch batch)
	{
		batch.end();
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.setColor(Color.WHITE);
		sr.begin(ShapeType.Line);
		sr.rect(mouthRect.x, mouthRect.y, mouthRect.width, mouthRect.height);
		sr.rect(bodyRect.x, bodyRect.y, bodyRect.width, bodyRect.height);
		sr.rect(this.getBoundingRectangle().x, this.getBoundingRectangle().y, this.getBoundingRectangle().width, this.getBoundingRectangle().height);
		sr.end();
		sr.setColor(Color.WHITE);
		batch.begin();
	}

	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(goingEast)
		{
			mouthRect.set(this.getX() + this.getWidth()*6/8f, this.getY() + this.getHeight()/2f - this.getHeight()/8f, this.getWidth()/8f, this.getHeight()/4f);
			bodyRect.set(mouthRect.getX() - this.getWidth() * 0.40f, this.getY() + this.getHeight() * 0.20f, this.getWidth() * 0.40f, this.getHeight() * 0.60f);
		}
		else
		{
			mouthRect.set(this.getX() + this.getWidth()/8f, this.getY() + this.getHeight()/2f - this.getHeight()/8f, this.getWidth()/8f, this.getHeight()/4f);
			bodyRect.set(mouthRect.getX() + mouthRect.getWidth(), this.getY() + this.getHeight() * 0.20f, this.getWidth()*0.40f, this.getHeight() * 0.60f);
		}
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
	
	@Override
	public boolean hadCollision(Player player)
	{
		if(passed == false)
		{
			return super.hadCollision(player) ||
					Intersector.intersectRectangles(player.getBoundingRectangle(), this.getBodyRectangle(), useless) ||
					Intersector.intersectRectangles(player.getBoundingRectangle(), this.getMouthRectangle(), useless);
		}
		return false;
	}

	public void setDirection(boolean goingEast)
	{
		// flip sprite for direction
		if(this.goingEast != goingEast)
		{
			this.getSprite().flip(true,false);
		}
		this.goingEast = goingEast;
	}
	
	@Override
	public Rectangle getBoundingRectangle()
	{
		Rectangle rect = super.getBoundingRectangle().setWidth(super.getBoundingRectangle().getWidth() * 0.20f);
		rect.setHeight(this.getHeight()/2f);
		rect.setY(rect.getY() + this.getHeight()/4f);
		if(!goingEast)
		{
			rect.setX(rect.getX() + super.getWidth() * 0.80f);
		}
		return rect;
	}
	
	public Rectangle getMouthRectangle()
	{
		return mouthRect;
	}
	
	public Rectangle getBodyRectangle()
	{
		return bodyRect;
	}
}
