package com.dr.bounds.ui;

import com.DR.dLib.dTweener;
import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dButton;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.MainGame;
import com.dr.bounds.SkinLoader;
import com.dr.bounds.animations.ShopItemCardExpandAnimation;
import com.dr.bounds.animations.ShopItemCardShrinkAnimation;

public class ShopItemCard extends dUICard implements AnimationStatusListener {

	private dText itemName;
	private dText itemPrice;
	private dImage itemImage;
	private dUICard imageCard;
	private byte skinID;
	public static final float CARD_HEIGHT = 192f, CARD_WIDTH = 192f;
	private dAnimation expandAnimation;
	private final int EXPAND_ANIM_ID = 2015;
	// for expand anim
	private float startX = 0, startY = 0, textStartSize = 0;
	private boolean expanded = false;
	// shrink
	private final int SHRINK_ANIM_ID = 2012;
	private dAnimation shrinkAnimation;
	// set/buy button
	private dButton acceptButton;
	private dButton cancelButton;
	
	public ShopItemCard(float x, float y, Texture texture, String name, int price, byte id) {
		super(x, y, texture);
		this.setClickable(true);
		this.setDimensions(CARD_WIDTH, CARD_HEIGHT);
		skinID = id;
		itemName = new dText(0,0,getFontSize(name,40f,10 ),name);
		itemName.setColor(Color.BLACK);
		
		itemPrice = new dText(0,0,32f,Integer.toString(price));
		itemPrice.setColor(44f/256f, 62f/256f, 80f/256f,1f);
		
		imageCard = new dUICard(0,0,texture);
		imageCard.setDimensions(CARD_WIDTH, CARD_HEIGHT * .6f);
		imageCard.setHasShadow(false);
		imageCard.setColor(246f/256f, 246f/256f, 246f/256f, 1f);
		itemImage = new dImage(0,0,SkinLoader.getTextureForSkinID((int)id));
	
		imageCard.addObject(itemImage,dUICard.CENTER, dUICard.CENTER);
		addObject(imageCard,dUICard.LEFT_NO_PADDING, dUICard.TOP_NO_PADDING);
		addObjectUnder(itemName, getIndexOf(imageCard));
	//	itemName.setX(getX() + getPadding());
		itemName.setX(getX() + getWidth() / 2f - itemName.getWidth() / 2f);
		addObject(itemPrice,dUICard.CENTER, dUICard.BOTTOM);
		
		acceptButton = new dButton(MainGame.VIRTUAL_WIDTH/2f, MainGame.VIRTUAL_HEIGHT*2f, new Sprite(texture), "SET", new Texture("circle.png"), 2f);
		acceptButton.setTextSize(92f);
		acceptButton.setDimensions(MainGame.VIRTUAL_WIDTH / 2f, 192f);
		acceptButton.setColor(46f/256f, 204f/256f, 113f/256f,1f);
		
		cancelButton = new dButton(0, MainGame.VIRTUAL_HEIGHT*2f, new Sprite(texture), "BACK");
		cancelButton.setTextSize(92f);
		cancelButton.setDimensions(MainGame.VIRTUAL_WIDTH / 2f, 192f);
		cancelButton.setColor(231f/256f, 76f/256f, 60f/256f,1f);
		
		expandAnimation = new ShopItemCardExpandAnimation(1f, this, EXPAND_ANIM_ID, this);
		shrinkAnimation = new ShopItemCardShrinkAnimation(1f,this,SHRINK_ANIM_ID,this);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		acceptButton.render(batch);
		cancelButton.render(batch);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(expandAnimation.isActive())
		{
			expandAnimation.update(delta);
		}
		if(shrinkAnimation.isActive())
		{
			shrinkAnimation.update(delta);
		}
		if(expanded)
		{
			acceptButton.update(delta);
			cancelButton.update(delta);
			if(acceptButton.isClicked())
			{
				MainGame.setPlayerSkin(skinID);
			}
		}
	}
	
	private float getFontSize(String text, float defaultSize, float maxLength)
	{
		if(text.length() >= maxLength)
		{
			return defaultSize / text.length() * maxLength;
		}
		return defaultSize;
	}
	
	public void expand()
	{
		if(expandAnimation.isActive() == false && shrinkAnimation.isActive() == false && expanded == false)
		{
			expandAnimation.start();
		}
	}
	
	public void shrink()
	{
		if(shrinkAnimation.isActive() == false && expandAnimation.isActive() == false && expanded)
		{
			shrinkAnimation.start();
		}
	}
	
	public dButton getCancelButton()
	{
		return cancelButton;
	}
	
	public boolean isFinishedShrink()
	{
		return shrinkAnimation.isFinished();
	}

	@Override
	public void onAnimationStart(int ID, float duration) {
		if(ID == EXPAND_ANIM_ID)
		{
			startX = getX();
			startY = getY();
			textStartSize = getFontSize(itemName.getText(),40f,10);
			this.setClipping(false);
			imageCard.setClipping(false);
			expanded = true;
		}
		else if(ID == SHRINK_ANIM_ID)
		{
			setClipping(true);
			imageCard.setClipping(true);
		}
	}

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta) {
		if(ID == EXPAND_ANIM_ID)
		{
			setDimensions(dTweener.ExponentialEaseOut(time, ShopItemCard.CARD_WIDTH, MainGame.VIRTUAL_WIDTH - ShopItemCard.CARD_WIDTH, duration)
					, dTweener.ExponentialEaseOut(time, ShopItemCard.CARD_HEIGHT, MainGame.VIRTUAL_HEIGHT - ShopItemCard.CARD_HEIGHT, duration));
			imageCard.setDimensions(getWidth(), dTweener.ExponentialEaseOut(time, CARD_HEIGHT*.6f, 333, duration));
			itemImage.setPos(getX() + imageCard.getWidth() / 2f - itemImage.getWidth() / 2f, getY() + imageCard.getHeight() / 2f - itemImage.getHeight() / 2f);
			itemImage.setDimensions(dTweener.ExponentialEaseOut(time, 64f, 64f, duration), dTweener.ExponentialEaseOut(time, 64f, 64f, duration));
			itemName.setPos(getX() + getWidth() / 2f - itemName.getWidth() / 2f, imageCard.getY() + imageCard.getHeight() + getPadding()*2f);
			itemName.setSize(dTweener.ExponentialEaseOut(time, textStartSize, textStartSize*2f, duration));
			itemPrice.setPos(getX() + getWidth() /2f - itemPrice.getWidth() / 2f, itemName.getY() + itemName.getHeight() + getPadding()*4f);
			itemPrice.setSize(dTweener.ExponentialEaseOut(time, 20f, 64f, duration));
			
			acceptButton.setY(dTweener.ExponentialEaseOut(time, MainGame.VIRTUAL_HEIGHT*2f, -MainGame.VIRTUAL_HEIGHT*2f + MainGame.VIRTUAL_HEIGHT * .55f, duration));
			cancelButton.setY(acceptButton.getY());
		}
		else if(ID == SHRINK_ANIM_ID)
		{
			setPosition(dTweener.ExponentialEaseOut(time, 0, startX, duration),dTweener.ExponentialEaseOut(time,0, startY, duration));
			imageCard.setDimensions(getWidth(), dTweener.ExponentialEaseOut(time, CARD_HEIGHT*.6f + 333, -333, duration));
			itemImage.setPos(getX() + imageCard.getWidth() / 2f - itemImage.getWidth() / 2f, getY() + imageCard.getHeight() / 2f - itemImage.getHeight() / 2f);
			itemImage.setDimensions(dTweener.ExponentialEaseOut(time, 128f, -64f, duration), dTweener.ExponentialEaseOut(time, 128f, -64f, duration));
			itemName.setPos(getX() + getWidth()/2f - itemName.getWidth()/2f, imageCard.getY() + imageCard.getHeight() + getPadding()*2f);
			itemName.setSize(dTweener.ExponentialEaseOut(time, textStartSize*2f, -textStartSize, duration));
			//itemPrice.setPos(getX() + getWidth() / 2f - itemPrice.getWidth()/2f, getY() + getHeight() - getPadding() - itemPrice.getHeight());
			itemPrice.setPos(getX() + getWidth() / 2f - itemPrice.getWidth()/2f,itemName.getY() + itemName.getHeight() + getPadding());
			itemPrice.setSize(dTweener.ExponentialEaseOut(time, 64f, -32f, duration));
			
			
			acceptButton.setY(dTweener.ExponentialEaseOut(time,MainGame.VIRTUAL_HEIGHT * .55f,MainGame.VIRTUAL_HEIGHT*2f, duration));
			cancelButton.setY(acceptButton.getY());
		}
	}
	
	@Override
	public void onAnimationFinish(int ID) {
		if(ID == EXPAND_ANIM_ID)
		{
			setClickable(false);
		}
		else if(ID == SHRINK_ANIM_ID)
		{
			setClickable(true);
			expanded = false;
		}
	}

}