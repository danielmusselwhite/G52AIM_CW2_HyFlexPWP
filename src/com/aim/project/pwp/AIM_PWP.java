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
import com.aim.project.pwp.solution.PWPSolution;
import com.aim.project.pwp.solution.SolutionRepresentation;
import com.aim.project.utilities.Utilities;

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
	
	private Random m_random;
		
	public AIM_PWP(long seed) {
		
		super(seed);
		this.seed = seed;
		// TODO - set default memory size and create the array of low-level heuristics
		
		m_random = new Random(seed);
		
		
		
		setMemorySize(2);//default memory size of 2 
		
		// setting up array of heuristics
		aoHeuristics = new HeuristicInterface[7];
		aoHeuristics[0] = new InversionMutation(m_random);
		aoHeuristics[1] = new AdjacentSwap(m_random);
		aoHeuristics[2] = new Reinsertion(m_random);
		aoHeuristics[3] = new NextDescent(m_random);
		aoHeuristics[4] = new DavissHillClimbing(m_random);
		aoHeuristics[5] = new OX(m_random);
		aoHeuristics[6] = new CX(m_random);
	}
	
	public PWPSolutionInterface getSolution(int index) {
		
		// DONE 
		return aoMemoryOfSolutions[index];
	}
	
	public PWPSolutionInterface getBestSolution() {
		
		return oBestSolution;
	}
	

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		
		
		// TODO - apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution
		
		// candidateSolution so we don't modify the main
		this.copySolution(currentIndex, candidateIndex);

		this.aoHeuristics[hIndex].apply(this.aoMemoryOfSolutions[candidateIndex], this.depthOfSearch, this.intensityOfMutation);
		
		// if it is also better than the current best value, update that
		if(this.getFunctionValue(currentIndex)<this.getBestSolutionValue())
			this.updateBestSolution(currentIndex);
		
		// return the value the new score is (may be same if it was rejected or better if it was accepted
		return this.getFunctionValue(candidateIndex);
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		
		// TODO - apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution
	
		
		SolutionRepresentation sr = (SolutionRepresentation) oInstance.createSolution(InitialisationMode.RANDOM).getSolutionRepresentation();
		double initialValue = oObjectiveFunction.getObjectiveFunctionValue(sr);
		
		aoMemoryOfSolutions[candidateIndex] = new PWPSolution(sr,initialValue);
		
		XOHeuristicInterface crossoverHeuristic = (XOHeuristicInterface) aoHeuristics[hIndex];
		crossoverHeuristic.apply(this.aoMemoryOfSolutions[parent1Index],  this.aoMemoryOfSolutions[parent2Index], this.aoMemoryOfSolutions[candidateIndex], this.depthOfSearch, this.intensityOfMutation);
	
		// if it is also better than the current best value, update that
		if(this.getFunctionValue(candidateIndex)<this.getBestSolutionValue())
			this.updateBestSolution(candidateIndex);
		
		
		return this.getFunctionValue(candidateIndex);
	}

	@Override
	public String bestSolutionToString() {
		
		// DONE return the location IDs of the best solution including DEPOT and HOME locations
		//		e.g. "DEPOT -> 0 -> 2 -> 1 -> HOME"
		
		String representation = new String();
		int[] locations = oBestSolution.getSolutionRepresentation().getSolutionRepresentation();
	
//		e.g. "DEPOT -> 0 -> 2 -> 1 -> HOME" 
		representation += "DEPOT -> ";
		for(int i=0; i<locations.length; i++) {
			representation += locations[i];
			representation+=" -> ";
		}
		representation+="HOME";
		
		return representation;
	}

	@Override
	public boolean compareSolutions(int iIndexA, int iIndexB) {

		// DONE return true if the objective values of the two solutions are the same, else false
		return aoMemoryOfSolutions[iIndexA].getObjectiveFunctionValue() == aoMemoryOfSolutions[iIndexB].getObjectiveFunctionValue();
	}

	@Override
	public void copySolution(int iIndexA, int iIndexB) {

		// DONE - BEWARE this should copy the solution, not the reference to it!
		//			That is, that if we apply a heuristic to the solution in index 'b',
		//			then it does not modify the solution in index 'a' or vice-versa.
		
		aoMemoryOfSolutions[iIndexB] = aoMemoryOfSolutions[iIndexA].clone();
	}

	@Override
	public double getBestSolutionValue() {
		// DONE
		return oBestSolution.getObjectiveFunctionValue();
	}
	
	@Override
	public double getFunctionValue(int index) {
		
		// DONE
		
		return this.aoMemoryOfSolutions[index].getObjectiveFunctionValue();
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		
		// TODO return an array of heuristic IDs based on the heuristic's type.
		switch(type) {
		case CROSSOVER:
			return new int[] {5,6};
		case LOCAL_SEARCH:
			return new int[] {3,4};
		case MUTATION:
			return new int[] {0,1,2};
		}
		
		return new int[] {};
		
//		ArrayList<Integer> ids = new ArrayList<Integer>();
//		
//		switch(type) {
//		
//		case CROSSOVER:
//			
//			// adding OX and CX
//			ids.add(5);
//			ids.add(6);
//			break;
//			
//			
//		case LOCAL_SEARCH:
//			
//			// adding next descent and davis bit
//			ids.add(3);
//			ids.add(4);
//			break;
//			
//			
//		case MUTATION:
//
//			//adding adjacentSwap, InversionMutation and Reinsertion
//			ids.add(0);
//			ids.add(1);
//			ids.add(2);
//			break;
//			
//			
//		default:
//			break;
//		}
//		
//		return Utilities.convertToArray(ids);
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		
		// TODO return the array of heuristic IDs that use depth of search.
		return new int[] {3,4};
		
//		ArrayList<Integer> alDOS = new ArrayList<Integer>();
//		
//		// for each heuristic check if it uses depth of search and if it does add its index to the array list
//		for(int i=0; i<this.aoHeuristics.length; i++)
//			if(aoHeuristics[i].usesDepthOfSearch())
//				alDOS.add(i);
//		
//		return Utilities.convertToArray(alDOS);
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		
		// TODO return the array of heuristic IDs that use intensity of mutation.
		return new int[] {0,1,2,5,6};
		
//		ArrayList<Integer> alIOM = new ArrayList<Integer>();
//		
//		// for each heuristic check if it uses intensity of mutation and if it does add its index to the array list
//		for(int i=0; i<this.aoHeuristics.length; i++)
//			if(aoHeuristics[i].usesIntensityOfMutation())
//				alIOM.add(i);
//		
//		return Utilities.convertToArray(alIOM);
	}

	@Override
	public int getNumberOfHeuristics() {

		// DONE - has to be hard-coded due to the design of the HyFlex framework...
		return 7;
	}

	@Override
	public int getNumberOfInstances() {

		// DONE return the number of available instances
		return instanceFiles.length;
	}

	@Override
	public void initialiseSolution(int index) {
		
		// DONE - initialise a solution in index 'index' 
		// 		making sure that you also update the best solution!
		
		SolutionRepresentation sr = (SolutionRepresentation) oInstance.createSolution(InitialisationMode.RANDOM).getSolutionRepresentation();
		double initialValue = oObjectiveFunction.getObjectiveFunctionValue(sr);
		
		aoMemoryOfSolutions[index] = new PWPSolution(sr, initialValue);
		
		//if there is a best solution..
		if(oBestSolution!=null) {
			//check if this one is better
			if(this.getFunctionValue(index)<this.getBestSolutionValue())
				this.updateBestSolution(index);
		}
		// if there isn't a best solution yet then this is the first and hence the current one to beat
		else {
			this.updateBestSolution(index);
		}
		
	}

	// DONE implement the instance reader that this method uses
	//		to correctly read in the PWP instance, and set up the objective function.
	@Override
	public void loadInstance(int instanceId) {

		String SEP = FileSystems.getDefault().getSeparator();
		String instanceName = "instances" + SEP + "pwp" + SEP + instanceFiles[instanceId] + ".pwp";

		Path path = Paths.get(instanceName);
		PWPInstanceReader oPwpReader = new PWPInstanceReader();
		oInstance = oPwpReader.readPWPInstance(path, m_random);

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
		
		if(aoMemoryOfSolutions!=null) 
			for(int i=0; i<aoMemoryOfSolutions.length && i<f; i++)
				newMemory[i] = aoMemoryOfSolutions[i];
			
		aoMemoryOfSolutions = newMemory;
		}
	}

	@Override
	public String solutionToString(int index) {

		// DONE
		String representation = new String();
		int[] locations = aoMemoryOfSolutions[index].getSolutionRepresentation().getSolutionRepresentation();
	
//		e.g. "DEPOT -> 0 -> 2 -> 1 -> HOME" 
		representation += "DEPOT -> ";
		for(int i=0; i<locations.length; i++) {
			representation += locations[i];
			representation+=" -> ";
		}
		representation+="HOME";
		
		return representation;
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
	
	
	// Just used at the very end to negate the floating point precision loss of information to allow for a more accurate comparison
	public double getTotalCostForBestSolution() {
		return oObjectiveFunction.getObjectiveFunctionValue(oBestSolution.getSolutionRepresentation());
	}
}
