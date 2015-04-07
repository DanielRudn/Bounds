package com.dr.bounds.maps.obstacles;

import com.DR.dLib.dTweener;
import com.DR.dLib.ui.dImage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.Player;

public class GapObstacle extends dObstacle {

	private float moveTime = 0f, moveDuration = 7f, startX = -300f, moveRightDistance = 200f, moveLeftDistance = -256f;
	private static final float GAP_WIDTH = 392f;
	private dImage rightSide;
	private boolean moveRight = true;
	
	public GapObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		this.setDimensions(512f, 48f);
		this.setColor(205f/255f, 220f/255f, 57f/255f, 1f);
		rightSide = new dImage(0, 0, texture);
		rightSide.setColor(this.getColor());
		rightSide.setDimensions(this.getWidth(), this.getHeight());
		moveRightDistance += MathUtils.random(64);
		moveLeftDistance -= MathUtils.random(128f);
		moveTime = MathUtils.random(7f);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		rightSide.render(batch);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(moveTime <= moveDuration)
		{
			moveTime += delta;
			if(moveRight)
			{
		//		this.setX(dTweener.BounceOut(moveTime, startX, moveRightDistance, moveDuration));
				this.setX(dTweener.LinearEase(moveTime, startX, moveRightDistance, moveDuration));
			}
			else
			{
		//		this.setX(dTweener.BounceOut(moveTime, startX, moveLeftDistance, moveDuration));
				this.setX(dTweener.LinearEase(moveTime, startX, moveLeftDistance, moveDuration));
			}	
		}
		else if(moveTime >= moveDuration)
		{
			moveTime = 0;
			startX = getX();
			moveRight = !moveRight;
		}
	}

	@Override
	protected boolean checkCollision()
	{
		if(Intersector.intersectRectangles(player.getBoundingRectangle(), this.getBoundingRectangle(), useless) || 
				Intersector.intersectRectangles(player.getBoundingRectangle(), this.getRightBoundRectangle(), useless))
		{
			return true;
		}
		return false;
	}
	
	@Override
	protected void onPositionChanged(float x, float y)
	{
		rightSide.setPos(x + this.getWidth() + GAP_WIDTH, y);
	}
	
	private Rectangle getRightBoundRectangle()
	{
		return rightSide.getBoundingRectangle();
	}

}
