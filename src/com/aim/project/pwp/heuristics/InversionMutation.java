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
		
		int[] newSolution = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int size = newSolution.length;
		
		for(int i=0; i<numberOfIterations; i++) {

			// pick 2 different random points
			int index1 = oRandom.nextInt(size);	
			int index2;
			do {
				index2 = oRandom.nextInt(size);
			} while(index1==index2);
			
			
			// working out which is the start and end index
			int startIndex = index1 < index2 ? index1 : index2;
			int endIndex = index2 > index1 ? index2 : index1;
			
			// create a subarray of all points between those indexes
			int[] subarrayOfLocations = getSubArrayBetweenTwoPoints(newSolution, startIndex, endIndex);
	
			//reversing this subarray
			subarrayOfLocations = Utilities.reverseArray(subarrayOfLocations);
			
			//putting the reversed subarray into the new solution
			newSolution = setValuesWithinRange(newSolution, subarrayOfLocations, startIndex, endIndex);

		}
		
		
		// updating to the new solution
		oSolution.getSolutionRepresentation().setSolutionRepresentation(newSolution);
		
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
		return false;
	}

}
