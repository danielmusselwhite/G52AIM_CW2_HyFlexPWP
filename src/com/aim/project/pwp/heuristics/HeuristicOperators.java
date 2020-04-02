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
		ArrayList<Integer> subarrayOfLocations = new ArrayList<Integer>();
		
		// copying values from these indexes into a subarray
		if(endIndex>startIndex)
			return Arrays.copyOfRange(solution, startIndex, endIndex);
		else {
			for(int i=startIndex; i<solution.length;i++) {
				subarrayOfLocations.add(solution[i]);
			}
			for(int i=0; i<endIndex;i++) {
				subarrayOfLocations.add(solution[i]);
			}
		}
		return Utilities.convertToArray(subarrayOfLocations);
	}
	
	//setting the values in the solution to the subarray between the limiting index values
	public int[] setValuesWithinRange(int[] solution, int[] subarray, int startIndex, int endIndex) {
		if(endIndex>startIndex) {
			for(int offset=0; offset+startIndex<endIndex; offset++) {
				solution[startIndex + offset]=subarray[offset];
			}
		}
		else{
			int offset=0;
			for(; (startIndex + offset)<solution.length;offset++) {
				solution[startIndex + offset]=subarray[offset];
			}
			for(int i=0; i<endIndex;i++) {
				solution[i]=subarray[offset];
				offset++;
			}
		}
		
		return solution;
	}
	
	//removing point at index 1 and putting it into index2, maintaining order of the list
	public int[] removeAndInsertPoint(int[] solution, int removeFromIndex, int insertToIndex) {
        
		ArrayList<Integer> oldSolution = Utilities.convertToArrayList(solution);
		
		//remove the element
		int removedElement = oldSolution.remove(removeFromIndex);
		
		//if we are removing after where we're inserting to
		if(removeFromIndex>insertToIndex) {
			oldSolution.add(insertToIndex, removedElement);
			return Utilities.convertToArray(oldSolution);
		}
		//else we are inserting after we're removing from
		else {
			oldSolution.add(insertToIndex-1, removedElement);
			return Utilities.convertToArray(oldSolution);
		}
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
	
	public double getDifferenceDeltaEvaluationReinsertionBefore(int[] newSolution, int size, int i1, int i2) {
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
		if(i2 == size-1) {
			c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[i2]); //if i2 is size it is the last edge from the last location to the home (so i2 is technically home which isn't in array so do cost between home and i2-1)
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
	public double getDifferenceDeltaEvaluationReinsertionAfter(int[] newSolution, int size, int i1, int i2) {
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

			//adding the element after it
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
			c+=this.getObjectiveFunction().getCost(newSolution[i2-1], newSolution[i2]);
			
			if(i2 == size-1) 
				c+=this.getObjectiveFunction().getCostBetweenHomeAnd(i2);

			else if(i2-1 == 0){
				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[i2-1]);
			}
			else {
				c+=this.getObjectiveFunction().getCost(newSolution[i2-2], newSolution[i2-1]);
			}
			
		}
		
		return c;
	}
	
	
	public double getDifferenceDeltaEvaluationInversion(int[] newSolution, int size, int startIndex, int endIndex) {
		double c = 0;
		
		//if we start inversion at index less than the index we end it at
		if(startIndex<endIndex) {
			//get cost between start index and element before it
			if(startIndex==0)
				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[startIndex]);
			else
				c+=this.getObjectiveFunction().getCost(newSolution[startIndex-1], newSolution[startIndex]);
			
			//get cost of paths between start and end index
			for(int i=startIndex; i<endIndex;i++)
				c+=this.getObjectiveFunction().getCost(newSolution[i], newSolution[i+1]);
			
			//get cost between end index and element after it
			if(endIndex==size-1)
				c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[endIndex]);
			else
				c+=this.getObjectiveFunction().getCost(newSolution[endIndex], newSolution[endIndex+1]);
		}
		
		//else we start inversion at index greater than the index we end it at
		else {
			
			//get cost of start element and the index before it
			c+=this.getObjectiveFunction().getCost(newSolution[startIndex-1], newSolution[startIndex]);
			
			//get cost of paths between start index and the final element
			for(int i=startIndex; i<size-1;i++)
				c+=this.getObjectiveFunction().getCost(newSolution[i], newSolution[i+1]);
			
			//get cost of path between final element and home
			c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[size-1]);
			
			//get cost between depot and first element
			c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[0]);
			
			//get cost of paths between first element and end index
			//get cost of paths between start index and the final element
			for(int i=0; i<endIndex;i++)
				c+=this.getObjectiveFunction().getCost(newSolution[i], newSolution[i+1]);
			
			//if the element after the end index isn't the element before the start index, add it
			if(startIndex-1!=endIndex+1)
				c+=this.getObjectiveFunction().getCost(newSolution[endIndex], newSolution[endIndex+1]);
		}
		
		
		
		return c;
	}
}

