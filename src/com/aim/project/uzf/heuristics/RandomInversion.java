package com.aim.project.uzf.heuristics;

import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;

import java.util.Random;

public class RandomInversion extends HeuristicOperators implements HeuristicInterface {
    public RandomInversion(Random random) {
        super(random);
    }

    @Override
    public int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

        int numberOfSwaps;
        if (intensityOfMutation == 1) {
            numberOfSwaps = 32;
        } else if (intensityOfMutation >= 0.8) {
            numberOfSwaps = 16;
        } else if (intensityOfMutation >= 0.6) {
            numberOfSwaps = 8;
        } else if (intensityOfMutation >= 0.4) {
            numberOfSwaps = 4;
        } else if (intensityOfMutation >= 0.2) {
            numberOfSwaps = 2;
        } else {
            numberOfSwaps = 1;
        }

        int[] solutionRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();
        for (int i = 0; i < numberOfSwaps; i++) {
            int firstIndex = random.nextInt(solutionRepresentation.length);
            int secondIndex = random.nextInt(solutionRepresentation.length);
            if (secondIndex == firstIndex) {
                secondIndex = (secondIndex + 1) % solutionRepresentation.length;
            }

            int temp = solutionRepresentation[firstIndex];
            solutionRepresentation[firstIndex] = solutionRepresentation[secondIndex];
            solutionRepresentation[secondIndex] = temp;
        }

        solution.getSolutionRepresentation().setSolutionRepresentation(solutionRepresentation);

        return f.getObjectiveFunctionValue(solution.getSolutionRepresentation());
    }

    @Override
    public boolean isCrossover() {
        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        return true;
    }

    @Override
    public boolean usesDepthOfSearch() {
        return false;
    }
}
/*
* The Random Inversion heuristic efficiently explores solution space by randomly swapping pairs of
* elements in the solution representation. The intensity of mutation parameter controls the number
* of swaps performed, allowing for adaptive exploration. By employing randomization, it achieves
* diversification of the search process, potentially escaping local optima. This heuristic's
* implementation directly modifies the solution representation, simplifying its application within the
* optimization framework.
* */
