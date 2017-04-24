package Blocks;

import java.awt.Color;
import java.util.ArrayList;

import edu.kzoo.grid.*;
import edu.kzoo.grid.display.*;
import edu.kzoo.grid.gui.GridAppFrame;

public class Monster extends ColorBlock {
	private GridAppFrame gui;
	private int health;
	private Location location;
	
	public Monster() {
		super(Color.BLUE);
		this.gui = gui;
		this.health = 1;
	}
	
	public void Move(Location location) {
		if(gui.getGrid().isValid(location) && gui.getGrid().isEmpty(location)) {
			super.changeLocation(location);
			gui.showGrid();
		}
	}

}
