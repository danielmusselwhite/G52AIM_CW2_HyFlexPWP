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
		
		// if this is the first run (i.e. lastPermutation doesn't exist yet) calculate distance between ALL cities
		//if(lastPermutation == null) {
			// (SIMPLE EVALUATION) 

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
			
		//}
		
			/*
		// else there was a last permutation, we can do delta evaluation
		else {
			// (DELTA EVALUATION)
			
			totalCost = lastCost;
			
			// find all indexes of elements that have changed between this solution and the last solution
			int[] differentElementIndexes = Utilities.getArrayOfIndexesWhereElementsDiffer(currentSolution, lastPermutation);
			
			// for each index of elements that differ
			for(int i=0; i< differentElementIndexes.length; i++) {
				
				//if the first element has changed we need to do the depot and the following city
				if(differentElementIndexes[i] == 0) {
					
					//remove the cost from the last permutation and add the cost from the new one
					totalCost-=getCostBetweenDepotAnd(lastPermutation[i]);
					totalCost+=getCostBetweenDepotAnd(currentSolution[i]);
					
					//remove the cost from the last permutation and add the cost from the new one
					totalCost-=getCost(lastPermutation[i], lastPermutation[i+1]);
					totalCost+=getCost(currentSolution[i], currentSolution[i+1]);
				}
				
				//else if the last element has changed we need to the previous city and the home
				else if(differentElementIndexes[i] == 0) {
					
					//remove the cost from the last permutation and add the cost from the new one
					totalCost-=getCost(lastPermutation[i-1], lastPermutation[i]);
					totalCost+=getCost(currentSolution[i-1], currentSolution[i]);
					
					//remove the cost from the last permutation and add the cost from the new one
					totalCost-=getCostBetweenHomeAnd(lastPermutation[i]);
					totalCost+=getCostBetweenHomeAnd(currentSolution[i]);
				}
				
				//else this is just two intermediate cities, calculate difference between travelling to and from this city
				else {
					//remove the cost from the last permutation and add the cost from the new one
					totalCost-=getCost(lastPermutation[i-1], lastPermutation[i]);
					totalCost+=getCost(currentSolution[i-1], currentSolution[i]);
					
					//remove the cost from the last permutation and add the cost from the new one
					totalCost-=getCost(lastPermutation[i], lastPermutation[i+1]);
					totalCost+=getCost(currentSolution[i], currentSolution[i+1]);
				}
			}
		}
		
		// Delta Evaluation pseudo
		// check current solution permutation against old solution permutation to find what edges have changed
		// then calculate the cost change caused by evaluating those changes edge
		// then apply that to the previous cost
		// instead of recalculating entire thing
		
		
		//store the last cost and last permutation
		lastCost = totalCost;
		lastPermutation = currentSolution;
		*/
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
