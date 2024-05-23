package com.aim.project.uzf.runners;


import com.aim.project.uzf.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 *
 * Runs a simple random IE hyper-heuristic then displays the best solution found
 */
public class SR_IE_VisualRunner extends HH_Runner_Visual {

	public SR_IE_VisualRunner(int instanceId) {
		super(instanceId);
	}
	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

		return new SR_IE_HH(seed);
	}

	public static void main(String [] args) {

		HH_Runner_Visual runner = new SR_IE_VisualRunner(3);
		runner.run();
	}

}
