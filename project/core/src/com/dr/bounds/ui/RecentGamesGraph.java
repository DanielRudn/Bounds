package com.dr.bounds.ui;

import java.util.ArrayList;

import com.DR.dLib.dObject;
import com.DR.dLib.dValues;
import com.DR.dLib.ui.dImage;
import com.DR.dLib.ui.dText;
import com.DR.dLib.ui.dUICard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class RecentGamesGraph extends dObject {

	/**
	 * A graph class that can plot connected points 
	 */
	private dImage xAxis, yAxis;
	private dText title;
	private ArrayList<dImage> points;
	private dText yLabelMin;
	private dText[] yLabels = new dText[5];
	// fix
	private Texture pointTexture;
	
	// shape renderer to draw connected lines between the points
	private ShapeRenderer sr;
	
	public RecentGamesGraph(float x, float y, Texture axisTexture, float width, float height, String title)
	{
		super(x, y, axisTexture);
		this.setUpdatable(false);
		yAxis = new dImage(0,0, axisTexture);
		xAxis = new dImage(0,0, axisTexture);
		xAxis.setDimensions(width, 3f);
		yAxis.setDimensions(3f, height);
		xAxis.setPos(x,y + yAxis.getHeight());
		yAxis.setPos(x,y + xAxis.getHeight());
		
		this.title = new dText(0,0,32f, title);
		this.title.setColor(Color.WHITE);
		this.title.setPosition(getGraphZeroX() + xAxis.getWidth()/2f - this.title.getWidth() / 2f, getY() - 16f - this.title.getHeight());
		
		pointTexture = new Texture("circle.png");
		pointTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		points = new ArrayList<dImage>();
		
		sr = new ShapeRenderer();
		sr.setProjectionMatrix(dValues.camera.combined);
	}

	@Override
	public void update(float delta) {}

	@Override
	public void render(SpriteBatch batch) {
		title.render(batch);
		xAxis.render(batch);
		yAxis.render(batch);
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
	sr.begin(ShapeType.Line);
		for(int x = 0; x < 4; x++)
		{
			for(int y = 0; y < 5; y++)
			{
				sr.setColor(1,1,1,0.1f);
				sr.rect(getGraphZeroX() + (.25f * xAxis.getWidth()) * x, getGraphZeroY() - (yAxis.getHeight() / 5f) * (y+1), .25f * xAxis.getWidth(), yAxis.getHeight() / 5f);			
			}
		}
		sr.end();
		sr.begin(ShapeType.Filled);
		for(int x = 0; x < points.size(); x++)
		{
			if(x != 0)
			{
				sr.setColor(new Color(46f/256f, 204f/256f, 113/256f, 1f));
				sr.rectLine(points.get(x-1).getX(), points.get(x-1).getY(),points.get(x).getX(), points.get(x).getY(), 4f);
			}
		}
		for(int x= 1; x < points.size(); x++)
		{
			sr.setColor(new Color(236f/256f, 240f/256f, 241f/256f, 1f));
			sr.circle(points.get(x).getX(),points.get(x).getY(), 4);
		}
		sr.end();
	}
	
	public void setPoints(ArrayList<Vector2> p)
	{
		points.clear();
		for(int x = 0; x < p.size(); x++)
		{
			points.add(new dImage(0,0, pointTexture));
			points.get(x).setDimensions(100f,100f);
			points.get(x).setColor(Color.BLACK);
			points.get(x).setPos(p.get(x).x,p.get(x).y);
		}
		float xMin = getMinX(), xMax = getMaxX(), yMin = 0, yMax = getMaxY();
		yLabelMin = new dText(0,0,32f,"" + (int)yMin);
		yLabelMin.setColor(Color.WHITE);
		yLabelMin.setPos(getX() - yLabelMin.getWidth() - 8f, getGraphZeroY() - yLabelMin.getHeight() / 2f);
		for(int x = 0; x < p.size(); x++)
		{
			yLabels[x] = (new dText(0,0,32,"" + (int)((x+1)*(yMax / 5f))));
			yLabels[x].setColor(Color.WHITE);
			yLabels[x].setPos(getX() - yLabels[x].getWidth() - 8f, getGraphZeroY() - yAxis.getHeight() * ((x+1)*.2f) - yLabels[x].getHeight() / 2f);
			points.get(x).setPos(getGraphZeroX() + xAxis.getWidth() * normalizeX(p.get(x).x, xMin, xMax), getGraphZeroY() - yAxis.getHeight() * normalizeY(p.get(x).y, yMin, yMax));
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
	
	private float normalizeY(float point, float min, float max)
	{
		return (point- min) / (max - min);
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
	
	private float normalizeX(float point, float min, float max)
	{
		return (point - min) / (max - min);
	}
	
	public float getWidth()
	{
		return xAxis.getWidth();
	}
	
	public float getHeight()
	{
		// 16f is the distance between title and y-axis
		return title.getHeight() + yAxis.getHeight() + 16f; 
	}
 }