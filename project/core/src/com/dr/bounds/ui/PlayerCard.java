package com.dr.bounds.ui;

import com.DR.dLib.dButton;
import com.DR.dLib.dImage;
import com.DR.dLib.dText;
import com.DR.dLib.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dr.bounds.MainGame;
import com.dr.bounds.SkinLoader;

public class PlayerCard extends dUICard {

	/*
	 * Player cards that appear in a list when inviting someone
	 */
	
	// image of the player's ball
	private dImage skinImage;
	// players name as a string
	private String displayName;
	// player id to send invite when tapped
	private String playerID;
	// players name object to be drawn
	private dText nameText;
	// maximum length of display name
	private final int MAX_LENGTH = 12;
	// invite button
	private dButton inviteButton;
	
	// TEMP
	private dText playerIDText;
	
	public PlayerCard(float x, float y, Texture texture, int skinID, String name) {
		super(x, y, texture);
		setDimensions(MainGame.VIRTUAL_WIDTH - 128f, 128f);
		setClickable(true);
		setPaddingLeft(16f);
		skinImage = new dImage(0,0,SkinLoader.getTextureForSkinID(skinID));
		displayName = name;
		nameText = new dText(0,0,getFontSize(),displayName);
		inviteButton = new dButton(0,0,new Sprite(texture), "invite");
		inviteButton.setTextColor(Color.BLACK);
		
		// temp
		playerIDText = new dText(0,0,32f,"");
		playerIDText.setColor(Color.GRAY);
		
		addObject(skinImage, dUICard.LEFT, dUICard.CENTER);
		addObject(nameText, dUICard.CENTER, dUICard.CENTER);
	}
	
	public PlayerCard(float x, float y, Texture texture, TextureRegion skin, String name) {
		super(x, y, texture);
		setDimensions(MainGame.VIRTUAL_WIDTH - 128f, 128f);
		setClickable(true);
		setPaddingLeft(16f);
		skinImage = new dImage(0,0, skin);
		displayName = name;
		nameText = new dText(0,0,getFontSize(),displayName);
		inviteButton = new dButton(0,0,new Sprite(texture), "invite");
		inviteButton.setTextColor(Color.BLACK);
		
		// temp
		playerIDText = new dText(0,0,32f,"");
		playerIDText.setColor(Color.GRAY);
		
		addObject(skinImage, dUICard.LEFT, dUICard.CENTER);
		addObject(nameText, dUICard.CENTER, dUICard.CENTER);
	}
	
	public void setDisplayName(String name)
	{
		displayName = name;
	}
	
	public void setPlayerID(String id)
	{
		playerID = id;
		playerIDText.setText(id);
		addObject(playerIDText, dUICard.RIGHT, dUICard.TOP);
	}
	
	private float getFontSize()
	{
		if(displayName.length() >= MAX_LENGTH)
		{
			return 64f / displayName.length() * MAX_LENGTH;
		}
		return 64f;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
	
	public String getPlayerID()
	{
		return playerID;
	}
}
