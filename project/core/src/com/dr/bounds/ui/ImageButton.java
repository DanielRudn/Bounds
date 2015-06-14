package com.dr.bounds.ui;

import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.BoundsAssetManager;

/** 
 * Rectangular button with an image/icon in the center and text at the bottom.
 * @author Daniel
 */
public class ImageButton extends dUICard {
	
	private static final float WIDTH = 128f, HEIGHT = 128f;
	private dImage buttonImage;
	private dText buttonText;
	
	public ImageButton(float x, float y, Texture cardTexture, dImage image, String text)
	{
		super(x, y, cardTexture);
		this.buttonImage = image;
		buttonImage.setDimensions(48f, 48f);
		this.setClickable(true, BoundsAssetManager.getTexture("circle"));
		this.setDimensions(WIDTH, HEIGHT);
		this.setHasShadow(false);
		
		buttonText = new dText(0, 0, 28f, text);
		buttonText.setColor(Color.WHITE);
		
		this.addObject(buttonImage, dUICard.CENTER, dUICard.CENTER);
		this.addObject(buttonText, dUICard.CENTER, dUICard.BOTTOM);
	}

	public ImageButton(float x, float y, Texture cardTexture, Texture imageTexture, String text) {
		this(x, y, cardTexture, new dImage(0, 0, imageTexture), text);
	}

}
