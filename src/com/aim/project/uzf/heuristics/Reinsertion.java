package com.aim.project.uzf.heuristics;

import java.util.Random;

import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.solution.UZFSolution;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {


	public Reinsertion(Random random) {

		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// Retrieve the solution representation
		int[] solutionRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();

		// Copy the solution to CURRENT_SOLUTION_INDEX and BACKUP_SOLUTION_INDEX
		memory[CURRENT_SOLUTION_INDEX] = (UZFSolution) solution;
	 	memory[BACKUP_SOLUTION_INDEX] = (UZFSolution) solution.clone();

		// Calculate the number of internal reinsertions based on the intensityOfMutation
		int numReinsertions;
		if (intensityOfMutation >= 0.0 && intensityOfMutation < 0.2) {
			numReinsertions = 1;
		} else if (intensityOfMutation >= 0.2 && intensityOfMutation < 0.4) {
			numReinsertions = 2;
		} else if (intensityOfMutation >= 0.4 && intensityOfMutation < 0.6) {
			numReinsertions = 3;
		} else if (intensityOfMutation >= 0.6 && intensityOfMutation < 0.8) {
			numReinsertions = 4;
		} else {
			numReinsertions = 5;
		}

		// Perform internal reinsertions
		for (int i = 0; i < numReinsertions; i++) {
			// Randomly select an element to remove
			int indexToRemove = random.nextInt(solutionRepresentation.length);
			int indexToInsert = random.nextInt(solutionRepresentation.length);

			if(indexToInsert == indexToRemove) {
				indexToInsert = (indexToInsert + 1) % solutionRepresentation.length;
			}

			if(indexToInsert < indexToRemove) { // reinsert element to the left
				int temp = solutionRepresentation[indexToRemove];
				for(int j = indexToRemove; j > indexToInsert; j--) {
					solutionRepresentation[j] = solutionRepresentation[j - 1];
				}
				solutionRepresentation[indexToInsert] = temp;
			} else { // reinsert element to the right
				int temp = solutionRepresentation[indexToRemove];
				for(int j = indexToRemove; j < indexToInsert; j++) {
					solutionRepresentation[j] = solutionRepresentation[j + 1];
				}
				solutionRepresentation[indexToInsert] = temp;
			}
		}

		// Update the solution representation in CURRENT_SOLUTION_INDEX
		memory[CURRENT_SOLUTION_INDEX].getSolutionRepresentation().setSolutionRepresentation(solutionRepresentation);

		// Return the objective function value of the current solution
		return f.getObjectiveFunctionValue(memory[CURRENT_SOLUTION_INDEX].getSolutionRepresentation());
	}

	@Override
	public boolean isCrossover() {

		// TODO
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		// TODO
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		return false;
	}

}
