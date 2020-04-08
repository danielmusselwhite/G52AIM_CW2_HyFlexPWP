package com.aim.project.pwp.runners;


import com.aim.project.pwp.hyperheuristics.MYHH_MS_IS;
import com.aim.project.pwp.hyperheuristics.MYHH_MS_SA;
import com.aim.project.pwp.hyperheuristics.MYHH_RWS_SA;
import com.aim.project.pwp.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G. Jackson
 * Runs a simple random IE hyper-heuristic then displays the best solution found
 */
public class MYHH_MS_SA_VisualRunner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

		//SELECTION: Roulette Wheel
		//ACCEPTANCE:Simulated Annealing
		return new MYHH_MS_SA(seed,10);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new MYHH_MS_SA_VisualRunner();
		runner.run();
	}

}
