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
		return 0d;
			
	}

	
	// parent index 1, parent index 2, child index
	@Override
	public double apply(PWPSolutionInterface p1, PWPSolutionInterface p2,
			PWPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
	
		// rule is iterations = 2(n+1) where n = number of 0.2's there are in IOM
		//by dividing IOM by 0.2 you have n
		//int iterations = (int) (2*((intensityOfMutation)/0.2d)+1);
		int iterations=0;
		
		if(intensityOfMutation >= 0 && intensityOfMutation<0.2)
			iterations = 1;
		else if(intensityOfMutation >= 0.2 && intensityOfMutation <0.4)
			iterations = 2;
		else if(intensityOfMutation >= 0.4 && intensityOfMutation<0.6)
			iterations = 3;
		else if(intensityOfMutation >= 0.6 && intensityOfMutation<0.8)
			iterations = 4;
		else if(intensityOfMutation >=0.8 && intensityOfMutation<1)
			iterations = 5;
		else if(intensityOfMutation == 1)
			iterations = 6;
		
		int size = p1.getNumberOfLocations()-2; //we can't modify the home nor the depot so minus 2 from total
		int cutPoint1;
		int cutPoint2;
		
		int[] child1SolutionRepresentation = new int[size];
		int[] child2SolutionRepresentation = new int[size];
		int[] parent1SolutionRepresentation = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] parent2SolutionRepresentation = p2.getSolutionRepresentation().getSolutionRepresentation();
		
		for(int j=0; j<iterations; j++) {
			
			
			// pick two random cut points while both the points are the first and the last element as nothing will change or whilst they are the same element
			do {
				int point1 = oRandom.nextInt(size);
				int point2 = oRandom.nextInt();
				
				// setting it so cutPoint1 is the smaller and cutPoint2 is the bigger
				cutPoint1 = point1<point2 ? point1 : point2;
				cutPoint2 = point2>point1 ? point2 : point1;
			} while((cutPoint1 == 0 && cutPoint2 == size-1) || cutPoint1 == cutPoint2);
			
			
			
			// perform OX maintaining the order
			
			child1SolutionRepresentation = new int[size];
			child2SolutionRepresentation = new int[size];
			parent2SolutionRepresentation = Utilities.shiftArray(parent2SolutionRepresentation, cutPoint2); //shifting it by cutPoint2 so it is read in that order
			
			
			//GENERATING CHILD 1
			
			// Copy parent 1 middle into child
			for(int i=cutPoint1; i<cutPoint2; i++) {
				child1SolutionRepresentation[i] = parent1SolutionRepresentation[i];
			}
			
			//get symbols from parent 2 starting from second cut point
			int[] parent2Symbols = new int[cutPoint2 - cutPoint1];
			
			// for each symbol from parent 2
			for(int i=0; i<parent2Symbols.length ;i++) {
				//if the child doesn't contain this value already (its not from parent 1 middle)
				if(!Utilities.arrayContainsValue(child1SolutionRepresentation, parent2Symbols[i]))
					parent2Symbols[i] = parent2SolutionRepresentation[i]; //adding this symbol to the parent2Symbols
					
			}
			
			// for each in the parent 2 symbols, wrap them around the child in order from the second cutpoint
			for(int i=0; i<parent2Symbols.length; i++) {
				child1SolutionRepresentation[(i+cutPoint2)%size] = parent2Symbols[i];
			}
			
			
			
			
			//GERNEATING CHILD 2
			// Copy parent 2 middle into child
			for(int i=cutPoint1; i<cutPoint2; i++) {
				child2SolutionRepresentation[i] = parent2SolutionRepresentation[i];
			}
			
			//get symbols from parent 1 starting from second cut point
			int[] parent1Symbols = new int[cutPoint2 - cutPoint1];
			
			// for each symbol from parent 1
			for(int i=0; i<parent1Symbols.length ;i++) {
				//if the child doesn't contain this value already (its not from parent 2 middle)
				if(!Utilities.arrayContainsValue(child2SolutionRepresentation, parent1Symbols[i]))
					parent1Symbols[i] = parent1SolutionRepresentation[i]; //adding this symbol to the parent1Symbols
					
			}
			
			// for each in the parent 2 symbols, wrap them around the child in order from the second cutpoint
			for(int i=0; i<parent1Symbols.length; i++) {
				child2SolutionRepresentation[(i+cutPoint2)%size] = parent1Symbols[i];
			}
			
			//setting the parents for the next iteration to be the children
			parent1SolutionRepresentation = child1SolutionRepresentation;
			parent2SolutionRepresentation = child2SolutionRepresentation;
		}
		
		//setting the child to be randomly 1 of the 2 generated children (50/50 chance of being either)
		if(oRandom.nextDouble()>0.5)
			c.getSolutionRepresentation().setSolutionRepresentation(child1SolutionRepresentation);  // move to the new solution
		else
			c.getSolutionRepresentation().setSolutionRepresentation(child2SolutionRepresentation);  // move to the new solution	
	
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
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.oObjectiveFunction = f;
	}
}
