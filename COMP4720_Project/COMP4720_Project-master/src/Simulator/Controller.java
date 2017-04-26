package Simulator;
import java.awt.Color;
import java.util.Random;

import Blocks.Monster;
import Blocks.Trap;
import Blocks.Wall;
import Blocks.Character;
import Blocks.Food;
import Blocks.Goal;

import Algorithms.SimulatedAnnealing;
import Algorithms.Genetic;
import edu.kzoo.grid.ColorBlock;
import edu.kzoo.grid.Grid;
import edu.kzoo.grid.GridObject;
import edu.kzoo.grid.Location;
import edu.kzoo.grid.gui.GridAppFrame;

public class Controller {
	 // Instance variable(s) for each Controller instance.
    private GridAppFrame gui;
    private Random random;
    Character character;
    
    /** Constructs a new Controller object. **/
    public Controller(GridAppFrame gui) {
        this.gui = gui;
        this.random = new Random(1);
        
    }
    
    public void SimulatedAnnealing() {
    	SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(gui);
    	
    	long start = System.currentTimeMillis();
    	System.out.println("Start: " + start + "; ");
    	int numRight = 0;
    	for (int i = 0; i < 50; i++) {
    		if(simulatedAnnealing.Simulate())
    			numRight++;
    	}
    	System.out.println("End time: " + ((System.currentTimeMillis() - start) / 1000));
    	System.out.println("Number right: " + numRight);
    }
    
    public void Genetic() {
    	Genetic genetic = new Genetic(gui);
    	
    	long start = System.currentTimeMillis();
    	System.out.println("Start: " + start + "; ");
    	int numRight = 0;
    	for (int i = 0; i < 50; i++) {
    		if(genetic.Simulate())
    			numRight++;
    	}
    	System.out.println("End time: " + ((System.currentTimeMillis() - start) / 1000));
    	System.out.println("Number right: " + numRight);
    }
    
    public enum UnitType {
    	CHARACTER,
    	MONSTER,
    	WALL,
    	TRAP,
    	GOAL,
    	FOOD,
    }
    
    private Location generateLocation(Grid grid) {
    	int row = random.nextInt(grid.numRows() + 1);
    	int col = random.nextInt(grid.numCols() + 1);
    	return new Location(row, col);
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
    	case GOAL: grid.add(new Goal(), location); break;
    	case FOOD: grid.add(new Food(), location); break;
    	default: grid.add(new Wall(), location);	
    	}
    }
    

    public void ExecuteLogic() {
    	Grid grid = gui.getGrid();
    	grid.removeAll();
        character = new Character(gui, grid);
        SpawnUnit(grid, UnitType.CHARACTER);
        
    	for(int i = 0; i < 35; i++)
    		SpawnUnit(grid, UnitType.WALL);
    	SpawnUnit(grid, UnitType.GOAL);
    	for(int i = 0; i < 5; i++)
    		SpawnUnit(grid, UnitType.MONSTER);
    	for(int i = 0; i < 5; i++)
    		SpawnUnit(grid, UnitType.TRAP);
    	for(int i = 0; i < 3; i++)
    		SpawnUnit(grid, UnitType.FOOD);
    	
        while(!character.getIsDead()){
	        System.out.println(character.location());
	    	character.Move(character.location());
        }


    	
    }
}
