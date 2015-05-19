package com.dr.bounds.maps.obstacles;

import com.DR.dLib.ui.dImage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.dObstacle;

public class ForestObstacle extends dObstacle {

	private dImage trunk;
	
	public ForestObstacle(float x, float y, Texture texture, Player p) {
		super(x, y, texture, p);
		float dimensions = 64 + MapGenerator.rng.nextInt(64);
		setColor(30f/256f, 130f/256f, 76f/256f,1f);
		trunk = new dImage(x + dimensions / 2f - 8f,y + dimensions - 8f,BoundsAssetManager.getTexture("card"));
		trunk.setDimensions(16f, 92f);
		
		trunk.setColor(130f/255f, 90f/255f, 44f/255f, 1f);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		trunk.render(batch);
		super.render(batch);
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

	public dImage getTrunk()
	{
		return trunk;
	}
}
