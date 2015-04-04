package com.dr.bounds.animations;

import com.DR.dLib.dTweener;
import com.DR.dLib.dValues;
import com.DR.dLib.animations.AnimationStatusListener;
import com.DR.dLib.animations.dAnimation;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class CameraShakeAnimation extends dAnimation {

	private Vector3 startPos = Vector3.Zero;
	private float rotation = 0, currentTickRotation = 0;
	private MoveCameraBackAnimation moveBack;

	@SuppressWarnings(value = { "all" })
	public CameraShakeAnimation(float duration, AnimationStatusListener listener, int ID) {
		super(duration, listener, ID, null);
		moveBack = new MoveCameraBackAnimation(0.25f, startPos);
		moveBack.stop();
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(moveBack.isActive())
		{
			moveBack.update(delta);
		}
	}

	@Override
	protected void animate(float time, float duration, float delta)
	{
		dValues.camera.translate(MathUtils.random(-2f,2f), MathUtils.random(-4f,4f), 0);
		currentTickRotation = MathUtils.random(-.25f,.25f);
		((OrthographicCamera)dValues.camera).rotate(currentTickRotation);
		rotation+=currentTickRotation;
		dValues.camera.update();
	}
	
	@Override
	public void start()
	{
		super.start();
		startPos = new Vector3(dValues.camera.position);
		rotation = 0f;
		//((OrthographicCamera)dValues.camera).zoom = 0.99f;
	}
	
	public Vector3 getCameraStartPosition()
	{
		return startPos;
	}
	
	public float getRotation()
	{
		return -1f * rotation;
	}
	
	public void moveBack()
	{
		moveBack.start(startPos);
	}

}

class MoveCameraBackAnimation extends dAnimation {
	
	private Vector3 startPos, endPos;
	
	@SuppressWarnings(value = { "all" })
	public MoveCameraBackAnimation(float duration, Vector3 startPos) {
		super(duration, null, 12, null);
		this.endPos = startPos;

	}

	@Override
	protected void animate(float time, float duration, float delta) {
		dValues.camera.position.set(dTweener.BounceOut(time, endPos.x, startPos.x - endPos.x, duration),
				dTweener.BounceOut(time, endPos.y, startPos.y - endPos.y, duration), dValues.camera.position.z);
	}
	
	public void start(Vector3 start)
	{
		super.start();
		this.endPos = new Vector3(dValues.camera.position);
		this.startPos = new Vector3(start);
	}
	
	
}
