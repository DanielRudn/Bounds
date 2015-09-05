package com.dr.bounds.screens;

import com.DR.dLib.dTweener;
import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.ExpandAnimation;
import com.DR.dLib.animations.ShrinkAnimation;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dButton;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dToggleCard;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;

public class SettingsScreen extends dScreen implements AnimationStatusListener {

	private final static float HEIGHT = 448f;
	private dAnimation showAnim, hideAnim;
	private dImage circleImage, gearImage;
	private dButton googleButton;
	private final static int SHOW_ANIM_ID = 777, HIDE_ANIM_ID = 888;
	private dText settingsText;
	private dToggleCard soundCard, vibrationCard;
	
	public SettingsScreen(float x, float y) {
		super(x, y, BoundsAssetManager.getTexture("card"));
		this.setColor(0f, 188f/256f, 212f/256f, 1f);
		this.setPadding(16f);
		this.setClickable(false);
		this.setHasShadow(false);
		this.setDimensions(MainGame.VIRTUAL_WIDTH, HEIGHT);
		circleImage = new dImage(-256,0, BoundsAssetManager.getTexture("circle"));
		circleImage.setColor(this.getColor());
		gearImage = new dImage(0, 0, BoundsAssetManager.getTexture("gear.png"));
		showAnim = new ExpandAnimation(circleImage, 3f, this, SHOW_ANIM_ID, circleImage.getColor(), MainGame.VIRTUAL_HEIGHT*2f);
		hideAnim = new ShrinkAnimation(circleImage, 2f, this, HIDE_ANIM_ID, 92f, MainGame.VIRTUAL_HEIGHT * 2f);

		this.addObject(circleImage, dUICard.LEFT, dUICard.BOTTOM);
		this.addObject(gearImage, dUICard.LEFT, dUICard.BOTTOM);
		gearImage.setPos(gearImage.getX() + this.getPadding()*3f, gearImage.getY() - this.getPadding()*3f);
		
		settingsText = new dText(0,0, 48f, "SETTINGS");
		settingsText.setColor(0f, 0f, 0f, 0f);
		this.addObject(settingsText, dUICard.CENTER, dUICard.TOP);
		
		soundCard = new dToggleCard(0,0, BoundsAssetManager.getTexture("card"), BoundsAssetManager.getTexture("button"), BoundsAssetManager.getTexture("circle"), "SOUND", this.getWidth(), 64f);
		soundCard.setToggleColor(this.getColor());
		soundCard.setHasShadow(false);
		soundCard.setAlpha(0f);
		this.addObjectUnder(soundCard, this.getIndexOf(settingsText));
		
		vibrationCard = new dToggleCard(0,0, BoundsAssetManager.getTexture("card"), BoundsAssetManager.getTexture("button"), BoundsAssetManager.getTexture("circle"), "VIBRATION", this.getWidth(), 64f);
		vibrationCard.setToggleColor(this.getColor());
		vibrationCard.setHasShadow(false);
		vibrationCard.setAlpha(0f);
		this.addObjectUnder(vibrationCard, this.getIndexOf(soundCard));
		
		googleButton = new dButton(0, 0, BoundsAssetManager.getTexture("googleButton.png"),"", BoundsAssetManager.getTexture("circle"), 2f);
		this.addObject(googleButton, dUICard.RIGHT, dUICard.BOTTOM);
		googleButton.setY(googleButton.getY() - 48f);
		
		this.setX(-this.getWidth() * 1.5f);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		gearImage.render(batch);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(this.isOpen())
		{
			this.setPos(0,MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f - this.getHeight());
		}
		if(showAnim.isActive())
		{
			showAnim.update(delta);
		}
		if(hideAnim.isActive())
		{
			hideAnim.update(delta);
		}
		
		// TODO: FIX
		if(MainGame.requestHandler.isConnected())
		{
			googleButton.setColor(Color.GRAY);
		}
		if(googleButton.isClicked())
		{
			MainGame.requestHandler.requestSignIn();
		}
		// TODO: FIX ^
	}
	
	@Override
	public void show()
	{
		super.show();
		showAnim.start();
	}

	@Override
	public void goBack() {
		if(showAnim.isActive() == false && hideAnim.isActive() == false && this.getX() == 0)
		{
			hideAnim.start();
		}
	}
	
	public boolean isOpen()
	{
		return this.getX() == 0;
	}

	@Override
	public void switchScreen(dScreen newScreen) { }
	
	@Override
	public void onAnimationStart(int ID, float duration)
	{
		if(ID == SHOW_ANIM_ID)
		{
			this.setPos(0,MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f - this.getHeight());
			this.setAlpha(0);
			circleImage.setDimensions(1, 1);
			circleImage.setPos(this.getX() + this.getPadding(), this.getY() + this.getHeight() - this.getPadding() * 2f);
		}
		else if(ID == HIDE_ANIM_ID)
		{
			this.setClipping(true);
			this.setColor(0f, 188f/256f, 212f/256f, 0f);
			circleImage.setAlpha(1f);
		}
	}

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta)
	{
		if(ID == SHOW_ANIM_ID)
		{
			if(time <= duration / 4f)
			{
				circleImage.setColor(dTweener.LinearEase(time, this.getColor().r, 236f/255f - this.getColor().r, duration / 4f),
						dTweener.LinearEase(time, this.getColor().g, 240f/255f - this.getColor().g, duration / 4f), 
								dTweener.LinearEase(time, this.getColor().b, 241f/255f - this.getColor().b, duration / 4f), 1f);
				settingsText.setAlpha(dTweener.LinearEase(time, 0f, 1f, duration / 4f));
				gearImage.setColor(dTweener.LinearEase(time, 1f, -1f, duration / 4f),
						dTweener.LinearEase(time, 1f, -1f, duration / 4f),
							dTweener.LinearEase(time, 1f, -1f, duration / 4f), 1f);
			}
			if(time <= duration / 2f)
			{
				soundCard.setX(dTweener.ExponentialEaseOut(time, -MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_WIDTH, duration / 2f));
				vibrationCard.setX(dTweener.ExponentialEaseOut(time - 0.15f, -MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_WIDTH, duration / 2f));
				googleButton.setX(dTweener.ExponentialEaseOut(time - 0.2f, -MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_WIDTH * 2f - this.getPadding() - googleButton.getWidth(), duration / 2f));
				gearImage.setOriginCenter();
				gearImage.getSprite().setRotation(dTweener.ExponentialEaseOut(time, 0, 360, duration / 2f));
			}
		}
		else if(ID == HIDE_ANIM_ID)
		{
			if(time <= duration / 4f)
			{
				circleImage.setColor(dTweener.LinearEase(time, 236f/255f, this.getColor().r - 236f/255f, duration / 4f),
						dTweener.LinearEase(time, 240f/255f, this.getColor().g - 240f/255f, duration / 4f), 
								dTweener.LinearEase(time, 241f/255f, this.getColor().b - 241f/255f, duration / 4f),
									dTweener.LinearEase(time, 1f, -1f, duration / 4f));
				settingsText.setAlpha(dTweener.LinearEase(time, 1f, -1f, duration / 4f));
				gearImage.setColor(dTweener.LinearEase(time, 0f, 1f, duration / 4f),
						dTweener.LinearEase(time, 0f, 1f, duration / 4f),
							dTweener.LinearEase(time, 0f, 1f, duration / 4f), 1f);
				circleImage.setX(dTweener.LinearEase(time, 128f, -192f, duration / 4f));
				googleButton.setX(dTweener.ExponentialEaseOut(time - 0.1f, MainGame.VIRTUAL_WIDTH - this.getPadding() - googleButton.getWidth(), -MainGame.VIRTUAL_WIDTH, duration / 4f));
				soundCard.setX(dTweener.ExponentialEaseOut(time - 0.15f, 0, -MainGame.VIRTUAL_WIDTH,  duration / 4f));
				vibrationCard.setX(dTweener.ExponentialEaseOut(time, 0, -MainGame.VIRTUAL_WIDTH, duration / 4f));
			}
			if(time <= duration / 2f)
			{
				gearImage.setOriginCenter();
				gearImage.getSprite().setRotation(dTweener.ExponentialEaseOut(time, 0, -360, duration / 2f));
			}
			else
			{
				this.setX(-MainGame.VIRTUAL_WIDTH * 1.5f);
			}
		}
	}
	
	@Override
	public void onAnimationFinish(int ID) { 
		if(ID == SHOW_ANIM_ID)
		{
			this.setClipping(false);
			this.setColor(circleImage.getColor());
			circleImage.setAlpha(0f);
		}
	}
}
