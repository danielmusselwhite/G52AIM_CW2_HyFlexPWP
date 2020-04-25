package com.aim.project.pwp.hyperheuristics.oldMyHHs;


import java.util.Random;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.utilities.Utilities;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class MYHH_MS_IS extends HyperHeuristic {

	private final int m_memoryBufferSize;
	
	public MYHH_MS_IS(long lSeed, int memoryBufferSize) {
		super(lSeed);
		
		this.m_memoryBufferSize = memoryBufferSize;//10
	}

	
	@Override
	protected void solve(ProblemDomain oProblem) {
		// TODO Auto-generated method stub
		oProblem.setMemorySize(3);	// 0S, 1SPrime,2XO

		
		oProblem.initialiseSolution(0); // holds the best
		oProblem.copySolution(0,1); // holds the one currently being explored
		double current = oProblem.getFunctionValue(0);
		
		oProblem.setIntensityOfMutation(0.2);
		oProblem.setDepthOfSearch(0.2);
		
		int h = 1;
		long iteration = 0;
		System.out.println("Iteration\tf(s)\tf(s')\tAccept");

		//creating new MS with default values
		MemoryBasedSelection ms = new MemoryBasedSelection(this.rng, oProblem.getNumberOfHeuristics()-2, m_memoryBufferSize);

		
		while(!hasTimeExpired() ) {
			
			// SELECTION
			
			h = ms.performMemorySelection();	// pick the heuristic

			// applying the heuristic
			
			double candidate;
			
			if(h < 5) {
				candidate = oProblem.applyHeuristic(h, 0, 1);
			} else {
				// perform XO on random solution and current solution to create new candidate
				oProblem.initialiseSolution(2);
				candidate = oProblem.applyHeuristic(h, 0, 2, 1);
			}
			ms.updateHeuristicMemory(h, candidate/current);
			
			// ACCEPTANCE
			
			// if this is a strict improvement on the candidateS
			if(candidate<current) {
				oProblem.copySolution(1, 0);	// update the best one
				current=candidate;
			}

			iteration++;
		}
		
		PWPSolutionInterface oSolution = ((AIM_PWP) oProblem).getBestSolution();
		SolutionPrinter oSP = new SolutionPrinter("out.csv");
		oSP.printSolution( ((AIM_PWP) oProblem).oInstance.getSolutionAsListOfLocations(oSolution));
		Utilities.myPrintSolution("myOut.csv", ((AIM_PWP) oProblem).oInstance.getSolutionAsListOfLocations(oSolution));
		System.out.println(String.format("Total iterations = %d", iteration));
	}

	@Override
	public String toString() {
		return "MyHH-MS-IS";
	}
	

private class MemoryBasedSelection{
		
		private int m_heuristicCount;
		private int m_memoryBufferSize;
		private int[] m_nextMemoryLocation;
		private double[][] m_heuristicPerformanceBuffer;
		
		public MemoryBasedSelection(Random rng, int heuristicsCount, int memoryBufferSize) {
			m_heuristicCount=heuristicsCount;
			m_memoryBufferSize = memoryBufferSize;
			
			m_heuristicPerformanceBuffer = new double[heuristicsCount][memoryBufferSize];
			m_nextMemoryLocation= new int [heuristicsCount];

			//defaulted all to a very high value so they are all very likely to be picked at the start and then be reset to their percentage increase/decrease
			for(int i=0; i<m_heuristicPerformanceBuffer.length; i++)
				for(int j=0; j<m_heuristicPerformanceBuffer[i].length; j++)
					m_heuristicPerformanceBuffer[i][j]=1000d;
		}
		
		private int getHeuristicNextMemoryLocationToBeModified(int heuristicIndex) {
			int nextIndex = m_nextMemoryLocation[heuristicIndex];
			m_nextMemoryLocation[heuristicIndex]=(m_nextMemoryLocation[heuristicIndex]+1)%m_memoryBufferSize;
			return nextIndex;
		}
		
		public void updateHeuristicMemory(int heuristicIndex, double d) {
			m_heuristicPerformanceBuffer[heuristicIndex][getHeuristicNextMemoryLocationToBeModified(heuristicIndex)] = d;
		}
		
		private double getScore(int heuristicIndex, int memoryIndex) {
			return m_heuristicPerformanceBuffer[heuristicIndex][memoryIndex];
		}
		
		private double getHeuristicScore(int heuristicIndex) {
			double sum = 0;
			for(int j=0; j<m_heuristicPerformanceBuffer[heuristicIndex].length; j++)
				sum+=getScore(heuristicIndex,j);
			return sum;
		}
		
		private double getTotalScore() {
			double sum = 0;
			for(int i=0; i<m_heuristicPerformanceBuffer.length; i++)
				sum+=getHeuristicScore(i);
			return sum;
		}
		
		private double selectRandomScorePoint() {
			double total = getTotalScore();
			int totalInt = (int) total;
			double totalDouble = total - ((int) total); // total value - the total value without everything after the decimal place = just the decimal place
			if(totalInt==0)
				return rng.nextDouble()%totalDouble;
			
			return rng.nextInt(totalInt) + (rng.nextDouble()%totalDouble);
		}
		
		public int performMemorySelection() {
			
			double selection = selectRandomScorePoint();
			
			int h=0;
			//select heuristic by choosing first heuristic to have a cumulative score above the random value
			double sum=0;
			for(int i=0; i<m_heuristicPerformanceBuffer.length; i++) {
				sum+=getHeuristicScore(i);
				if(sum>selection) {
					h=i;
					break;
				}
					
			}
			return h;
		}
		
	}
	
}
