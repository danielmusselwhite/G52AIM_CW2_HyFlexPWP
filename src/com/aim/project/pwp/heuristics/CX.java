package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import com.aim.project.utilities.Utilities;


public class CX implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public CX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		return 0d;
	}

	@Override
	public double apply(PWPSolutionInterface p1, PWPSolutionInterface p2,
			PWPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		
		int[] parent1Solution = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] parent2Solution = p2.getSolutionRepresentation().getSolutionRepresentation();
		

		
		// defaulting the child solution to have all the values from parent 2
		int[] childSolution = parent2Solution;
		
		//picking a random index to be the first one for us to explore
		int nextIndex = oRandom.nextInt(p1.getSolutionRepresentation().getSolutionRepresentation().length);
		//setting up the cycle the start point for the cycle
		ArrayList<Integer> cycle = new ArrayList<Integer>();
		
		// Finding the cycle and adding it to the child
		//whilst the cycle doesn't contain the next element..
		while(!cycle.contains(parent1Solution[nextIndex])) { 
			// add this element to the child at its index
			childSolution[nextIndex] = parent1Solution[nextIndex];
			// add this element to the cycle
			cycle.add(parent1Solution[nextIndex]);
			
			//getting where the element at this index is in parent two, in parent one.
			nextIndex = Utilities.arrayGetIndexOf(parent1Solution, parent2Solution[nextIndex]);

		}
		
		//setting the child to the new solution representation
		c.getSolutionRepresentation().setSolutionRepresentation(childSolution);  // move to the new solution
		
		return c.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}


	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {
		
		this.oObjectiveFunction = oObjectiveFunction;
	}
}
