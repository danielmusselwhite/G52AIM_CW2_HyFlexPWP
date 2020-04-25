package com.aim.project.pwp.runners;


import com.aim.project.pwp.hyperheuristics.SR_IE_HH;
import com.aim.project.pwp.hyperheuristics.oldMyHHs.MYHH_RWS_SA;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G. Jackson
 * Runs a simple random IE hyper-heuristic then displays the best solution found
 */
public class MYHH_RWS_SA_VisualRunner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

		//SELECTION: Roulette Wheel
		//ACCEPTANCE:Simulated Annealing
		return new MYHH_RWS_SA(seed,7, 1, 14);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new MYHH_RWS_SA_VisualRunner();
		runner.run();
	}

}
