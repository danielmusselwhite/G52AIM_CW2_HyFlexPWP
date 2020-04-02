package com.aim.project.pwp.heuristics;


import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


/**
 * 
 * @author Warren G. Jackson
 * Performs adjacent swap, returning the first solution with strict improvement
 *
 */
public class NextDescent extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public NextDescent(Random oRandom) {
	
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
		
		// initialising solution, it starts with the bestEval
		double bestEval = oSolution.getObjectiveFunctionValue();
		
		int size = oSolution.getNumberOfLocations()-2; //we can't modify the home nor the depot so minus 2 from total
		
		//random starting point and calculating the finish point based on that
		int randomStart = oRandom.nextInt(size);
		
		int acceptedSolutionCounter = 0;
		
		//c = delta evaluation cost (initialised to current value)
		double c = oSolution.getObjectiveFunctionValue();
		
		// for each value in the solution..
		for(int i=randomStart; i<size; i++) {
			
			//stop searching when we have accepted enough solutions to satisfy our depth of search
			if(acceptedSolutionCounter>=acceptedSolutionLimit)
				break;
			
			//apply perturbation operator and store its cost
			c=applyPerturbationOperator(oSolution, i,c);
			
			// if the cost of doing this flip is greater than or equal to the currentBestCost, flip the bit back
			if(c>bestEval)
				c=applyPerturbationOperator(oSolution, i,c);
			
			// else the cost was strictly improving, accept it
			else 
				acceptedSolutionCounter++;	
		}
		

		// for each value in the solution..
		for(int i=0; i<randomStart; i++) {
			
			//stop searching when we have accepted enough solutions to satisfy our depth of search
			if(acceptedSolutionCounter>=acceptedSolutionLimit)
				break;
			
			//apply perturbation operator and store its cost
			c=applyPerturbationOperator(oSolution, i,c);
			
			// if the cost of doing this flip is greater than or equal to the currentBestCost, flip the bit back
			if(c>bestEval)
				c=applyPerturbationOperator(oSolution, i,c);
			
			// else the cost was strictly improving, accept it
			else 
				acceptedSolutionCounter++;	
		}
		
		oSolution.setObjectiveFunctionValue(c);
		
		return c;
		
			
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
	
	//Perturbation operator used in hill climbing algorithms: adjacent swap
	protected double applyPerturbationOperator(PWPSolutionInterface solution, int index, double c) {

		int[] newSolution = solution.getSolutionRepresentation().getSolutionRepresentation();
		int size = newSolution.length;
		
		//delta evaluation subtracting old values
		c-=this.getDifferenceDeltaEvaluationAdjacentSwap(newSolution, size, index, (index+1)%size);
		
		newSolution = swapPoints(newSolution, index, (index+1)%size);	// swapping the two adjacent points
		
		solution.getSolutionRepresentation().setSolutionRepresentation(newSolution);  // move to the new solution (error checking will be done in the class that uses this by comparing the returned cost with the previous cost)
		
		c+=this.getDifferenceDeltaEvaluationAdjacentSwap(newSolution, size, index, (index+1)%size);
		
		// returning the cost of the new solution
		return c;
	}
}
