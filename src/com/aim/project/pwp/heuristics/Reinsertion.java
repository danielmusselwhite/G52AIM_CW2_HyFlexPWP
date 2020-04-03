package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.utilities.Utilities;


public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public Reinsertion(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		// rule is numberOfIterations = 2(n+1) where n = number of 0.2's there are in intensityOfMutation
		//by dividing IOM by 0.2 you have n
		//int numberOfIterations = (int) (2*((intensityOfMutation)/0.2d)+1);
		
		int numberOfIterations=0;
		
		if(intensityOfMutation >= 0 && intensityOfMutation<0.2)
			numberOfIterations = 1;
		else if(intensityOfMutation >= 0.2 && intensityOfMutation <0.4)
			numberOfIterations = 2;
		else if(intensityOfMutation >= 0.4 && intensityOfMutation<0.6)
			numberOfIterations = 3;
		else if(intensityOfMutation >= 0.6 && intensityOfMutation<0.8)
			numberOfIterations = 4;
		else if(intensityOfMutation >=0.8 && intensityOfMutation<1)
			numberOfIterations = 5;
		else if(intensityOfMutation == 1)
			numberOfIterations = 6;
		
		int[] newSolution = solution.getSolutionRepresentation().getSolutionRepresentation();
		int size = newSolution.length;
		
		//c = delta evaluation cost (initialised to current value)
		double c = solution.getObjectiveFunctionValue();
				
		for(int i=0; i<numberOfIterations; i++) {
			// pick 2 different random points
			int removeFromIndex = oRandom.nextInt(size);	
			int insertToIndex;
			do {
				insertToIndex = oRandom.nextInt(size);
			} while(removeFromIndex==insertToIndex);
			
			c=deltaEvaluation(newSolution, size, removeFromIndex, insertToIndex, c);
			
			newSolution=removeAndInsertPoint(newSolution, removeFromIndex, insertToIndex);
			solution.getSolutionRepresentation().setSolutionRepresentation(newSolution);
			
//			if(c == getObjectiveFunction().getObjectiveFunctionValue(solution.getSolutionRepresentation())) 
//			System.out.println("GOOD DELTA AND ACTUAL ARE BOTH " + c);
//						
//			if(c != getObjectiveFunction().getObjectiveFunctionValue(solution.getSolutionRepresentation()))
//			System.out.println("ERROR DELTA C IS " + c + " BUT ACTUAL VALUE : " + getObjectiveFunction().getObjectiveFunctionValue(solution.getSolutionRepresentation()));

		}

		solution.setObjectiveFunctionValue(c);
		
		return c;
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

	private double deltaEvaluation(int[] newSolution, int size, int removeFromIndex, int insertToIndex, double c) {
		
		//if R < I
		// - : 'R,R-1' 'R,R+1', 'I, I+1'
		// + : 'R-1,R+1' 'I,R' 'R,I+1'
		if(removeFromIndex < insertToIndex) {

			//-R,R+1
			c-=this.getObjectiveFunction().getCost(newSolution[removeFromIndex],newSolution[removeFromIndex+1]);
			
			//-R,R-1
			//+R-1,R+1
			if(removeFromIndex == 0) {
				c-=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[removeFromIndex]);
				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[removeFromIndex+1]);
			}
			else {
				c-=this.getObjectiveFunction().getCost(newSolution[removeFromIndex-1], newSolution[removeFromIndex]);
				c+=this.getObjectiveFunction().getCost(newSolution[removeFromIndex-1], newSolution[removeFromIndex+1]);
			}
			
			//+I,R
			c+=this.getObjectiveFunction().getCost(newSolution[removeFromIndex], newSolution[insertToIndex]);
				
			//-I,I+1
			//+R,I+1
			if(insertToIndex == size-1) {
				c-=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[insertToIndex]);
				c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[removeFromIndex]);
			}
			else {
				c-=this.getObjectiveFunction().getCost(newSolution[insertToIndex], newSolution[insertToIndex+1]);
				c+=this.getObjectiveFunction().getCost(newSolution[removeFromIndex], newSolution[insertToIndex+1]);
			}
			
		}
		//else I < R
		// - : 'R,R-1' 'R,R+1', 'I, I-1'
		// + : 'R-1,R+1' 'I,R' 'R,I-1'
		else {
			//-R,R-1
			c-=this.getObjectiveFunction().getCost(newSolution[removeFromIndex], newSolution[removeFromIndex-1]);
			
			//-R,R+1
			//+R-1,R+1
			if(removeFromIndex == size-1) {
				c-=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[removeFromIndex]);
				c+=this.getObjectiveFunction().getCostBetweenHomeAnd(newSolution[removeFromIndex-1]);
			}
			else {
				c-=this.getObjectiveFunction().getCost(newSolution[removeFromIndex], newSolution[removeFromIndex+1]);
				c+=this.getObjectiveFunction().getCost(newSolution[removeFromIndex-1], newSolution[removeFromIndex+1]);
			}
			
			//+I,R
			c+=this.getObjectiveFunction().getCost(newSolution[removeFromIndex], newSolution[insertToIndex]);
				
			//-I,I-1
			//+R,I-1
			if(insertToIndex == 0) {
				c-=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[insertToIndex]);
				c+=this.getObjectiveFunction().getCostBetweenDepotAnd(newSolution[removeFromIndex]);
			}
			else {
				c-=this.getObjectiveFunction().getCost(newSolution[insertToIndex-1], newSolution[insertToIndex]);
				c+=this.getObjectiveFunction().getCost(newSolution[insertToIndex-1], newSolution[removeFromIndex]);
			}
			
		}
		
		return c;
	}
	
	//removing point at index 1 and putting it into index2, maintaining order of the list
	private int[] removeAndInsertPoint(int[] solution, int removeFromIndex, int insertToIndex) {
        
		ArrayList<Integer> newSolution = Utilities.convertToArrayList(solution);
		
		//remove the element
		int removedElement = newSolution.remove(removeFromIndex);

		newSolution.add(insertToIndex, removedElement);
		return Utilities.convertToArray(newSolution);
		
	}
	
	
	private void printState(Boolean first, int[] arr, int R, int I) {
	
		if(first)
			System.out.print("\nFirst");
		else
			System.out.print("\nAfter");
		System.out.print(" Array = ");
		for (int i=0; i<arr.length; i++) {
			System.out.print(arr[i]+",");	
		}
		System.out.println(" R = "+R+" I = "+I);
	}
}
