package com.aim.project.pwp.heuristics;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;

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
	
	//getting OFI for delta eval
	public ObjectiveFunctionInterface getObjectiveFunction() {
		return oObjectiveFunction;
	}
	
	//function for swapping two points
	public void swapPoints(int[] solution, int index1, int index2) {
		int temp = solution[index2];
		solution[index2] = solution[index1];
		solution[index1] = temp;
	}
}
