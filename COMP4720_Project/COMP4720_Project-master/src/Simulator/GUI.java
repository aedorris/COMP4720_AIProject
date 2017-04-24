package Simulator;
import java.awt.Color;

import edu.kzoo.grid.Grid;
import edu.kzoo.grid.gui.EnabledDisabledStates;
import edu.kzoo.grid.gui.GridAppFrame;
import edu.kzoo.grid.gui.ThreadedControlButton;
import edu.kzoo.grid.gui.nuggets.NewBoundedGridButton;

public class GUI extends GridAppFrame {
    // Specify attributes of the Start button.
    private static final boolean DISPLAY_AFTER_START = true;

    // Instance variable(s) for each GUI instance.
    private Controller controller;

    /** Constructs a new GUI object. **/
    public GUI() {
        // Create object that knows how to run the animation.
        controller = new Controller(this);

        // Include our various components.
        includeControlComponent(new NewBoundedGridButton(this, "New Grid"),
                                EnabledDisabledStates.NEEDS_APP_WAITING);
        
        includeControlComponent(new ThreadedControlButton(this, "Start NoAlgorithm", DISPLAY_AFTER_START)
        { public void act() { controller.ExecuteLogic(); }},
    EnabledDisabledStates.NEEDS_GRID_AND_APP_WAITING);
        
        includeControlComponent(
                new ThreadedControlButton(this, "Start Simulated Annealing", DISPLAY_AFTER_START)
                    { public void act() { controller.SimulatedAnnealing(); }},
                EnabledDisabledStates.NEEDS_GRID_AND_APP_WAITING);
        
        includeControlComponent(
                new ThreadedControlButton(this, "Start Genetic", DISPLAY_AFTER_START)
                    { public void act() { controller.Genetic(); }},
                EnabledDisabledStates.NEEDS_GRID_AND_APP_WAITING);

        includeSpeedSlider();

        // Construct the visible window with the specified components.
        constructWindowContents("AI Project", Color.WHITE, 400, 400, 10);
    }
}
