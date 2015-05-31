package com.dr.bounds.screens;

import com.DR.dLib.dTweener;
import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.SlideInOrderAnimation;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dButton;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;

public class PauseScreen extends dScreen implements AnimationStatusListener {
	
	// if the game is paused
	private boolean isActive = false;
	// buttons that show up at the bottom of the pause screen
	private dButton menuButton, resetButton, resumeButton;
	private dImage tempL, tempC, tempR;
	// pause text
	private dText pauseText;
	// show animation
	private dAnimation showAnim;
	private static final int SHOW_ANIM_ID = 05302015;
	// hide animation
	private dAnimation hideAnim;
	private static final int HIDE_ANIM_ID = 05312015;
	
	private GameScreen gs;
	
	private float test = 0, testDuration = 2f;
	private boolean testAnim = false, reset = false;
	
	public PauseScreen(float x, float y, Texture texture, GameScreen gameScreen) {
		super(x, y, texture);
		this.setPadding(128f);
		this.setColor(0f, 0f, 0f, 0f);
		menuButton = new dButton(0, 0, new Sprite(BoundsAssetManager.getTexture("circle.png")), "", BoundsAssetManager.getTexture("circle.png"), 1f);
		menuButton.setDimensions(92f, 92f);
		menuButton.setTextColor(Color.BLACK);
		menuButton.setTextSize(48f);
		resetButton = new dButton(0, 0, new Sprite(BoundsAssetManager.getTexture("circle.png")), "");
		resetButton.setDimensions(92f, 92f);
		resetButton.setTextColor(Color.BLACK);
		resetButton.setTextSize(48f);
		resumeButton = new dButton(0, 0, new Sprite(BoundsAssetManager.getTexture("circle.png")), "", BoundsAssetManager.getTexture("circle.png"), 1f);
		resumeButton.setDimensions(92f, 92f);
		resumeButton.setTextColor(Color.BLACK);
		resumeButton.setTextSize(48f);
		
		pauseText = new dText(0, 0, 92f, "PAUSED");
		pauseText.setColor(Color.WHITE);
		pauseText.setShadow(true);
		
		showAnim = new SlideInOrderAnimation(2f, this, SHOW_ANIM_ID, 0, -256f, menuButton, resetButton, resumeButton);
		this.setShowAnimation(showAnim);
		hideAnim = new SlideInOrderAnimation(2f, this, HIDE_ANIM_ID, 0, 256f, resumeButton, resetButton, menuButton); // same as above but reverse
		this.setHideAnimation(hideAnim);
		
		tempL = new dImage(0, 0, BoundsAssetManager.getTexture("backButton.png"));
		tempL.setDimensions(48f, 48f);
		tempL.setColor(Color.BLACK);
		tempC = new dImage(0, 0, BoundsAssetManager.getTexture("replay.png"));
		tempC.setDimensions(48f, 48f);
		tempC.setColor(Color.BLACK);
		
		this.gs = gameScreen;
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		if(testAnim == false)
		{
			tempL.render(batch);
			tempC.render(batch);
		}
	}
	
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(showAnim.isActive())
		{
			showAnim.update(delta);
		}
		if(hideAnim.isActive())
		{
			hideAnim.update(delta);
		}
		resumeButton.update(delta);
		if(resumeButton.isClicked() && hideAnim.isActive() == false)
		{
			this.hide();
		}
		else if(resetButton.isClicked())
		{
			this.setClipping(false);
			testAnim = true;
		}
		
		if(testAnim && test <= testDuration)
		{
			test+=delta;
			resetButton.setDimensions(dTweener.ExponentialEaseOut(test, 92f, MainGame.VIRTUAL_HEIGHT*2f, testDuration), dTweener.ExponentialEaseOut(test, 92f, MainGame.VIRTUAL_HEIGHT*2f, testDuration));
			resetButton.setOriginCenter();
			if(test >= testDuration / 2f)
			{
				if(reset == false)
				{
					gs.reset();
					reset = true;
					isActive = false;
					gs.update(delta);
					isActive = true;
				}
				resetButton.setAlpha(dTweener.LinearEase(test - testDuration / 2f, 1f, -1f, 0.5f));
				resetButton.setY(MainGame.camera.position.y);
			}
		}
		if(testAnim && test >= 1.52f)
		{
			reset = false;
			testAnim = false;
			isActive = false;
		}
		
		tempL.setPos(menuButton.getX() + menuButton.getWidth()/2f - tempL.getWidth()/2f, menuButton.getY() + menuButton.getHeight()/2f - tempL.getHeight()/2f + 4f);
		tempC.setPos(resetButton.getX() + resetButton.getWidth()/2f - tempC.getWidth()/2f + 2f, resetButton.getY() + resetButton.getHeight()/2f - tempC.getHeight()/2f);
	}
	
	@Override
	public void show()
	{
		isActive = true;
		this.setPos(MainGame.camera.position.x - MainGame.VIRTUAL_WIDTH / 2f, MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f);
		resumeButton.setAlpha(1f);
		resumeButton.setDimensions(92f, 92f);
		resumeButton.setOrigin(0, 0);
		this.addObject(menuButton, dUICard.LEFT, dUICard.BOTTOM);
		this.addObject(resumeButton, dUICard.RIGHT, dUICard.BOTTOM);
		this.addObject(pauseText, dUICard.CENTER, dUICard.CENTER);
		this.addObject(resetButton, dUICard.CENTER, dUICard.BOTTOM);
		menuButton.setY(menuButton.getY() + 256f);
		resetButton.setY(resetButton.getY() + 256f);
		resumeButton.setY(resumeButton.getY() + 256f);
		pauseText.setX(pauseText.getX() - MainGame.VIRTUAL_WIDTH);
		super.show();
	}
	
	@Override
	public void hide()
	{
		hideAnim.start();
	}
	
	@Override
	public boolean isPaused()
	{
		return isActive; // TODO: double check implementation of isPaused in dScreen.
	}
	
	@Override
	public void onAnimationStart(int ID, float duration) 
	{
		if(ID == SHOW_ANIM_ID)
		{
			
		}
	}
	

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta)
	{
		if(ID == SHOW_ANIM_ID)
		{
			if(time <= duration / 2f)
			{
				pauseText.setX(dTweener.ExponentialEaseOut(time, -MainGame.VIRTUAL_WIDTH/2f - pauseText.getWidth()/2f, MainGame.VIRTUAL_WIDTH, duration / 2f));
				this.setAlpha(dTweener.LinearEase(time, 0, 0.25f, duration / 2f));
			}
		}
		else if(ID == HIDE_ANIM_ID)
		{
			if(time <= duration / 2f)
			{
				this.setAlpha(dTweener.LinearEase(time, 0.25f, -0.25f, duration / 2f));
				pauseText.setX(dTweener.ExponentialEaseOut(time, MainGame.VIRTUAL_WIDTH/2f - pauseText.getWidth()/2f, -MainGame.VIRTUAL_WIDTH, duration / 2f));
			}
			else 
			{
				hideAnim.stop();
			}
		}
	}
	
	@Override
	public void onAnimationFinish(int ID)
	{
		if(ID == SHOW_ANIM_ID)
		{
			
		}
		else if(ID == HIDE_ANIM_ID)
		{
			this.removeObject(this.getIndexOf(menuButton));
			this.removeObject(this.getIndexOf(resetButton));
			this.removeObject(this.getIndexOf(resumeButton));
			this.removeObject(this.getIndexOf(pauseText));
			isActive = false;
		}
	}
	
	@Override
	public void goBack() {
		this.hide();
	}

	@Override
	public void switchScreen(dScreen newScreen) {
		
	}

}
