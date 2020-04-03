package com.aim.project.pwp.solution;

import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

/**
 * 
 * @author Warren G. Jackson
 * 
 *
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] aiSolutionRepresentation;

	public SolutionRepresentation(int[] aiRepresentation) {

		this.aiSolutionRepresentation = aiRepresentation;
	}

	@Override
	public int[] getSolutionRepresentation() {

		return aiSolutionRepresentation;
	}

	@Override
	public void setSolutionRepresentation(int[] aiSolutionRepresentation) {

		this.aiSolutionRepresentation = aiSolutionRepresentation;
	}

	@Override
	public int getNumberOfLocations() {
		// DONE return the total number of locations in this instance (includes DEPOT and HOME).
		return this.getSolutionRepresentation().length + 2; // all of the drop offs +2 (depot and home)
	}

	@Override
	public SolutionRepresentationInterface clone() {

		return new SolutionRepresentation(aiSolutionRepresentation.clone());
	}

}
