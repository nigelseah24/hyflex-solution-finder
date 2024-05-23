package com.aim.project.uzf.heuristics;

import java.util.Random;

import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.solution.UZFSolution;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {



	public DavissHillClimbing(Random random) {

		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double dos, double iom) {

		// TODO

		// Calculate the number of internal iterations based on the intensity of mutation
		int numIterations;
		if (iom >= 0.0 && iom < 0.2) {
			numIterations = 1;
		} else if (iom >= 0.2 && iom < 0.4) {
			numIterations = 2;
		} else if (iom >= 0.4 && iom < 0.6) {
			numIterations = 3;
		} else if (iom >= 0.6 && iom < 0.8) {
			numIterations = 4;
		} else {
			numIterations = 5;
		}

		// Retrieve the solution representation
		int[] solutionRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();

		memory[CURRENT_SOLUTION_INDEX] = (UZFSolution) solution;
		memory[BACKUP_SOLUTION_INDEX] = (UZFSolution) solution.clone();

		// Get the objective value of the current solution
		int bestObjectiveValue = f.getObjectiveFunctionValue(memory[CURRENT_SOLUTION_INDEX].getSolutionRepresentation());
		int tempObjectiveValue;

		// Perform the hill climbing
		for (int i = 0; i < numIterations; i++) {
			int[] permutation = generateRandomPermutation(solutionRepresentation.length);
			for (int j = 0; j < solutionRepresentation.length; j++) {

				// adjacent swap as neighbourhood operator
				tempObjectiveValue = adjacentSwap(memory[CURRENT_SOLUTION_INDEX], permutation[j]);

				if (tempObjectiveValue < bestObjectiveValue) {
					bestObjectiveValue = tempObjectiveValue;
					copySolution(CURRENT_SOLUTION_INDEX, BACKUP_SOLUTION_INDEX);
				} else {
					copySolution(BACKUP_SOLUTION_INDEX, CURRENT_SOLUTION_INDEX);
				}
			}
		}

		solution.getSolutionRepresentation().setSolutionRepresentation(memory[CURRENT_SOLUTION_INDEX].getSolutionRepresentation().getSolutionRepresentation());

		return f.getObjectiveFunctionValue(memory[CURRENT_SOLUTION_INDEX].getSolutionRepresentation());
	}

	// Generate a random permutation
	private int[] generateRandomPermutation(int length) {
		int[] permutation = new int[length];
		for (int i = 0; i < length; i++) {
			permutation[i] = i;
		}
		for (int i = length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			int temp = permutation[index];
			permutation[index] = permutation[i];
			permutation[i] = temp;
		}
		return permutation;
	}

	@Override
	public boolean isCrossover() {

		// TODO
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		// TODO
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		return true;
	}
}
