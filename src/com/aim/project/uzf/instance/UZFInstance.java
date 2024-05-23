package com.aim.project.uzf.instance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.aim.project.uzf.UZFObjectiveFunction;
import com.aim.project.uzf.interfaces.ObjectiveFunctionInterface;
import com.aim.project.uzf.interfaces.UZFInstanceInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.solution.SolutionRepresentation;
import com.aim.project.uzf.solution.UZFSolution;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFInstance implements UZFInstanceInterface {

	private final int numberOfLocations;
	private final Location[] aoLocations;
	private final Location foodPreparationLocation;
	private final Random random;
	private Set<Integer> visited;


	public UZFInstance(int numberOfLocations, Location[] aoLocations, Location foodPreparationLocation, Random random) {

		// TODO
		this.numberOfLocations = numberOfLocations;
		this.aoLocations = aoLocations;
		this.foodPreparationLocation = foodPreparationLocation;
		this.random = random;
	}

	@Override
	public UZFSolution createSolution(InitialisationMode mode) {
		UZFObjectiveFunction UZFObjectiveFunction = new UZFObjectiveFunction(this);
		if (mode == InitialisationMode.RANDOM) {
			// Generate a random solution
			int[] randomSolution = generateRandomSolution();
			return new UZFSolution(new SolutionRepresentation(randomSolution), UZFObjectiveFunction.getObjectiveFunctionValue(new SolutionRepresentation(randomSolution)));
		} else if (mode == InitialisationMode.CONSTRUCTIVE) {
			// Generate a solution using the nearest neighbour greedy algorithm
			int[] constructiveSolution = generateConstructiveSolution();
			return new UZFSolution(new SolutionRepresentation(constructiveSolution), UZFObjectiveFunction.getObjectiveFunctionValue(new SolutionRepresentation(constructiveSolution)));
		} else {
			// Unsupported mode
			System.err.println("Unsupported initialization mode");
			return null;
		}
	}

	private int[] generateRandomSolution() {
		int numberOfLocations = this.numberOfLocations;
		int[] randomSolution = new int[numberOfLocations];

		// Generate a random permutation of location IDs
		for (int i = 0; i < numberOfLocations; i++) {
			randomSolution[i] = i;
		}
		for (int i = 0; i < numberOfLocations; i++) {
			int index = random.nextInt(numberOfLocations);
			int temp = randomSolution[index];
			randomSolution[index] = randomSolution[i];
			randomSolution[i] = temp;
		}
		return randomSolution;
	}

	private int[] generateConstructiveSolution() {
		int numberOfLocations = getNumberOfLocations();
		int[] solution = new int[numberOfLocations];
		this.visited = new HashSet<>();

		solution[0] = this.random.nextInt(numberOfLocations); // Start from a random location

		visited.add(solution[0]);

		for (int i = 1; i < this.numberOfLocations; i++) {
			int iCurrentLocation = solution[i - 1];
			int iNextLocation = -1;
			int iMinCost = Integer.MAX_VALUE;
			for (int j = 0; j < this.numberOfLocations; j++) {
				if (!visited.contains(j)) { // if the location has not been visited
					int iCost = this.getUZFObjectiveFunction().getCost(iCurrentLocation, j);
					if (iCost < iMinCost) {
						iMinCost = iCost;
						iNextLocation = j;
					}
				}
			}
			solution[i] = iNextLocation;
			visited.add(iNextLocation);
		}

		return solution;
	}

	@Override
	public ObjectiveFunctionInterface getUZFObjectiveFunction() {

		// TODO
		return new UZFObjectiveFunction(this);
	}

	@Override
	public int getNumberOfLocations() {

		// TODO
		return this.numberOfLocations;
	}

	@Override
	public Location getLocationForEnclosure(int iEnclosureId) {

		// TODO
		return this.aoLocations[iEnclosureId];
	}

	@Override
	public Location getLocationOfFoodPreparationArea() {

		// TODO
		return this.foodPreparationLocation;
	}

	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(UAVSolutionInterface oSolution) {

		// TODO
		int[] solution = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		ArrayList<Location> locations = new ArrayList<>();
		for (int locationId : solution) {
			locations.add(getLocationForEnclosure(locationId));
		}
		return locations;
	}

}
