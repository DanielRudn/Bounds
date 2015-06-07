package com.dr.bounds.animations;

import com.DR.dLib.dTweener;
import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.dAnimation;
import com.dr.bounds.ui.ShopItemCard;

public class ShopItemCardShrinkAnimation extends dAnimation {
	
	public ShopItemCardShrinkAnimation(float duration, AnimationStatusListener listener, int ID, ShopItemCard objectToAnimate) {
		super(duration, listener, ID, objectToAnimate);
	}

	@Override
	protected void animate(float time, float duration, float delta) {
		((ShopItemCard)getAnimatedObjects()[0]).setHeight(dTweener.ExponentialEaseOut(time, ShopItemCard.CARD_HEIGHT + 128f,-128f, duration));
	}

}
