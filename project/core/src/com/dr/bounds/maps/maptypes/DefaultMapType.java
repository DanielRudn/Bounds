package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.MapType;
import com.dr.bounds.maps.dObstacle;

public class DefaultMapType extends MapType {
	
	public DefaultMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator);
		typeName = "Void/Default";
		super.setScoreIncrementAmount(1);
		bgColor = new Color(44f/256f, 62f/256f, 80f/256f,1f);
		// add 12 obstacles to start with
		for(int x = 0; x < 12; x++)
		{
			obstacles.add(new dObstacle(0,0, BoundsAssetManager.getTexture("obstacle.png"), player));
			obstacles.get(x).setHeight(32f);
			obstacles.get(x).setRegenerate(false);
		//	obstacles.get(x).setColor(210f/256f, 82f/256f, 127f/256f,1f);
			obstacles.get(x).setColor(236f/256f, 64f/256f, 122f/256f, 1f);
		}
		super.particleEffect.load(Gdx.files.internal("spin.p"), Gdx.files.internal(""));
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(super.hideParticleEffect == false)
		{
			super.particleEffect.setPosition(MainGame.VIRTUAL_WIDTH/2f + 32f, MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT);
		}
	}
	
	@Override
	protected void generateObstacle(int index)
	{
		int side = MapGenerator.rng.nextInt(11); // 0,1,5,6,7 is LEFT, 2,3,8,9,10 is RIGHT, 4 is center
		if(side == 0 || side == 1 || side == 5 || side == 6 || side == 7)// left
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH));
			obstacles.get(index).setX(-16f); // -32f to account for the curve
		}
		else if(side == 2 || side == 3 || side == 8 || side == 9 || side == 10)// right
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH));
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH - obstacles.get(index).getWidth() + 16f);	
		}
		else if(side == 4)// center
		{
			obstacles.get(index).setWidth(MIN_WIDTH + MapGenerator.rng.nextInt(MAX_WIDTH) - 32f);
			obstacles.get(index).setX(MainGame.VIRTUAL_WIDTH / 2f - obstacles.get(index).getWidth() / 2f + (-50 + MapGenerator.rng.nextInt(100)));
		}
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		BoundsAssetManager.dispose("obstacle.png");
	}

}
