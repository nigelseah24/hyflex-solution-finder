package com.aim.project.uzf.runners;

import com.aim.project.uzf.hyperheuristics.DMA_HH;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 *
 * Runs a simple random IE hyper-heuristic then displays the best solution found
 */
public class DMA_VisualRunner extends HH_Runner_Visual {

    public DMA_VisualRunner(int instanceId) {
        super(instanceId);
    }
    @Override
    protected HyperHeuristic getHyperHeuristic(long seed) {

        return new DMA_HH(seed);
    }

    public static void main(String [] args) {

        DMA_VisualRunner runner = new DMA_VisualRunner(3);
        runner.run();
    }

}
