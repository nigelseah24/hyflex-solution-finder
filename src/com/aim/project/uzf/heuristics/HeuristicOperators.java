package com.aim.project.uzf.heuristics;

import java.util.Random;

import com.aim.project.uzf.interfaces.ObjectiveFunctionInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.solution.UZFSolution;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 * <br>
 * This class is included (and all non-crossover heuristics subclass this class) to simplify your implementation and it
 * is intended that you include any common operations in this class to simplify your implementation of the other heuristics.
 * Furthermore, if you implement and test common functionality here, it is less likely that you introduce a bug elsewhere!
 * <br>
 * For example, think about common neighbourhood operators and any other incremental changes that you might perform
 * while applying low-level heuristics.
 */
public abstract class HeuristicOperators {

	protected ObjectiveFunctionInterface f;

	protected final Random random;

	public HeuristicOperators(Random random) {

		this.random = random;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		this.f = f;
	}

	public UZFSolution[] memory = new UZFSolution[2];

	public static final int CURRENT_SOLUTION_INDEX = 0;

	public static final int BACKUP_SOLUTION_INDEX = 1;

	protected int adjacentSwap(UZFSolution solution, int index) {
		int[] representation = solution.getSolutionRepresentation().getSolutionRepresentation();
		int length = representation.length;

		// Swap the adjacent elements
		int nextIndex = (index + 1) % length;
		int temp = representation[index];
		representation[index] = representation[nextIndex];
		representation[nextIndex] = temp;

		// Update the solution representation in the current index
		solution.getSolutionRepresentation().setSolutionRepresentation(representation);

		// Calculate and return the objective value of the modified solution
		return f.getObjectiveFunctionValue(solution.getSolutionRepresentation());
	}

	// Method to copy a solution from one index to another in the memory array
	protected void copySolution(int fromIndex, int toIndex) {
		memory[toIndex] = (UZFSolution) memory[fromIndex].clone();
	}

	public abstract int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation);

	public abstract boolean isCrossover();

	public abstract boolean usesIntensityOfMutation();

	public abstract boolean usesDepthOfSearch();
}
