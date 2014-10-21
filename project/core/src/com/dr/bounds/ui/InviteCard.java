package com.dr.bounds.ui;

import com.DR.dLib.dButton;
import com.DR.dLib.dText;
import com.DR.dLib.dUICard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dr.bounds.MainGame;

public class InviteCard extends dUICard{

	// Name of the inviter
	private String inviterName;
	// object to display for text
	private dText inviterNameText;
	// invite description, trade or versus..
	private dText inviteDescriptionText;
	// button to accept invite
	private dButton acceptButton;
	// button to decline invite
	private dButton declineButton;
	// ID of the invite, used for accepting
	private String inviteID;
	// divider between text and buttons
	private dUICard horizontalDivider;
	// divider between buttons
	private dUICard verticalDivider;
	
	public InviteCard(float x, float y, Texture texture, String invName, String invID) {
		super(x, y, texture);
		setDimensions(MainGame.VIRTUAL_WIDTH - 128f, 256f);
		inviterName = invName;
		inviteID = invID;
		inviterNameText = new dText(0,0,64f,inviterName);
		addObject(inviterNameText, dUICard.CENTER, dUICard.TOP);
		
		inviteDescriptionText = new dText(0,0,48f,"challenges you to do\nsomething important!!!");
		inviteDescriptionText.setMultiline(true);
		inviteDescriptionText.setColor(Color.GRAY);
		addObjectUnder(inviteDescriptionText, getIndexOf(inviterNameText));
		
		acceptButton = new dButton(0,0,new Sprite(texture),"accept");
		acceptButton.setTextColor(Color.GREEN);
		acceptButton.setDimensions(getWidth() / 2f, 64f);
		acceptButton.setTextSize(64f);
		addObject(acceptButton,dUICard.RIGHT_NO_PADDING, dUICard.BOTTOM_NO_PADDING);
		
		declineButton = new dButton(0,0,new Sprite(texture),"decline");
		declineButton.setTextColor(Color.RED);
		declineButton.setDimensions(getWidth() / 2f, 64f);
		declineButton.setTextSize(64f);
		addObject(declineButton, dUICard.LEFT_NO_PADDING, dUICard.BOTTOM_NO_PADDING);
		
		horizontalDivider = new dUICard(0,0,texture);
		horizontalDivider.setDimensions(getWidth(), 4f);
		horizontalDivider.setColor(0,0,0,0.5f);
		horizontalDivider.setHasShadow(false);
		horizontalDivider.setUpdatable(false);
		addObject(horizontalDivider, dUICard.LEFT_NO_PADDING, dUICard.BOTTOM);
		horizontalDivider.setY(acceptButton.getY() - horizontalDivider.getHeight());
		
		verticalDivider = new dUICard(0,0,texture);
		verticalDivider.setDimensions(4f, acceptButton.getHeight());
		verticalDivider.setColor(0,0,0,0.5f);
		verticalDivider.setUpdatable(false);
		verticalDivider.setHasShadow(false);
		addObject(verticalDivider, dUICard.CENTER, dUICard.BOTTOM_NO_PADDING);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
	}
	
	public void setInviteID(String id)
	{
		inviteID = id;
	}
	
	public String getInviteID()
	{
		return inviteID;
	}

}
