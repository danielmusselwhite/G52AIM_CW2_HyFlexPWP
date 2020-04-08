package com.aim.project.pwp.hyperheuristics;


import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.utilities.Utilities;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class MYHH_MS1B_IS extends HyperHeuristic {

	private final int m_memoryBufferSize;
	private final double m_defaultScore;
	
	public MYHH_MS1B_IS(long lSeed, int memoryBufferSize, double defaultScore) {
		super(lSeed);
		
		this.m_memoryBufferSize = memoryBufferSize;//10
		this.m_defaultScore = defaultScore;
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
		MemoryBasedSelection ms = new MemoryBasedSelection(this.rng, oProblem.getNumberOfHeuristics()-2, m_memoryBufferSize, m_defaultScore);

		
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
		return "MyHH-MS1B-IS";
	}

private class MemoryBasedSelection{
		
		private int m_heuristicCount;
		private int m_memoryBufferSize;
		private ArrayList<HeuristicScorePair> m_heuristicPerformanceBuffer;
		private int m_defaultScore;
		
		public MemoryBasedSelection(Random rng, int heuristicsCount, int memoryBufferSize, double defaultScore) {
			m_heuristicCount=heuristicsCount;
			m_memoryBufferSize = memoryBufferSize;
			
			m_heuristicPerformanceBuffer = new ArrayList<HeuristicScorePair>();

		}
		
		//adding new score and if the size is now greater than the limit, remove the first element from the queue
		public void updateHeuristicMemory(int heuristicIndex, double d) {
			m_heuristicPerformanceBuffer.add(new HeuristicScorePair(heuristicIndex, d));
			
			if(m_heuristicPerformanceBuffer.size()>m_memoryBufferSize)
				m_heuristicPerformanceBuffer.remove(0);
		}
		
		private double[] getHeuristicScores() {

			int[] heuristicCount = new int[m_heuristicCount];
			double[] heuristicScores = new double [m_heuristicCount];
			
			//getting total of all heuristics in the buffer (used to calculate average)
			for(int i=0; i<m_heuristicPerformanceBuffer.size(); i++) {
				HeuristicScorePair thisHeuristic = this.m_heuristicPerformanceBuffer.get(i);
				heuristicScores[thisHeuristic.getHeuristicID()]+=thisHeuristic.getScore();	
				heuristicCount[thisHeuristic.getHeuristicID()]++;
			}
			
			//calculating final scores for all of the scores in the buffer
			for(int i=0; i<heuristicScores.length; i++) {
				if(heuristicCount[i]==0)
					heuristicScores[i]=m_defaultScore;
				else
					heuristicScores[i]/=heuristicCount[i];
			}
				
			return heuristicScores;
		}
		
		private double getTotalHeuristicScores(double[] heuristicScores) {
			double sum=0;
			for(int i=0; i<heuristicScores.length; i++)
				sum+=heuristicScores[i];
			return sum;
		}
		
		private double selectRandomScorePoint(double[] heuristicScores) {
			double total = getTotalHeuristicScores(heuristicScores);
			
			int totalInt = (int) total;
			double totalDouble = total - ((int) total); // total value - the total value without everything after the decimal place = just the decimal place
			if(totalInt==0)
				return rng.nextDouble()%totalDouble;
			
			return rng.nextInt(totalInt) + (rng.nextDouble()%totalDouble);
		}
		
		public int performMemorySelection() {
			double[] heuristicScores = getHeuristicScores();
			
			double selection = selectRandomScorePoint(heuristicScores);
			
			int h=0;
			//select heuristic by choosing first heuristic to have a cumulative score above the random value
			double sum=0;
			for(int i=0; i<heuristicScores.length; i++) {
				sum+=heuristicScores[i];
				if(sum>selection) {
					h=i;
					break;
				}
					
			}
			return h;
		}
		
	}

	private class HeuristicScorePair{
		private final int m_heuristicID;
		private final double m_score;
		public HeuristicScorePair(int hid, double score) {
			this.m_score = score;
			this.m_heuristicID = hid;
		}
		
		public int getHeuristicID() {
			return m_heuristicID;
		}
		
		public double getScore() {
			return m_score;
		}
	}
	
}
