package com.aim.project.uzf.heuristics;


import java.util.Random;

import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.solution.UZFSolution;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class NextDescent extends HeuristicOperators implements HeuristicInterface {

	public NextDescent(Random random) {

		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double dos, double iom) {

		// TODO
		// Calculate the number of internal iterations based on the depthOfSearch
		int numIterations;
		if (dos >= 0.0 && dos < 0.2) {
			numIterations = 1;
		} else if (dos >= 0.2 && dos < 0.4) {
			numIterations = 2;
		} else if (dos >= 0.4 && dos < 0.6) {
			numIterations = 3;
		} else if (dos >= 0.6 && dos < 0.8) {
			numIterations = 4;
		} else {
			numIterations = 5;
		}

		// Retrieve the solution representation
		int[] solutionRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();

		// Copy the solution to CURRENT_SOLUTION_INDEX and BACKUP_SOLUTION_INDEX
		memory[CURRENT_SOLUTION_INDEX] = (UZFSolution) solution;
		memory[BACKUP_SOLUTION_INDEX] = (UZFSolution) solution.clone();

		// Get the objective value of the current solution
		int bestObjectiveValue = f.getObjectiveFunctionValue(memory[CURRENT_SOLUTION_INDEX].getSolutionRepresentation());
		int tempObjectiveValue;

		// Perform the hill climbing
		for (int i = 0; i < numIterations; i++) {
			for (int j = random.nextInt(solutionRepresentation.length); j < solutionRepresentation.length; j++) {

				// adjacent swap as neighbourhood operator
				tempObjectiveValue = adjacentSwap(memory[CURRENT_SOLUTION_INDEX], j);

				if (tempObjectiveValue < bestObjectiveValue) {
					// Update the best objective value
					bestObjectiveValue = tempObjectiveValue;
					copySolution(CURRENT_SOLUTION_INDEX, BACKUP_SOLUTION_INDEX);
				} else {
					// Restore the solution to the backup index
					copySolution(BACKUP_SOLUTION_INDEX, CURRENT_SOLUTION_INDEX);
				}
			}
		}

		solution.getSolutionRepresentation().setSolutionRepresentation(memory[CURRENT_SOLUTION_INDEX].getSolutionRepresentation().getSolutionRepresentation());

		// Return the objective function value of the current solution
		return f.getObjectiveFunctionValue(memory[CURRENT_SOLUTION_INDEX].getSolutionRepresentation());
	}

	@Override
	public boolean isCrossover() {

		// TODO
		return random.nextBoolean();
	}

	@Override
	public boolean usesIntensityOfMutation() {

		// TODO
		return random.nextBoolean();
	}

	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		return random.nextBoolean();
	}
}
