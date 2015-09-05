package com.dr.bounds.ui;

import java.util.ArrayList;

import com.DR.dLib.dTweener;
import com.DR.dLib.dValues;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.DR.dLib.utils.dUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.dr.bounds.BoundsAssetManager;

public class RecentGamesGraph extends dUICard {

	/**
	 * A graph class that can plot connected points 
	 */
	private dImage xAxis, yAxis, scoreTag;
	private dText title, scoreText;
	private ArrayList<dImage> points;
	// radius of the points connecting lines
	private static final float POINT_RADIUS = 8f;
	private dText yLabelMin;
	private dText[] yLabels = new dText[5];
	// fix
	private Texture pointTexture;
	// Index of the highest score, draws a circle around the point
	private int highestScoreIndex = 0;
	
	// shape renderer to draw connected lines between the points
	private ShapeRenderer sr;
	
	// test
	private boolean animActive = false;
	private float time = 0, duration = 1.5f;
	private ArrayList<Vector2> pointPos;
	
	public RecentGamesGraph(float x, float y, Texture axisTexture, float width, float height, String title)
	{
		super(x, y, axisTexture);
		this.setUpdatable(true);
		yAxis = new dImage(0,0, axisTexture);
		xAxis = new dImage(0,0, axisTexture);
		xAxis.setDimensions(width, 3f);
		yAxis.setDimensions(3f, height);
		
		scoreTag = new dImage(0,0, BoundsAssetManager.getTexture("tag.png"));
		scoreTag.setAlpha(0.5f);
		scoreTag.setDimensions(92f, 48f);
		scoreText = new dText(0,0, 42f, "0");
		scoreText.setColor(210f/256f, 82f/256f, 127f/256f,1f);
		
		this.title = new dText(0,0,36f, title);
		this.title.setColor(Color.WHITE);
		
		pointTexture = BoundsAssetManager.getTexture("circle");
		points = new ArrayList<dImage>();
		
		sr = new ShapeRenderer();
		sr.setProjectionMatrix(dValues.camera.combined);
		
		pointPos = new ArrayList<Vector2>();
		
		addObject(xAxis,dUICard.CENTER,dUICard.CENTER);
		addObject(yAxis,dUICard.CENTER, dUICard.CENTER);
		addObject(this.title, dUICard.CENTER, dUICard.CENTER);
		xAxis.setPos(x,y + yAxis.getHeight());
		yAxis.setPos(x,y + xAxis.getHeight());
		this.title.setPosition(getGraphZeroX() + xAxis.getWidth()/2f - this.title.getWidth() / 2f, getY() - 24f - this.title.getHeight());
	//	this.title.setPosition(getGraphZeroX(), getY() - 24f - this.title.getHeight());
		
	}

	// TODO: Make an animation class for this 
	@Override
	public void update(float delta) {
		super.update(delta);
		if(time <= duration && animActive)
		{
			time += delta;
			for (int x = 0; x < points.size(); x++)
			{
				if(time - 0.15f*x > 0)
				{
					//points.get(x).setY(dTweener.ExponentialEaseOut(time - 0.15f*x, this.getGraphZeroY(), pointPos.get(x).y - this.getGraphZeroY(), duration));
					points.get(x).setY(dTweener.ElasticOut(time - 0.15f*x, this.getGraphZeroY(), pointPos.get(x).y - this.getGraphZeroY(), duration,7f));
					scoreTag.setPos(points.get(points.size()-1).getX() + scoreTag.getWidth()/2f - this.getPadding()*4f, points.get(points.size()-1).getY() - scoreTag.getHeight()/2f);
					scoreText.setPos(scoreTag.getX() + scoreTag.getWidth() / 2f - scoreText.getWidth()/2f + getPadding(), scoreTag.getY() + scoreTag.getHeight()/2f - scoreText.getHeight() /2f);
				}
				else
				{
					points.get(x).setY(this.getGraphZeroY());
				}
			}
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		title.render(batch);
		xAxis.render(batch);
		yAxis.render(batch);
		scoreTag.render(batch);
		scoreText.render(batch);
		yLabelMin.render(batch);
		for(int x = 0; x < yLabels.length; x++)
		{
			if(yLabels[x] != null)
			{
				yLabels[x].render(batch);
			}
		}
		// for some reason, the previous item isn't rendered unless this line is here
		points.get(0).render(batch);
		// draw grid
		Gdx.gl.glEnable(GL20.GL_BLEND);
		sr.begin(ShapeType.Line);
		for(int x = 0; x < 4; x++)
		{
			for(int y = 0; y < 5; y++)
			{
				sr.setColor(1,1,1,0.25f);
				sr.rect(getGraphZeroX() + (.25f * xAxis.getWidth()) * x, getGraphZeroY() - (yAxis.getHeight() / 5f) * (y+1), .25f * xAxis.getWidth(), yAxis.getHeight() / 5f);			
			}
		}
		sr.end();
		sr.begin(ShapeType.Filled);
		sr.setColor(151f/256f, 216f/256f, 84f/256f, 1f);
		// draw lines
		for(int x = 0; x < points.size(); x++)
		{
			if(x != 0)
			{
				sr.rectLine(points.get(x-1).getX(), points.get(x-1).getY(),points.get(x).getX(), points.get(x).getY(), 5f);
			}
		}
		// draw circles
		for(int x= 0; x < points.size(); x++)
		{
			sr.setColor(new Color(236f/256f, 240f/256f, 241f/256f, .25f));
			if(x == highestScoreIndex) // TODO: might remove 
			{
				sr.circle(points.get(x).getX(), points.get(x).getY(), 16);
			}
			sr.setColor(new Color(236f/256f, 240f/256f, 241f/256f, 1f));
			sr.circle(points.get(x).getX(),points.get(x).getY(), POINT_RADIUS);
		}
		sr.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public void setPoints(ArrayList<Vector2> p)
	{
		points.clear();
		for(int x = 0; x < p.size(); x++)
		{
			points.add(new dImage(0,0, pointTexture));
			points.get(x).setDimensions(100f,100f);
			points.get(x).setColor(Color.BLACK);
			addObject(points.get(x), dUICard.CENTER, dUICard.CENTER);
			points.get(x).setPos(p.get(x).x,p.get(x).y);
		}
		scoreText.setText(Integer.toString((int)points.get(points.size()-1).getY()));
		float xMin = getMinX(), xMax = getMaxX(), yMin = 0, yMax = getMaxY();
		yLabelMin = new dText(0,0,32f,"" + (int)yMin);
		yLabelMin.setColor(Color.WHITE);
		addObject(yLabelMin, dUICard.CENTER, dUICard.CENTER);
		yLabelMin.setPos(getX() - yLabelMin.getWidth() - 8f, getGraphZeroY() - yLabelMin.getHeight() / 2f);
		for(int x = 0; x < p.size(); x++)
		{
			yLabels[x] = (new dText(0,0,32,"" + (int)((x+1)*(yMax / 5f))));
			yLabels[x].setColor(Color.WHITE);
			addObject(yLabels[x], dUICard.CENTER, dUICard.CENTER);
			if(yMax == points.get(x).getY())
			{
				highestScoreIndex = x;
			}
			yLabels[x].setPos(getX() - yLabels[x].getWidth() - 8f, getGraphZeroY() - yAxis.getHeight() * ((x+1)*.2f) - yLabels[x].getHeight() / 2f);
			points.get(x).setPos(getGraphZeroX() + xAxis.getWidth() * dUtils.normalize(p.get(x).x, xMin, xMax), getGraphZeroY() - yAxis.getHeight() * dUtils.normalize(p.get(x).y, yMin, yMax));
			pointPos.add(new Vector2(points.get(x).getPos()));
		}
	}
	
	private float getGraphZeroX()
	{
		return xAxis.getX() + yAxis.getWidth();
	}

	private float getGraphZeroY()
	{
		return xAxis.getY() + xAxis.getHeight();
	}
	
	private float getMaxY()
	{
		float max = points.get(0).getY();
		for(int x = 0; x < points.size(); x++)
		{
			if(points.get(x).getY() > max)
			{
				max = points.get(x).getY();
			}
		}
		return max;
	}
	
	public float getMinY()
	{
		float min = points.get(0).getY();
		for(int x = 0; x < points.size(); x++)
		{
			if(points.get(x).getY()< min)
			{
				min = points.get(x).getY();
			}
		}
		return min;
	}
	
	private float getMaxX()
	{
		float max = points.get(0).getX();
		for(int x = 0; x < points.size(); x++)
		{
			if(points.get(x).getX() > max)
			{
				max = points.get(x).getX();
			}
		}
		return max;
	}
	
	public float getMinX()
	{
		float min = points.get(0).getX();
		for(int x = 0; x < points.size(); x++)
		{
			if(points.get(x).getX() < min)
			{
				min = points.get(x).getX();
			}
		}
		return min;
	}
	
	@Override
	public float getWidth()
	{
		return xAxis.getWidth();
	}
	
	@Override
	public float getHeight()
	{
		// 16f is the distance between title and y-axis
		return title.getHeight() + yAxis.getHeight() + 16f; 
	}
	
	@Override
	public void setPosition(Vector2 pos)
	{
		super.setPosition(pos);
		sr.translate(position.x - pos.x, position.y - pos.y, 0);
	}
	
	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		sr.translate(position.x - x, position.y - y, 0);
	}
	
	@Override
	public void setPos(Vector2 pos)
	{
		super.setPos(pos);
		sr.translate(position.x - pos.x, position.y - pos.y, 0);
	}
	
	@Override
	public void setPos(float x, float y)
	{
		super.setPos(x, y);
		sr.translate(position.x - x, position.y - y, 0);
	}
	
	@Override
	public void setX(float x)
	{
		super.setX(x);
		sr.translate(position.x - x, 0, 0);
	}
	
	@Override
	public void setY(float y)
	{
		super.setY(y);
		sr.translate(0, position.y - y, 0);
	}
	
	public void show()
	{
		this.animActive = true;
		time = 0;
	}
	
 }