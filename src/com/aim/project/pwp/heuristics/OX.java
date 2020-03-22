package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import com.aim.project.utilities.Utilities;


public class OX implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public OX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
		
		// what to do here?
			
			
	}

	
	// parent index 1, parent index 2, child index
	@Override
	public double apply(PWPSolutionInterface p1, PWPSolutionInterface p2,
			PWPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		

		// rule is numberOfIterations = 2(n+1) where n = number of 0.2's there are in intensityOfMutation
		//by dividing IOM by 0.2 you have n
		//int numberOfIterations = (int) (2*((dIntensityOfMutation)/0.2d)+1);
		
		int numberOfIterations=0;
		
		if(intensityOfMutation >= 0 && intensityOfMutation<0.2)
			numberOfIterations = 1;
		else if(intensityOfMutation >= 0.2 && intensityOfMutation <0.4)
			numberOfIterations = 2;
		else if(intensityOfMutation >= 0.4 && intensityOfMutation<0.6)
			numberOfIterations = 3;
		else if(intensityOfMutation >= 0.6 && intensityOfMutation<0.8)
			numberOfIterations = 4;
		else if(intensityOfMutation >=0.8 && intensityOfMutation<1)
			numberOfIterations = 5;
		else if(intensityOfMutation == 1)
			numberOfIterations = 6;
		
		int size = p1.getNumberOfLocations()-2; //we can't modify the home nor the depot so minus 2 from total

		int cutPoint1;
		int cutPoint2;
		
		// pick two random cut points while both the points are the first and the last element as nothing will change or whilst they are the same element
		do {
			int point1 = oRandom.nextInt(size);
			int point2 = oRandom.nextInt();
			
			// setting it so cutPoint1 is the smaller and cutPoint2 is the bigger
			cutPoint1 = point1<point2 ? point1 : point2;
			cutPoint2 = point2>point1 ? point2 : point1;
		} while((cutPoint1 == 0 && cutPoint2 == size-1) || cutPoint1 == cutPoint2);
		
		
		
		// perform OX maintaining the order
		
		int[] childSolutionRepresentation = new int[c.getSolutionRepresentation().getSolutionRepresentation().length];
		int[] parent1SolutionRepresentation = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] parent2SolutionRepresentation = Utilities.shiftArray(p2.getSolutionRepresentation().getSolutionRepresentation(), cutPoint2); //shifting it by cutPoint2 so it is read in that order
		
		// Copy parent 1 middle into child
		for(int i=cutPoint1; i<cutPoint2; i++) {
			childSolutionRepresentation[i] = parent1SolutionRepresentation[i];
		}
		
		//get symbols from parent 2 starting from second cut point
		int[] parent2Symbols = new int[cutPoint2 - cutPoint1];
		
		// for each symbol from parent 2
		for(int i=0; i<parent2Symbols.length ;i++) {
			//if the child doesn't contain this value already (its not from parent 1 middle)
			if(!Utilities.arrayContainsValue(childSolutionRepresentation, parent2Symbols[i]))
				parent2Symbols[i] = parent2SolutionRepresentation[i]; //adding this symbol to the parent2Symbols
				
		}
		
		// for each in the parent 2 symbols, wrap them around the child in order from the second cutpoint
		for(int i=0; i<parent2Symbols.length; i++) {
			childSolutionRepresentation[(i+cutPoint2)%size] = parent2Symbols[i];
		}
		
		//setting the child to the new solution representation
		c.getSolutionRepresentation().setSolutionRepresentation(childSolutionRepresentation);  // move to the new solution
		
		return c.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}


	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.oObjectiveFunction = f;
	}
}
