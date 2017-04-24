package Blocks;

import java.awt.Color;
import java.util.ArrayList;

import edu.kzoo.grid.*;
import edu.kzoo.grid.display.*;
import edu.kzoo.grid.gui.GridAppFrame;

public class Goal extends ColorBlock {
	private GridAppFrame gui;
	private Location location;
	
	public Goal() {
		super(Color.YELLOW);
		this.gui = gui;
	}


}
