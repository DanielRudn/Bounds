package com.dr.bounds;

import com.DR.dLib.dObject;
import com.DR.dLib.dTweener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dr.bounds.screens.GameScreen;

public class Player extends dObject {
	
	private final int SKIN_DIMENSIONS = 64;
	private int skinID = MainGame.PLACEHOLDER_SKIN_ID;
	private boolean moveCenter = false;
	private boolean changeVelocity = false;
	private Vector2 targetVelocity = new Vector2(0,0);
	private Vector2 playerVelocity = new Vector2(0,0);
	private float squeezeTime = 0;
	private boolean squeezed = true;
	// y position when the user taps the screen
	private float startY = 0;
	// bounding rectangle used for collisions
	private Rectangle boundingRect = new Rectangle(SKIN_DIMENSIONS, SKIN_DIMENSIONS,SKIN_DIMENSIONS,SKIN_DIMENSIONS);
	
	// temp
	private ParticleEffect trailEffect = new ParticleEffect();
	
	public Player(float x, float y, RequestHandler rq) {
		super(x, y, new Sprite(SkinLoader.getTextureForSkinID(MainGame.PLACEHOLDER_SKIN_ID)));
	}
	
	public Player(float x,float y, int id)
	{
		super(x,y,new Sprite(SkinLoader.getTextureForSkinID(MainGame.PLACEHOLDER_SKIN_ID)));
		setSkinID(id);
		trailEffect.load(Gdx.files.internal("trail.p"), Gdx.files.internal(""));
	}
	
	public Player(float x, float y)
	{
		super(x,y,new Sprite(SkinLoader.getTextureForSkinID(MainGame.PLACEHOLDER_SKIN_ID)));
	}
	
	public Player(float x, float y, Texture t)
	{
		super(x,y,t);
	}
	
	public Player(float x, float y, TextureRegion t)
	{
		super(x,y, new Sprite(t));
	}
	

	@Override
	public void render(SpriteBatch batch) {
		trailEffect.draw(batch);
		getSprite().draw(batch);
	}

	@Override
	public void update(float delta) {
		// add velocity
		setPosition(getX() + playerVelocity.x * delta, getY() + playerVelocity.y * delta);
		boundingRect.set(getX() + 8f, getY() + 8f, getWidth()-16f, getHeight()-16f);
		trailEffect.update(delta);
		trailEffect.setPosition(getX() + getWidth()/2f, getY() + getHeight() / 2f);
	//	boundingRect.set(0,0,0,0);
		if(Gdx.input.isTouched() && Gdx.input.justTouched())
		{
			if(touchedLeftSide())// user touched left half of screen
			{
				targetVelocity.set(-32f*32f,0);
				if(moveCenter)
				{
					playerVelocity.set(-18*18f,0);
					moveCenter = false;
				}
			}
			else // user touched right half of screen
			{
				targetVelocity.set(32f*32f,0);
				if(moveCenter)
				{
					playerVelocity.set(18*18f,0);
					moveCenter = false;
				}
			}
			changeVelocity = true;
		}
		
		if(changeVelocity)
		{
			changeVelocity(delta);
		}
		if(moveCenter)
		{
			moveCenter(delta);
		}
		
		if(squeezed)
		{
			if(squeezeTime <= 1.5f)
			{
				squeezeTime+=delta;
				setScale(dTweener.ExponentialEaseOut(squeezeTime, 24f, 40f, 1.5f) / getWidth(), dTweener.ExponentialEaseOut(squeezeTime, 76f, -12f, 1.5f) / getHeight());
			}
			else
			{
				squeezed = false;
			}
		}
	}
	
	private void changeVelocity(float delta)
	{
		playerVelocity.set(dTweener.MoveToAndSlow(playerVelocity.x, targetVelocity.x, delta*10f), dTweener.MoveToAndSlow(playerVelocity.y, targetVelocity.y, delta));
		setY(getY() - GameScreen.CAMERA_SPEED * delta * 1.1f);
		// check if passed bounds and need to move back to center
		checkBounds();
	}
	
	private void checkBounds()
	{
		if(getX() <= -5|| getX() >= MainGame.VIRTUAL_WIDTH - getWidth() + 5)// changes with +- 5 to account for ball squeezing
		{
			moveCenter = true;
			changeVelocity = false;
			setScale(16f / getWidth(),76f / getHeight());
			playerVelocity.set(0,0);
			targetVelocity.set(0,0);
			squeezeTime = 0;
			squeezed = true;
			startY = getY();
		}
	}
	
	private void moveCenter(float delta)
	{
		if(getX() < MainGame.VIRTUAL_WIDTH/2f - getWidth()/2f - 14f || getX() > MainGame.VIRTUAL_WIDTH/2f - getWidth()/2f + 14f)
		{
			//setPosition(dTweener.MoveToAndSlow(getX(), MainGame.VIRTUAL_WIDTH/2f - getWidth()/2f, 4f*delta),getY());
			setX(dTweener.MoveToAndSlow(getX(), MainGame.VIRTUAL_WIDTH/2f - getWidth()/2f,5.5f*delta));
			setY(dTweener.MoveToAndSlow(getY(), startY - 475f, 5.5f*delta));
		}
		else
		{
			moveCenter = false;
		}
	}
	
	private boolean touchedLeftSide()
	{
		if(MainGame.getVirtualMouseX() <= MainGame.VIRTUAL_WIDTH/2f)
		{
			return true;
		}
		return false;
	}
	
	public void reset()
	{
		setPos(MainGame.VIRTUAL_WIDTH / 2f - getWidth()/2f, MainGame.VIRTUAL_HEIGHT / 2f - getHeight() / 2f);
		moveCenter = false;
		startY = getY();
	}
	
	
	public void setSkinID(int id)
	{
		skinID = id;
		getSprite().setRegion(SkinLoader.getTextureForSkinID(skinID));
	}
	
	public int getSkinID()
	{
		return skinID;
	}
	
	public boolean isMovingCenter()
	{
		return moveCenter;
	}
	
	@Override
	public Rectangle getBoundingRectangle()
	{
		return boundingRect;
	}
}
