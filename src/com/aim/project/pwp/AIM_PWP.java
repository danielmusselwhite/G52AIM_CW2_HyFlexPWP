package com.aim.project.pwp;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import com.aim.project.utilities.Utilities;

import AbstractClasses.ProblemDomain;

public class AIM_PWP extends ProblemDomain implements Visualisable {

	private String[] instanceFiles = {
		"square", "libraries-15", "carparks-40", "tramstops-85", "trafficsignals-446", "streetlights-35714"
	};
	
	private PWPSolutionInterface[] aoMemoryOfSolutions;
	
	public PWPSolutionInterface oBestSolution;
	
	private int iBestSolutionIndex;
	
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
		aoHeuristics[0] = new AdjacentSwap(m_random);
		aoHeuristics[1] = new InversionMutation(m_random);
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

		this.aoHeuristics[hIndex].apply(this.aoMemoryOfSolutions[candidateIndex], 0.5d, 0.5d);
		
		// if this candidate is an improvement, accept it 
		if(this.getFunctionValue(candidateIndex)<this.getFunctionValue(currentIndex)) {
			this.copySolution(candidateIndex, currentIndex);
			
			// if it is also better than the current best value, update that
			if(this.getFunctionValue(currentIndex)<this.getBestSolutionValue())
				this.updateBestSolution(currentIndex);
		}
				
		// else reject it
		else
			this.copySolution(currentIndex, candidateIndex);
	
		// return the value the new score is (may be same if it was rejected or better if it was accepted
		return this.getFunctionValue(currentIndex);
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		
		// TODO - apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution
	
		
		XOHeuristicInterface crossoverHeuristic = (XOHeuristicInterface) aoHeuristics[hIndex];
		crossoverHeuristic.apply(this.aoMemoryOfSolutions[parent1Index],  this.aoMemoryOfSolutions[parent2Index], this.aoMemoryOfSolutions[candidateIndex], this.depthOfSearch, this.intensityOfMutation);
	
		return this.getFunctionValue(candidateIndex);
	}

	@Override
	public String bestSolutionToString() {
		
		// DONE return the location IDs of the best solution including DEPOT and HOME locations
		//		e.g. "DEPOT -> 0 -> 2 -> 1 -> HOME"
		
		return this.solutionToString(this.iBestSolutionIndex);
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
		//get the value of the first solution
		double bestSolutionValue = getFunctionValue(0);
		//go through each of the other solutions iteratively and check if they produce a better solution value
		for(int i=1; i<getNumberOfInstances(); i++) {
			double thisSolutionValue = getFunctionValue(i);
			if(thisSolutionValue<bestSolutionValue)
				bestSolutionValue = thisSolutionValue;
		}
			
		return bestSolutionValue;
	}
	
	@Override
	public double getFunctionValue(int index) {
		
		// DONE
		
		return this.aoMemoryOfSolutions[index].getObjectiveFunctionValue();
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		
		// TODO return an array of heuristic IDs based on the heuristic's type.

		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		switch(type) {
		
		case CROSSOVER:
			
			// check each heuristic to see if its a crossover and if it is, add its id
			for(int i=0; i<aoHeuristics.length; i++) 
				if(aoHeuristics[i].isCrossover())
					ids.add(i);
			break;
			
			
		case LOCAL_SEARCH:
			// check each heuristic to see if its a local search and if it is, add its id
			for(int i=0; i<aoHeuristics.length; i++) 
				if(aoHeuristics[i].usesDepthOfSearch())
					ids.add(i);
			break;
			
			
		case MUTATION:
			// check each heuristic to see if its a mutation and if it is, add its id
			for(int i=0; i<aoHeuristics.length; i++) 
				if(aoHeuristics[i].usesIntensityOfMutation())
					ids.add(i);
			break;
			
			
		default:
			break;
		}
		
		return Utilities.convertToArray(ids);
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		
		// DONE return the array of heuristic IDs that use depth of search.
		
		ArrayList<Integer> alDOS = new ArrayList<Integer>();
		
		// for each heuristic check if it uses depth of search and if it does add its index to the array list
		for(int i=0; i<this.aoHeuristics.length; i++)
			if(aoHeuristics[i].usesDepthOfSearch())
				alDOS.add(i);
		
		return Utilities.convertToArray(alDOS);
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		
		// DONE return the array of heuristic IDs that use intensity of mutation.
		
		ArrayList<Integer> alIOM = new ArrayList<Integer>();
		
		// for each heuristic check if it uses intensity of mutation and if it does add its index to the array list
		for(int i=0; i<this.aoHeuristics.length; i++)
			if(aoHeuristics[i].usesIntensityOfMutation())
				alIOM.add(i);
		
		return Utilities.convertToArray(alIOM);
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
		
		//PWPInstanceReader oPwpReader = new PWPInstanceReader();
		//aoMemoryOfSolutions[index]= oPwpReader;
				
		
		this.loadInstance(index);
		this.updateBestSolution(index);
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
			for(int i=0; i<getNumberOfInstances(); i++)
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
		iBestSolutionIndex = index;
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
