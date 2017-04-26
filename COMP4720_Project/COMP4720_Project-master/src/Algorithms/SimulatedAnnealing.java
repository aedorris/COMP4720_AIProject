package Algorithms;

import Blocks.*;
import Blocks.Character;
import Simulator.Controller.UnitType;
import edu.kzoo.grid.*;
import edu.kzoo.grid.gui.*;

import java.awt.*;
import java.math.*;
import java.util.*;

public class SimulatedAnnealing {
	private GridAppFrame gui;
	private Random random;
	
	private Character character;
	private Goal goal;

	public SimulatedAnnealing(GridAppFrame gui) {
		this.gui = gui;
		random = new Random(2);
	}
	
	private Location generateLocation(Grid grid) {
    	int row = random.nextInt(grid.numRows() + 1);
    	int col = random.nextInt(grid.numCols() + 1);
    	return new Location(row, col);
    }
    
    // Returns the euclidean distance between the cell and the target.
    private float Cost(Location current, Location target) {
    	System.out.println((float)(
    		Math.abs((target.row() - current.row()) * (target.row() - current.row())) + 
    		Math.abs((target.col() - current.col()) * (target.col() - current.col()))));
    	return (float)(
    		Math.abs((target.row() - current.row()) * (target.row() - current.row())) + 
    		Math.abs((target.col() - current.col()) * (target.col() - current.col())));
    }
        
    // Returns a random neighbor at the specified location.
    private Location Neighbor(Grid grid, Location location) {
    	Location neighbor = new Location(0, 0);
    	do {
    		neighbor = grid.getNeighbor(location, Direction.randomDirection());
    	} while(!grid.isValid(neighbor) && !grid.isEmpty(neighbor));
    	return neighbor;
    }
    
    // Returns the acceptance probability for the new location.
    private double Probability(float current_cost, float next_cost, float temp) {
    	return Math.exp((current_cost - next_cost) / temp);
    }
	
    private float Temperature(int k, int k_max) {
    	if(character.location().equals(goal.location()))
    		return 0.0f;
    	else
    		return (float)k / k_max;
    }
    
    public enum UnitType {
    	CHARACTER,
    	MONSTER,
    	WALL,
    	TRAP,
    	GOAL,
    	FOOD,
    }
    
    private void SpawnUnit(Grid grid, UnitType unit) {
    	Location location;
    	do {
    		location = generateLocation(grid);
    	} while(!grid.isEmpty(location));
    	
    	switch(unit) {
    	case CHARACTER: grid.add(character, location); break;
    	case MONSTER: grid.add(new Monster(), location); break;
    	case TRAP: grid.add(new Trap(), location); break;
    	case GOAL: grid.add(goal, location); break;
    	case FOOD: grid.add(new Food(), location); break;
    	default: grid.add(new Wall(), location);	
    	}
    }
    
	public boolean Simulate() {
		Grid grid = gui.getGrid();
    	grid.removeAll();
    	
    	character = new Character(gui, grid);
    	goal = new Goal();
    	
    	SpawnUnit(grid, UnitType.CHARACTER);
    	SpawnUnit(grid, UnitType.GOAL);
        
    	for(int i = 0; i < 40; i++)
    		SpawnUnit(grid, UnitType.WALL);
    	for(int i = 0; i < 10; i++)
    		SpawnUnit(grid, UnitType.MONSTER);
    	for(int i = 0; i < 10; i++)
    		SpawnUnit(grid, UnitType.TRAP);
    	for(int i = 0; i < 6; i++)
    		SpawnUnit(grid, UnitType.FOOD);
        
    	gui.showGrid();
    	int k = 1;
    	int k_max = Short.MAX_VALUE;
    	
    	while (k < k_max) {
    		long start = System.currentTimeMillis();
        	if (System.currentTimeMillis() - start > 5000 || character.getIsDead() == true) {
        		return false;
        	}
    		// Getting temperature value and ensuring t != 0 
    		// as it would suggest the solution has been found already.
    		float t = Temperature(k, k_max);
    		if(t == 0.0f)
    			return true;
    		
    		// Get the random neighbor to the location.
    		Location next = Neighbor(grid, character.location());
    		
    		// Calculate the current cost and the suggested random locations cost.
    		float current_cost = Cost(character.location(), goal.location());
    		float next_cost = Cost(next, goal.location());    		
    		
    		// Pass cost into accepted probability function and compare against a random number.
    		if(Probability(current_cost, next_cost, t) > Math.random()) {
    			// Do not consider move if we are next to a neighboring cell.
    			boolean consider = true;
    			for(Location location : character.GetNeighbors()) {
    					if(goal.location().equals(location)) {
    						consider = false;
    						character.simMove(location);
    						return true;
    					
    				}
    			}
    			// If the goal is not adjacent, move to the next location.
    			if(consider) {
    				character.simMove(next);
    			}
    		}
//    		System.out.println(k);
    		k++;
    	}
    	
    	return false;
	}
	}