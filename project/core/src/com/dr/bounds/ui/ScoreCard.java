package com.dr.bounds.ui;

import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * Used in GameOverScreen to display score, combo, coins.
 * @author Daniel
 */
public class ScoreCard extends dUICard {
	
	private static final float CARD_WIDTH = 256f, CARD_HEIGHT = 192f;
	private dText topLabel;
	// Center text, the one that is the score or combo or coins.
	private dText mainText;
	// the card containing the record/total for this card
	private dUICard bestCard;
	// Text for the best/total 
	private dText bestText;

	public ScoreCard(float x, float y, Texture texture, String topLabel) {
		super(x, y, texture);
		setColor(41f/256f, 128f/256f, 185f/256f, 1f);
		setHasShadow(false);
		setClipping(false);
		setDimensions(CARD_WIDTH, CARD_HEIGHT);
		setPaddingLeft(64f);
		setPaddingTop(8f);

		this.topLabel = new dText(0,0,32f,topLabel);
		this.topLabel.setColor(Color.WHITE);
		
		mainText = new dText(0,0,128f,"0");
		mainText.setColor(Color.WHITE);
		
		bestCard = new dUICard(0,0,texture);
		bestCard.setDimensions(getWidth(), 64f);
		bestCard.setColor(getColor());
		bestCard.setHasShadow(false);
		
		bestText = new dText(0,0,48f,"best 0");
		bestText.setColor(Color.WHITE);
		
		bestCard.addObject(bestText, dUICard.CENTER, dUICard.CENTER);
		
		addObject(mainText, dUICard.CENTER, dUICard.CENTER);
		addObject(this.topLabel, dUICard.CENTER, dUICard.TOP);
		addObject(bestCard, dUICard.LEFT_NO_PADDING, dUICard.BOTTOM);
		bestCard.setY(getY() + getHeight() + 16f);
	}
	
	/**
	 * Sets the text for the centered object, usually the score/combo/coins
	 * @param text
	 */
	public void setMainText(String text)
	{
		mainText.setText(text);
		// center the text
		mainText.setX(getX() + getWidth() / 2f - mainText.getWidth() / 2f);
	}
	
	/**
	 * Sets the text at the bottom usually for the best/total 
	 * @param text
	 */
	public void setBottomText(String text)
	{
		bestText.setText("best " + text);
		bestText.setX(bestCard.getX() + bestCard.getWidth() / 2f - bestText.getWidth()/2f);
	}

}
