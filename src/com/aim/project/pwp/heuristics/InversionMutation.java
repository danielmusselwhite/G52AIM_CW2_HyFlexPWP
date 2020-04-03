package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.utilities.Utilities;


public class InversionMutation extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public InversionMutation(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
		// rule is numberOfIterations = 2(n+1) where n = number of 0.2's there are in intensityOfMutation
		//by dividing IOM by 0.2 you have n
		//int numberOfIterations = (int) (2*((dIntensityOfMutation)/0.2d)+1);
		int numberOfIterations=0;
		
		if(dIntensityOfMutation >= 0 && dIntensityOfMutation<0.2)
			numberOfIterations = 1;
		else if(dIntensityOfMutation >= 0.2 && dIntensityOfMutation <0.4)
			numberOfIterations = 2;
		else if(dIntensityOfMutation >= 0.4 && dIntensityOfMutation<0.6)
			numberOfIterations = 3;
		else if(dIntensityOfMutation >= 0.6 && dIntensityOfMutation<0.8)
			numberOfIterations = 4;
		else if(dIntensityOfMutation >=0.8 && dIntensityOfMutation<1)
			numberOfIterations = 5;
		else if(dIntensityOfMutation == 1)
			numberOfIterations = 6;
		
		int[] newSolution = oSolution.getSolutionRepresentation().getSolutionRepresentation();//.clone();
		int size = newSolution.length;
		
		//c = delta evaluation cost (initialised to current value)
		double c = oSolution.getObjectiveFunctionValue();
				
		for(int i=0; i<numberOfIterations; i++) {


			
			// pick 2 different random points
			int i1 = oRandom.nextInt(size);	
			int i2;
			do {
				i2 = oRandom.nextInt(size);
			} while(i1==i2);
			
			int startIndex = i1 < i2 ? i1 : i2;
			int endIndex = i1 < i2 ? i2 : i1;
			
			// delta evaluation
			c=this.deltaEvaluation(newSolution, size, startIndex, endIndex, c);
			
			this.invertBetweenPoints(newSolution, startIndex, endIndex);
			
//			if(c == getObjectiveFunction().getObjectiveFunctionValue(oSolution.getSolutionRepresentation()))
//			System.out.println("GOOD DELTA AND ACTUAL ARE BOTH " + c);
//						
//			if(c != getObjectiveFunction().getObjectiveFunctionValue(oSolution.getSolutionRepresentation()))
//			System.out.println("ERROR DELTA C IS " + c + " BUT ACTUAL VALUE : " + getObjectiveFunction().getObjectiveFunctionValue(oSolution.getSolutionRepresentation()));

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
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}
	
	private void invertBetweenPoints(int[] solution, int startIndex, int endIndex) {
		int i=startIndex;
		int j=endIndex;
		
		while(i<j) {
			this.swapPoints(solution, i, j);
			i++;
			j--;
		}
	}
	
	private double deltaEvaluation(int[] newSolution, int size, int startIndex, int endIndex, double c) {
		
		//only need to calculate the start and end changes as all the rest will have the same distances just in stead of being x->y its now y->x but still same distance
		
		//get cost between start index and element before it
		if(startIndex==0) {
			c-=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[startIndex]);
			c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[endIndex]);
		}
		else {
			c-=this.getObjectiveFunction().getCost(newSolution[startIndex-1], newSolution[startIndex]);
			c+=this.getObjectiveFunction().getCost(newSolution[startIndex-1], newSolution[endIndex]);
		}
			
		
		//get cost between end index and element after it
		if(endIndex==size-1) {
			c-=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[endIndex]);
			c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[startIndex]);
		}
			
		else {
			c-=this.getObjectiveFunction().getCost(newSolution[endIndex], newSolution[endIndex+1]);
			c+=this.getObjectiveFunction().getCost(newSolution[startIndex], newSolution[endIndex+1]);
		}
			
	
		return c;

	}

}
