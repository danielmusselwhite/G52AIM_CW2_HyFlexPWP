package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.utilities.Utilities;


/**
 * 
 * @author Warren G. Jackson
 * Performs adjacent swap, returning the first solution with strict improvement
 *
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public DavissHillClimbing(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {

		
		// rule is acceptedSolutionLimit = 2(n+1) where n = number of 0.2's there are in dDepthOfSearch
		//by dividing DOS by 0.2 you have n
		//int acceptedSolutionLimit = (int) (2*((dDepthOfSearch)/0.2d)+1);
		
		int acceptedSolutionLimit=0;
		
		if(dDepthOfSearch >= 0 && dDepthOfSearch<0.2)
			acceptedSolutionLimit = 1;
		else if(dDepthOfSearch >= 0.2 && dDepthOfSearch <0.4)
			acceptedSolutionLimit = 2;
		else if(dDepthOfSearch >= 0.4 && dDepthOfSearch<0.6)
			acceptedSolutionLimit = 3;
		else if(dDepthOfSearch >= 0.6 && dDepthOfSearch<0.8)
			acceptedSolutionLimit = 4;
		else if(dDepthOfSearch >=0.8 && dDepthOfSearch<1)
			acceptedSolutionLimit = 5;
		else if(dDepthOfSearch == 1)
			acceptedSolutionLimit = 6;
		
		// bestEval will be the original eval at the start
		double bestEval = oSolution.getObjectiveFunctionValue();
		
		int size = oSolution.getNumberOfLocations()-2; //number of locations minus the home and depot
		
		// create a randomised permutation for the order of the indexes to be checked in
		int[] indexPermutation = Utilities.shuffleArray(Utilities.getArrayOfBitIndexes(size), oRandom);
		
		// used to only accept DOS moves
		int acceptedSolutionCounter = 0;
		
		// for each value in the solution..
		for(int i=0; i<size; i++) {
			//stop searching when we have accepted enough solutions to satisfy our depth of search
			if(acceptedSolutionCounter>=acceptedSolutionLimit)
				break;
			
			
			// if the cost of doing this flip is greater than the currentBestCost, flip the bit back
			if(applyPerturbationOperator(oSolution, indexPermutation[i])>bestEval)
				applyPerturbationOperator(oSolution, indexPermutation[i]);
			
			// else the cost was improving or equal to, accept it
			else 
				acceptedSolutionCounter++;
			
		}	
		
		
//		System.out.println("Davis");
//		for(int i=0; i<oSolution.getSolutionRepresentation().getSolutionRepresentation().length; i++) {
//			System.out.print(oSolution.getSolutionRepresentation().getSolutionRepresentation()[i]+"-");
//		}
//		System.out.println();
		
		oSolution.setObjectiveFunctionValue(this.getSolutionCost(oSolution.getSolutionRepresentation()));
		
		// returning the cost of the new solution
		return this.getSolutionCost(oSolution.getSolutionRepresentation());
		
		
	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return true;
	}
	
	//adjacent swap
	private double applyPerturbationOperator(PWPSolutionInterface solution, int index) {

		int[] newSolution = solution.getSolutionRepresentation().getSolutionRepresentation();
		int size = newSolution.length;
		
		newSolution = swapPoints(newSolution, index, (index+1)%size);	// swapping the two adjacent points
		
		solution.getSolutionRepresentation().setSolutionRepresentation(newSolution);  // move to the new solution (error checking will be done in the class that uses this by comparing the returned cost with the previous cost)
		
		solution.setObjectiveFunctionValue(this.getSolutionCost(solution.getSolutionRepresentation()));
		
		// returning the cost of the new solution
		return this.getSolutionCost(solution.getSolutionRepresentation());
	}
}
