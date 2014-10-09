package com.dr.bounds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SkinLoader {

	public static final Texture SKINS = new Texture("skins.png");
	private final static int SKINS_PER_LINE = 8, NUM_LINES = 8, SKIN_DIMENSIONS = 64;
	
	public static TextureRegion getTextureForSkinID(int id)
	{
		SKINS.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// find what line the skin is on
		int currentLine = 0, tempID = id;
		while(tempID > 7)//0 to 7
		{
			tempID-=8;// 8 per line
			currentLine++;
		}
		int startX = SKIN_DIMENSIONS * tempID;
		int startY = SKIN_DIMENSIONS * currentLine;
		TextureRegion skin =  new TextureRegion(SKINS,startX,startY,SKIN_DIMENSIONS,SKIN_DIMENSIONS);
		skin.flip(false, true);
		return skin;
	}
}
