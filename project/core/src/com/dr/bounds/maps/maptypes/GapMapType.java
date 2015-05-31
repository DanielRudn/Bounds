package com.dr.bounds.maps.maptypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.maps.MapGenerator;
import com.dr.bounds.maps.MapType;
import com.dr.bounds.maps.obstacles.GapObstacle;

public class GapMapType extends MapType {

	public GapMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator);
		this.MIN_DISTANCE = this.MAX_DISTANCE;
		this.MAX_DISTANCE = this.MAX_DISTANCE + 128;
		gen.setScoreIncrementAmount(2);
		typeName = "Gap";
		bgColor = new Color(48f/256f, 63f/256f, 159f/256f,1f);
		for(int x = 0; x < 10; x++)
		{
			obstacles.add(new GapObstacle(0,0, BoundsAssetManager.getTexture("card.png"), player));
			obstacles.get(x).setRegenerate(false);
		}
		super.particleEffect.load(Gdx.files.internal("spin2.p"), Gdx.files.internal(""));
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
	protected void generateBlock(int index)
	{
		((GapObstacle) obstacles.get(index)).reset();
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}
	
	@Override
	protected void checkCollision(int index)
	{
		if(!gen.hadCollision() &&
				Intersector.intersectRectangles(player.getBoundingRectangle(), obstacles.get(index).getBoundingRectangle(), useless) || 
				Intersector.intersectRectangles(player.getBoundingRectangle(), ((GapObstacle)obstacles.get(index)).getRightBoundRectangle(), useless))
		{
			gen.setHadCollision(true);
		}
	}
	
}
