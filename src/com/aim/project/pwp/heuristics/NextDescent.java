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
				
		// bestEval will be the original eval at the start
		double bestEval = oSolution.getObjectiveFunctionValue();
		
		int[] newSolution = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int size = newSolution.length;
		
		//random starting point and calculating the finish point based on that
		int randomStart = oRandom.nextInt(size);
		
		// used to only accept DOS moves
		int acceptedSolutionCounter = 0;
		
		//c = delta evaluation cost (initialised to current value)
		double c = oSolution.getObjectiveFunctionValue();
		
		// for each value in the solution..
		for(int i1=randomStart; i1<size; i1++) {
			//stop searching when we have accepted enough solutions to satisfy our depth of search
			if(acceptedSolutionCounter>=acceptedSolutionLimit)
				break;
			
			int i2 = (i1+1)%size;
			
			//delta evaluation calculating what the cost difference will be
			double tempC=this.deltaEvaluation(newSolution, size, i1, i2, c);
			
			//if the cost will be an improvement or equal, actually flip it
			if(tempC<=bestEval) {
				acceptedSolutionCounter++;
				c=tempC;
				bestEval=c;
				swapPoints(newSolution, i1, i2);
				
//				if(c == getObjectiveFunction().getObjectiveFunctionValue(oSolution.getSolutionRepresentation()))
//				System.out.println("GOOD DELTA AND ACTUAL ARE BOTH " + c);
//					
//				if(c != getObjectiveFunction().getObjectiveFunctionValue(oSolution.getSolutionRepresentation()))
//				System.out.println("ERROR DELTA C IS " + c + " BUT ACTUAL VALUE : " + getObjectiveFunction().getObjectiveFunctionValue(oSolution.getSolutionRepresentation()));
			}
			
		}	
		
		// for each value in the solution..
		for(int i1=0; i1<randomStart; i1++) {
			//stop searching when we have accepted enough solutions to satisfy our depth of search
			if(acceptedSolutionCounter>=acceptedSolutionLimit)
				break;
			
			int i2 = (i1+1)%size;
			
			//delta evaluation calculating what the cost difference will be
			double tempC=this.deltaEvaluation(newSolution, size, i1, i2, c);
			
			//if the cost will be an improvement or equal, actually flip it
			if(tempC<=bestEval) {
				acceptedSolutionCounter++;
				c=tempC;
				bestEval=c;
				swapPoints(newSolution, i1, i2);

//				if(c == getObjectiveFunction().getObjectiveFunctionValue(oSolution.getSolutionRepresentation()))
//				System.out.println("GOOD DELTA AND ACTUAL ARE BOTH " + c);
//							
//				if(c != getObjectiveFunction().getObjectiveFunctionValue(oSolution.getSolutionRepresentation()))
//				System.out.println("ERROR DELTA C IS " + c + " BUT ACTUAL VALUE : " + getObjectiveFunction().getObjectiveFunctionValue(oSolution.getSolutionRepresentation()));
			}
			
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
	
	private double deltaEvaluation(int[] newSolution, int size, int i1, int i2, double c) {
		
		//if i1 is the last element..
		if(i1 == size-1) {
			//cost of edges at i1
			c-=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i1]);
			c+=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i2]);
			c-=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i1]);
			c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i2]);
			
			//cost of edges at i2
			c-=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i2]);
			c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i1]);
			c-=this.getObjectiveFunction().getCost(newSolution[i2], newSolution[i2+1]);
			c+=this.getObjectiveFunction().getCost(newSolution[i1], newSolution[i2+1]);
		}
		//else if i1 is an intermediate node
		else {
			//cost of edges at i1
			if(i1 == 0) {
				c-=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i1]);
				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i2]);
			}
			else {
				c-=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i1]);
				c+=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i2]);
			}
				
			
			//cost of edges at i2
			if(i2 == size-1) {
				c-=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i2]);
				c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i1]);
			}
				
			else {
				c-=this.getObjectiveFunction().getCost(newSolution[i2], newSolution[i2+1]);	
				c+=this.getObjectiveFunction().getCost(newSolution[i1], newSolution[i2+1]);	
			}
				
		}
		
		return c;
	}
}
