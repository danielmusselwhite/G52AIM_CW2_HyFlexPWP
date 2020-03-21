package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.utilities.Utilities;


public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public Reinsertion(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		// rule is numberOfIterations = 2(n+1) where n = number of 0.2's there are in intensityOfMutation
		//by dividing IOM by 0.2 you have n
		//int numberOfIterations = (int) (2*((intensityOfMutation)/0.2d)+1);
		
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
		
		int[] newSolution = solution.getSolutionRepresentation().getSolutionRepresentation();
		int size = newSolution.length;
		
		for(int i=0; i<numberOfIterations; i++) {

			// pick 2 different random points
			int index1 = oRandom.nextInt(size);	
			int index2;
			do {
				index2 = oRandom.nextInt(size);
			} while(index1==index2);
			
			//removing point at index 1 and putting it into index2, maintaining order of the list
			newSolution = removeAndInsertPoint(newSolution, index1, index2); 
			
			
		}
		
		
		// updating to the new solution
		solution.getSolutionRepresentation().setSolutionRepresentation(newSolution);
		
		// returning the cost of the new solution
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}

}
