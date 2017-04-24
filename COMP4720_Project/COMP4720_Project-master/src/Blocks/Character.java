package Blocks;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.kzoo.grid.*;
import edu.kzoo.grid.display.*;
import edu.kzoo.grid.gui.GridAppFrame;

public class Character extends ColorBlock {
	private GridAppFrame gui;
	private Grid grid;
	private int health;
	private boolean dead = false;
	private boolean goalReached = false;
	
	public Character(GridAppFrame gui, Grid grid) {
		super(Color.RED);
		this.gui = gui;
		this.grid = grid;
		this.health = 3;
	}
	
	public boolean getGoalReached() {
		return goalReached;
	}
	
	public ArrayList<Location> GetNeighbors() {
		return super.grid().neighborsOf(super.location());
	}
	
	public List<Location> allNeighbors(Location location) {
		List<Location> locations = new ArrayList<Location>();
		locations = grid.neighborsOf(location);
			
		return locations;
	}
	
	public void genMove(Location location) {
		if (gui.getGrid().isEmpty(location) && gui.getGrid().isValid(location)) {
			super.changeLocation(location);
			gui.showGrid();
		}
		else if(gui.getGrid().objectAt(location) instanceof Monster) {
			attack(location);
		}
		else if(gui.getGrid().objectAt(location) instanceof Trap) {
//			System.out.println("You stepped on a trap!");
			damage();
			if (dead == false) {
				grid.remove(location);
				super.changeLocation(location);
				gui.showGrid();
			}
		}
		else if(gui.getGrid().objectAt(location) instanceof Food) {
//			System.out.println("You ate some food! Yum!");
			health += 1;
//			System.out.println("You healed 1 HP! You're at: " + health);
			grid.remove(location);
			super.changeLocation(location);
			gui.showGrid();
		}
		else if(gui.getGrid().objectAt(location) instanceof Goal) {
//			System.out.println("You win!");
			grid.remove(location);
			super.changeLocation(location);
			goalReached = true;
			gui.showGrid();
		}
	}
	
	public void simMove(Location location) {
		
		if (gui.getGrid().isEmpty(location) && gui.getGrid().isValid(location)) {
			super.changeLocation(location);
			gui.showGrid();
		}
		else if(gui.getGrid().objectAt(location) instanceof Monster) {
			attack(location);
		}
		else if(gui.getGrid().objectAt(location) instanceof Trap) {
//			System.out.println("You stepped on a trap!");
			damage();
			if (dead == false) {
				grid.remove(location);
				super.changeLocation(location);
				gui.showGrid();
			}
		}
		else if(gui.getGrid().objectAt(location) instanceof Food) {
//			System.out.println("You ate some food! Yum!");
			health += 1;
//			System.out.println("You healed 1 HP! You're at: " + health);
			grid.remove(location);
			super.changeLocation(location);
			gui.showGrid();
		}
		else if(gui.getGrid().objectAt(location) instanceof Goal) {
//			System.out.println("You win!");
			grid.remove(location);
			super.changeLocation(location);
			goalReached = true;
			gui.showGrid();
		}
	}
	
	public void Move(Location location) {
		
		Random rando = new Random();
		boolean validDirection = false;
		List<Location> neighbors = this.allNeighbors(location);
		Location neighbor;
		while (!validDirection) {
			neighbor = neighbors.get(rando.nextInt(neighbors.size()));
			if (gui.getGrid().isEmpty(neighbor) && gui.getGrid().isValid(neighbor)) {
				super.changeLocation(neighbor);
				gui.showGrid();
				validDirection = true;
			}
			else if(gui.getGrid().objectAt(neighbor) instanceof Monster) {
				attack(neighbor);
				validDirection = true;
			}
			else if(gui.getGrid().objectAt(neighbor) instanceof Trap) {
//				System.out.println("You stepped on a trap!");
				damage();
				if (dead == false) {
					grid.remove(neighbor);
					super.changeLocation(neighbor);
					gui.showGrid();
				}
				validDirection = true;
			}
			else if(gui.getGrid().objectAt(neighbor) instanceof Food) {
//				System.out.println("You ate some food! Yum!");
				health += 1;
//				System.out.println("You healed 1 HP! You're at: " + health);
				grid.remove(neighbor);
				super.changeLocation(neighbor);
				gui.showGrid();
				validDirection = true;
			}
			else if(gui.getGrid().objectAt(neighbor) instanceof Goal) {
//				System.out.println("You win!");
				grid.remove(neighbor);
				super.changeLocation(neighbor);
				gui.showGrid();
				validDirection = true;
				dead = true;
			}
			
		}
		
//		if(gui.getGrid().isValid(location)) {
//			if (gui.getGrid().isEmpty(location)) {
//			super.changeLocation(location);
//			gui.showGrid();
//			}
//			else if(gui.getGrid().objectAt(location) instanceof Monster) {
//				attack(location);
//			}
//		}
	}
	
	public boolean getIsDead() {
		return dead;
	}
	
	public void damage() {
		this.health -= 1;
//		System.out.println("You took damage!\nYour health is at: " + health);
		if (this.health == 0)
			death();
	}
	
	public void attack(Location location) {
		grid.remove(location);
		damage();
		if (dead == false) {
//			System.out.println("You killed the monster!");
			super.changeLocation(location);
			gui.showGrid();
		}
	}
	
	public void death() {
		this.removeFromGrid();
//		System.out.println("You are dead.");
		dead = true;
	}

}
