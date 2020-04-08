package com.aim.project.pwp.runners;

import java.util.Random;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.hyperheuristics.MYHH_MS1B_IS;
import com.aim.project.pwp.hyperheuristics.MYHH_MS_IS;
import com.aim.project.pwp.hyperheuristics.MYHH_MS_SA;
import com.aim.project.pwp.hyperheuristics.MYHH_RWS_IS;
import com.aim.project.pwp.hyperheuristics.MYHH_RWS_SA;
import com.aim.project.pwp.hyperheuristics.SR_IE_HH;

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
		
		
//		for(int i=0; i<5; i++) {
//			AIM_PWP problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(3);
//			HyperHeuristic sr_ie_hh = new SR_IE_HH(SEEDS[i]);
//			sr_ie_hh.setTimeLimit(TIMELIMIT);
//			sr_ie_hh.loadProblemDomain(problem);
//			sr_ie_hh.run();
//			logResult("tramstops-85",sr_ie_hh.toString(), i, sr_ie_hh.getBestSolutionValue(), SEEDS[i]);
//			
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(3);
//			HyperHeuristic myhh_rws_sa = new MYHH_RWS_SA(SEEDS[i],RWS_DEFAULT, RWS_MIN, RWS_MAX);
//			myhh_rws_sa.setTimeLimit(TIMELIMIT);
//			myhh_rws_sa.loadProblemDomain(problem);
//			myhh_rws_sa.run();
//			logResult("tramstops-85",myhh_rws_sa.toString(), i, myhh_rws_sa.getBestSolutionValue(), SEEDS[i]);
////			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			HyperHeuristic myhh_rws_is = new MYHH_RWS_IS(SEEDS[i],RWS_DEFAULT, RWS_MIN, RWS_MAX);
////			myhh_rws_is.setTimeLimit(TIMELIMIT);
////			myhh_rws_is.loadProblemDomain(problem);
////			myhh_rws_is.run();
////			logResult("tramstops-85",myhh_rws_is.toString(), i, myhh_rws_is.getBestSolutionValue(), SEEDS[i]);
//			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			HyperHeuristic myhh_ms_sa = new MYHH_MS_SA(SEEDS[i],3);
////			myhh_ms_sa.setTimeLimit(TIMELIMIT);
////			myhh_ms_sa.loadProblemDomain(problem);
////			myhh_ms_sa.run();
////			logResult("tramstops-85",myhh_ms_sa.toString()+"-3", i, myhh_ms_sa.getBestSolutionValue(), SEEDS[i]);
////			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			HyperHeuristic myhh_ms_is = new MYHH_MS_IS(SEEDS[i],3);
////			myhh_ms_is.setTimeLimit(TIMELIMIT);
////			myhh_ms_is.loadProblemDomain(problem);
////			myhh_ms_is.run();
////			logResult("tramstops-85",myhh_ms_is.toString()+"-3", i, myhh_ms_is.getBestSolutionValue(), SEEDS[i]);
////			
////			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			myhh_ms_sa = new MYHH_MS_SA(SEEDS[i],5);
////			myhh_ms_sa.setTimeLimit(TIMELIMIT);
////			myhh_ms_sa.loadProblemDomain(problem);
////			myhh_ms_sa.run();
////			logResult("tramstops-85",myhh_ms_sa.toString()+"-5", i, myhh_ms_sa.getBestSolutionValue(), SEEDS[i]);
////			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			myhh_ms_is = new MYHH_MS_IS(SEEDS[i],5);
////			myhh_ms_is.setTimeLimit(TIMELIMIT);
////			myhh_ms_is.loadProblemDomain(problem);
////			myhh_ms_is.run();
////			logResult("tramstops-85",myhh_ms_is.toString()+"-5", i, myhh_ms_is.getBestSolutionValue(), SEEDS[i]);
////
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			myhh_ms_sa = new MYHH_MS_SA(SEEDS[i],10);
////			myhh_ms_sa.setTimeLimit(TIMELIMIT);
////			myhh_ms_sa.loadProblemDomain(problem);
////			myhh_ms_sa.run();
////			logResult("tramstops-85",myhh_ms_sa.toString()+"-10", i, myhh_ms_sa.getBestSolutionValue(), SEEDS[i]);
//			
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(3);
//			HyperHeuristic myhh_ms_is = new MYHH_MS_IS(SEEDS[i],10);
//			myhh_ms_is.setTimeLimit(TIMELIMIT);
//			myhh_ms_is.loadProblemDomain(problem);
//			myhh_ms_is.run();
//			logResult("tramstops-85",myhh_ms_is.toString()+"-10", i, myhh_ms_is.getBestSolutionValue(), SEEDS[i]);
////			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			myhh_ms_sa = new MYHH_MS_SA(SEEDS[i],25);
////			myhh_ms_sa.setTimeLimit(TIMELIMIT);
////			myhh_ms_sa.loadProblemDomain(problem);
////			myhh_ms_sa.run();
////			logResult("tramstops-85",myhh_ms_sa.toString()+"-25", i, myhh_ms_sa.getBestSolutionValue(), SEEDS[i]);
////			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			myhh_ms_is = new MYHH_MS_IS(SEEDS[i],25);
////			myhh_ms_is.setTimeLimit(TIMELIMIT);
////			myhh_ms_is.loadProblemDomain(problem);
////			myhh_ms_is.run();
////			logResult("tramstops-85",myhh_ms_is.toString()+"-25", i, myhh_ms_is.getBestSolutionValue(), SEEDS[i]);
////			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			myhh_ms_sa = new MYHH_MS_SA(SEEDS[i],50);
////			myhh_ms_sa.setTimeLimit(TIMELIMIT);
////			myhh_ms_sa.loadProblemDomain(problem);
////			myhh_ms_sa.run();
////			logResult("tramstops-85",myhh_ms_sa.toString()+"-50", i, myhh_ms_sa.getBestSolutionValue(), SEEDS[i]);
////			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			myhh_ms_is = new MYHH_MS_IS(SEEDS[i],50);
////			myhh_ms_is.setTimeLimit(TIMELIMIT);
////			myhh_ms_is.loadProblemDomain(problem);
////			myhh_ms_is.run();
////			logResult("tramstops-85",myhh_ms_is.toString()+"-50", i, myhh_ms_is.getBestSolutionValue(), SEEDS[i]);
////			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			myhh_ms_sa = new MYHH_MS_SA(SEEDS[i],100);
////			myhh_ms_sa.setTimeLimit(TIMELIMIT);
////			myhh_ms_sa.loadProblemDomain(problem);
////			myhh_ms_sa.run();
////			logResult("tramstops-85",myhh_ms_sa.toString()+"-100", i, myhh_ms_sa.getBestSolutionValue(), SEEDS[i]);
////			
////			problem = new AIM_PWP(SEEDS[i]);
////			problem.loadInstance(3);
////			myhh_ms_is = new MYHH_MS_IS(SEEDS[i],100);
////			myhh_ms_is.setTimeLimit(TIMELIMIT);
////			myhh_ms_is.loadProblemDomain(problem);
////			myhh_ms_is.run();
////			logResult("tramstops-85",myhh_ms_is.toString()+"-100", i, myhh_ms_is.getBestSolutionValue(), SEEDS[i]);
//		
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(3);
//			HyperHeuristic myhh_ms1b_is = new MYHH_MS1B_IS(SEEDS[i],200,1);
//			myhh_ms1b_is.setTimeLimit(TIMELIMIT);
//			myhh_ms1b_is.loadProblemDomain(problem);
//			myhh_ms1b_is.run();
//			logResult("tramstops-85",myhh_ms1b_is.toString()+"-200", i, myhh_ms1b_is.getBestSolutionValue(), SEEDS[i]);
//		
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(3);
//			myhh_ms1b_is = new MYHH_MS1B_IS(SEEDS[i],100,1);
//			myhh_ms1b_is.setTimeLimit(TIMELIMIT);
//			myhh_ms1b_is.loadProblemDomain(problem);
//			myhh_ms1b_is.run();
//			logResult("tramstops-85",myhh_ms1b_is.toString()+"-100", i, myhh_ms1b_is.getBestSolutionValue(), SEEDS[i]);
//			
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(3);
//			myhh_ms1b_is = new MYHH_MS1B_IS(SEEDS[i],50,1);
//			myhh_ms1b_is.setTimeLimit(TIMELIMIT);
//			myhh_ms1b_is.loadProblemDomain(problem);
//			myhh_ms1b_is.run();
//			logResult("tramstops-85",myhh_ms1b_is.toString()+"-50", i, myhh_ms1b_is.getBestSolutionValue(), SEEDS[i]);
//			
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(3);
//			myhh_ms1b_is = new MYHH_MS1B_IS(SEEDS[i],10,1);
//			myhh_ms1b_is.setTimeLimit(TIMELIMIT);
//			myhh_ms1b_is.loadProblemDomain(problem);
//			myhh_ms1b_is.run();
//			logResult("tramstops-85",myhh_ms1b_is.toString()+"-10", i, myhh_ms1b_is.getBestSolutionValue(), SEEDS[i]);
//		}
//		
//		for(int i=0; i<5; i++) {
//			AIM_PWP problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(4);
//			HyperHeuristic sr_ie_hh = new SR_IE_HH(SEEDS[i]);
//			sr_ie_hh.setTimeLimit(TIMELIMIT);
//			sr_ie_hh.loadProblemDomain(problem);
//			sr_ie_hh.run();
//			logResult("trafficsignals-446",sr_ie_hh.toString(), i, sr_ie_hh.getBestSolutionValue(), SEEDS[i]);
//			
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(4);
//			HyperHeuristic myhh_rws_sa = new MYHH_RWS_SA(SEEDS[i],RWS_DEFAULT, RWS_MIN, RWS_MAX);
//			myhh_rws_sa.setTimeLimit(TIMELIMIT);
//			myhh_rws_sa.loadProblemDomain(problem);
//			myhh_rws_sa.run();
//			logResult("trafficsignals-446",myhh_rws_sa.toString(), i, myhh_rws_sa.getBestSolutionValue(), SEEDS[i]);
//
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(4);
//			HyperHeuristic myhh_ms_is = new MYHH_MS_IS(SEEDS[i],10);
//			myhh_ms_is.setTimeLimit(TIMELIMIT);
//			myhh_ms_is.loadProblemDomain(problem);
//			myhh_ms_is.run();
//			logResult("trafficsignals-446",myhh_ms_is.toString()+"-10", i, myhh_ms_is.getBestSolutionValue(), SEEDS[i]);
//
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(4);
//			HyperHeuristic myhh_ms1b_is = new MYHH_MS1B_IS(SEEDS[i],200,1);
//			myhh_ms1b_is.setTimeLimit(TIMELIMIT);
//			myhh_ms1b_is.loadProblemDomain(problem);
//			myhh_ms1b_is.run();
//			logResult("trafficsignals-446",myhh_ms1b_is.toString()+"-200", i, myhh_ms1b_is.getBestSolutionValue(), SEEDS[i]);
//		
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(4);
//			myhh_ms1b_is = new MYHH_MS1B_IS(SEEDS[i],100,1);
//			myhh_ms1b_is.setTimeLimit(TIMELIMIT);
//			myhh_ms1b_is.loadProblemDomain(problem);
//			myhh_ms1b_is.run();
//			logResult("trafficsignals-446",myhh_ms1b_is.toString()+"-100", i, myhh_ms1b_is.getBestSolutionValue(), SEEDS[i]);
//			
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(4);
//			myhh_ms1b_is = new MYHH_MS1B_IS(SEEDS[i],50,1);
//			myhh_ms1b_is.setTimeLimit(TIMELIMIT);
//			myhh_ms1b_is.loadProblemDomain(problem);
//			myhh_ms1b_is.run();
//			logResult("trafficsignals-446",myhh_ms1b_is.toString()+"-50", i, myhh_ms1b_is.getBestSolutionValue(), SEEDS[i]);
//			
//			problem = new AIM_PWP(SEEDS[i]);
//			problem.loadInstance(4);
//			myhh_ms1b_is = new MYHH_MS1B_IS(SEEDS[i],10,1);
//			myhh_ms1b_is.setTimeLimit(TIMELIMIT);
//			myhh_ms1b_is.loadProblemDomain(problem);
//			myhh_ms1b_is.run();
//			logResult("trafficsignals-446",myhh_ms1b_is.toString()+"-10", i, myhh_ms1b_is.getBestSolutionValue(), SEEDS[i]);
//		}
	}

	public static void main(String [] args) {
		long seed = 31012020l;
		TestFrameConfig config = new TestFrameConfig();
		TestFrame frame = new TestFrame(config, seed);
		frame.runTests();
	}
}
