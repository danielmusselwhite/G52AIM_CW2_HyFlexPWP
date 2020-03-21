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
		
		// candidateSolution so we don't modify the main
		PWPSolutionInterface newSolution = oSolution.clone();
		PWPSolutionInterface candidateSolution = oSolution.clone();

		int size = oSolution.getNumberOfLocations()-2; //we can't modify the home nor the depot so -2
		
		// initialising solution, it starts with the bestEval
		double bestEval = newSolution.getObjectiveFunctionValue();
		
		// create a randomised permutation for the order of the indexes to be checked in
		int[] indexPermutation = Utilities.shuffleArray(Utilities.getArrayOfBitIndexes(size), oRandom);
		
		int acceptedSolutionCounter = 0;
		
		// for each value in the solution..
		for(int i=0; i<size; i++) {
			
			//stop searching when we have accepted enough solutions to satisfy our depth of search
			if(acceptedSolutionCounter>=acceptedSolutionLimit)
				break;
			
			// move to the neighbouring solution (by means of adjacent swap with index given by perm at i) then check if this provides a shorter root
			if(applyPerturbationOperator(candidateSolution, indexPermutation[i])<=bestEval) {
				//if it does, accept this solution (clone it to the oSolution)
				newSolution = candidateSolution.clone();
				acceptedSolutionCounter++;
			}
			//else if it doesn't, deny the solution putting it back to the old solution then continuing
			else {
				candidateSolution = newSolution.clone();
			}
			
		}
		
		// updating to the new solution
		oSolution.getSolutionRepresentation().setSolutionRepresentation(newSolution.getSolutionRepresentation().getSolutionRepresentation());
		
		// returning the cost of the new solution
		return oSolution.getObjectiveFunctionValue();
		
		
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
		return true;
	}
	
	//adjacent swap
	private double applyPerturbationOperator(PWPSolutionInterface candidateSolution, int index) {

		int[] newSolution = candidateSolution.getSolutionRepresentation().getSolutionRepresentation();
		int size = newSolution.length;
		
		newSolution = swapPoints(newSolution, index, (index+1)%size);	// swapping the two adjacent points
		
		
		// updating to the new solution
		candidateSolution.getSolutionRepresentation().setSolutionRepresentation(newSolution);
		
		// returning the cost of the new solution
		return candidateSolution.getObjectiveFunctionValue();
	}
}
