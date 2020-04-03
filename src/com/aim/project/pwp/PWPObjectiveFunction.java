package com.aim.project.pwp;

import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

public class PWPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final PWPInstanceInterface oInstance;
	
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
		
		return totalCost;
	}
	
	@Override
	public double getCost(int iLocationA, int iLocationB) {
		return this.calculateEuclidianDistance(oInstance.getLocationForDelivery(iLocationA), oInstance.getLocationForDelivery(iLocationB));
	}

	@Override
	public double getCostBetweenDepotAnd(int iLocation) {
		return this.calculateEuclidianDistance(oInstance.getLocationForDelivery(iLocation), oInstance.getPostalDepot());
	}

	@Override
	public double getCostBetweenHomeAnd(int iLocation) {
		return this.calculateEuclidianDistance(oInstance.getLocationForDelivery(iLocation), oInstance.getHomeAddress());
	}
	
	private double calculateEuclidianDistance(Location l1, Location l2) {
		double x1, x2, y1, y2;
		x1 = l1.getX();
		x2 = l2.getX();
		y1 = l1.getY();
		y2 = l2.getY();
		
		return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
	}
}
