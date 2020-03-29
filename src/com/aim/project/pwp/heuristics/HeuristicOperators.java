package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Arrays;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;
import com.aim.project.pwp.solution.PWPSolution;
import com.aim.project.utilities.Utilities;

public class HeuristicOperators {

	private ObjectiveFunctionInterface oObjectiveFunction;

	public HeuristicOperators() {

	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		this.oObjectiveFunction = f;
	}

	/**
	 * TODO implement any common functionality here so that your
	 * 			heuristics can reuse them!
	 * E.g.  you may want to implement the swapping of two delivery locations here!
	 */
	
	//function for swapping two points
	public int[] swapPoints(int[] solution, int index1, int index2) {
		int temp = solution[index2];
		solution[index2] = solution[index1];
		solution[index1] = temp;
		
		return solution;		
	}
	
	//getting the subarray between two points
	public int[] getSubArrayBetweenTwoPoints(int[] solution, int startIndex, int endIndex) {
		// create a subarray of all points between those indexes
		int[] subarrayOfLocations = new int[Math.abs(startIndex-endIndex)];

		// copying values from these indexes into a subarray
		subarrayOfLocations = Arrays.copyOfRange(solution, startIndex, endIndex);
		
		return subarrayOfLocations;
	}
	
	//setting the values in the solution to the subarray between the limiting index values
	public int[] setValuesWithinRange(int[] solution, int[] subarray, int startIndex, int endIndex) {
		for(int offset=0; offset+startIndex<endIndex; offset++) {
			solution[startIndex + offset]=subarray[offset];
		}
		return solution;
	}
	
	//removing point at index 1 and putting it into index2, maintaining order of the list
	public int[] removeAndInsertPoint(int[] solution, int removeFromIndex, int insertToIndex) {
        
        //convert to array lists as its easier to add and remove from
        ArrayList<Integer> newSolution = new ArrayList<Integer>();
		ArrayList<Integer> oldSolution = Utilities.convertToArrayList(solution);
		
		//remove the element
		int removedElement = oldSolution.remove(removeFromIndex);
		
		//add all the elements in order they appear up to the index we are inserting at
		for(int i=0; i<insertToIndex; i++){
		    newSolution.add(oldSolution.get(i));
		}
		
		//insert the removed element at this index
		newSolution.add(removedElement);
		
		//add all the indexes left after the inserted index
		for(int i=insertToIndex; i<oldSolution.size(); i++){
		    newSolution.add(oldSolution.get(i));
		}
		
		//convert back to int array and return
		return Utilities.convertToArray(newSolution);
	}
	
	
	public double getSolutionCost(SolutionRepresentationInterface solutionRepresentation) {
		return oObjectiveFunction.getObjectiveFunctionValue(solutionRepresentation);
	}
	
	public ObjectiveFunctionInterface getObjectiveFunction() {
		return oObjectiveFunction;
	}
	
	public double getDifferenceDeltaEvaluation(int[] newSolution, int size, int i1, int i2) {
		int c=0;
		
		//if i1 is the last element..
		if(i1 == size-1) {
			//cost of edges at i1
			c+=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i1]);
			c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i1]);
			
			//cost of edges at i2
			c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i2]);
			c+=this.getObjectiveFunction().getCost(newSolution[i2], newSolution[i2+1]);
		}
		//else if i1 is an intermediate node
		else {
			//cost of edges at i1
			if(i1 == 0)
				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i1]);
			else
				c+=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i1]);
			
			//cost of edges at i2
			if(i2 == size-1)
				c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i2]);
			else
				c+=this.getObjectiveFunction().getCost(newSolution[i2], newSolution[i2+1]);	
		}
		
		return c;
	}
	
}

