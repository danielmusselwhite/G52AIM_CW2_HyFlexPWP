package com.aim.project.pwp.hyperheuristics;


import java.util.Random;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class MyHH extends HyperHeuristic {

	private final int m_iDefaultScore, m_iLowerBound, m_iUpperBound;
	
	public MyHH(long lSeed, int defaultScore, int lowerBound, int upperBound) {
		super(lSeed);
		
		this.m_iDefaultScore = defaultScore;//5
		this.m_iLowerBound = lowerBound;//1
		this.m_iUpperBound = upperBound;//10
	}

	
	@Override
	protected void solve(ProblemDomain oProblem) {
		// TODO Auto-generated method stub
		oProblem.setMemorySize(4);	// 0Best, 1S, 2SPrime, 3ParentForXO

		
		oProblem.initialiseSolution(0); // holds the best
		oProblem.copySolution(0,1); // holds the one currently being explored
		double currentBest = oProblem.getFunctionValue(0);
		double candidateS = currentBest;
		
		oProblem.setIntensityOfMutation(0.2);
		oProblem.setDepthOfSearch(0.2);
		
		int h = 1;
		long iteration = 0;
		System.out.println("Iteration\tf(s)\tf(s')\tAccept");

		//creating new RWS with default values
		MyRouletteWheel rws = new MyRouletteWheel(this.rng, oProblem.getNumberOfHeuristics(), this.m_iDefaultScore, this.m_iLowerBound, this.m_iUpperBound);
		//creating cooling schedule
		MyGeometricCooling gc = new MyGeometricCooling(currentBest);
		
		while(!hasTimeExpired() ) {
			
			// SELECTION
			
			h = rws.performRouletteWheelSelection();	// pick the heuristic
			
			h=0;//always reinsertion
			
			// applying the heuristic
			
			double candidateSPrime;
			
			if(h < 5) {
				candidateSPrime = oProblem.applyHeuristic(h, 1, 2);
			} else {
				oProblem.initialiseSolution(3); // maybe don't pick a completely random and instead do a bit of exploration on the current?
				candidateSPrime = oProblem.applyHeuristic(h, 1, 3, 2);
			}
			
			
			
			
			
			
			// ACCEPTANCE
			
			// if this is a strict improvement on the candidateS
			if(candidateSPrime<candidateS) {
				oProblem.copySolution(2, 1);	// update s to be s'
				candidateS=candidateSPrime;
				
				// if this is also a strict improvement on the current best
				if(candidateSPrime<currentBest) {
					oProblem.copySolution(2, 0);	// update the best one
					currentBest=candidateSPrime;
				}
			
				rws.incrementScore(h, candidateS, candidateSPrime); // increase the chance of picking this heuristic again
				
			}
			// else this doesn't improve on the last candidate S (and hence not the current best)
			else{
				// if this has resulted in a worse solution
				if(candidateSPrime>candidateS)
					rws.decrementScore(h, candidateS, candidateSPrime); // decrease the chance of picking this heuristic again
			
				
				// calculate difference between cost of s' and s, delta < 0 means improvement made
				double delta = Math.abs(oProblem.getFunctionValue(2) - oProblem.getFunctionValue(1)); // delta <- f(s') - f(s*);
				double r = rng.nextDouble(); // r  <- random \in [0,1];
				
				// accept with boltzmann probability
				if(r<gc.boltzmannProbability(delta, gc.getCurrentTemperature()))
					oProblem.copySolution(2,1); //update s to be s' despite being worse BUT do not update the actual best
			}
				
			gc.advanceTemperature(this.getElapsedTime()/this.getTimeLimit());
			iteration++;
		}
		
		PWPSolutionInterface oSolution = ((AIM_PWP) oProblem).getBestSolution();
		SolutionPrinter oSP = new SolutionPrinter("out.csv");
		oSP.printSolution( ((AIM_PWP) oProblem).oInstance.getSolutionAsListOfLocations(oSolution));
		System.out.println(String.format("Total iterations = %d", iteration));
	}

	@Override
	public String toString() {
		return "MyHH";
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
				arrScores[i] = defaultScore;
		}
		
		public float getScore(int index) {
			return arrScores[index];
		}
		
		// maybe make it do percentage increase based on how much it improved?
		public void incrementScore(int index, double sCost, double sPrimeCost) {
			if(arrScores[index]+1 <= m_iUpperBound)
				arrScores[index]++;
			
//			if((int) Math.ceil(arrScores[index]*1.1d) <= m_iUpperBound)
//				arrScores[index] = (int) Math.ceil(arrScores[index]*1.1d);
			
//			if((int) Math.ceil(arrScores[index]*sPrimeCost/sCost) > m_iLowerBound)
//				arrScores[index]=(int) Math.ceil(arrScores[index]* ((float)sPrimeCost/(float)sCost));
		}
	
		// maybe make it do percentage decrease based on how much it improved?
		public void decrementScore(int index, double sCost, double sPrimeCost) {
			if(arrScores[index]-1 >= m_iLowerBound)
				arrScores[index]--;
//			
//			if((int) Math.ceil(arrScores[index]*0.9d) <= m_iLowerBound)
//				arrScores[index] = (int) Math.ceil(arrScores[index]*1.1d);
			
//			if((int) Math.ceil(arrScores[index]*sPrimeCost/sCost) > m_iLowerBound)
//				arrScores[index]=(int) Math.ceil(arrScores[index]* ((float)sPrimeCost/(float)sCost));
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
	
	private class MyGeometricCooling{
		private double m_dCurrentTemperature;
		
		public MyGeometricCooling(double initialSolutionFitness) {
			
			double c = 1.0d;
			this.m_dCurrentTemperature = c * initialSolutionFitness;
		}
		
		public double getCurrentTemperature() {
			return this.m_dCurrentTemperature;
		}
		
		public void advanceTemperature(double alpha) {
			this.m_dCurrentTemperature=alpha*this.m_dCurrentTemperature;
		}
		
		public double boltzmannProbability(double delta, double T) {
			return Math.exp(-delta/T);
		}

	}
}
