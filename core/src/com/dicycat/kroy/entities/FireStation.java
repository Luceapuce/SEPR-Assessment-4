package com.dicycat.kroy.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class FireStation extends Entity {

	public FireStation(Vector2 spawnPos) {
		super(spawnPos, new Texture("FireStationTemp.png"), new Vector2(298,175), Integer.MAX_VALUE);
			
	}

}
