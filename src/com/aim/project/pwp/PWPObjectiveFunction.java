package com.aim.project.pwp;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;
import com.aim.project.utilities.Utilities;

public class PWPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final PWPInstanceInterface oInstance;
	private int[] lastPermutation;
	private double lastCost;
	
	public PWPObjectiveFunction(PWPInstanceInterface oInstance) {
		
		this.oInstance = oInstance;
	}

	@Override
	public double getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {
		
		int[] currentSolution = oSolution.getSolutionRepresentation();
		double totalCost=0;
		

		// (SIMPLE EVALUATION) 

		for(int i=0; i<currentSolution.length; i++) {
			// if this is the first location then the one before it is the depot
			if(i==0)
				totalCost+=getCostBetweenDepotAnd(currentSolution[i]);
			
			// else this isn't the fist location so the one before it is another city
			else {
				totalCost+=getCost(currentSolution[i], currentSolution[i-1]);
				// if this is also the last city then add the distance between it and the home
				if(i==currentSolution.length-1) 
					totalCost+=getCostBetweenHomeAnd(currentSolution[i]);
			}
				
		}
			
		
		// Delta Evaluation pseudo
		// check current solution permutation against old solution permutation to find what edges have changed
		// then calculate the cost change caused by evaluating those changes edge
		// then apply that to the previous cost
		// instead of recalculating entire thing
		
		// Do it in the heuristic whenever they are updated
		
		
		//store the last cost and last permutation
		lastCost = totalCost;
		lastPermutation = currentSolution;
		
		return totalCost;
	}
	
	@Override
	public double getCost(int iLocationA, int iLocationB) {
		return Utilities.calculateEuclidianDistance(oInstance.getLocationForDelivery(iLocationA), oInstance.getLocationForDelivery(iLocationB));
	}

	@Override
	public double getCostBetweenDepotAnd(int iLocation) {
		return Utilities.calculateEuclidianDistance(oInstance.getPostalDepot(), oInstance.getLocationForDelivery(iLocation));
	}

	@Override
	public double getCostBetweenHomeAnd(int iLocation) {
		return Utilities.calculateEuclidianDistance(oInstance.getHomeAddress(), oInstance.getLocationForDelivery(iLocation));
	}
	
}
