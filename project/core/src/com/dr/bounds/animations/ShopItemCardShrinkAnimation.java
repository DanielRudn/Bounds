package com.dr.bounds.animations;

import com.DR.dLib.dTweener;
import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.dAnimation;
import com.dr.bounds.MainGame;
import com.dr.bounds.ui.ShopItemCard;

public class ShopItemCardShrinkAnimation extends dAnimation {
	
	public ShopItemCardShrinkAnimation(float duration, AnimationStatusListener listener, int ID, ShopItemCard objectToAnimate) {
		super(duration, listener, ID, objectToAnimate);
	}

	@Override
	protected void animate(float time, float duration, float delta) {
		((ShopItemCard)getAnimatedObjects()[0]).setDimensions(dTweener.ExponentialEaseOut(time, MainGame.VIRTUAL_WIDTH, -MainGame.VIRTUAL_WIDTH + ShopItemCard.CARD_WIDTH, duration),
				dTweener.ExponentialEaseOut(time, MainGame.VIRTUAL_HEIGHT, -MainGame.VIRTUAL_HEIGHT + ShopItemCard.CARD_HEIGHT, duration));
	}

}
