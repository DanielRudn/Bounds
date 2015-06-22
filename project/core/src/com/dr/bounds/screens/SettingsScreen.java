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

	private dAnimation showAnim, hideAnim;
	private dImage circleImage, gearImage;
	private dButton googleButton;
	private dUICard expandedCard;
	private final static int SHOW_ANIM_ID = 777, HIDE_ANIM_ID = 888;
	private dText settingsText;
	private dToggleCard soundCard, vibrationCard;
	
	public SettingsScreen(float x, float y) {
		super(x, y, BoundsAssetManager.getTexture("circle"));
		this.setColor(0f, 188f/256f, 212f/256f, 1f);
		this.setClickable(true);
		this.setHasShadow(true);
		this.setDimensions(92f, 92f);
		circleImage = new dImage(-256,0, BoundsAssetManager.getTexture("circle"));
		circleImage.setColor(this.getColor());
		gearImage = new dImage(0, 0, BoundsAssetManager.getTexture("gear.png"));
		showAnim = new ExpandAnimation(circleImage, 3f, this, SHOW_ANIM_ID, circleImage.getColor(), MainGame.VIRTUAL_HEIGHT*2f);
		hideAnim = new ShrinkAnimation(circleImage, 2f, this, HIDE_ANIM_ID, 92f, MainGame.VIRTUAL_HEIGHT * 2f);
		
		expandedCard = new dUICard(0,0, BoundsAssetManager.getTexture("card"));
		expandedCard.setDimensions(MainGame.VIRTUAL_WIDTH, 444f);
		expandedCard.setHasShadow(false);
		expandedCard.setAlpha(0f);
		expandedCard.setPadding(16f);
		expandedCard.addObject(circleImage, dUICard.CENTER, dUICard.CENTER);
		expandedCard.addObject(gearImage, dUICard.LEFT, dUICard.BOTTOM);
		
		settingsText = new dText(0,0, 48f, "SETTINGS");
		settingsText.setColor(0f, 0f, 0f, 0f);
		expandedCard.addObject(settingsText, dUICard.CENTER, dUICard.TOP);
		
		soundCard = new dToggleCard(0,0, BoundsAssetManager.getTexture("card"), BoundsAssetManager.getTexture("button"), BoundsAssetManager.getTexture("circle"), "SOUND", expandedCard.getWidth(), 64f);
		soundCard.setToggleColor(this.getColor());
		soundCard.setHasShadow(false);
		soundCard.setAlpha(0f);
		expandedCard.addObjectUnder(soundCard, expandedCard.getIndexOf(settingsText));
		
		vibrationCard = new dToggleCard(0,0, BoundsAssetManager.getTexture("card"), BoundsAssetManager.getTexture("button"), BoundsAssetManager.getTexture("circle"), "VIBRATION", expandedCard.getWidth(), 64f);
		vibrationCard.setToggleColor(this.getColor());
		vibrationCard.setHasShadow(false);
		vibrationCard.setAlpha(0f);
		expandedCard.addObjectUnder(vibrationCard, expandedCard.getIndexOf(soundCard));
		
		googleButton = new dButton(0, 0, BoundsAssetManager.getTexture("googleButton.png"),"", BoundsAssetManager.getTexture("circle"), 2f);
		expandedCard.addObject(googleButton, dUICard.RIGHT, dUICard.BOTTOM);
		googleButton.setY(googleButton.getY() - 48f);
		
		expandedCard.setX(-expandedCard.getWidth() * 1.5f);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		gearImage.render(batch);
		expandedCard.render(batch);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		expandedCard.update(delta);
		if(this.isOpen())
		{
			expandedCard.setPos(0,MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f - expandedCard.getHeight());
		}
		if(this.isClicked() && hideAnim.isActive() == false)
		{
			showAnim.start();
		}
		if(showAnim.isActive())
		{
			showAnim.update(delta);
		}
		if(hideAnim.isActive())
		{
			hideAnim.update(delta);
		}
//		circleImage.setPos(this.getPos());
		gearImage.setPos(this.getX() + 46f - gearImage.getWidth() / 2f,this.getY() + 40f - gearImage.getHeight() / 2f);
		
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
	public void goBack() {
		if(showAnim.isActive() == false && hideAnim.isActive() == false && expandedCard.getX() == 0)
		{
			hideAnim.start();
		}
	}
	
	public boolean isOpen()
	{
		return expandedCard.getX() == 0;
	}

	@Override
	public void switchScreen(dScreen newScreen) { }
	
	@Override
	public void onAnimationStart(int ID, float duration)
	{
		if(ID == SHOW_ANIM_ID)
		{
			expandedCard.setPos(0,MainGame.camera.position.y + MainGame.VIRTUAL_HEIGHT / 2f - expandedCard.getHeight());
			circleImage.setDimensions(this.getWidth(), this.getHeight());
			circleImage.setPos(this.getX(), this.getY());
		}
		else if(ID == HIDE_ANIM_ID)
		{
			this.setDimensions(92f, 92f);
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
				googleButton.setX(dTweener.ExponentialEaseOut(time - 0.2f, -MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_WIDTH * 2f - expandedCard.getPadding() - googleButton.getWidth(), duration / 2f));
				gearImage.setOriginCenter();
				gearImage.getSprite().setRotation(dTweener.ExponentialEaseOut(time, 0, 360, duration / 2f));
			}
			if(circleImage.getWidth() >= this.getWidth() && this.getWidth() != 0)
			{
				this.setDimensions(0, 0);
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
				googleButton.setX(dTweener.ExponentialEaseOut(time - 0.1f, MainGame.VIRTUAL_WIDTH - expandedCard.getPadding() - googleButton.getWidth(), -MainGame.VIRTUAL_WIDTH, duration / 4f));
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
				expandedCard.setX(-MainGame.VIRTUAL_WIDTH * 1.5f);
			}
		}
	}
	
	@Override
	public void onAnimationFinish(int ID) { 
	}
}
