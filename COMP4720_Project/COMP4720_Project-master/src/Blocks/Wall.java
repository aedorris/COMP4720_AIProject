package Blocks;

import java.awt.Color;
import java.util.ArrayList;

import edu.kzoo.grid.*;
import edu.kzoo.grid.display.*;
import edu.kzoo.grid.gui.GridAppFrame;

public class Wall extends ColorBlock {
	private GridAppFrame gui;
	private Location location;
	
	public Wall() {
		super(Color.BLACK);
		this.gui = gui;
	}

}
