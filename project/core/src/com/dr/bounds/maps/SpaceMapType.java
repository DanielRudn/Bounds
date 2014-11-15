package com.dr.bounds.maps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class SpaceMapType extends MapType {

	// useless
	private Rectangle useless = new Rectangle(0,0,0,0);
	
	public SpaceMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator, new Texture("SPACE_BG.png"));
		MIN_DISTANCE = MAX_DISTANCE;
		MAX_DISTANCE *= 2;
		MIN_WIDTH = 192;
		MAX_WIDTH = 333 - MIN_WIDTH;
		
		// this map type awards 2 points per obstacle
		gen.setScoreIncrementAmount(2);
		
		typeName = "Space";
		// add 4 obstacles to start with
		for(int x = 0; x < 4; x++)
		{
			obstacles.add(new dPlanetObstacle(0,0, new Texture("circle.png"), player, MapGenerator.rng));
			obstacles.get(x).setRegenerate(false);
		}
	}

	@Override
	public void update(float delta)
	{
		super.update(delta);
		for(int x = 0; x < obstacles.size(); x++)
		{
			obstacles.get(x).update(delta);
			if(obstacles.get(x).shouldRegenerate())
			{
				generate(x);
				obstacles.get(x).setRegenerate(false);
			}
			// check if player had collision
			if(obstacles.get(x).getClass().getName().equals(dPlanetObstacle.class.getName()))
			{
				if(((dPlanetObstacle)obstacles.get(x)).hasMoon() && Intersector.intersectRectangles(player.getBoundingRectangle(), ((dPlanetObstacle)obstacles.get(x)).getMoonBoundingRectangle(), useless))
				{
					if(gen.hadCollision() == false)
					{
						// send message to opponent saying player lost
						MainGame.requestHandler.sendReliableMessage(new byte[]{'L'});
					}
					gen.setHadCollision(true);
					// test might have to remove
					break;
				}
				if(hadCirclularCollision(obstacles.get(x).getPos(), player.getPos(), x))
				{
					if(gen.hadCollision() == false)
					{
						// send message to opponent saying player lost
						MainGame.requestHandler.sendReliableMessage(new byte[]{'L'});
					}
					gen.setHadCollision(true);
					// test might have to remove
					break;
				}
			}
		}
	}
	

	private boolean hadCirclularCollision(Vector2 f, Vector2 i, int index)
	{
		float radiusPlanet = obstacles.get(index).getWidth() / 2f;
		float radiusPlayer = player.getWidth() / 2f;
		return Math.pow((f.x + radiusPlanet) - (i.x + radiusPlayer), 2) + Math.pow((f.y + radiusPlanet) - (i.y + radiusPlayer), 2) <= Math.pow(radiusPlanet + radiusPlayer, 2); 
	}
	
	@Override
	protected void generateBlock(int index)
	{
		int side = MapGenerator.rng.nextInt(11); // 0,1,5,6,7 is LEFT, 2,3,8,9,10 is RIGHT, 4 is center
		if(side == 0 || side == 1 || side == 5 || side == 6 || side == 7)// left
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH));
			obstacles.get(index).setHeight(obstacles.get(index).getWidth());
			obstacles.get(index).setX(32f);
		}
		else if(side == 2 || side == 3 || side == 8 || side == 9 || side == 10)// right
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH));
			obstacles.get(index).setHeight(obstacles.get(index).getWidth());
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH - obstacles.get(index).getWidth() - 32f);
		}
		else if(side == 4)// center		
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH) - 32f);
			obstacles.get(index).setHeight(obstacles.get(index).getWidth());
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH / 2f - obstacles.get(index).getWidth() / 2f + (-50 + MapGenerator.rng.nextInt(100)));
		}
		((dPlanetObstacle)obstacles.get(index)).generate();
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}

}
