package com.dr.bounds.maps;

import java.util.Random;

import com.DR.dLib.dImage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class SpaceMapType extends MapType {

	// useless
	private Rectangle useless = new Rectangle(0,0,0,0);
	// tile-able backgrounds for current map type
	private dImage firstBG, secondBG;
	
	public SpaceMapType(int type, Random random, Player player, MapGenerator generator) {
		super(type, random, player, generator);
		MIN_DISTANCE = MAX_DISTANCE;
		MAX_DISTANCE *= 2;
		MIN_WIDTH = 192;
		MAX_WIDTH = 300;
		
		typeName = "Space";
		// add 5 obstacles to start with
		for(int x = 0; x < 5; x++)
		{
			obstacles.add(new dPlanetObstacle(0,0, new Texture("circle.png"), player, rng));
			obstacles.get(x).setRegenerate(false);
		}
		
		firstBG = new dImage(0,0, new Texture("SPACE_BG.png"));
		secondBG = new dImage(0,-MainGame.VIRTUAL_HEIGHT, new Texture("SPACE_BG.png"));
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		firstBG.render(batch);
		secondBG.render(batch);
		super.render(batch);
	}

	@Override
	public void update(float delta) {
		for(int x = 0; x < obstacles.size(); x++)
		{
			obstacles.get(x).update(delta);
			if(obstacles.get(x).shouldRegenerate())
			{
				generateDefault(x);
				obstacles.get(x).setRegenerate(false);
			}
			// check if player had collision
			if(obstacles.get(x).getClass().getName().equals(dPlanetObstacle.class.getName()))
			{
				if(((dPlanetObstacle)obstacles.get(x)).hasMoon() && Intersector.intersectRectangles(player.getBoundingRectangle(), ((dPlanetObstacle)obstacles.get(x)).getMoonBoundingRectangle(), useless))
				{
					gen.setHadCollision(true);
					// send message to opponent saying player lost
					MainGame.requestHandler.sendReliableMessage(new byte[]{'L'});
					// test might have to remove
					break;
				}
				if(getDistance(obstacles.get(x).getPos(), player.getPos(), x) <=  obstacles.get(x).getWidth() / 2f)
				{
					gen.setHadCollision(true);
					// send message to opponent saying player lost
					MainGame.requestHandler.sendReliableMessage(new byte[]{'L'});
					// test might have to remove
					break;
				}
			}
		}
		
		// update backgrounds
		if(firstBG.getY() >= MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f)
		{
			firstBG.setY(secondBG.getY() - firstBG.getHeight());
		}
		if(secondBG.getY() >= MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f)
		{
			secondBG.setY(firstBG.getY() - secondBG.getHeight());
		}
	}
	
	private float getDistance(Vector2 f, Vector2 i, int index)
	{
		return Vector2.dst(f.x + obstacles.get(index).getWidth() / 2f, f.y + obstacles.get(index).getWidth() / 2f,
				player.getX(), player.getY());
	}
	
	@Override
	protected void generateDefault(int index)
	{
		//reset passed for this obstacles
		obstacles.get(index).setPassed(false);
		int side = rng.nextInt(11); // 0,1,5,6,7 is LEFT, 2,3,8,9,10 is RIGHT, 4 is center
		if(side == 0 || side == 1 || side == 5 || side == 6 || side == 7)// left
		{
			obstacles.get(index).setWidth(MIN_WIDTH + rng.nextInt(MAX_WIDTH));
			obstacles.get(index).setHeight(obstacles.get(index).getWidth());
			obstacles.get(index).setX(32f);
		}
		else if(side == 2 || side == 3 || side == 8 || side == 9 || side == 10)// right
		{
			obstacles.get(index).setWidth(MIN_WIDTH + rng.nextInt(MAX_WIDTH));
			obstacles.get(index).setHeight(obstacles.get(index).getWidth());
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH - obstacles.get(index).getWidth() - 32f);
		}
		else if(side == 4)// center
		{
			obstacles.get(index).setWidth(MIN_WIDTH + rng.nextInt(MAX_WIDTH) - 32f);
			obstacles.get(index).setHeight(obstacles.get(index).getWidth());
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH / 2f - obstacles.get(index).getWidth() / 2f + (-50 + rng.nextInt(100)));
		}
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - rng.nextInt(MAX_DISTANCE));
	}
	
	@Override
	public void reset()
	{
		// reset backgrounds
		firstBG.setPos(0,0);
		secondBG.setPos(0,-MainGame.VIRTUAL_HEIGHT);
	}

}
