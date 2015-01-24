package com.dr.bounds;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.DR.dLib.dObject;
import com.DR.dLib.dTweener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.dr.bounds.screens.GameScreen;

public class Player extends dObject {
	
	private final int SKIN_DIMENSIONS = 64;
	private static int skinID = MainGame.PLACEHOLDER_SKIN_ID;
	private boolean moveCenter = false;
	private boolean changeVelocity = false;
	private Vector2 targetVelocity = new Vector2(0,0);
	private Vector2 playerVelocity = new Vector2(0,0);
	private float squeezeTime = 0;
	private boolean squeezed = true;
	// whether the player hit a wall, used for combos
	private boolean hitWall = false;
	// y position when the user taps the screen
	private float startY = 0;
	// bounding rectangle used for collisions
	private Rectangle boundingRect = new Rectangle(SKIN_DIMENSIONS, SKIN_DIMENSIONS,SKIN_DIMENSIONS,SKIN_DIMENSIONS);
	// 5 recent scores
	public static final	List<Integer> recentScores = new ArrayList<Integer>(5);
	// unlocked skins
	public static final Set<Byte> unlockedSkins = new TreeSet<Byte>();
	// best score
	public static int bestScore = 0;
	// best combo
	public static int bestCombo = 0;
	// amount of coins
	private int numCoins = 0;
	// temp
	private ParticleEffect trailEffect = new ParticleEffect();
	
	public Player(float x,float y, int id)
	{
		super(x,y,new Sprite(AssetManager.SkinLoader.getTextureForSkinID(MainGame.PLACEHOLDER_SKIN_ID)));
		setSkinID(id);
		trailEffect.load(Gdx.files.internal("trail.p"), Gdx.files.internal(""));
		loadPlayerData();
	}

	@Override
	public void render(SpriteBatch batch) {
		trailEffect.draw(batch);
		getSprite().draw(batch);
	}

	@Override
	public void update(float delta) {
		// add velocity
		setPosition(getX() + playerVelocity.x * delta, getY() + playerVelocity.y * delta);
		boundingRect.set(getX() + 8f, getY() + 8f, getWidth()-16f, getHeight()-16f);
		trailEffect.update(delta);
		trailEffect.setPosition(getX() + getWidth()/2f, getY() + getHeight() / 2f);
		if(Gdx.input.isTouched() && Gdx.input.justTouched())
		{
			if(touchedLeftSide())// user touched left half of screen
			{
				targetVelocity.set(-32f*32f,0);
				if(moveCenter)
				{
					playerVelocity.set(-18*18f,0);
					moveCenter = false;
				}
			}
			else // user touched right half of screen
			{
				targetVelocity.set(32f*32f,0);
				if(moveCenter)
				{
					playerVelocity.set(18*18f,0);
					moveCenter = false;
				}
			}
			changeVelocity = true;
		}
		
		if(changeVelocity)
		{
			changeVelocity(delta);
		}
		if(moveCenter)
		{
			moveCenter(delta);
		}
		
		if(squeezed)
		{
			if(squeezeTime <= 1.5f)
			{
				squeezeTime+=delta;
				setScale(dTweener.ExponentialEaseOut(squeezeTime, 24f, 40f, 1.5f) / getWidth(), dTweener.ExponentialEaseOut(squeezeTime, 76f, -12f, 1.5f) / getHeight());
			}
			else
			{
				squeezed = false;
			}
		}
	}
	
	private void changeVelocity(float delta)
	{
		playerVelocity.set(dTweener.MoveToAndSlow(playerVelocity.x, targetVelocity.x, delta*10f), dTweener.MoveToAndSlow(playerVelocity.y, targetVelocity.y, delta));
		setY(getY() - GameScreen.CAMERA_SPEED * delta * 1.1f);
		// check if passed bounds and need to move back to center
		checkBounds();
	}
	
	private void checkBounds()
	{
		if(getX() <= -5|| getX() >= MainGame.VIRTUAL_WIDTH - getWidth() + 5)// changes with +- 5 to account for ball squeezing
		{
			moveCenter = true;
			changeVelocity = false;
			setScale(16f / getWidth(),76f / getHeight());
			playerVelocity.set(0,0);
			targetVelocity.set(0,0);
			squeezeTime = 0;
			squeezed = true;
			startY = getY();
			hitWall = true;
		}
		else
		{
			hitWall = false;
		}
	}
	
	private void moveCenter(float delta)
	{
		if(getX() < MainGame.VIRTUAL_WIDTH/2f - getWidth()/2f - 14f || getX() > MainGame.VIRTUAL_WIDTH/2f - getWidth()/2f + 14f)
		{
			//setPosition(dTweener.MoveToAndSlow(getX(), MainGame.VIRTUAL_WIDTH/2f - getWidth()/2f, 4f*delta),getY());
			setX(dTweener.MoveToAndSlow(getX(), MainGame.VIRTUAL_WIDTH/2f - getWidth()/2f,5.5f*delta));
			setY(dTweener.MoveToAndSlow(getY(), startY - 475f, 5.5f*delta));
		}
		else
		{
			moveCenter = false;
		}
	}
	
	private boolean touchedLeftSide()
	{
		if(MainGame.getVirtualMouseX() <= MainGame.VIRTUAL_WIDTH/2f)
		{
			return true;
		}
		return false;
	}
	
	// runs when the program fails to load player data
	private void setDefaultValues()
	{
		for(int x = 0; x < 5; x++)
		{
			recentScores.add(0);
		}
		// default skin
		unlockedSkins.add((byte) 1);
		setSkinID(unlockedSkins.iterator().next());
		bestScore = 0;
		bestCombo = 0;
	}
	
	private void loadPlayerData()
	{
		XmlReader reader = new XmlReader();
		try {
			Element pData = reader.parse(Gdx.files.local("pData.xml"));
			// load scores
			Element scores = pData.getChildByName("Scores").getChildByName("RecentScores");
			bestScore = Integer.parseInt(pData.getChildByName("Scores").getAttribute("best"));
			bestCombo = Integer.parseInt(pData.getChildByName("Scores").getAttribute("combo"));
			String[] scoreArray = scores.get("OldestToLatest").replaceAll("[ \t\n\f\r]", "").split(",");
			for(int x = 0; x < scoreArray.length; x++)
			{
				recentScores.add(Integer.parseInt(scoreArray[x]));
			}
			// load unlocked skins
			Element skins = pData.getChildByName("Skins");
			setSkinID(Integer.parseInt(skins.getAttribute("current")));
			String[] skinArray = skins.get("SkinID").replaceAll("[ \t\n\f\r]", "").split(","); 
			for(int x = 0; x < skinArray.length; x++)
			{
				unlockedSkins.add(Byte.parseByte(skinArray[x]));
			}
		}catch (IOException e)	{
			e.printStackTrace();
		}
		catch(SerializationException se)
		{
			// file not found
			setDefaultValues();
		}
		catch(GdxRuntimeException gdx)
		{
			// error parsing file
			setDefaultValues();
		}
		System.out.println("bestScore: " + bestScore + " bestCombo: " + bestCombo);
	}
	
	public static void savePlayerData()
	{
		StringWriter stringWriter = new StringWriter();
		// write
		XmlWriter writer = new XmlWriter(stringWriter);
		try {
			writer.element("pData")
				.element("Scores").attribute("best", bestScore).attribute("combo", bestCombo)
					.element("RecentScores").attribute("OldestToLatest", recentScores.toString().replaceAll("\\[", "").replaceAll("\\]", "")).pop()
				.pop()
				.element("Skins").attribute("current",skinID).attribute("SkinID", unlockedSkins.toString().replaceAll("\\[", "").replaceAll("\\]", "")).pop()
			.pop();
		// save
		FileHandle pData = Gdx.files.local("pData.xml");
		pData.writeString(stringWriter.toString(), false);
		writer.close();
		}catch (IOException e)	{
			e.printStackTrace();
		}
	}
	
	public static void addRecentScore(int score)
	{
		// shift current scores down
		for(int x = 1; x < recentScores.size(); x++)
		{
			recentScores.set(x-1, recentScores.get(x));
		}
		recentScores.set(recentScores.size()-1, score);
	}
	
	public static boolean isSkinUnlocked(Byte id)
	{
		return unlockedSkins.contains((Byte)id);
	}
	
	// reset state when player dies in a level
	public void reset()
	{
		setOrigin(0,0);
		setPos(MainGame.VIRTUAL_WIDTH / 2f - getWidth()/2f, MainGame.VIRTUAL_HEIGHT / 2f - getHeight() / 2f);
		moveCenter = false;
		startY = getY();
		setAlpha(1f);
		getSprite().setSize(64f,64f);
	}
	
	
	public void setSkinID(int id)
	{
		skinID = id;
		getSprite().setRegion(AssetManager.SkinLoader.getTextureForSkinID(skinID));
	}
	
	public int getSkinID()
	{
		return skinID;
	}
	
	public void setCoins(int coins)
	{
		numCoins = coins;
	}
	
	public int getCoins()
	{
		return numCoins;
	}
	
	public boolean isMovingCenter()
	{
		return moveCenter;
	}
	
	public boolean hasHitWall()
	{
		return hitWall;
	}
	
	@Override
	public Rectangle getBoundingRectangle()
	{
		return boundingRect;
	}
}
