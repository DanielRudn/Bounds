package com.dr.bounds.screens;

import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.SlideElasticAnimation;
import com.DR.dLib.ui.dScreen;
import com.badlogic.gdx.graphics.Texture;
import com.dr.bounds.MainGame;

public class MultiplayerScreen extends dScreen implements AnimationStatusListener {
	
	// animations
	private SlideElasticAnimation showAnimation;
	private final int SHOW_ANIM_ID = 12345;

	public MultiplayerScreen(float x, float y, Texture texture) {
		super(x, y, texture);
		showAnimation = new SlideElasticAnimation(2f, this, SHOW_ANIM_ID, MainGame.VIRTUAL_WIDTH, 0, this);
		setShowAnimation(showAnimation);
	}

	@Override
	public void goBack() {
		switchScreen(MainGame.menuScreen);
	}

	@Override
	public void switchScreen(dScreen newScreen) {
		this.hide();
		newScreen.show();
		MainGame.currentScreen = newScreen;
		MainGame.previousScreen = this;
	}

	@Override
	public void onAnimationStart(int ID, float duration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void whileAnimating(int ID, float time, float duration, float delta) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void onAnimationFinish(int ID) {
		// TODO Auto-generated method stub
		
	}

}
