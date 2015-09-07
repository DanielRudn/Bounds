package com.dr.bounds.maps;

import com.dr.bounds.Player;
import com.dr.bounds.maps.maptypes.DefaultMapType;
import com.dr.bounds.maps.maptypes.GapMapType;
import com.dr.bounds.maps.maptypes.IceMapType;
import com.dr.bounds.maps.maptypes.MachineryMapType;
import com.dr.bounds.maps.maptypes.RotatingMapType;
import com.dr.bounds.maps.maptypes.OceanMapType;
import com.dr.bounds.maps.maptypes.SpaceMapType;
import com.dr.bounds.maps.maptypes.SpikeMapType;

public class MapTypeFactory {
	
	// map generation type
	public static final int TYPE_DEFAULT = 0, TYPE_SPACE = 1, TYPE_MACHINERY = 2, TYPE_OCEAN = 3, TYPE_SPIKE = 4, TYPE_ROTATE = 5, TYPE_ICE = 6, TYPE_GAP = 7;
	// number of map types
	public static final int NUMBER_MAPS = 8;

	private MapTypeFactory() { }
	
	public static MapType getMapType(int mapID, Player player, MapGenerator gen)
	{
		if(mapID == TYPE_DEFAULT)
		{
			return new DefaultMapType(mapID, player, gen);
		}
		else if(mapID == TYPE_MACHINERY)
		{
			return new MachineryMapType(mapID, player, gen);
		}
		else if(mapID == TYPE_SPACE)
		{
			return new SpaceMapType(mapID, player, gen);
		}
		else if(mapID == TYPE_OCEAN)
		{
			return new OceanMapType(mapID, player, gen);
		}
		else if(mapID == TYPE_SPIKE)
		{
			return new SpikeMapType(mapID, player, gen);
		}
		else if(mapID == TYPE_ROTATE)
		{
			return new RotatingMapType(mapID, player, gen);
		}
		else if(mapID == TYPE_ICE)
		{
			return new IceMapType(mapID, player, gen);
		}
		else if(mapID == TYPE_GAP)
		{
			return new GapMapType(mapID, player, gen);
		}
		return null;
	}
	
}
