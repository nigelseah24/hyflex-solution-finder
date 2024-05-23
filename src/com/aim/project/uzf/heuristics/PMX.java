package com.aim.project.uzf.heuristics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.aim.project.uzf.interfaces.ObjectiveFunctionInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.interfaces.XOHeuristicInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class PMX implements XOHeuristicInterface {

	private final Random random;

	private ObjectiveFunctionInterface f;

	public PMX(Random random) {

		this.random = random;
	}

	@Override
	public int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// TODO
		return -1;
	}

	@Override
	public double apply(UAVSolutionInterface p1, UAVSolutionInterface p2, UAVSolutionInterface c, double depthOfSearch, double intensityOfMutation) {

		// TODO - done
		// the solution passed to this method is actually the candidate solution
		// return the objective value of the candidate solution

		int[] parent1 = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] parent2 = p2.getSolutionRepresentation().getSolutionRepresentation();
		int tourLength = parent1.length;

		int cutPoint1 = random.nextInt(tourLength);
		int cutPoint2 = random.nextInt(tourLength);
		if (cutPoint2 == cutPoint1) {
			cutPoint2 = (cutPoint1 + 1) % (tourLength);
		}

		int start = Math.min(cutPoint1, cutPoint2);
		int end = Math.max(cutPoint1, cutPoint2);

		int[][] children = pmx(parent1, parent2, start, end);

		// randomly choose between child1 and child2
		if (random.nextBoolean()) {
			c.getSolutionRepresentation().setSolutionRepresentation(children[0]);
		} else {
			c.getSolutionRepresentation().setSolutionRepresentation(children[1]);
		}

		return f.getObjectiveFunctionValue(c.getSolutionRepresentation());
	}

	public static int[][] pmx(int[] parent1, int[] parent2, int sliceIndex1, int sliceIndex2) {
		if (parent1.length != parent2.length) {
			throw new IllegalArgumentException("Parents have different lengths!");
		}

		int[] child1 = Arrays.copyOf(parent1, parent1.length);
		int[] child2 = Arrays.copyOf(parent2, parent2.length);
		Map<Integer, Integer> mapping1 = new HashMap<>();
		Map<Integer, Integer> mapping2 = new HashMap<>();

		// Map Slice:
		for (int i = sliceIndex1; i < sliceIndex2; i++) {
			mapping1.put(parent2[i], parent1[i]);
			child1[i] = parent2[i];

			mapping2.put(parent1[i], parent2[i]);
			child2[i] = parent1[i];
		}

		// Repair Lower Slice:
		for (int i = 0; i < sliceIndex1; i++) {
			while (mapping1.containsKey(child1[i])) {
				child1[i] = mapping1.get(child1[i]);
			}
			while (mapping2.containsKey(child2[i])) {
				child2[i] = mapping2.get(child2[i]);
			}
		}

		// Repair Upper Slice:
		for (int i = sliceIndex2; i < parent1.length; i++) {
			while (mapping1.containsKey(child1[i])) {
				child1[i] = mapping1.get(child1[i]);
			}
			while (mapping2.containsKey(child2[i])) {
				child2[i] = mapping2.get(child2[i]);
			}
		}

		return new int[][] { child1, child2 };
	}

	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		// TODO
		this.f = f;
	}

	@Override
	public boolean isCrossover() {

		// TODO
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		// TODO
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		return false;
	}
}

/*
* apply method: This method implements the crossover operation between two parent solutions to generate offspring.
It takes the two parent solutions (p1 and p2), the candidate solution (c), depth of search, and intensity of mutation as parameters.
* Slice Points: Two random slice points (sliceIndex1 and sliceIndex2) are selected within the length of the parent solutions.
These points determine the segments that will be swapped between parents.
* PMX Operation: The PMX operation involves swapping the elements between the slice points between the parent solutions.
This is done to create initial offspring (child1 and child2). The elements within the slices are exchanged between the parents while maintaining the order of elements outside the slices.
* Repair Mechanism: After the initial crossover, a repair mechanism is applied to ensure that each offspring contains unique elements.
This process involves mapping elements between the parents and replacing duplicate elements in the offspring with their corresponding
elements from the other parent.
* Objective Value Calculation: The objective function value of the candidate solution (c) is computed using the provided objective
function interface (f). This value represents the fitness of the generated offspring.
* Set Objective Function: The setObjectiveFunction method allows setting the objective function interface used for computing the
objective function value of solutions.
* */
