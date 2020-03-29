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
	
	// i1 = i,	i2 = (i+1)%size
	public double getDifferenceDeltaEvaluationAdjacentSwap(int[] newSolution, int size, int i1, int i2) {
		double c=0;
		
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
	
	public double getDifferenceDeltaEvaluation2Nodes(int[] newSolution, int size, int i1, int i2) {
		double c=0;
		
		//if i1 is the last element..
		if(i1 == size-1) {
			c+=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i1]);
			c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i1]);
		}
		//else if i1 is the first element
		else if(i1 == 0){
			c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i1]);
			c+=this.getObjectiveFunction().getCost(newSolution[i1], newSolution[i1+1]);
		}
		//else if i1 is an intermediate node
		else {
			c+=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i1]);
			c+=this.getObjectiveFunction().getCost(newSolution[i1], newSolution[i1+1]);
		}
		
		//if i2 is the last edge..
		if(i2 == size) {
			c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i2]);
		}
		//else if i2 is the first element
		else if(i2 == 0){
			c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i2]);
		}
		//else if i2 is an intermediate node
		else {
			//if i2 - 1 is i1 we don't want to add it twice
			if(i2-1!=i1)
				c+=this.getObjectiveFunction().getCost(newSolution[i2-1], newSolution[i2]);
		}
		
		return c;
	}
	
	//i1 = value removing from, i2 = value inserting in front of
	public double getDifferenceDeltaEvaluationReinsertion(int[] newSolution, int size, int i1, int i2) {
		double c=0;

		//if we are inserting the element to an index before where it was removed from
		if(i1>i2) {
		
			// add cost between i1 and element after it
			if(i1 == size-1) {
				c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i1]);
			}
			else {
				c+=this.getObjectiveFunction().getCost(newSolution[i1], newSolution[i1+1]);
			}
			
			// adding the cost of the new value inserted at i2
			
			//add cost of i2 and element before it
			if(i2 == 0){
				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i2]);
			}
			else {
				c+=this.getObjectiveFunction().getCost(newSolution[i2-1], newSolution[i2]);
			}

			c+=this.getObjectiveFunction().getCost(newSolution[i2], newSolution[i2+1]);
			
		}
		
		//else we are inserting the element to an index after where it was removed from
		else {
		
			// add cost between i1 and element before it
			if(i1 == 0) {
				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i1]);
			}
			else {
				c+=this.getObjectiveFunction().getCost(newSolution[i1-1], newSolution[i1]);
			}
			
			// adding the cost of the new value inserted at i2
			if(i2 == size-1)
				c+=this.getObjectiveFunction().getCostBetweenHomeAnd(i2);
			c+=this.getObjectiveFunction().getCost(newSolution[i2-1], newSolution[i2]);

			if(i2-1 == 0){
				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i2-1]);
			}
			else {
				c+=this.getObjectiveFunction().getCost(newSolution[i2-2], newSolution[i2-1]);
			}
		}
		
		return c;
	}
}

