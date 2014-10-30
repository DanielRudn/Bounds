package com.dr.bounds.maps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class FactoryMapType extends MapType{

	// useless please get rid of
	private Rectangle useless = new Rectangle(0,0,0,0);
	
	public FactoryMapType(int type, Player p, Texture obstacleTexture, MapGenerator generator) {
		super(type, p, generator, new Texture("MACHINE_BG.png"));
		// this map type awards 1 point per obstacle
		gen.setScoreIncrementAmount(1);
		typeName = "Factory";
		// add 12 obstacles to start with
		for(int x = 0; x < 12; x++)
		{
			obstacles.add(new dObstacle(0,0, obstacleTexture, p));
			obstacles.get(x).setRegenerate(false);
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		for(int x = 0; x < obstacles.size(); x++)
		{
			obstacles.get(x).update(delta);
			if(obstacles.get(x).shouldRegenerate())
			{
				generateDefault(x);
				obstacles.get(x).setRegenerate(false);
			}
			// check if player had collision
			if(gen.hadCollision() == false && Intersector.intersectRectangles(player.getBoundingRectangle(), obstacles.get(x).getBoundingRectangle(), useless)) // FIX
			{
				//obstacles.get(x).setColor(Color.BLUE);
				gen.setHadCollision(true);
				// send message to opponent saying player lost
				MainGame.requestHandler.sendReliableMessage(new byte[]{'L'});
				// test might have to remove
				break;
			}
		}
	}

}
