package com.dr.bounds.maps.obstacles;

import com.DR.dLib.ui.dImage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.dObstacle;

public class ForestObstacle extends dObstacle {

	private dImage trunk;
	// used for more precise collision
	private Rectangle topRect;
	
	public ForestObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		float dimensions = 64 + MapGenerator.rng.nextInt(64);
		setColor(30f/256f, 130f/256f, 76f/256f,1f);
		trunk = new dImage(x + dimensions / 2f - 8f,y + dimensions - 8f,BoundsAssetManager.getTexture("card"));
		trunk.setDimensions(16f, 92f);
		
		trunk.setColor(130f/255f, 90f/255f, 44f/255f, 1f);
		topRect = new Rectangle(getX() + getWidth()/4f, getY(), getWidth()/2f, getHeight()/2f);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		trunk.render(batch);
		super.render(batch);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		topRect.set(getX() + getWidth()/4f, getY(), getWidth()/2f, getHeight()/2f);
	}
	
	@Override
	protected void renderDebug(SpriteBatch batch)
	{
		batch.end();
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.begin(ShapeType.Line);
		sr.rect(trunk.getBoundingRectangle().x, trunk.getBoundingRectangle().y, trunk.getBoundingRectangle().width, trunk.getBoundingRectangle().height);
		sr.rect(this.getBoundingRectangle().x, this.getBoundingRectangle().y, this.getBoundingRectangle().width, this.getBoundingRectangle().height);
		sr.rect(topRect.x, topRect.y, topRect.width, topRect.height);
		sr.end();
		batch.begin();
	}
	
	@Override
	public void setX(float x)
	{
		super.setX(x);
		trunk.setX(x + getWidth() / 2f - trunk.getWidth() / 2f);
	}
	
	@Override
	public void setY(float y)
	{
		super.setY(y);
		trunk.setY(y + getHeight() - 8f);
	}
	
	@Override
	public Rectangle getBoundingRectangle()
	{
		return super.getBoundingRectangle().set(getX() + this.getWidth()/8f, getY() + this.getHeight()/2f,
				getWidth() - this.getWidth()/4f, getHeight()/2f);
	}

	public dImage getTrunk()
	{
		return trunk;
	}
	
	public Rectangle getTopRectangle()
	{
		return topRect;
	}
}
