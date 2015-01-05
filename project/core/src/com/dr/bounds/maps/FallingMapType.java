package com.dr.bounds.maps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class FallingMapType extends MapType{

	private Rectangle useless = new Rectangle();
	
	public FallingMapType(int type, Player player, MapGenerator generator) {
		super(type, player, generator, new Texture("SPACE_BG.png"));
		gen.setScoreIncrementAmount(1);
		this.MIN_WIDTH = 64;
		this.MAX_WIDTH = 256;
		typeName="Asteroids";
		Texture obstacleTexture = new Texture("asteroid.png");
		for(int x = 0; x < 12; x++)
		{
			obstacles.add(new dAsteroidObstacle(0,0, obstacleTexture, player));
			obstacles.get(x).setRegenerate(false);
		}
	}
	
	@Override
	public void update(float delta) {
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
			if(gen.hadCollision() == false && Intersector.intersectRectangles(player.getBoundingRectangle(), obstacles.get(x).getBoundingRectangle(), useless)) // FIX
			{
				//obstacles.get(x).setColor(Color.BLUE);
				gen.setHadCollision(true);
				// send message to opponent saying player lost
				//MainGame.requestHandler.sendReliableMessage(new byte[]{'L'});
				// test might have to remove
				break;
			}
		}
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
		obstacles.get(index).setY(obstacles.get(getPreviousIndex(index)).getY() - MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f - MIN_DISTANCE - MapGenerator.rng.nextInt(MAX_DISTANCE));
	}

}
