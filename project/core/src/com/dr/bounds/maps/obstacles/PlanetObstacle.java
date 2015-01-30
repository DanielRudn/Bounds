package com.dr.bounds.maps.obstacles;

import java.util.Random;

import com.DR.dLib.ui.dImage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.AssetManager;
import com.dr.bounds.Player;

public class PlanetObstacle extends dObstacle {

	// whether this planet has a moon orbiting it
	private boolean hasMoon = false;
	// the moon object
	private dImage moonImage;
	// the x value for the moon rotation
	private float rotationTime = 0;
	// the offset from start position of the angle for the moon
	private float angleOffset = 0;
	// rotation speed of the moon
	private float rotationSpeed = 1f;
	// minimum rotation speed
	private final float MIN_ROTATION_SPEED = 0.5f;
	// the distance between the planet and the moon
	private int moonDistance = 32;
	// whether the planet currently has a ring around it
	private boolean hasRing = false;
	// object for the ring
	private dImage ringImage;
	// circle object to help complement the planet shape
	private dImage shadeImage;
	// colors for planet and moon
	private static Color[] planetColors = new Color[]{new Color(38f/256f, 194f/256f, 129f/256f,1f), // green
			new Color(52f/256f, 152f/256f, 219f/256f,1f), // blue
			new Color(242f/256f, 120f/256f, 75f/256f,1f), // red
			new Color(241f/256f, 196f/256f, 15f/256f,1f), //yellow
			new Color(243f/256f, 156f/256f, 18f/256f,1f),// orange
			new Color(219f/256f, 10f/256f, 91f/256f, 1f), // raspberry 
			new Color(145f/256f, 61f/256f, 136f/256f ,1f), // purple-ish
			new Color(174f/256f, 168f/256f, 211f/256f, 1f), // really light purple
			new Color(253f/256f, 227f/256f, 167f/256f, 1f), // light peach
			new Color(22f/256f, 242f/256f, 219f/256f, 1f), // blue/ turqoise
			new Color(214f/256f, 69f/256f, 65f/256f, 1f)}; // light red
			
			
	// rng to determine values
	private Random rng;
	
	public PlanetObstacle(float x, float y, Texture texture, Player p, Random rng) {
		super(x, y, texture, p);
		this.rng = rng;
		shadeImage = new dImage(0,0,texture);
		generate();
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		//shadeImage.render(batch);
		if(hasMoon)
		{
			moonImage.render(batch);
		}
		if(hasRing)
		{
			ringImage.render(batch);
		}
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(hasMoon)
		{
			if(rotationTime < 2 * Math.PI)
			{
				moonImage.setPos((float) (getX() + getWidth()/2f + Math.cos(rotationTime) * (moonImage.getWidth() + getWidth()/2f + moonDistance)),
						(float)(getY() + getHeight()/2f + Math.sin(rotationTime) * (moonImage.getWidth() + getHeight()/2f + moonDistance)));
				rotationTime+=delta*rotationSpeed;
			}
			else if(rotationTime >= 2 * Math.PI)
			{
				rotationTime = 0;
			}
		}
	}
	
	public void generate()
	{
		int colorIndex = rng.nextInt(planetColors.length);
		setColor(planetColors[colorIndex]);
		/*
		shadeImage.setColor(getColor().r + 0.1f, getColor().g + 0.1f, getColor().b + 0.1f, 1f);
		shadeImage.setWidth(getWidth() - 64);
		shadeImage.setHeight(getHeight());
		shadeImage.setPos(getX() + 32, getY());
		*/
		int flip = rng.nextInt(3);
		if(flip == 0)
		{
			getSprite().flip(true, false);
		}
		else if(flip == 1)
		{
			getSprite().flip(false,true);
		}

		int moonProbability = rng.nextInt(2);
		if(moonProbability == 0)
		{
			hasMoon = false;
		}
		else if(moonProbability == 1)
		{
			if(hasMoon == false)
			{
				moonImage = new dImage(0,0,AssetManager.getTexture("moon.png"));
			}
			hasMoon = true;
			moonImage.setDimensions(64f, 64f);
			try{
				moonImage.setColor(planetColors[colorIndex+1]);
			}
			catch(Exception e)
			{
				moonImage.setColor(planetColors[0]);
			}
			moonImage.setPos(getX() - 15 - moonImage.getWidth(), getY() - 15 - moonImage.getHeight());
			angleOffset = rng.nextInt(2);
			rotationTime = angleOffset;
			rotationSpeed = MIN_ROTATION_SPEED + rng.nextFloat();
			moonDistance = rng.nextInt(64);
		}
		// check for a ring around the planet
		int ringProbability = rng.nextInt(2);
		if(ringProbability == 0)
		{
			hasRing = false;
		}
		else if(ringProbability == 1)
		{
			if(hasRing == false)
			{
				ringImage = new dImage(0,0,AssetManager.getTexture("planetRing.png"));
			}
			hasRing = true;
			ringImage.setHeight(this.getHeight() * 1.5f);
			ringImage.setPos(getX() + getWidth() / 2f, getY() - ringImage.getHeight() / 5.75f);
			ringImage.setColor(getColor().r + 0.3f, getColor().g + 0.3f, getColor().b + 0.3f, 1f);
		}
	}
	
	private void updatePlanetObjectPositions()
	{
		if(hasMoon)
		{
			moonImage.setPos(getX() - 15 - moonImage.getWidth(), getY() - 15 - moonImage.getHeight());
		}
		if(hasRing)
		{
			ringImage.setPos(getX() + getWidth() / 2f, getY() - ringImage.getHeight() / 5.75f);
		}
		shadeImage.setPos(getX() + 32, getY());
	}
	
	@Override
	public void setPos(float x, float y)
	{
		super.setPos(x, y);
		updatePlanetObjectPositions();
	}
	
	@Override
	public void setX(float x)
	{
		super.setX(x);
		updatePlanetObjectPositions();
	}
	
	@Override
	public void setY(float y)
	{
		super.setY(y);
		updatePlanetObjectPositions();
	}
	
	public Rectangle getMoonBoundingRectangle()
	{
		return moonImage.getBoundingRectangle();
	}
	
	public boolean hasMoon()
	{
		return hasMoon;
	}

}
