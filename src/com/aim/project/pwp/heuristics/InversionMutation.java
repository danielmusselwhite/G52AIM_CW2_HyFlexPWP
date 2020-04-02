package com.aim.project.pwp.heuristics;

import java.util.Arrays;
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
		
		int[] newSolution = oSolution.getSolutionRepresentation().getSolutionRepresentation().clone();
		int size = newSolution.length;
		
		//c = delta evaluation cost (initialised to current value)
//		double c = oSolution.getObjectiveFunctionValue();
		
		for(int i=0; i<numberOfIterations; i++) {


			
			// pick 2 different random points
			int startIndex = oRandom.nextInt(size);	
			int endIndex;
			do {
				endIndex = oRandom.nextInt(size);
			} while(startIndex==endIndex);
			
//			c-=this.getDifferenceDeltaEvaluationInversion(newSolution, size, startIndex, endIndex);
			
			// create a subarray of all points between those indexes
			int[] subarrayOfLocations = getSubArrayBetweenTwoPoints(newSolution, startIndex, endIndex);
	
			//reversing this subarray
			subarrayOfLocations = Utilities.reverseArray(subarrayOfLocations);
			
			//putting the reversed subarray into the new solution
			newSolution = setValuesWithinRange(newSolution, subarrayOfLocations, startIndex, endIndex);

//			c+=this.getDifferenceDeltaEvaluationInversion(newSolution, size, startIndex, endIndex);
		}
		
		
		// updating to the new solution
		oSolution.getSolutionRepresentation().setSolutionRepresentation(newSolution);
		
		oSolution.setObjectiveFunctionValue(this.getSolutionCost(oSolution.getSolutionRepresentation()));
//		oSolution.setObjectiveFunctionValue(c);
		
//		System.out.println("Inversion");
//		for(int i=0; i<oSolution.getSolutionRepresentation().getSolutionRepresentation().length; i++) {
//			System.out.print(oSolution.getSolutionRepresentation().getSolutionRepresentation()[i]+"-");
//		}
//		System.out.println();
		
		// returning the cost of the new solution
		return this.getSolutionCost(oSolution.getSolutionRepresentation());
//		return c;
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
