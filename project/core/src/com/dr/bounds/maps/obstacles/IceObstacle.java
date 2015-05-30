package com.dr.bounds.maps.obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.dObstacle;

public class IceObstacle extends dObstacle {

	// used for more precise collision
	private Rectangle bottomRect;
	
	public IceObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		setColor(129f/256f, 207f/256f, 224f/256f,1f);
		bottomRect = new Rectangle(x + this.getWidth()/3f, y + this.getHeight()/2f, this.getWidth()/4f, this.getHeight()/2f);
	}
	
	@Override
	protected void renderDebug(SpriteBatch batch)
	{
		batch.end();
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.setColor(Color.ORANGE);
		sr.begin(ShapeType.Line);
		sr.rect(bottomRect.x, bottomRect.y, bottomRect.width, bottomRect.height);
		sr.rect(this.getBoundingRectangle().x, this.getBoundingRectangle().y, this.getBoundingRectangle().width, this.getBoundingRectangle().height);
		sr.end();
		sr.setColor(Color.WHITE);
		batch.begin();
	}

	@Override
	public void update(float delta)
	{
		super.update(delta);
		setY(getY() + MapGenerator.rng.nextInt(250)*delta);
		bottomRect.set(getX() + this.getWidth()/3f, getY() + this.getHeight()/2f, this.getWidth()/4f, this.getHeight()/2f);
	}
	
	@Override
	public Rectangle getBoundingRectangle()
	{
		return super.getBoundingRectangle().setHeight(this.getHeight()/2f);
	}

	/**
	 * Returns the rectangle representing the bottom half of this obstacle for more precise collision
	 * @return Returns the rectangle representing the bottom half of this obstacle for more precise collision
	 */
	public Rectangle getBottomRectangle()
	{
		return bottomRect;
	}
}
