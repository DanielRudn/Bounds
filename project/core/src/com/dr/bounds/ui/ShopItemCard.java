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
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.animations.ShopItemCardExpandAnimation;
import com.dr.bounds.animations.ShopItemCardShrinkAnimation;

public class ShopItemCard extends dUICard implements AnimationStatusListener {

	private dText itemName;
	private dText itemPrice;
	private dImage itemImage, coinImage;
	private dUICard imageCard;
	private byte skinID;
	public static final float CARD_HEIGHT = 256f, CARD_WIDTH = 256f;
	private dAnimation expandAnimation;
	private final int EXPAND_ANIM_ID = 2015;
	// for expand anim
	private float startX = 0, startY = 0, endY = 0, endX = 0;
	private boolean expanded = false;
	// shrink
	private final int SHRINK_ANIM_ID = 2012;
	private dAnimation shrinkAnimation;
	// set/buy button
	private dButton acceptButton;
	private dButton cancelButton;
	// black screen to fade in when an item is clicked
	private dImage fadeCover;
	// checkmark shown if this item is owned
	private dImage ownedImage;
	// Player instance
	private Player player;
	
	public ShopItemCard(float x, float y, Texture texture, String name, int price, byte id, Player p) {
		super(x, y, texture);
		this.setClickable(true, BoundsAssetManager.getTexture("circle"));
		this.setDimensions(CARD_WIDTH, CARD_HEIGHT);
		this.setPaddingTop(12f);
		player = p;
		
		skinID = id;
		itemName = new dText(0,0,getFontSize(name,48f,10),name);
		itemName.setColor(Color.BLACK);
		
		itemPrice = new dText(0,0,48f,Integer.toString(price));
		itemPrice.setColor(234f/256f,76f/256f,136f/256f,1f);
		
		imageCard = new dUICard(0,0,texture);
		imageCard.setDimensions(CARD_WIDTH, CARD_HEIGHT * 0.2f);
		imageCard.setHasShadow(false);
		imageCard.setColor(240f/256f, 240f/256f, 240f/256f, 1f);
		itemImage = new dImage(0,0,BoundsAssetManager.SkinLoader.getTextureForSkinID((int)id));
		itemImage.setDimensions(92f, 92f);
		
		coinImage = new dImage(0,0, BoundsAssetManager.getTexture("coin.png"));
		coinImage.setDimensions(32f, 32f);
	
		addObject(itemImage,dUICard.CENTER, dUICard.CENTER);
		addObject(imageCard,dUICard.LEFT_NO_PADDING, dUICard.BOTTOM_NO_PADDING);
		addObject(itemName, dUICard.CENTER, dUICard.TOP);
		imageCard.addObject(itemPrice, dUICard.LEFT, dUICard.CENTER);
		imageCard.addObjectToRightOf(coinImage, imageCard.getIndexOf(itemPrice));
		coinImage.setY(itemPrice.getY());
		
		if(player.isSkinUnlocked(id))
		{
			acceptButton = new dButton(MainGame.VIRTUAL_WIDTH/2f, MainGame.VIRTUAL_HEIGHT*2f, texture, "SET", BoundsAssetManager.getTexture("circle"), 2f);
			acceptButton.setColor(46f/256f, 204f/256f, 113f/256f,1f);
			acceptButton.setTextColor(this.getColor());

			ownedImage=  new dImage(0,0,BoundsAssetManager.getTexture("checkMark.png"));
			ownedImage.setDimensions(32f, 32f);
			imageCard.addObject(ownedImage,dUICard.RIGHT, dUICard.CENTER);
		}
		else
		{
			acceptButton = new dButton(MainGame.VIRTUAL_WIDTH/2f, MainGame.VIRTUAL_HEIGHT*2f, texture, "BUY", BoundsAssetManager.getTexture("circle"), 2f);
			acceptButton.setColor(Color.GRAY);
			acceptButton.setTextColor(this.getColor());
		}
		acceptButton.setTextSize(48f);
		acceptButton.setDimensions(CARD_WIDTH/2f, 128f);
		
		cancelButton = new dButton(acceptButton.getX() - acceptButton.getWidth(), MainGame.VIRTUAL_HEIGHT*2f, texture, "BACK");
		cancelButton.setTextSize(48f);
		cancelButton.setDimensions(CARD_WIDTH/2f, 128f);
		cancelButton.setColor(231f/256f, 76f/256f, 60f/256f,1f);
		cancelButton.setTextColor(this.getColor());
		
		expandAnimation = new ShopItemCardExpandAnimation(1f, this, EXPAND_ANIM_ID, this);
		shrinkAnimation = new ShopItemCardShrinkAnimation(1f,this,SHRINK_ANIM_ID,this);
		
		fadeCover = new dImage(0,0,texture);
		fadeCover.setDimensions(MainGame.VIRTUAL_WIDTH, MainGame.VIRTUAL_HEIGHT);
		fadeCover.setColor(0,0,0,0);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		if(expanded)
		{
			fadeCover.render(batch);
		}
		super.render(batch);
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
				if(acceptButton.getText().equals("BUY"))
				{
					// buy
					// TODO: Save item price in an integer variable
					if(player.getCoins() >= Integer.parseInt(itemPrice.getText()))
					{
						player.setCoins(player.getCoins() - Integer.parseInt(itemPrice.getText()));
						acceptButton.setText("SET");
						acceptButton.setColor(46f/256f, 204f/256f, 113f/256f,1f);
						ownedImage=  new dImage(0,0,BoundsAssetManager.getTexture("checkMark.png"));
						ownedImage.setDimensions(40f, 40f);
						addObject(ownedImage,dUICard.RIGHT, dUICard.CENTER);
						ownedImage.setPos(getX() + getWidth() - getPadding() - ownedImage.getWidth(), getY() + CARD_HEIGHT - getPadding() - ownedImage.getHeight());
						player.getUnlockedSkins().add(skinID);
						player.savePlayerData();
					}
					else
					{
						// not enough coins, do something
					}
				}
				else if(acceptButton.getText().equals("SET"))
				{
					MainGame.setPlayerSkin(skinID);
				}
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
	
	public boolean isFinishedShrinking()
	{
		return shrinkAnimation.isFinished();
	}

	@Override
	public void onAnimationStart(int ID, float duration) {
		if(ID == EXPAND_ANIM_ID)
		{
			startX = getX();
			startY = getY();
			this.setClipping(true);
			imageCard.setClipping(true);
			expanded = true;
			addObjectUnder(cancelButton, getIndexOf(imageCard));
			addObjectToRightOf(acceptButton, getIndexOf(cancelButton));
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
			setHeight(dTweener.ExponentialEaseOut(time, ShopItemCard.CARD_HEIGHT, 128f, duration));
			itemImage.setPos(getX() + ShopItemCard.CARD_WIDTH / 2f - itemImage.getWidth() / 2f, getY() + ShopItemCard.CARD_HEIGHT / 2f - itemImage.getHeight() / 2f);
			itemName.setPos(getX() + ShopItemCard.CARD_WIDTH / 2f - itemName.getWidth() / 2f, getY() + this.getPadding());
			imageCard.setPos(getX(), getY() + ShopItemCard.CARD_HEIGHT - imageCard.getHeight());
			cancelButton.setPos(imageCard.getX(), imageCard.getY() + imageCard.getHeight());
			acceptButton.setPos(cancelButton.getX() + cancelButton.getWidth(), cancelButton.getY());
			fadeCover.setAlpha(dTweener.LinearEase(time, 0, .4f, duration));
		}
		else if(ID == SHRINK_ANIM_ID)
		{
			setPosition(dTweener.ExponentialEaseOut(time, endX,-endX + startX, duration),dTweener.ExponentialEaseOut(time, endY,-endY + startY, duration));
			itemImage.setPos(getX() + ShopItemCard.CARD_WIDTH / 2f - itemImage.getWidth() / 2f, getY() + ShopItemCard.CARD_HEIGHT / 2f - itemImage.getHeight() / 2f);
			itemName.setPos(getX() + ShopItemCard.CARD_WIDTH / 2f - itemName.getWidth() / 2f, getY() + this.getPadding());
			imageCard.setPos(getX(), getY() + ShopItemCard.CARD_HEIGHT - imageCard.getHeight());
			cancelButton.setPos(imageCard.getX(), imageCard.getY() + imageCard.getHeight());
			acceptButton.setPos(cancelButton.getX() + cancelButton.getWidth(), cancelButton.getY());
			fadeCover.setAlpha(dTweener.ExponentialEaseOut(time, .4f, -.395f, duration));
		}
	}
	
	@Override
	public void onAnimationFinish(int ID) {
		if(ID == EXPAND_ANIM_ID)
		{
			setClickable(false);
			endX = getX();
			endY = getY();
		}
		else if(ID == SHRINK_ANIM_ID)
		{
			setClickable(true);
			expanded = false;
			fadeCover.setAlpha(0);
			this.removeObject(this.getIndexOf(acceptButton));
			this.removeObject(this.getIndexOf(cancelButton));
		}
	}

}