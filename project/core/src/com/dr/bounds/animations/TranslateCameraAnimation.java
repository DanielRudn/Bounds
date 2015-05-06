package com.dr.bounds.animations;

import com.DR.dLib.dTweener;
import com.DR.dLib.dValues;
import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.dAnimation;
import com.badlogic.gdx.math.Vector2;

public class TranslateCameraAnimation extends dAnimation{

	private Vector2 start;
	private float deltaX, deltaY;
	
	@SuppressWarnings(value = { "all" })
	public TranslateCameraAnimation(float duration, AnimationStatusListener listener, int ID, float deltaX, float deltaY)
	{
		super(duration, listener, ID, null);
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		System.out.println("deltaY: " + deltaY);
	}

	@Override
	protected void animate(float time, float duration, float delta)
	{
		dValues.camera.position.set(dTweener.LinearEase(time, start.x, deltaX, duration),
				dTweener.LinearEase(time, start.y, deltaY, duration),
				dValues.camera.position.z);
	}
	
	@Override
	public void start()
	{
		super.start();
		start = new Vector2(dValues.camera.position.x, dValues.camera.position.y);
	}

}
