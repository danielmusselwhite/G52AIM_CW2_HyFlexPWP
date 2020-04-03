package com.aim.project.pwp.runners;


import com.aim.project.pwp.hyperheuristics.MyHH;
import com.aim.project.pwp.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G. Jackson
 * Runs a simple random IE hyper-heuristic then displays the best solution found
 */
public class MYHH_VisualRunner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

		//SELECTION: Roulette Wheel
		//ACCEPTANCE:Simulated Annealing
		return new MyHH(seed, 7, 1, 14);
//		return new MyHH(seed, 5, 1, 10);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new MYHH_VisualRunner();
		runner.run();
	}

}
