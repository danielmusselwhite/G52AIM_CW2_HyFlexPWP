package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import com.aim.project.utilities.Utilities;


public class CX implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public CX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public double apply(PWPSolutionInterface p1, PWPSolutionInterface p2,
			PWPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		
		// rule is iterations = 2(n+1) where n = number of 0.2's there are in IOM
		//by dividing IOM by 0.2 you have n
		//int iterations = (int) (2*((intensityOfMutation)/0.2d)+1);
		int iterations=0;
		
		if(intensityOfMutation >= 0 && intensityOfMutation<0.2)
			iterations = 1;
		else if(intensityOfMutation >= 0.2 && intensityOfMutation <0.4)
			iterations = 2;
		else if(intensityOfMutation >= 0.4 && intensityOfMutation<0.6)
			iterations = 3;
		else if(intensityOfMutation >= 0.6 && intensityOfMutation<0.8)
			iterations = 4;
		else if(intensityOfMutation >=0.8 && intensityOfMutation<1)
			iterations = 5;
		else if(intensityOfMutation == 1)
			iterations = 6;
		
		
		
		int[] parent1Solution = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] parent2Solution = p2.getSolutionRepresentation().getSolutionRepresentation();
		int[] childSolution1 = parent1Solution;
		int[] childSolution2 = parent2Solution;

		for(int i=0; i<iterations; i++){
			//GENERATING CHILD 1
			
			// defaulting the child1 solution to have all the values from parent 2
			childSolution1 = parent2Solution;
			
			
			//picking a random index to be the first one for us to explore
			int nextIndex = oRandom.nextInt(p1.getSolutionRepresentation().getSolutionRepresentation().length);
			//setting up the cycle the start point for the cycle
			ArrayList<Integer> cycle = new ArrayList<Integer>();
			
			// Finding the cycle and adding it to the child
			//whilst the cycle doesn't contain the next element..
			while(!cycle.contains(parent1Solution[nextIndex])) { 
				// add this element to the child at its index
				childSolution1[nextIndex] = parent1Solution[nextIndex];
				// add this element to the cycle
				cycle.add(parent1Solution[nextIndex]);
				
				//getting where the element at this index is in parent two, in parent one.
				nextIndex = Utilities.arrayGetIndexOf(parent1Solution, parent2Solution[nextIndex]);

			}
			
			
			//GENERATING CHILD 2
			
			// defaulting the child1 solution to have all the values from parent 2
			childSolution2 = parent1Solution;
			
			
			//picking a random index to be the first one for us to explore
			nextIndex = oRandom.nextInt(p1.getSolutionRepresentation().getSolutionRepresentation().length);
			//setting up the cycle the start point for the cycle
			cycle = new ArrayList<Integer>();
			
			// Finding the cycle and adding it to the child
			//whilst the cycle doesn't contain the next element..
			while(!cycle.contains(parent2Solution[nextIndex])) { 
				// add this element to the child at its index
				childSolution2[nextIndex] = parent2Solution[nextIndex];
				// add this element to the cycle
				cycle.add(parent2Solution[nextIndex]);
				
				//getting where the element at this index is in parent 1, in parent 2.
				nextIndex = Utilities.arrayGetIndexOf(parent2Solution, parent1Solution[nextIndex]);

			}
			
			//setting the parents for the next iteration to be the children
			parent1Solution = childSolution1;
			parent2Solution = childSolution2;
		}

		//setting the child to be randomly 1 of the 2 generated children (50/50 chance of being either)
		if(oRandom.nextDouble()>0.5)
			c.getSolutionRepresentation().setSolutionRepresentation(childSolution1);  // move to the new solution
		else
			c.getSolutionRepresentation().setSolutionRepresentation(childSolution2);  // move to the new solution	
	
		return c.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}


	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {
		
		this.oObjectiveFunction = oObjectiveFunction;
	}
}
