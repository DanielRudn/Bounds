package com.dr.bounds.maps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class DefaultMapType extends MapType {

	private Rectangle useless = new Rectangle(0,0,0,0);
	
	public DefaultMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator, new Texture("DEFAULT_BG.png"));
		typeName = "Void/Default";
		gen.setScoreIncrementAmount(1);
		Texture obstacleTexture = new Texture("obstacle.png");
		obstacleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// add 12 obstacles to start with
		for(int x = 0; x < 12; x++)
		{
			obstacles.add(new dObstacle(0,0, obstacleTexture, player));
			obstacles.get(x).setRegenerate(false);
			obstacles.get(x).setColor(new Color(214f/256f, 69f/256f, 65f/256f,1f));
		}
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		// update collision
		for(int x = 0; x < obstacles.size(); x++)
		{
			obstacles.get(x).update(delta);
			if(obstacles.get(x).shouldRegenerate())
			{
				generate(x);
				obstacles.get(x).setRegenerate(false);
			}
			// check if player had collision
			if(gen.hadCollision() == false && Intersector.intersectRectangles(player.getBoundingRectangle(), obstacles.get(x).getBoundingRectangle(), useless)) // FIX
			{
				//obstacles.get(x).setColor(Color.BLUE);
				gen.setHadCollision(true);
				// send message to opponent saying player lost
			//	MainGame.requestHandler.sendReliableMessage(new byte[]{'L'});
				// test might have to remove
				break;
			}
		}
	}

}
