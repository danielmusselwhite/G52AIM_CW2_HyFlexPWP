package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.PWPObjectiveFunction;
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
		
		// rule is numberOfIterations = 2^n where n = number of 0.2's there are in intensityOfMutation
		//by dividing IOM by 0.2 you have n
		//int numberOfIterations = (int) Math.pow(2, (int)((intensityOfMutation)/0.2d));
		
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
		
		int[] newSolution = solution.getSolutionRepresentation().getSolutionRepresentation().clone();
		int size = newSolution.length;
		
		// c = objectiveFunction
		double c = this.getSolutionCost(solution.getSolutionRepresentation());
		
		for(int i=0; i<numberOfIterations; i++) {

			int i1 = oRandom.nextInt(size);	// pick random point
			int i2 = (i1+1)%size;			// adjacent point AFTER this point (circular)
//			
//			if(i1-1<0)
//				c-=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i1]);
//			else
//				c-=this.getObjectiveFunction().getCost(newSolution[i1], newSolution[i1-1]);
//			if(i2+1>newSolution.length-1)
//				c-=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i2]);
//			else
//				c-=this.getObjectiveFunction().getCost(newSolution[i2], newSolution[i2+1]);
//			
			newSolution = swapPoints(newSolution, i1, i2);	// swapping the two adjacent points
			
//			if(i1-1<0)
//				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i1]);
//			else
//				c+=this.getObjectiveFunction().getCost(newSolution[i1], newSolution[i1-1]);
//			if(i2+1>newSolution.length-1)
//				c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i2]);
//			else
//				c+=this.getObjectiveFunction().getCost(newSolution[i2], newSolution[i2+1]);
			
		}
		
//		
//		System.out.println("AdjacentSwap");
//		for(int i=0; i<newSolution.length; i++) {
//			System.out.print(newSolution[i]+"-");
//		}
//		System.out.println();
//		
		// updating to the new solution
		solution.getSolutionRepresentation().setSolutionRepresentation(newSolution);
		
		solution.setObjectiveFunctionValue(this.getSolutionCost(solution.getSolutionRepresentation()));
		
		// returning the cost of the new solution
		return this.getSolutionCost(solution.getSolutionRepresentation());
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

