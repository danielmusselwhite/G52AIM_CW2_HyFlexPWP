package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public AdjacentSwap(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		int numberOfIterations=0;
		
		if(intensityOfMutation >= 0 && intensityOfMutation<0.2)
			numberOfIterations = 1;
		else if(intensityOfMutation >= 0.2 && intensityOfMutation <0.4)
			numberOfIterations = 2;
		else if(intensityOfMutation >= 0.4 && intensityOfMutation<0.6)
			numberOfIterations = 4;
		else if(intensityOfMutation >= 0.6 && intensityOfMutation<0.8)
			numberOfIterations = 8;
		else if(intensityOfMutation >=0.8 && intensityOfMutation<1)
			numberOfIterations = 16;
		else if(intensityOfMutation == 1)
			numberOfIterations = 32;
		
		int[] newSolution = solution.getSolutionRepresentation().getSolutionRepresentation();
		int size = newSolution.length;
		
		//c = delta evaluation cost (initialised to current value)
		double c = solution.getObjectiveFunctionValue();
		
		for(int i=0; i<numberOfIterations; i++) {

			int i1 = oRandom.nextInt(size);	// pick random point
			int i2 = (i1+1)%size;			// adjacent point AFTER this point (circular)
			
			//delta evaluation
			c=this.deltaEvaluation(newSolution, size, i1, i2, c);
			
			swapPoints(newSolution, i1, i2);	// swapping the two adjacent points
			
//			if(c == getObjectiveFunction().getObjectiveFunctionValue(solution.getSolutionRepresentation()))
//			System.out.println("GOOD DELTA AND ACTUAL ARE BOTH" + c);
//			
//			if(c != getObjectiveFunction().getObjectiveFunctionValue(solution.getSolutionRepresentation()))
//                System.out.println("ERROR DELTA C IS " + c + " BUT ACTUAL VALUE : " + getObjectiveFunction().getObjectiveFunctionValue(solution.getSolutionRepresentation()));
		}
		
		solution.setObjectiveFunctionValue(c);

		return c;
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

	private double deltaEvaluation(int[] newSolution, int size, int i1, int i2, double c) {
		
		//if i1 is the last element..
		if(i1 == size-1) {
			//cost of edges at i1, swapped with i2
			c-=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i1]);
			c+=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i2]);
			c-=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i1]);
			c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i2]);
			
			//cost of edges at i2, swapped with i1
			c-=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i2]);
			c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i1]);
			c-=this.getObjectiveFunction().getCost(newSolution[i2], newSolution[i2+1]);
			c+=this.getObjectiveFunction().getCost(newSolution[i1], newSolution[i2+1]);
		}
		//else if i1 is an intermediate node
		else {
			//cost of edges at i1, swapped with i2
			if(i1 == 0) {
				c-=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i1]);
				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i2]);
			}
			else {
				c-=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i1]);
				c+=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i2]);
			}
				
			
			//cost of edges at i2, swapped with i1
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

