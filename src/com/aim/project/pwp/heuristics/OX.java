package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import com.aim.project.utilities.Utilities;


public class OX implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public OX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
		return oSolution.getObjectiveFunctionValue();
			
	}

	
	// parent index 1, parent index 2, child index
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
		
		int size = p1.getNumberOfLocations()-2; //we can't modify the home nor the depot so minus 2 from total
		int cutPoint1;
		int cutPoint2;
		
		int[] child1SolutionRepresentation = new int[size];
		int[] child2SolutionRepresentation = new int[size];
		int[] parent1SolutionRepresentation = p1.getSolutionRepresentation().getSolutionRepresentation().clone();
		int[] parent2SolutionRepresentation = p2.getSolutionRepresentation().getSolutionRepresentation().clone();
		
		
		
		for(int j=0; j<iterations; j++) {
			
			// pick two random cut points while both the points are the first and the last element as nothing will change or whilst they are the same element
			do {
				int point1 = oRandom.nextInt(size);
				int point2 = oRandom.nextInt(size);
				
				// setting it so cutPoint1 is the smaller and cutPoint2 is the bigger
				cutPoint1 = point1<point2 ? point1 : point2;
				cutPoint2 = point2>point1 ? point2 : point1;
			} while((cutPoint1 == 0 && cutPoint2 == size-1) || cutPoint1 == cutPoint2);
			

			// perform OX maintaining the order
			
			child1SolutionRepresentation = new int[size];
			child2SolutionRepresentation = new int[size];
			
			
			
			
			//GENERATING CHILD 1
			
			//getting the symbols from the middle of parent 1 between the 2 cutpoints
			ArrayList<Integer> child1Parent1Symbols = new ArrayList<Integer>();
			for(int i=cutPoint1; i<=cutPoint2; i++) {
				child1Parent1Symbols.add(parent1SolutionRepresentation[i]);
			}
			
			
			//get symbols from parent 2 starting from second cut point
			ArrayList<Integer> child1Parent2Symbols = new ArrayList<Integer>();
			for(int i=cutPoint2; i<parent2SolutionRepresentation.length ;i++) {
				if(!Utilities.arrayListContainsValue(child1Parent1Symbols, parent2SolutionRepresentation[i]))
					child1Parent2Symbols.add(parent2SolutionRepresentation[i]);
					
			}
			for(int i=0; i<cutPoint2; i++) {
				if(!Utilities.arrayListContainsValue(child1Parent1Symbols, parent2SolutionRepresentation[i]))
					child1Parent2Symbols.add(parent2SolutionRepresentation[i]);
					
			}

			//copying the middle into it
			for(int i=cutPoint1; i<=cutPoint2; i++) {
				child1SolutionRepresentation[i] = child1Parent1Symbols.get(i-cutPoint1);
			}
			
			
			//copying the sides into it
			{
				// index used for parent 2 symbol
				int k=0;
				
				for(int i=cutPoint2+1; i<child1SolutionRepresentation.length; i++, k++) {
					child1SolutionRepresentation[i] = child1Parent2Symbols.get(k);
				}

				for(int i=0; i<cutPoint1; i++, k++) {
					child1SolutionRepresentation[i] = child1Parent2Symbols.get(k);
				}
				
			}

			
			
			
		
			
			
			//GERNEATING CHILD 2
			
			//getting the symbols from the middle of parent 2 between the 2 cutpoints
			ArrayList<Integer> child2Parent2Symbols = new ArrayList<Integer>();
			for(int i=cutPoint1; i<=cutPoint2; i++) {
				child2Parent2Symbols.add(parent2SolutionRepresentation[i]);
			}

			//get symbols from parent 1 starting from second cut point
			ArrayList<Integer> child2Parent1Symbols = new ArrayList<Integer>();
			for(int i=cutPoint2; i<parent1SolutionRepresentation.length ;i++) {
				if(!Utilities.arrayListContainsValue(child2Parent2Symbols, parent1SolutionRepresentation[i]))
					child2Parent1Symbols.add(parent1SolutionRepresentation[i]);
					
			}
			for(int i=0; i<cutPoint2; i++) {
				if(!Utilities.arrayListContainsValue(child2Parent2Symbols, parent1SolutionRepresentation[i]))
					child2Parent1Symbols.add(parent1SolutionRepresentation[i]);
					
			}

			//copying the middle into it
			for(int i=cutPoint1; i<=cutPoint2; i++) {
				child2SolutionRepresentation[i] = child2Parent2Symbols.get(i-cutPoint1);
			}

			//copying the sides into it
			{
				// index used for parent 2 symbol
				int k=0;
				
				for(int i=cutPoint2+1; i<child2SolutionRepresentation.length; i++, k++) {
					child2SolutionRepresentation[i] = child2Parent1Symbols.get(k);
				}
				
				
				for(int i=0; i<cutPoint1; i++, k++) {
					child2SolutionRepresentation[i] = child2Parent1Symbols.get(k);
				}
				
			}

		}
			
		
		//setting the child to be randomly 1 of the 2 generated children (50/50 chance of being either)
		if(oRandom.nextDouble()>0.5)
			c.getSolutionRepresentation().setSolutionRepresentation(child1SolutionRepresentation);  // move to the new solution
		else
			c.getSolutionRepresentation().setSolutionRepresentation(child2SolutionRepresentation);  // move to the new solution	
	
		c.setObjectiveFunctionValue(oObjectiveFunction.getObjectiveFunctionValue(c.getSolutionRepresentation()));
		
		
//		System.out.println("OX");
//		for(int i=0; i<c.getSolutionRepresentation().getSolutionRepresentation().length; i++) {
//			System.out.print(c.getSolutionRepresentation().getSolutionRepresentation()[i]+"-");
//		}
//		System.out.println();
//		
		return oObjectiveFunction.getObjectiveFunctionValue(c.getSolutionRepresentation());
	}

	@Override
	public boolean isCrossover() {
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}


	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.oObjectiveFunction = f;
	}
	
}
