package com.aim.project.pwp;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;
import com.aim.project.utilities.Utilities;

public class PWPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final PWPInstanceInterface oInstance;
	
	public PWPObjectiveFunction(PWPInstanceInterface oInstance) {
		
		this.oInstance = oInstance;
	}

	@Override
	public double getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {
		// SIMPLE EVALUATION FOR NOW
		int[] currentSolution = oSolution.getSolutionRepresentation();
		double totalCost=0;
		for(int i=0; i<currentSolution.length; i++) {
			// if this is the first location (after the depot)
			if(i==0)
				totalCost+=getCostBetweenDepotAnd(currentSolution[i]);
			// if this is the last location (before home)
			else if(i==currentSolution.length)
				totalCost+=getCostBetweenHomeAnd(currentSolution[i]);
			else
				totalCost+=getCost(currentSolution[i], currentSolution[i-1]);
		}
		
		// Delta Evaluation DO LATER
		// check current solution permutation against old solution permutation to find what edges have changed
		// then calculate the cost change caused by evaluating those changes edge
		// then apply that to the previous cost
		// instead of recalculating entire thing
		
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
