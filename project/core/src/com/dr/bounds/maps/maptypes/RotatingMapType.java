package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.graphics.Color;
import com.dr.bounds.AssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.obstacles.RotatingObstacle;

public class RotatingMapType extends MapType{

	public RotatingMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator);
		this.MIN_WIDTH = 256;
		this.MAX_WIDTH = 456 - MIN_WIDTH / 2;
		this.MIN_DISTANCE = 512;
		this.MAX_DISTANCE = (int)MainGame.VIRTUAL_HEIGHT/2;
		this.typeName = "ROTATING";
		//bgColor = new Color(20f/256f,20f/256f,20f/256f,1f);
		bgColor = new Color(96f/256f, 125f/256f, 139f/256f, 1f);
		gen.setScoreIncrementAmount(1);

		for(int x = 0; x < 15; x++)
		{
			obstacles.add(new RotatingObstacle(0,0, AssetManager.getTexture("rotating.png"), player));
			obstacles.get(x).setRegenerate(false);
		}
	}
	
	/*@Override
	protected void checkCollision(int index)
	{
		if(Intersector.distanceSegmentPoint(getObstacles().get(index).getSprite().getVertices()[0], getObstacles().get(index).getSprite().getVertices()[1],
				getObstacles().get(index).getSprite().getVertices()[15], getObstacles().get(index).getSprite().getVertices()[16],
				player.getX() + player.getWidth()/2f,
				player.getY() + player.getHeight()/2f) <= player.getWidth() / 2f)
		{
			gen.setHadCollision(true);
		}
		if(Intersector.distanceSegmentPoint(getObstacles().get(index).getSprite().getVertices()[5], getObstacles().get(index).getSprite().getVertices()[6],
				getObstacles().get(index).getSprite().getVertices()[10], getObstacles().get(index).getSprite().getVertices()[11],
				player.getX() + player.getWidth()/2f,
				player.getY() + player.getHeight()/2f) <= player.getWidth() / 2f)
		{
			gen.setHadCollision(true);
		}
	} */
	
	@Override
	protected void generateBlock(int index)
	{
		((RotatingObstacle)obstacles.get(index)).setRotation(0);
		obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH) - 32f);
		//obstacles.get(index).setHeight(8f + MapGenerator.rng.nextInt(48));
		int side = MapGenerator.rng.nextInt(6);
		if(side == 1)// left
		{
			obstacles.get(index).setX(-obstacles.get(index).getWidth()/2f);
		}
		else if(side == 2)// right
		{
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH - obstacles.get(index).getWidth()/2f);
		}
		else  // center
		{
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH / 2f - obstacles.get(index).getWidth()/2f);
		}
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}

}
