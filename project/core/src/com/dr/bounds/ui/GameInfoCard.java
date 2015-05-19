package com.dr.bounds.ui;

import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;

/**
 * Used in GameOverScreen to display Coins, Combo
 * @author Daniel
 */
public class GameInfoCard extends dUICard {
	
	private static final float CARD_WIDTH = MainGame.VIRTUAL_WIDTH, CARD_HEIGHT = 160;
	
	private dText coinText, comboText, coinLabel, comboLabel, bestComboText;
	private dImage dividerImage;

	public GameInfoCard(float x, float y, Texture texture) {
		super(x, y, texture);
		setColor(1f, 1f, 1f, 0.65f);
		setHasShadow(false);
		setClipping(false);
		setDimensions(CARD_WIDTH, CARD_HEIGHT);
		setPaddingLeft(128f);
		setPaddingTop(8f);
		
		coinText = new dText(0, 0, 64f, "0");
		coinText.setColor(210f/256f, 82f/256f, 127f/256f,1f);
		coinLabel = new dText(0,0, 32f, "COINS");
		coinLabel.setColor(coinText.getColor());
		
		comboText = new dText(0, 0, 64f, "0");
		comboText.setColor(210f/256f, 82f/256f, 127f/256f,1f);
		comboLabel = new dText(0,0, 32f, "COMBO");
		comboLabel.setColor(coinText.getColor());
		bestComboText = new dText(0, 0, 32f, "BEST: 0");
		bestComboText.setColor(comboLabel.getColor());
		
		dividerImage = new dImage(0,0, BoundsAssetManager.getTexture("card"));
		dividerImage.setDimensions(5f,CARD_HEIGHT/2f);
		dividerImage.setColor(Color.GRAY);
		
		this.addObject(coinText, dUICard.LEFT, dUICard.CENTER);
		this.addObjectUnder(coinLabel, this.getIndexOf(coinText));
		this.addObject(comboText, dUICard.RIGHT, dUICard.CENTER);
		this.addObjectUnder(comboLabel, this.getIndexOf(comboText));
		this.addObjectOnTopOf(bestComboText, this.getIndexOf(comboText));
		comboLabel.setX(comboText.getX() + comboText.getWidth()/2f - comboLabel.getWidth()/2f);
		bestComboText.setPos(comboLabel.getX(), bestComboText.getY() + 24f);
		this.addObject(dividerImage, dUICard.CENTER, dUICard.CENTER);
	}
	
	public void setCoinText(String text)
	{
		coinText.setText(text);
	}
	
	public void setComboText(String text)
	{
		comboText.setText(text);
	}
	
	public void setBestComboText(String text)
	{
		bestComboText.setText("BEST: " + text);
	}
	
}
