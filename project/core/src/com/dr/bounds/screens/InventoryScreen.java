package com.dr.bounds.screens;

import java.util.ArrayList;

import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.SlideElasticAnimation;
import com.DR.dLib.animations.SlideExponentialAnimation;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dButton;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.DR.dLib.ui.dUICardList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class InventoryScreen extends dUICardList implements AnimationStatusListener {

	private static final float WIDTH = 640f, HEIGHT = 820f;
	private static final int SKINS_PER_PAGE = 12;
	// Reference to player for coin count, current skin and inventory details
	private Player player;
	// Darker cards containing info.
	private dUICard topCard, bottomCard;
	// Color of darker cards
	private Color accentColor;
	// Buttons and text on bottom card
	private dButton prevPage, nextPage; 
	private dText currentPageText;
	// List of items
	private ArrayList<dUICard> items;
	// Animations
	private dAnimation showAnim, hideAnim, pageSwitchAnimation;
	private static final int SHOW_ANIM_ID = 61915, HIDE_ANIM_ID = 62015, PAGE_SWITCH_ANIM_ID = 68260;
	// page info
	private int currentPage = 0, totalPages = 0;
	
	public InventoryScreen(Player p) {
		super(0, 0, BoundsAssetManager.getTexture("card"), new ArrayList<dUICard>());
		this.setPadding(24f);
		this.setPaddingLeft(88f);
		this.setHasShadow(true);
	//	this.setColor(225f/256f, 225f/256f, 225f/256f, 1f); light grey
		this.setColor(251f/256f, 251f/256f, 251f/256f, 1f);
		this.setDimensions(WIDTH, HEIGHT);
		this.player = p;
		player.setUpdatable(false);
		
	//	accentColor = new Color(111f/256f, 111f/256f, 111f/256f, 1f);
		accentColor = new Color(24f/256f, 166f/256f, 206f/256f, 1f);
		topCard = new dUICard(0, 0, BoundsAssetManager.getTexture("card"));
		topCard.setDimensions(WIDTH, 92f);
		topCard.setHasShadow(false);
		topCard.setColor(accentColor);
		dText titleText = new dText(0, 0, 64f, "Inventory");
		titleText.setColor(this.getColor());
		dUICard coinInfo = new dUICard(0, 0, BoundsAssetManager.getTexture("card"));
		coinInfo.setHasShadow(false);
		coinInfo.setDimensions(128f, 64f);
		coinInfo.setClipping(false);
		coinInfo.setColor(topCard.getColor());
		dText coinText = new dText(0, 0, 42f, "x" + String.format("%,d", player.getCoins()));
		coinText.setColor(this.getColor());
		dImage coinImage = new dImage(0, 0, BoundsAssetManager.getTexture("coin.png"));
		coinImage.setDimensions(48f, 48f);
		coinInfo.addObject(coinImage, dUICard.LEFT, dUICard.CENTER);
		coinInfo.addObjectToRightOf(coinText, coinInfo.getIndexOf(coinImage));
		coinText.setY(coinText.getY() - 12f);
		topCard.addObject(titleText, dUICard.CENTER, dUICard.CENTER);
		topCard.addObject(player, dUICard.RIGHT, dUICard.CENTER);
		topCard.addObject(coinInfo, dUICard.LEFT, dUICard.CENTER);
		
		bottomCard = new dUICard(0, 0, BoundsAssetManager.getTexture("card"));
		bottomCard.setDimensions(WIDTH, 92f);
		bottomCard.setColor(accentColor);
		bottomCard.setHasShadow(false);	
		
		prevPage = new dButton(0, 0, BoundsAssetManager.getTexture("card"), "prev");
		prevPage.setColor(accentColor);
		prevPage.setTextSize(42f);
		prevPage.setTextColor(this.getColor());
		prevPage.setDimensions(128f, bottomCard.getHeight());
		prevPage.setTextColor(Color.GRAY);
		nextPage = new dButton(0, 0, BoundsAssetManager.getTexture("card"), "next");
		nextPage.setColor(accentColor);
		nextPage.setTextSize(42f);
		nextPage.setTextColor(this.getColor());
		nextPage.setDimensions(128f, bottomCard.getHeight());
		
		currentPageText = new dText(0, 0, 48f, "Page 1");
		currentPageText.setColor(this.getColor());
		
		bottomCard.addObject(prevPage, dUICard.LEFT, dUICard.CENTER);
		bottomCard.addObject(currentPageText, dUICard.CENTER, dUICard.CENTER);
		bottomCard.addObject(nextPage, dUICard.RIGHT, dUICard.CENTER);
		
		
		this.addObject(topCard, dUICard.LEFT_NO_PADDING, dUICard.TOP_NO_PADDING);
		this.addObject(bottomCard, dUICard.LEFT_NO_PADDING, dUICard.BOTTOM_NO_PADDING);
		
		items = new ArrayList<dUICard>();
		for(int x = 0; x < player.getUnlockedSkins().size(); x++)
		{	
			if(x != 0 && x % SKINS_PER_PAGE == 0)
			{
				totalPages++;
			}
			items.add(new dUICard(0, 0, BoundsAssetManager.getTexture("circle")));
			items.get(x).setHasShadow(false);
			items.get(x).setDimensions(92f, 92f);
			items.get(x).setClickable(true);
			items.get(x).setColor(topCard.getColor());
			if(x == 0 || x % SKINS_PER_PAGE == 0)
			{
				this.addObjectUnder(items.get(x), dUICard.LEFT, this.getIndexOf(topCard));
				items.get(x).setX(items.get(x).getX() + WIDTH * totalPages);
			}
			else if(x % 3 == 0)
			{
				this.addObjectUnder(items.get(x), dUICard.LEFT, this.getIndexOf(items.get(x-3)));
				items.get(x).setX(items.get(x).getX() + WIDTH * totalPages);
			}
			else
			{
				this.addObjectToRightOf(items.get(x), this.getIndexOf(items.get(x-1)));
			}
		}
		// gray out the next page button if there is only one page.
		if(totalPages == 0) 
		{
			nextPage.setTextColor(Color.GRAY);
		}
		
		int numSkins = 0;
		for(Byte skin : player.getUnlockedSkins()) // TODO: When adding upgrades to inventory, change the called to player.getUnlockedSkins() in here and the update method ****
		{
			dUICard previousSkin = items.get(numSkins);
			Sprite sprite = new Sprite(BoundsAssetManager.SkinLoader.getTextureForSkinID((int)skin));
			sprite.flip(false, true);
			dUICard newSkin = new dUICard(0, 0, sprite);
			newSkin.setDimensions(previousSkin.getWidth(), previousSkin.getHeight());
			newSkin.setClickable(previousSkin.isClickable());
			this.removeObject(this.getIndexOf(previousSkin));
			this.addObject(newSkin, dUICard.LEFT, dUICard.TOP);
			newSkin.setPos(previousSkin.getPos());
			items.set(numSkins, newSkin);
			
			numSkins++;
		} 
		
		
		showAnim = new SlideElasticAnimation(2f, null, SHOW_ANIM_ID, MainGame.VIRTUAL_WIDTH, 0, this);
		hideAnim = new SlideElasticAnimation(2f, null, HIDE_ANIM_ID, -MainGame.VIRTUAL_WIDTH, 0, this);
		this.setShowAnimation(showAnim);
		this.setHideAnimation(hideAnim);
		
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		
		for(int x = 0; x < items.size(); x++)
		{
			if(items.get(x).isClicked())
			{
				int clickedID = 0;
				for(Byte skin : player.getUnlockedSkins())
				{
					if(clickedID == x)
					{
						player.setSkinID((int)skin);
					}
					clickedID++;
				}
			}
		}
		
		if(nextPage.isClicked() && currentPage != totalPages)
		{
			pageSwitchAnimation = new SlideExponentialAnimation(1f, this, PAGE_SWITCH_ANIM_ID, -WIDTH, 0, items.toArray(new dUICard[]{}));
			pageSwitchAnimation.start();
			currentPage++;
			currentPageText.setText("Page " + (currentPage+1));
			if(currentPage == totalPages)
			{
				nextPage.setTextColor(Color.GRAY);
			}
			prevPage.setTextColor(this.getColor());
		}
		else if(prevPage.isClicked() && currentPage != 0)
		{
			pageSwitchAnimation = new SlideExponentialAnimation(1f, this, PAGE_SWITCH_ANIM_ID, WIDTH, 0, items.toArray(new dUICard[]{}));
			pageSwitchAnimation.start();
			currentPage--;
			currentPageText.setText("Page " + (currentPage+1));
			if(currentPage == 0)
			{
				prevPage.setTextColor(Color.GRAY);
			}
			nextPage.setTextColor(this.getColor());
		}
		
		if(showAnim.isActive())
		{
			this.setY(MainGame.camera.position.y - this.getHeight() / 2f);
		}
		if(hideAnim.isActive())
		{
			this.setY(MainGame.camera.position.y - this.getHeight() / 2f);
		}
		if(pageSwitchAnimation != null && pageSwitchAnimation.isActive())
		{
			pageSwitchAnimation.update(delta);
		}
	}
	
	@Override
	public void show()
	{
		this.setX(this.getX() - MainGame.VIRTUAL_WIDTH);
		super.show();
	}

	@Override
	public void hide()
	{
		hideAnim.start();
	}
	
	@Override
	public void goBack() {
		this.hideAnim.start();
	}

	@Override
	public void switchScreen(dScreen newScreen) {
		
	}
	
	/*
	 * AnimationStatusListener
	 */

	@Override
	public void onAnimationStart(int ID, float duration)
	{
	}

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta)
	{
	}
	
	@Override
	public void onAnimationFinish(int ID)
	{}
	

}
