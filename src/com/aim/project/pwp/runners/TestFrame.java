package com.aim.project.pwp.runners;

import java.awt.Color;
import java.util.Random;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.hyperheuristics.MYHH_MS1B_IS;
import com.aim.project.pwp.hyperheuristics.SR_IE_HH;
import com.aim.project.pwp.hyperheuristics.oldMyHHs.MYHH_MS1B_SA;
import com.aim.project.pwp.hyperheuristics.oldMyHHs.MYHH_MS_IS;
import com.aim.project.pwp.hyperheuristics.oldMyHHs.MYHH_MS_SA;
import com.aim.project.pwp.hyperheuristics.oldMyHHs.MYHH_RWS_IS;
import com.aim.project.pwp.hyperheuristics.oldMyHHs.MYHH_RWS_SA;
import com.aim.project.pwp.visualiser.PWPView;

import AbstractClasses.HyperHeuristic;


public class TestFrame {
	protected final long TIMELIMIT;
	protected final int NUMBER_OF_RUNS;
	protected final long[] SEEDS;
	protected final String NEW_LINE = System.lineSeparator();
	protected final TestFrameConfig CFG;
	protected final int RWS_DEFAULT, RWS_MIN, RWS_MAX;
	
	public TestFrame(TestFrameConfig config, long seed) {
		
		this.CFG = config;
		
		this.TIMELIMIT = CFG.getBenchmarkedTimeLimit();
		this.RWS_DEFAULT = CFG.getRWSDefault();
		this.RWS_MIN = CFG.getRWSMin();
		this.RWS_MAX = CFG.getRWSMax();
		
		// use a seeded random number generator to generate "TOTAL_RUNS" seeds
		Random random = new Random(seed);
		
		NUMBER_OF_RUNS = CFG.getTotalRuns();
		
		SEEDS = new long[NUMBER_OF_RUNS];
		
		for(int i = 0; i < NUMBER_OF_RUNS; i++) {
			SEEDS[i] = random.nextLong();
		}
	}
	
	public void logResult(String problem, String methodName, int runId, double bestSolutionValue, long seed) {
		
		System.out.println("HyperHeuristic: " + methodName + NEW_LINE +
			"Problem: " + problem + NEW_LINE +
			"Run ID: " + runId + NEW_LINE +
			"Seed: " + seed + NEW_LINE + 
			"Best Solution Value: " + bestSolutionValue + NEW_LINE + NEW_LINE + NEW_LINE);
	}
	
	public void runTests() {
		
		// using problem.getTotalCostForBestSolution() instead of hh.bestSolutionValue()
		// because by using the simple evaluation it will totally recalculate negating the floating point precision problem
		// whereas hh.bestSolutionValue() uses the best solution found by the hh's (who's llh's use delta evaluation which causes the floating point errors to materialise)
		// gives more accurate results
		

		
		
		
		
		
		
		
		//TODO MAKE NUMBER OF RUNS AGAIN
		for(int i=0; i<NUMBER_OF_RUNS; i++) {
			AIM_PWP problem = new AIM_PWP(SEEDS[i]);
			problem.loadInstance(2);
			HyperHeuristic myhh_ms_is = new MYHH_MS1B_IS(SEEDS[i],50,1);
			myhh_ms_is.setTimeLimit(TIMELIMIT);
			myhh_ms_is.loadProblemDomain(problem);
			myhh_ms_is.run();
			logResult("carparks-40",myhh_ms_is.toString()+"-50", i, problem.getTotalCostForBestSolution(), SEEDS[i]);
			
			AIM_PWP problem1 = new AIM_PWP(SEEDS[i]);
			problem1.loadInstance(2);
			HyperHeuristic sr_ie_hh = new SR_IE_HH(SEEDS[i]);
			sr_ie_hh.setTimeLimit(TIMELIMIT);
			sr_ie_hh.loadProblemDomain(problem1);
			sr_ie_hh.run();
			logResult("carparks-40",sr_ie_hh.toString(), i, problem1.getTotalCostForBestSolution(), SEEDS[i]);
		}
		
		//TODO MAKE NUMBER OF RUNS AGAIN
		for(int i=0; i<NUMBER_OF_RUNS; i++) {

			AIM_PWP problem = new AIM_PWP(SEEDS[i]);
			problem.loadInstance(3);
			HyperHeuristic myhh_ms_is = new MYHH_MS1B_IS(SEEDS[i],50,1);
			myhh_ms_is.setTimeLimit(TIMELIMIT);
			myhh_ms_is.loadProblemDomain(problem);
			myhh_ms_is.run();
			logResult("tramstops-85",myhh_ms_is.toString()+"-50", i, problem.getTotalCostForBestSolution(), SEEDS[i]);
			
			AIM_PWP problem1 = new AIM_PWP(SEEDS[i]);
			problem1.loadInstance(3);
			HyperHeuristic sr_ie_hh = new SR_IE_HH(SEEDS[i]);
			sr_ie_hh.setTimeLimit(TIMELIMIT);
			sr_ie_hh.loadProblemDomain(problem1);
			sr_ie_hh.run();
			logResult("tramstops-85",sr_ie_hh.toString(), i, problem1.getTotalCostForBestSolution(), SEEDS[i]);
		}
		
		//TODO MAKE NUMBER OF RUNS AGAIN
		for(int i=0; i<NUMBER_OF_RUNS; i++) {

			AIM_PWP problem = new AIM_PWP(SEEDS[i]);
			problem.loadInstance(4);
			HyperHeuristic myhh_ms_is = new MYHH_MS1B_IS(SEEDS[i],50,1);
			myhh_ms_is.setTimeLimit(TIMELIMIT);
			myhh_ms_is.loadProblemDomain(problem);
			myhh_ms_is.run();
			logResult("trafficsignals-446",myhh_ms_is.toString()+"-50", i, problem.getTotalCostForBestSolution(), SEEDS[i]);
			
			AIM_PWP problem1 = new AIM_PWP(SEEDS[i]);
			problem1.loadInstance(4);
			HyperHeuristic sr_ie_hh = new SR_IE_HH(SEEDS[i]);
			sr_ie_hh.setTimeLimit(TIMELIMIT);
			sr_ie_hh.loadProblemDomain(problem1);
			sr_ie_hh.run();
			logResult("trafficsignals-446",sr_ie_hh.toString(), i, problem1.getTotalCostForBestSolution(), SEEDS[i]);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	
	}

	public static void main(String [] args) {
		long seed = 31012020l;
		TestFrameConfig config = new TestFrameConfig();
		TestFrame frame = new TestFrame(config, seed);
		frame.runTests();
	}
}
