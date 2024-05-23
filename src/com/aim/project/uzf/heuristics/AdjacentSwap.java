package com.aim.project.uzf.heuristics;

import java.util.Random;

import com.aim.project.uzf.instance.Location;
import com.aim.project.uzf.instance.UZFInstance;
import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.interfaces.UZFInstanceInterface;
import com.aim.project.uzf.solution.UZFSolution;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	private UZFInstanceInterface instance;

	public AdjacentSwap(Random random) {

		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// Retrieve the solution representation
		int[] solutionRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();

		// Copy the solution to CURRENT_SOLUTION_INDEX and BACKUP_SOLUTION_INDEX
		memory[CURRENT_SOLUTION_INDEX] = (UZFSolution) solution;
		memory[BACKUP_SOLUTION_INDEX] = (UZFSolution) solution.clone();

		// Calculate the number of internal swaps based on the intensityOfMutation
		int numSwaps;
		if (intensityOfMutation >= 0.0 && intensityOfMutation < 0.2) {
			numSwaps = 1;
		} else if (intensityOfMutation >= 0.2 && intensityOfMutation < 0.4) {
			numSwaps = 2;
		} else if (intensityOfMutation >= 0.4 && intensityOfMutation < 0.6) {
			numSwaps = 4;
		} else if (intensityOfMutation >= 0.6 && intensityOfMutation < 0.8) {
			numSwaps = 8;
		} else if (intensityOfMutation >= 0.8 && intensityOfMutation < 1.0) {
			numSwaps = 16;
		} else {
			numSwaps = 32;
		}

		int currentCost = solution.getObjectiveFunctionValue();

		// Perform internal swaps
		for (int i = 0; i < numSwaps; i++) {
			// Randomly select an index to swap (excluding the last index)
			int firstIndex = random.nextInt(solutionRepresentation.length);

			// if the last index is selected, swap with the first index
			int secondIndex = (firstIndex + 1) % solutionRepresentation.length;
			// Swap adjacent elements

			// Calculate the delta cost after swapping
//			currentCost += getDeltaCost(solutionRepresentation, firstIndex, secondIndex);
			int temp = solutionRepresentation[firstIndex];
			solutionRepresentation[firstIndex] = solutionRepresentation[secondIndex];
			solutionRepresentation[secondIndex] = temp;
		}

		// Update the solution representation in CURRENT_SOLUTION_INDEX
		memory[CURRENT_SOLUTION_INDEX].getSolutionRepresentation().setSolutionRepresentation(solutionRepresentation);

		// Return the objective function value of the current solution
		return f.getObjectiveFunctionValue(memory[CURRENT_SOLUTION_INDEX].getSolutionRepresentation());
	}

	public int getDeltaCost(int[] tour, int firstIndex, int secondIndex){

		int tourLength = tour.length;
		Location[] locations = new Location[tourLength+1];
		locations[0] = instance.getLocationOfFoodPreparationArea();
		for (int i = 1; i <= tourLength; i++) {
			locations[i] = instance.getLocationForEnclosure(tour[i - 1]);
		}
		tourLength += 1;
		firstIndex += 1;
		secondIndex += 1;

		int valueBeforeSwap = f.getCost(locations[(firstIndex - 1 + tourLength) % tourLength], locations[firstIndex]) + f.getCost(locations[secondIndex], locations[(secondIndex + 1) % tourLength]);
		int valueAfterSwap = f.getCost(locations[(firstIndex - 1 + tourLength) % tourLength], locations[secondIndex]) + f.getCost(locations[firstIndex], locations[(secondIndex + 1) % tourLength]);

		return valueAfterSwap - valueBeforeSwap;
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

/*
* The getDeltaCost method calculates the change in cost (objective function value) resulting
* from swapping two adjacent elements in a tour. It retrieves the current cost before the swap and
* the cost after the swap, considering the effects on the tour's structure. By directly evaluating
* the cost impact of swaps, it enables the heuristic to make informed decisions on accepting or
* rejecting moves, enhancing efficiency. This efficiency improvement is achieved by avoiding
* redundant evaluations of the entire solution after each swap and instead focusing solely on the
* local changes.
* */
