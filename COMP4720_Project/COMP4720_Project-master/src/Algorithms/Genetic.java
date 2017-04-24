package Algorithms;

import Blocks.*;
import Blocks.Character;
import edu.kzoo.grid.*;
import edu.kzoo.grid.gui.*;

import java.util.*;

import Algorithms.SimulatedAnnealing.UnitType;

public class Genetic {
    private GridAppFrame gui;
    private Random random;
    
    private Character character;
    private Goal goal;
    private ArrayList<String> population;
    
    public Genetic(GridAppFrame gui) {
        this.gui = gui;
        random = new Random(2);
        population = new ArrayList<String>();
    }
    
    private boolean isValid(Location location) {
    	if(gui.getGrid().isValid(location) && !(gui.getGrid().objectAt(location) instanceof Wall))
    		return true;
    	return false;
    }
    
    private String Crossover(String x, String y) {
    	int bitstring_size = x.length();
    	int crossover_point = random.nextInt(bitstring_size);
    	return x.substring(0, crossover_point) + y.substring(crossover_point);
    }
    
    // Returns the euclidean distance between the cell and the target.
    private float Cost(Location current, Location target) {
    	return (float)Math.sqrt(
    		Math.abs((target.row() - current.row()) * (target.row() - current.row())) + 
    		Math.abs((target.col() - current.col()) * (target.col() - current.col())));
    }
    
    private float MaximumCost(Location current, Location target) {
    	return Cost(current, target) + (0.50f * Cost(current, target));
    }	
    
    private Location SingleMoveLocation(String move, Location new_location) {
    	Location location = new_location;
    	switch (move) {
        case "00":
           	if(isValid(new Location(location.row() - 1, location.col())))
            	location = new Location(location.row() - 1, location.col());
            break;
        case "01":
            if(isValid(new Location(location.row() + 1, location.col())))
            	location = new Location(location.row() + 1, location.col());
            break;
        case "10":
        	if(isValid(new Location(location.row(), location.col() - 1)))
           		location = new Location(location.row(), location.col() - 1);
            break;
        case "11":
            if(isValid(location = new Location(location.row(), location.col() + 1)))
                location = new Location(location.row(), location.col() + 1);
            break;
    	}
    	
    	return location;
    }
    
    private Location TotalMoveLocation(String chromosome) {
    	Location initial = character.location();
    	    	
    	String current = "";
    	String overall = chromosome;
    	for(int i = 0; i < Math.floor(MaximumCost(character.location(), goal.location())); i++) {
    		current = overall.substring(0, 2);
    		overall = overall.substring(2);
    		initial = SingleMoveLocation(current, initial);
    	}
    	
    	return initial;
    }

    private float Fitness(String chromosome) {
    	// Set an accepted distance of about 1.5 times the original.
    	// This gives us some leeway in the population. 
    	float cost = Cost(character.location(), goal.location());
    	float max_cost = cost + (0.5f * cost);
 
    	return -Cost(TotalMoveLocation(chromosome), goal.location()) - (max_cost / 100.0f);
    }
    
    private Location generateLocation(Grid grid) {
    	int row = random.nextInt(grid.numRows() + 1);
    	int col = random.nextInt(grid.numCols() + 1);
    	return new Location(row, col);
    }
    
    private String Mutate(String chromosome) {
    	int mutate_pos = random.nextInt(chromosome.length());
    	int mutate_val = random.nextInt(2);
    	return chromosome.substring(0, mutate_pos) + mutate_val + chromosome.substring(mutate_pos + 1);
    }
    
    // Performs random selection.
    private String Selection(ArrayList<String> population) {
    	return population.get(random.nextInt(population.size()));
    }
    
    private void Populate(Location initial, Location target) {
    	population.clear();
    	
    	// Set an accepted distance of about 1.5 times the original.
    	// This gives us some leeway in the population. 
    	float cost = Cost(initial, target);
    	float max_cost = cost + (0.5f * cost);
    	
    	// up down left right in bitstring notation.
    	String[] moves = {"00", "01", "10", "11"};
    	
    	for(int i = 0; i < Byte.MAX_VALUE; i++) {
    		String chromosome = "";
	    	for(int j = 0; j < Math.floor(max_cost); j++) {
	    		chromosome += moves[random.nextInt(3)];
	    	}
//	    	System.out.println(chromosome);
	    	population.add(chromosome);	    	
    	}
    }
    
    private enum UnitType {
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
	    	for(int i = 0; i < 50; i++)
	    		SpawnUnit(grid, UnitType.WALL);
	    	for(int i = 0; i < 10; i++)
	    		SpawnUnit(grid, UnitType.MONSTER);
	    	for(int i = 0; i < 10; i++)
	    		SpawnUnit(grid, UnitType.TRAP);
	    	for(int i = 0; i < 6; i++)	
	    		SpawnUnit(grid, UnitType.FOOD);
	    	gui.showGrid();
	    long start = System.currentTimeMillis();
	    while(!character.getGoalReached()) {
    		if (System.currentTimeMillis() - start > 5000 || character.getIsDead())
    			return false;
	    	// Initialize our population.
	    	Populate(character.location(), goal.location());
			
	    	for(int i = 0; i < Byte.MAX_VALUE; i++) {
				ArrayList<String> new_population = new ArrayList<String>();
				for(int j = 0; j < population.size(); j++) {	
//					System.out.println(i + " : " +j );
					String x = Selection(population);
					String y = Selection(population);
					String child = Crossover(x, y);
					if(random.nextFloat() < 0.1f)
						child = Mutate(child);
					new_population.add(child);
				}
				population = new_population;
	    	}
	    	
	    	// Find fittest of the population.
	    	String fittest = "";
	    	for(int i = 0; i < population.size(); i++) {
	    		if(i == 0) 
	    			fittest = population.get(0);
	    		else if(Fitness(population.get(i)) > Fitness(fittest))
	    			fittest = population.get(i);
	    	}
	    	character.genMove(TotalMoveLocation(fittest));
	    	if(!character.getIsDead()) {
				for(Location location : character.GetNeighbors()) {
					// Check to see if stack is empty due to empty stack exceptions.
					if(!character.getGoalReached()) {
						if(goal.location().equals(location)) {
							character.genMove(location);
						}
					}
				}
	    	}
    	}
		return true;
	}
}