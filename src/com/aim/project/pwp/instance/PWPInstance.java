package com.aim.project.pwp.instance;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.IntStream;

import com.aim.project.pwp.PWPObjectiveFunction;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.solution.SolutionRepresentation;
import com.aim.project.utilities.Utilities;
import com.aim.project.pwp.solution.PWPSolution;


public class PWPInstance implements PWPInstanceInterface {
	
	private final Location[] aoLocations;
	
	private final Location oPostalDepotLocation;
	
	private final Location oHomeAddressLocation;
	
	private final int iNumberOfLocations;
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction = null;
	
	/**
	 * 
	 * @param numberOfLocations The TOTAL number of locations (including DEPOT and HOME).
	 * @param aoLocations The delivery locations.
	 * @param oPostalDepotLocation The DEPOT location.
	 * @param oHomeAddressLocation The HOME location.
	 * @param random The random number generator to use.
	 */
	public PWPInstance(int numberOfLocations, Location[] aoLocations, Location oPostalDepotLocation, Location oHomeAddressLocation, Random random) {
		
		this.iNumberOfLocations = numberOfLocations;
		this.oRandom = random;
		this.aoLocations = aoLocations;
		this.oPostalDepotLocation = oPostalDepotLocation;
		this.oHomeAddressLocation = oHomeAddressLocation;
	}

	@Override
	public PWPSolution createSolution(InitialisationMode mode) {
				
		int[] permutationRepresentation = null;
		
		switch(mode) {
			
		case RANDOM:
			// creating the initial permutation representation, in order
			permutationRepresentation = new int[iNumberOfLocations];
			for(int i=0; i<iNumberOfLocations; i++)
				permutationRepresentation[i]=i;
			// shuffling into a random permutation
			permutationRepresentation = Utilities.shuffleArray(permutationRepresentation, oRandom);
			break;
			
		default:
			return null;
		}
		
		// return new PWP solution based on this permutation representation
		SolutionRepresentation initialSolution = new SolutionRepresentation(permutationRepresentation);
		return new PWPSolution(initialSolution,  getPWPObjectiveFunction().getObjectiveFunctionValue(initialSolution));
	}
	
	@Override
	public ObjectiveFunctionInterface getPWPObjectiveFunction() {
		
		if(oObjectiveFunction == null) {
			this.oObjectiveFunction = new PWPObjectiveFunction(this);
		}

		return oObjectiveFunction;
	}

	@Override
	public int getNumberOfLocations() {

		return iNumberOfLocations;
	}

	@Override
	public Location getLocationForDelivery(int deliveryId) {

		return aoLocations[deliveryId];
	}

	@Override
	public Location getPostalDepot() {
		
		return this.oPostalDepotLocation;
	}

	@Override
	public Location getHomeAddress() {
		
		return this.oHomeAddressLocation;
	}
	
	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(PWPSolutionInterface oSolution) {
		
		// for each index in the oSolutions representation
		ArrayList<Location> currentSolution = new ArrayList<Location>();
		for(int i=0; i<oSolution.getNumberOfLocations(); i++) {
			//get the location represented by this index and add it to the array list
			int thisLocationsIndex = oSolution.getSolutionRepresentation().getSolutionRepresentation()[i];
			Location thisLocation = aoLocations[thisLocationsIndex];
			currentSolution.add(thisLocation);
		}
		
		return currentSolution;
	}

}
