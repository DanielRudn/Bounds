package com.dr.bounds.ui;

import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.BoundsAssetManager;

/**
 * A circular button with an image/icon in the middle and no text.
 * @author Daniel
 */
public class CircleImageButton extends dUICard {

	private static final float BUTTON_WIDTH = 92f, BUTTON_HEIGHT = 92f;
	private dImage image;
	
	public CircleImageButton(float x, float y, Texture imageTexture) {
		super(x, y, BoundsAssetManager.getTexture("circle.png"));
		this.setDimensions(BUTTON_WIDTH, BUTTON_HEIGHT);
		this.setClickable(true);
		this.setClipping(false);
		image = new dImage(0, 0, imageTexture);
		image.setColor(Color.BLACK);
		image.setDimensions(BUTTON_WIDTH / 2f, BUTTON_HEIGHT / 2f);
		addObject(image, dUICard.CENTER, dUICard.CENTER);
	}
	
	public CircleImageButton(float x, float y, Texture imageTexture, Color imageColor)
	{
		this(x, y, imageTexture);
		image.setColor(imageColor);
	}
}
