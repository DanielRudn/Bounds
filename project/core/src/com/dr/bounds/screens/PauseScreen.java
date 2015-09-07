package com.dr.bounds.screens;

import com.DR.dLib.dTweener;
import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.SlideInOrderAnimation;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.maps.MapTypeFactory;
import com.dr.bounds.ui.CircleImageButton;

public class PauseScreen extends dScreen implements AnimationStatusListener {
	
	// if the game is paused
	private boolean isActive = false;
	// buttons that show up at the bottom of the pause screen
	private CircleImageButton menuButton, resetButton, resumeButton;
	// pause text
	private dText pauseText;
	// show animation
	private dAnimation showAnim;
	private static final int SHOW_ANIM_ID = 05302015;
	// hide animation
	private dAnimation hideAnim;
	private static final int HIDE_ANIM_ID = 05312015;
	// whether to unpause and conitnue the game after hide animation
	private boolean unpause = false;
	
//	private GameScreen gs;
	
	public PauseScreen(float x, float y, Texture texture) {
		super(x, y, texture);
		this.setPadding(128f);
		this.setColor(0f, 0f, 0f, 0f);
		menuButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("backButton.png"));
		resetButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("replay.png"));
		resumeButton = new CircleImageButton(0, 0, BoundsAssetManager.getTexture("backButton.png"));
		
		pauseText = new dText(0, 0, 92f, "PAUSED");
		pauseText.setColor(Color.WHITE);
		pauseText.setShadow(true);
		
		showAnim = new SlideInOrderAnimation(2f, this, SHOW_ANIM_ID, 0, -256f, menuButton, resetButton, resumeButton);
		this.setShowAnimation(showAnim);
		hideAnim = new SlideInOrderAnimation(2f, this, HIDE_ANIM_ID, 0, 256f, resumeButton, resetButton, menuButton); // same as above but reverse
		this.setHideAnimation(hideAnim);
		
//		this.gs = gameScreen;
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
	}
	
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		resumeButton.update(delta);
		if(resumeButton.isClicked())
		{
			this.hide();
			unpause = true;
		}
		else if(menuButton.isClicked())
		{
			this.hide();
		}
	}

	@Override
	public void show()
	{
		isActive = true;
		this.setPos(MainGame.camera.position.x - MainGame.VIRTUAL_WIDTH / 2f, MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f);
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
				if (unpause)
				{
					this.setAlpha(dTweener.LinearEase(time, 0.25f, -0.25f, duration / 2f));
				} 
				else 
				{
					this.setAlpha(dTweener.LinearEase(time, 0.25f, 0.75f, duration / 2f));
				}
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
			if(unpause)
			{
				this.removeObject(this.getIndexOf(menuButton));
				this.removeObject(this.getIndexOf(resetButton));
				this.removeObject(this.getIndexOf(resumeButton));
				this.removeObject(this.getIndexOf(pauseText));
				isActive = false;
				unpause = false;
			}
			else
			{
				MainGame.camera.position.y = MainGame.VIRTUAL_HEIGHT / 2f;
				MainGame.currentScreen = MainGame.menuScreen;
				MainGame.menuScreen.show(true);
			}
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
