package com.dr.bounds.screens;

import java.io.IOException;
import java.util.ArrayList;

import com.DR.dLib.utils.DocGrabberListener;
import com.DR.dLib.utils.GoogleDocGrabber;
import com.DR.dLib.dTweener;
import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.ExpandAnimation;
import com.DR.dLib.animations.dAnimation;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.DR.dLib.ui.dUICardList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.dr.bounds.BoundsAssetManager;
import com.dr.bounds.MainGame;
import com.dr.bounds.Player;
import com.dr.bounds.animations.ShopItemsSlideAnimation;
import com.dr.bounds.ui.LoadingIcon;
import com.dr.bounds.ui.ShopItemCard;

public class ShopScreen extends dUICardList implements DocGrabberListener, AnimationStatusListener, InputProcessor {

	// String for shop file
	private final String url ="https://docs.google.com/document/d/1fapoD_xnTPEAYMpJUGr9zWHy1vKzDvybvoDcWkcHQOU/edit?usp=sharing";
	private GoogleDocGrabber docGrabber;
	// Holds ShopItemCard Containers
	//private static ArrayList<dUICard> itemCardContainers = new ArrayList<dUICard>();
	// Holds 1 ShopItemCard
	private ArrayList<dUICard> itemCardList = new ArrayList<dUICard>();
	private Texture cardTexture;
	private dUICard titleCard;
	private dUICard currentContainer;
	private dUICard itemCard;
	private boolean itemsLoaded = false;
	// Player instance
	private Player player;
	// animations
	private dAnimation showAnim;
	private dImage circleCover;
	private static final int SHOW_ANIM_ID = 19999;
	// card/list show animation
	private dAnimation cardShowAnim;
	private static final int SHOW_CARD_ANIM_ID = 222222;
	// only draws this item when clicked
	private ShopItemCard expandedItem = null;
	private boolean startedShrink = false;
	// message of the day container
	private dUICard motdContainer;
	// sidebar
	private ShopSideBar sidebar;
	// loading icon
	private LoadingIcon loadingIcon;
	// text for player coins
	private dText coinText;
	// used to determine when to expand a shopItemCard, when clicked
	private int touchedIndex = -1;
	
	public ShopScreen(float x, float y, Texture texture, Player p) {
		super(x, y, texture, new ArrayList<dUICard>());
		this.setColor(236f/256f, 240f/256f, 241f/256f,0f);
		cardTexture = texture;
		player = p;
		Gdx.input.setInputProcessor(this);
		
		Texture circle = new Texture("circle.png");
		circleCover = new dImage(0,0,circle);
		showAnim = new ExpandAnimation(circleCover, 2.75f, this, SHOW_ANIM_ID, new Color(236f/256f, 240f/256f, 241f/256f,1f), MainGame.VIRTUAL_HEIGHT * 2f);
		this.setShowAnimation(showAnim);
		titleCard = new dUICard(0,0, texture);
		titleCard.setDimensions(getWidth(), 92f);
		titleCard.setColor(234f/256f,76f/256f,136f/256f,1f);
		titleCard.setHasShadow(false);
		dText title = new dText(0,0,64f,"SHOP");
		title.setColor(236f/256f, 240f/256f, 241f/256f,1f);
		titleCard.addObject(title, dUICard.CENTER, dUICard.CENTER);
		this.setTitleCard(titleCard);
		titleCard.setY(getY() - titleCard.getHeight() - getPadding());
		
		loadingIcon = new LoadingIcon(getX() + getWidth() / 2f - 92f, getY() + getHeight()/2f - 92f, circle);
		sidebar = new ShopSideBar(0,0,texture,this);
		
		coinText = new dText(0,0,48f, "Coins: " + player.getCoins());
		coinText.setColor(title.getColor());
		titleCard.addObject(coinText, dUICard.RIGHT, dUICard.CENTER);
		
		//cardShowAnim = new SlideInArrayAnimation(getList(), 2.5f, this, SHOW_CARD_ANIM_ID);
		cardShowAnim = new ShopItemsSlideAnimation(2.5f,this,SHOW_CARD_ANIM_ID,itemCardList);
		
		if(itemsLoaded == false)
		{
			docGrabber = new GoogleDocGrabber(url, this);
			loadItemsFromBackend();
			itemsLoaded = true;
		}
	}
	
	private void loadItemsFromBackend()
	{
		docGrabber.connect();
		loadingIcon.start();
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		circleCover.render(batch);
		super.render(batch);
		sidebar.render(batch);
		loadingIcon.render(batch);
		if(expandedItem != null)
		{
			expandedItem.render(batch);
		}
	}
	
	@Override
	public void update(float delta)
	{
		if(expandedItem == null)
		{
			super.update(delta);
			sidebar.update(delta);
			loadingIcon.update(delta);
		}
		else
		{
			expandedItem.update(delta);
			if(expandedItem.getCancelButton().isClicked())
			{
				expandedItem.shrink();
				startedShrink = true;
			}
			if(Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.isKeyJustPressed(Keys.BACK))
			{
				expandedItem.shrink();
				startedShrink = true;
			}
			if(expandedItem.isFinishedShrink() && startedShrink)
			{
				startedShrink = false;
				expandedItem = null;
			}
		}
		if(showAnim.isActive())
		{
			showAnim.update(delta);
		}
		if(cardShowAnim.isActive())
		{
			cardShowAnim.update(delta);
		}
		// update number of coins 
		// TODO: FIX
		coinText.setText("Coins: " + player.getCoins());
	}

	@Override
	public void goBack() {
		switchScreen(MainGame.menuScreen);
	}

	@Override
	public void switchScreen(dScreen newScreen) {
		MainGame.currentScreen = newScreen;
		newScreen.show();
	}
	
	public void setTitleCardColor(Color c)
	{
		titleCard.setColor(c);
	}
	
	/**
	 * load shop data from a string. Used when loading from the google doc
	 * @param xmlString
	 */
	private void loadDataFromXML(String xmlString)
	{
		System.out.println("Loading from string");
		XmlReader reader = new XmlReader();
		Element shop = reader.parse(xmlString);
		parseShopXML(shop);
		// if everything was ok, save the xml
		saveShopXML(xmlString);
	}

	/**
	 * Load shop data from a local xml file.
	 * @param file File location
	 * @throws IOException if file is not found or able to be loaded.
	 */
	private void loadDataFromXML(FileHandle file) throws IOException
	{
		System.out.println("Loading from file..");
		System.out.println("Exists: " + file.exists());
		XmlReader reader = new XmlReader();
		Element shop = reader.parse(file);
		parseShopXML(shop);
	}
	
	private void saveShopXML(String xml)
	{
		System.out.println("Saving:");
		FileHandle shopData = Gdx.files.local("shopData.xml");
		if(shopData.exists() == false)
		{
			try {
				shopData.file().createNewFile();
				shopData.file().setWritable(true);
			} catch (IOException e) {
				System.out.println("Failed making shopData file");
			}
		}
		shopData.writeString(xml, false);
	}
	
	private void parseShopXML(final Element shop)
	{
		System.out.println("called");
		Gdx.app.postRunnable(new Runnable()
		{
				@Override
				public void run() {
					// load motd
					motdContainer = new dUICard(0,0,BoundsAssetManager.getTexture("card"));
					motdContainer.setColor(titleCard.getColor());
					motdContainer.setDimensions(ShopItemCard.CARD_WIDTH, 64f);
					dText motd = new dText(0,0,48f, shop.get("MOTD"));
					motd.setColor(236f/256f, 240f/256f, 241f/256f,1f);
					motdContainer.addObject(motd, dUICard.CENTER, dUICard.CENTER);
				//	addCardAsObject(motdContainer);
					itemCardList.add(motdContainer);
				//	motdContainer.setX(motdContainer.getX() + getPadding()*4f);
					for(int x = 0; x < shop.getChildrenByName("Item").size; x++)
					{
						final Element e = shop.getChildrenByName("Item").get(x);
						if(Integer.parseInt(e.get("version")) <= MainGame.GAME_VERSION)
						{
							itemCard = new ShopItemCard(0,0,cardTexture,e.get("name"), Integer.parseInt(e.get("price")), Byte.parseByte(e.get("id")), player);
							itemCardList.add(itemCard);
							currentContainer = new dUICard(0,0,cardTexture);
							currentContainer.setClipping(false);
							currentContainer.setDimensions(itemCard.getWidth(), itemCard.getHeight()*1.25f);
							currentContainer.setHasShadow(false);
							currentContainer.setAlpha(0);
							currentContainer.addObject(itemCard, dUICard.CENTER, dUICard.CENTER);
							addCardAsObject(currentContainer);
							currentContainer.setX(currentContainer.getX() + getPadding()*4f);
						}
					}
					cardShowAnim = new ShopItemsSlideAnimation(2.5f,null,SHOW_CARD_ANIM_ID, itemCardList);
					cardShowAnim.start();
					loadingIcon.stop();
				}
		});
	}
	
	/*
	 * ====================LISTENERS===================
	 */
	
	/*
	 * DocGrabberListener
	 */
	
	@Override
	public void onConnected(String docURL, String docText) {
		System.out.println("Connected\n" + docText);
		loadDataFromXML(docText);
	}

	@Override
	public void onConnectionFailed(Throwable t) {
		System.out.println("Failed " +  t.getMessage());
		try{
			//System.out.println("File string: " + Gdx.files.local("shopData.xml").readString());
			loadDataFromXML(Gdx.files.local("shopData.xml"));
		}catch(IOException ioe)
		{
			// Failed to load shop, uh oh
		}
	}

	@Override
	public void onConnectionCancelled() {
		System.out.println("cancelled");
	}
	
	/*
	 * AnimationStatusListener
	 */

	@Override
	public void onAnimationStart(int ID, float duration)
	{
		if(ID == SHOW_ANIM_ID)
		{
			circleCover.setPos(MainGame.camera.position.x, MainGame.camera.position.y);
			sidebar.setX(-sidebar.getWidth());
			titleCard.setY(-titleCard.getHeight());
		}
	}

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta)
	{
		if(ID == SHOW_ANIM_ID)
		{
			if(time > 0.5f)
			{
				titleCard.setY(dTweener.ElasticOut(time-0.5f, getY() - titleCard.getHeight() - getPadding(), titleCard.getHeight() + getPadding(), duration-0.5f,7.5f));
				sidebar.setX(dTweener.ElasticOut(time-0.5f, -sidebar.getWidth(), sidebar.getWidth(), duration-0.5f,7f));
			}
		}
	}
	
	@Override
	public void onAnimationFinish(int ID)
	{
		
	}

	/*
	 * InputProcessor
	 */
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for(int x = 0; x < itemCardList.size(); x++)
		{
				if(expandedItem == null && itemCardList.get(x).getBoundingRectangle().contains(MainGame.getVirtualMouseX(), MainGame.getVirtualMouseY()))
				{
					touchedIndex = x;
					return false;
				}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for(int x = 1; x < itemCardList.size(); x++)
		{
				if(touchedIndex == x && expandedItem == null && itemCardList.get(x).getBoundingRectangle().contains(MainGame.getVirtualMouseX(), MainGame.getVirtualMouseY()) && this.getScrollDelta() <= 0.1f)
				{
					expandedItem = (ShopItemCard)itemCardList.get(x);
					expandedItem.expand();
				}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}

// side bar inner class
class ShopSideBar extends dUICard
{
	private dImage skinIcon, homeIcon;
	// buttons/cards that can be clicked
	private dUICard shopHome, skins;
	// ShopScreen instance
	private ShopScreen shopScreen;
	
	public ShopSideBar(float x, float y, Texture texture, ShopScreen container)
	{
		super(x, y, texture);
		this.setDimensions(92f,MainGame.VIRTUAL_HEIGHT);
		this.setColor(226f/256f, 230f/256f, 231/256f,1f);
		this.setPadding(0);
		this.setHasShadow(false);
		shopScreen = container;
		Texture circle = BoundsAssetManager.getTexture("circle");
		circle.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shopHome = new dUICard(0,0,texture);
		shopHome.setHasShadow(false);
		shopHome.setClickable(true, circle);
		shopHome.setDimensions(getWidth(),getWidth());
		shopHome.setColor(234f/256f,76f/256f,136f/256f,1f);
		homeIcon = new dImage(0,0,new Sprite(BoundsAssetManager.getTexture("homeIcon.png")));
		homeIcon.setColor(0,0,0,0.25f);
		homeIcon.setDimensions(shopHome.getWidth() / 2f, shopHome.getHeight() / 2f);
		shopHome.addObject(homeIcon, dUICard.CENTER, dUICard.CENTER);
		skins = new dUICard(0,0,texture);
		skins.setClickable(true, circle);
		skins.setHasShadow(false);
		skins.setDimensions(shopHome.getWidth(), shopHome.getHeight());
		skins.setColor(80f/256f,210f/256f,192f/256f,1f);
		skinIcon = new dImage(0,0,new Sprite(circle));
		skinIcon.setColor(0,0,0, 0.25f);
		skinIcon.setDimensions(skins.getWidth() / 2f, skins.getHeight() / 2f);
		skins.addObject(skinIcon, dUICard.CENTER, dUICard.CENTER);
		
		addObject(shopHome,dUICard.LEFT_NO_PADDING,dUICard.TOP_NO_PADDING);
		addObjectUnder(skins,getIndexOf(shopHome));
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(shopHome.isClicked())
		{
			shopScreen.setTitleCardColor(shopHome.getColor());
		}
		else if(skins.isClicked())
		{
			shopScreen.setTitleCardColor(skins.getColor());
		}
	}
}
