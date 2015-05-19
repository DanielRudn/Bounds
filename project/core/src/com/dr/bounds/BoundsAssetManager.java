package com.dr.bounds;

import com.DR.dLib.ui.dText;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BoundsAssetManager {

	// map of textures with the key being the name, and the value is the texture
	// private static final Map<String, Texture> textures = new TreeMap<String, Texture>();
	private static AssetManager manager;
	
	private BoundsAssetManager(){}
	
	private static void loadTexture(String path, TextureFilter filter) throws Exception
	{
		if(filter == null)
		{
			manager.load(path, Texture.class);
		}
		else
		{
			TextureParameter params = new TextureParameter();
			params.minFilter = filter;
			params.magFilter = filter;
			manager.load(path, Texture.class, params);
		}
		// load the texture in.
		while(!manager.update())
		{

		}
	//	System.out.println("[AssetManager (" + manager.getLoadedAssets() + " items)]: loaded in: " + path);
	}
	
	public static void loadAll()
	{
		manager = new AssetManager();
		try
		{
			loadTexture("card.png", TextureFilter.Linear);
			loadTexture("button.png", TextureFilter.Linear);
			loadTexture("circle.png", TextureFilter.Linear);
		}
		catch(Exception e)
		{
			
		}
	}
	
	/**
	 * Get a texture by name, if the texture is not already loaded, then it is loaded in and returned.
	 * @param name Name of Texture to get
	 * @return Returns a texture, null if it does not exist in the assets folder
	 */
	public static Texture getTexture(String name)
	{
		
		String nameNoExtensions = removeExtensions(name);
		if(!name.contains(".png"))
		{
			name += ".png";
		}
		if(manager.isLoaded(name))
		{
			return manager.get(name, Texture.class);
		}
		else if(manager.isLoaded(nameNoExtensions))
		{
			return manager.get(nameNoExtensions, Texture.class);
		}
		
		// didn't find it, that means it didn't exist in the map, so we load the texture in and try again
		try
		{
			loadTexture(name, TextureFilter.Linear);
		}
		catch(Exception e)
		{
			// failed to load the file in, return null
			return null;
		}
		
		// try again now that its been loaded in
		return getTexture(name);
	}
	
	/**
	 * Disposes of a single texture
	 * @param name name of the texture
	 */
	public static void dispose(String name)
	{
		String nameNoExtensions = removeExtensions(name);
		if(!name.contains(".png"))
		{
			name += ".png";
		}
		if(manager.isLoaded(name))
		{
			manager.unload(name);
		//	System.out.println("[AssetManager (" + manager.getLoadedAssets() + " items)]: disposed of " + name);
		}
		else if(manager.isLoaded(nameNoExtensions))
		{
			manager.unload(nameNoExtensions);
	//		System.out.println("[AssetManager (" + manager.getLoadedAssets() + " items)]: disposed of " + nameNoExtensions);
		}
	}
	
	/**
	 * Dispose of every texture as well as the AssetManager itself
	 */
	public static void disposeAll()
	{
		manager.clear();
		dText.GAME_FONT.dispose();
		manager.dispose();
	}
	
	private static String removeExtensions(String name)
	{
		try
		{
			return name.substring(0,name.indexOf("."));
		}
		catch(IndexOutOfBoundsException ioobe) 
		{ 
			return name;
		}
	}

	
	public static class SkinLoader 
	{
		private static final int SKINS_PER_LINE = 8;
		private static final int SKIN_DIMENSIONS = 64;
		
		public static TextureRegion getTextureForSkinID(int id)
		{
			// find what line the skin is on
			int currentLine = 0, tempID = id;
			while(tempID > SKINS_PER_LINE-1)//0 to 7
			{
				tempID-=SKINS_PER_LINE;// 8 per line
				currentLine++;
			}
			int startX = SKIN_DIMENSIONS * tempID;
			int startY = SKIN_DIMENSIONS * currentLine;
			TextureRegion skin =  new TextureRegion(BoundsAssetManager.getTexture("skins.png"), startX, startY, SKIN_DIMENSIONS, SKIN_DIMENSIONS);
			skin.flip(false, true);
			return skin;
		}
	}
}


