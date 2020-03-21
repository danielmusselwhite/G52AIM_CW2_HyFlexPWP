package com.aim.project.pwp;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

import com.aim.project.pwp.heuristics.AdjacentSwap;
import com.aim.project.pwp.heuristics.CX;
import com.aim.project.pwp.heuristics.DavissHillClimbing;
import com.aim.project.pwp.heuristics.InversionMutation;
import com.aim.project.pwp.heuristics.NextDescent;
import com.aim.project.pwp.heuristics.OX;
import com.aim.project.pwp.heuristics.Reinsertion;
import com.aim.project.pwp.instance.InitialisationMode;
import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.reader.PWPInstanceReader;
import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.Visualisable;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;

import AbstractClasses.ProblemDomain;

public class AIM_PWP extends ProblemDomain implements Visualisable {

	private String[] instanceFiles = {
		"square", "libraries-15", "carparks-40", "tramstops-85", "trafficsignals-446", "streetlights-35714"
	};
	
	private PWPSolutionInterface[] aoMemoryOfSolutions;
	
	public PWPSolutionInterface oBestSolution;
	
	public PWPInstanceInterface oInstance;
	
	private HeuristicInterface[] aoHeuristics;
	
	private ObjectiveFunctionInterface oObjectiveFunction;
	
	private final long seed;
		
	public AIM_PWP(long seed) {
		
		super(seed);

		// TODO - set default memory size and create the array of low-level heuristics
		
	}
	
	public PWPSolutionInterface getSolution(int index) {
		
		// TODO 
	}
	
	public PWPSolutionInterface getBestSolution() {
		
		// TODO 
	}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		
		
		// TODO - apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		
		// TODO - apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution
	}

	@Override
	public String bestSolutionToString() {
		
		// TODO return the location IDs of the best solution including DEPOT and HOME locations
		//		e.g. "DEPOT -> 0 -> 2 -> 1 -> HOME"
	}

	@Override
	public boolean compareSolutions(int iIndexA, int iIndexB) {

		// TODO return true if the objective values of the two solutions are the same, else false
	}

	@Override
	public void copySolution(int iIndexA, int iIndexB) {

		// TODO - BEWARE this should copy the solution, not the reference to it!
		//			That is, that if we apply a heuristic to the solution in index 'b',
		//			then it does not modify the solution in index 'a' or vice-versa.
		
		
	}

	@Override
	public double getBestSolutionValue() {
		// DONE
		//get the value of the first solution
		double bestSolutionValue = oObjectiveFunction.getObjectiveFunctionValue(aoMemoryOfSolutions[0].getSolutionRepresentation());
		//go through each of the other solutions iteratively and check if they produce a better solution value
		for(int i=1; i<getNumberOfInstances(); i++) {
			double thisSolutionValue = oObjectiveFunction.getObjectiveFunctionValue(aoMemoryOfSolutions[i].getSolutionRepresentation());
			if(thisSolutionValue<bestSolutionValue)
				bestSolutionValue = thisSolutionValue;
		}
			
		return bestSolutionValue;
	}
	
	@Override
	public double getFunctionValue(int index) {
		
		// TODO
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		
		// TODO return an array of heuristic IDs based on the heuristic's type.
		
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		
		// TODO return the array of heuristic IDs that use depth of search.
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		
		// TODO return the array of heuristic IDs that use intensity of mutation.
	}

	@Override
	public int getNumberOfHeuristics() {

		// DONE - has to be hard-coded due to the design of the HyFlex framework...
		return 7;
	}

	@Override
	public int getNumberOfInstances() {

		// DONE return the number of available instances
		return aoMemoryOfSolutions.length;
	}

	@Override
	public void initialiseSolution(int index) {
		
		// TODO - initialise a solution in index 'index' 
		// 		making sure that you also update the best solution!
		
	}

	// TODO implement the instance reader that this method uses
	//		to correctly read in the PWP instance, and set up the objective function.
	@Override
	public void loadInstance(int instanceId) {

		String SEP = FileSystems.getDefault().getSeparator();
		String instanceName = "instances" + SEP + "pwp" + SEP + instanceFiles[instanceId] + ".pwp";

		Path path = Paths.get(instanceName);
		Random random = new Random(seed);
		PWPInstanceReader oPwpReader = new PWPInstanceReader();
		oInstance = oPwpReader.readPWPInstance(path, random);

		oObjectiveFunction = oInstance.getPWPObjectiveFunction();
		
		for(HeuristicInterface h : aoHeuristics) {
			h.setObjectiveFunction(oObjectiveFunction);
		}
	}

	@Override
	public void setMemorySize(int f) {

		// DONE sets a new memory size
		// IF the memory size is INCREASED, then
		//		the existing solutions should be copied to the new memory at the same indices.
		// IF the memory size is DECREASED, then
		//		the first 'size' solutions are copied to the new memory.
		
		// if f1 is less than or equal to 1 ignore it
		if(f>1) {
			
			//copy over solutions into new array of size f then overwrite the memory once done
			PWPSolutionInterface[] newMemory = new PWPSolutionInterface[f];
			for(int i=0; i<getNumberOfInstances(); i++)
				newMemory[i] = aoMemoryOfSolutions[i];
			aoMemoryOfSolutions = newMemory;
		}
	}

	@Override
	public String solutionToString(int index) {

		// TODO

	}

	@Override
	public String toString() {

		// DONE change 'AAA' to be your username
		return "psydm7's G52AIM PWP";
	}
	
	private void updateBestSolution(int index) {
		oBestSolution = aoMemoryOfSolutions[index];
	}
	
	@Override
	public PWPInstanceInterface getLoadedInstance() {

		return this.oInstance;
	}

	@Override
	public Location[] getRouteOrderedByLocations() {

		int[] city_ids = getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		Location[] route = Arrays.stream(city_ids).boxed().map(getLoadedInstance()::getLocationForDelivery).toArray(Location[]::new);
		return route;
	}
}
