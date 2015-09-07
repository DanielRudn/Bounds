package com.dr.bounds.maps.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.dObstacle;

public class IceObstacle extends dObstacle {

	// used for more precise collision
	private Rectangle bottomRect;
	// color of the end of the trail
	private static final Color trailColor = new Color(255f/256f, 255f/256f, 220f/256f,0f);
	private static final float TRAIL_WIDTH = 8f, TRAIL_HEIGHT = -192f;
	
	public IceObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		setColor(0f, 171f/256f, 166f/256f, 1f);
		bottomRect = new Rectangle(x + this.getWidth()/3f, y + this.getHeight()/2f, this.getWidth()/4f, this.getHeight()/2f);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		batch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		sr.setProjectionMatrix(MainGame.camera.combined);
		sr.begin(ShapeType.Filled);
		sr.rect(this.getX() + this.getWidth()/2f - TRAIL_WIDTH/2f, this.getY() + this.getHeight()/4f, TRAIL_WIDTH, TRAIL_HEIGHT, this.getColor(), this.getColor(), trailColor, trailColor);
		sr.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		batch.begin();
		super.render(batch);
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
	public boolean hadCollision(Player player)
	{	
		if(passed == false)
		{
			return super.hadCollision(player) || Intersector.intersectRectangles(player.getBoundingRectangle(), this.getBottomRectangle(), useless);
		}
		return false;
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
