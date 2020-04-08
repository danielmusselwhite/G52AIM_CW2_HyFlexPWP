package com.aim.project.pwp.hyperheuristics;


import java.util.Random;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.utilities.Utilities;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class MYHH_RWS_IS extends HyperHeuristic {

	private final int m_iDefaultScore, m_iLowerBound, m_iUpperBound;
	
	public MYHH_RWS_IS(long lSeed, int defaultScore, int lowerBound, int upperBound) {
		super(lSeed);
		
		this.m_iDefaultScore = defaultScore;//5
		this.m_iLowerBound = lowerBound;//1
		this.m_iUpperBound = upperBound;//10
	}

	
	@Override
	protected void solve(ProblemDomain oProblem) {
		// TODO Auto-generated method stub
		oProblem.setMemorySize(3);	// 0S, 1SOrune, 2XO

		
		oProblem.initialiseSolution(0); // holds the best
		oProblem.copySolution(0,1); // holds the one currently being explored
		double current = oProblem.getFunctionValue(0);
		
		oProblem.setIntensityOfMutation(0.2);
		oProblem.setDepthOfSearch(0.2);
		
		int h = 1;
		long iteration = 0;
		System.out.println("Iteration\tf(s)\tf(s')\tAccept");

		//creating new RWS with default values
		MyRouletteWheel rws = new MyRouletteWheel(this.rng, oProblem.getNumberOfHeuristics()-2, this.m_iDefaultScore, this.m_iLowerBound, this.m_iUpperBound);

		while(!hasTimeExpired() ) {
			
			// SELECTION
			
			h = rws.performRouletteWheelSelection();	// pick the heuristic

			// applying the heuristic
			
			double candidate;
			
			if(h < 5) {
				candidate = oProblem.applyHeuristic(h, 0, 1);
			} else {
				// perform XO on random solution and current solution to create new candidate
				oProblem.initialiseSolution(2);
				candidate = oProblem.applyHeuristic(h, 0, 2, 1);
			}
			
			
			
			
			
			
			// ACCEPTANCE
			
			// if this is a strict improvement on the candidateS
			if(candidate<current) {
				oProblem.copySolution(1, 0);	// update the best one
				current=candidate;
		
				rws.incrementScore(h); // increase the chance of picking this heuristic again
				
			}
			// else this doesn't improve on the last candidate S (and hence not the current best)
			else{
				// if this has resulted in a worse solution
				if(candidate>current)
					rws.decrementScore(h); // decrease the chance of picking this heuristic again
			
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
		return "MyHH-RWS-IS";
	}
	

	private class MyRouletteWheel {
		
		private int[] arrScores;
		private final Random m_rng;
		private final int m_iDefaultScore, m_iLowerBound, m_iUpperBound;
		
		public MyRouletteWheel(Random rng, int heuristicsCount, int defaultScore, int lowerBound, int upperBound) {
			this.m_iDefaultScore = defaultScore;//5
			this.m_iLowerBound = lowerBound;//1
			this.m_iUpperBound = upperBound;//10
			this.m_rng = rng;
			arrScores = new int[heuristicsCount];
			for(int i=0; i<heuristicsCount; i++)
				arrScores[i] = m_iDefaultScore;
		}
		
		public float getScore(int index) {
			return arrScores[index];
		}
		
		public void incrementScore(int index) {
			if(arrScores[index]+1 <= m_iUpperBound)
				arrScores[index]++;
		}
	
		public void decrementScore(int index) {
			if(arrScores[index]-1 >= m_iLowerBound)
				arrScores[index]--;
		}
		
		public int getTotalScore() {
			int sum = 0;
			for(int i=0; i<arrScores.length; i++)
				sum+=getScore(i);
			
			return sum;
		}
		
		public int performRouletteWheelSelection() {
			float selection = rng.nextInt(getTotalScore()-1); // random value between 0 (inc) and sum of scores (exc)
			
			//select heuristic by choosing first heuristic to have a cumulative score above the random value
			int sum = 0;
			for(int i=0; i<arrScores.length; i++) {
				sum+=getScore(i);
				if(sum>selection) {
					return i;
				}
			}
			
			return -1;
		}
	
	}

}
