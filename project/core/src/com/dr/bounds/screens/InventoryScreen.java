package com.dr.bounds.screens;

import java.util.ArrayList;

import com.DR.dLib.animations.SlideElasticAnimation;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dButton;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;

public class InventoryScreen extends dScreen {

	private static final float WIDTH = 640f, HEIGHT = 820f;
	// Reference to player for coin count, current skin and inventory details
	private Player player;
	// Darker cards containing info.
	private dUICard topCard, bottomCard;
	// List of items
	private ArrayList<dButton> items;
	// Animations
	private dAnimation showAnim, hideAnim;
	private static final int SHOW_ANIM_ID = 61915, HIDE_ANIM_ID = 62015;
	
	public InventoryScreen(Player p) {
		super(0, 0, BoundsAssetManager.getTexture("card"));
		this.setPadding(24f);
		this.setPaddingLeft(88f);
		this.setHasShadow(true);
		this.setColor(225f/256f, 225f/256f, 225f/256f, 1f);
		this.setDimensions(WIDTH, HEIGHT);
		this.player = p;
		player.setUpdatable(false);
		
		topCard = new dUICard(0, 0, BoundsAssetManager.getTexture("card"));
		topCard.setDimensions(WIDTH, 92f);
		topCard.setHasShadow(false);
		topCard.setColor(111f/256f, 111f/256f, 111f/256f, 1f);
		dText titleText = new dText(0, 0, 72f, "Inventory");
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
		bottomCard.setColor(111f/256f, 111f/256f, 111f/256f, 1f);
		bottomCard.setHasShadow(false);
		
		this.addObject(topCard, dUICard.LEFT_NO_PADDING, dUICard.TOP_NO_PADDING);
		this.addObject(bottomCard, dUICard.LEFT_NO_PADDING, dUICard.BOTTOM_NO_PADDING);
		
		
		items = new ArrayList<dButton>();
		for(int x = 0; x < 12; x++)
		{
			items.add(new dButton(0, 0, BoundsAssetManager.getTexture("circle"), ""));
			items.get(x).setDimensions(92f, 92f);
			items.get(x).setColor(topCard.getColor());
			if(x == 0)
			{
				this.addObjectUnder(items.get(x), dUICard.LEFT, this.getIndexOf(topCard));
			}
			else if(x % 3 == 0)
			{
				this.addObjectUnder(items.get(x), dUICard.LEFT, this.getIndexOf(items.get(x-3)));
			}
			else
			{
				this.addObjectToRightOf(items.get(x), this.getIndexOf(items.get(x-1)));
			}

			
		}
		
		int numSkins = 0;
		for(Byte skin : player.getUnlockedSkins()) // TODO: When adding upgrades to inventory, change the called to player.getUnlockedSkins() in here and the update method ****
		{
			dButton previousSkin = items.get(numSkins);
			Sprite sprite = new Sprite(BoundsAssetManager.SkinLoader.getTextureForSkinID((int)skin));
			sprite.flip(false, true);
			dButton newSkin = new dButton(0, 0, sprite, "");
			newSkin.setDimensions(previousSkin.getWidth(), previousSkin.getHeight());
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
		
		for(int x = 0; x < player.getUnlockedSkins().size(); x++)
		{
			if(items.get(x).isClicked())
			{
				int clickedID = 0;
				for(Byte skin : player.getUnlockedSkins())
				{
					if(clickedID == x)
					{
						player.setSkinID((int) skin);
					}
					clickedID++;
				}
			}
		}
		
		if(showAnim.isActive())
		{
			this.setY(MainGame.camera.position.y - this.getHeight() / 2f);
		}
		if(hideAnim.isActive())
		{
			this.setY(MainGame.camera.position.y - this.getHeight() / 2f);
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
	
	

}
