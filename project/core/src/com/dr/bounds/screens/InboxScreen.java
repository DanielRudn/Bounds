package com.dr.bounds.screens;

import java.util.ArrayList;

import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.ExpandAnimation;
import com.DR.dLib.animations.ShrinkAnimation;
import com.DR.dLib.ui.dButton;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dScreen;
import com.DR.dLib.ui.dText;
import com.DR.dLib.dTweener;
import com.DR.dLib.ui.dUICard;
import com.DR.dLib.ui.dUICardList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dr.bounds.MainGame;
import com.dr.bounds.animations.SlideInArrayAnimation;
import com.dr.bounds.animations.SlideOutArrayAnimation;
import com.dr.bounds.ui.InviteCard;
import com.dr.bounds.ui.LoadingIcon;

public class InboxScreen extends dUICardList implements AnimationStatusListener {
	
	// title card at the top
	private dUICard titleCard;
	// timer for showing the transition animation to this screen
	private final float SHOW_DURATION = 3f;
	// time to show list of cards
	private final float SHOW_CARD_DURATION = 2f;
	private boolean showCards = false;
	// animations
	private ExpandAnimation startAnimation;
	private SlideInArrayAnimation startCardsAnimation;
	private static final int SHOW_CARDS_ID = 23456;
	private static final int SHOW_ANIM_ID = 12345;
	private static final int HIDE_ANIM_ID = 34567;
	private ShrinkAnimation hideAnimation;
	private SlideOutArrayAnimation hideCardsAnimation;
	private static final int HIDE_CARDS_ID = 45678;
	// loading icon to show while invitations load
	private LoadingIcon loadingIcon;
	// used for transition to show
	private dImage circleCover;
	// next screen
	private dScreen newScreen = null;

	public InboxScreen(float x, float y, Texture texture, ArrayList<dUICard> list) {
		super(x, y, texture, list);
		setColor(new Color(26f/256f, 188f/256f, 156f/256f, 0f));
		
		titleCard = new dUICard(0,0,texture);
		titleCard.setDimensions(getWidth(), 92f);
		titleCard.setHasShadow(false);
		titleCard.setColor(new Color(22f/256f, 160f/256f, 133f/256f,1f));
		dText titleText = new dText(0,0,64f,"INBOX");
		titleText.setColor(Color.WHITE);
		titleCard.addObject(titleText, dUICard.CENTER, dUICard.CENTER);
		setTitleCard(titleCard);
		titleCard.setY(-titleCard.getHeight()*1.5f);
		
		Texture circle = new Texture("circle.png");
		circle.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		circleCover = new dImage(0,0,circle);
		startAnimation = new ExpandAnimation(circleCover, SHOW_DURATION, this, SHOW_ANIM_ID, new Color(26f/256f, 188f/256f, 156f/256f, 1f), MainGame.VIRTUAL_HEIGHT * 2.5f);
		startCardsAnimation = new SlideInArrayAnimation(list, SHOW_CARD_DURATION, this, SHOW_CARDS_ID);
		this.setShowAnimation(startAnimation);
		hideAnimation = new ShrinkAnimation(circleCover, 1f, this, HIDE_ANIM_ID, 0, MainGame.VIRTUAL_HEIGHT * 2.5f);
		hideCardsAnimation = new SlideOutArrayAnimation(list, 1.5f, this, HIDE_CARDS_ID);
		this.setHideAnimation(hideAnimation);
		loadingIcon = new LoadingIcon(getWidth()/2f - circle.getWidth() / 2f,getHeight()/2f - circle.getHeight() / 2f,circle);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(startAnimation.isActive())
		{
			startAnimation.update(delta);
		}
		if(startCardsAnimation.isActive())
		{
			startCardsAnimation.update(delta);
		}
		else if(startCardsAnimation.isActive() == false)
		{
			loadingIcon.update(delta);
		}
		if(hideAnimation.isActive())
		{
			hideAnimation.update(delta);
			hideCardsAnimation.update(delta);
		}
		if(showCards && (startAnimation.isFinished() || startAnimation.getTime() >= .6f) && startCardsAnimation.isActive() == false)
		{
			startCardsAnimation.start();
			loadingIcon.stop();
		}
		
		if(startAnimation.isFinished() && startCardsAnimation.isFinished())
		{
			for(int x = 0; x < getSize(); x++)
			{
				if(getListItem(x).isVisible() && ((dButton) getListItem(x).getObject(2)).isClicked())// object at 2 is the accept button
				{
				//	MainGame.requestHandler.acceptInvite(((InviteCard)getListItem(x)).getInviteID());
					MainGame.currentScreen.switchScreen(MainGame.waitingRoomScreen);
				}
			}
		}

	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		if(newScreen != null)
		{
			newScreen.render(batch);
		}
		if(MainGame.previousScreen != null)
		{
			MainGame.previousScreen.render(batch);
		}
		circleCover.render(batch);
		loadingIcon.render(batch);
		super.render(batch);
	}
	
	public void showCards()
	{
		showCards = true;
	}
	
	@Override
	public void show()
	{
		super.show();
	//	MainGame.requestHandler.loadInvitations();
	}
	
	@Override
	public void goBack() {
		this.hide();
		this.newScreen = MainGame.menuScreen;
	}

	@Override
	public void switchScreen(dScreen newScreen) {
		this.newScreen = null;
		MainGame.currentScreen = newScreen;
		newScreen.show();
		MainGame.previousScreen = this;
	}

	@Override
	public void onAnimationStart(int ID, float duration) {
		if(ID == SHOW_ANIM_ID)
		{
			setTitleCard(titleCard);
			titleCard.setY(-titleCard.getHeight()*1.5f);
			loadingIcon.start();
		}
		else if(ID == HIDE_ANIM_ID)
		{
			setVisible(true);
			hideCardsAnimation.start();
			startAnimation.stop();
			startCardsAnimation.stop();
			loadingIcon.stop();
		}
	}

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta) {
		if(ID == SHOW_ANIM_ID)
		{
			if(time >= .6f)
			{
				titleCard.setY(dTweener.ElasticOut(time - .6f, -titleCard.getHeight() - getPadding(), titleCard.getHeight() + getPadding(), duration - .6f,5f));
			}
		}
		else if(ID == HIDE_ANIM_ID)
		{
			if(time >= .15f)
			{
				titleCard.setY(dTweener.ExponentialEaseOut(time - .15f, MainGame.camera.position.y - MainGame.VIRTUAL_HEIGHT / 2f,  -titleCard.getHeight() - getPadding() * 1.5f, duration - .15f));
			}
		}
	}

	@Override
	public void onAnimationFinish(int ID) {
		if(ID == SHOW_CARDS_ID)
		{
			showCards = false;
		}
		else if(ID == HIDE_ANIM_ID)
		{
			MainGame.currentScreen = newScreen;
			MainGame.previousScreen = null;
			newScreen = null;
		}
	}
}
