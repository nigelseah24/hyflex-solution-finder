package com.aim.project.uzf;

import com.aim.project.uzf.heuristics.*;
import com.aim.project.uzf.instance.InitialisationMode;
import com.aim.project.uzf.instance.Location;
import com.aim.project.uzf.instance.UZFInstance;
import com.aim.project.uzf.instance.reader.UAVInstanceReader;
import com.aim.project.uzf.interfaces.*;

import AbstractClasses.ProblemDomain;
import com.aim.project.uzf.solution.UZFSolution;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFDomain extends ProblemDomain implements Visualisable {

	private UZFInstance loadedInstance;
	private UZFSolution[] memory; // SR_IE_HH uses memory of size 4
	private UZFObjectiveFunction objectiveFunction;
	private HeuristicInterface mutationHeuristic;
	private XOHeuristicInterface crossoverHeuristic;
	private final int[] usesDepthOfSearch;
	private final int[] usesIntensityOfMutation;
	private final int[] mutationHeuristics;
	private final int[] crossoverHeuristics;
	private final int[] localSearchHeuristics;

	public UZFDomain(long seed) {

		// TODO - set default memory size and create the array of low-level heuristics
		super(seed);
		memory = new UZFSolution[2];
		usesIntensityOfMutation = new int[] {0, 4, 5};
		usesDepthOfSearch = new int[] {1, 2};
		mutationHeuristics = new int[] {0, 4, 5};
		crossoverHeuristics = new int[] {3};
		localSearchHeuristics = new int[] {1, 2};
	}

	// applyHeuristic for mutation heuristics
	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {

		// TODO - apply heuristic and return the objective value of the candidate solution
		double depthOfSearch = 0;
		double intensityOfMutation = 0;

		// Randomly get the depth of search and intensity of mutation
		for (int i = 0; i < usesDepthOfSearch.length; i++) {
			if (hIndex == usesDepthOfSearch[i]) {
				depthOfSearch = this.rng.nextDouble();
				break;
			}
		}
		for (int i = 0; i < usesIntensityOfMutation.length; i++) {
			if (hIndex == usesIntensityOfMutation[i]) {
				intensityOfMutation = this.rng.nextDouble();
				break;
			}
		}

		// Copy solution and apply heuristic on candidate solution so that if its worse, we can apply the heuristic of the next iteration
		// back on the current solution which is better than the candidate solution
		this.copySolution(currentIndex, candidateIndex);

		int cost = objectiveFunction.getObjectiveFunctionValue(this.memory[candidateIndex].getSolutionRepresentation());
		System.out.println("Initial Cost: " + cost);

		switch(hIndex) {
			case 0:
				mutationHeuristic = new AdjacentSwap(this.rng);
				break;
			case 1:
				mutationHeuristic = new DavissHillClimbing(this.rng);
				break;
			case 2:
				mutationHeuristic = new NextDescent(this.rng);
				break;
			case 4:
				mutationHeuristic = new Reinsertion(this.rng);
				break;
			case 5:
				mutationHeuristic = new RandomInversion(this.rng);
				break;
			default:
				System.out.println("Invalid heuristic index");
				System.exit(-1);
		}

		// set objective function value for the candidate solution
		mutationHeuristic.setObjectiveFunction(this.objectiveFunction);

		// apply the heuristic on the candidate solution and get the objective function value (cost)
		cost = mutationHeuristic.apply(this.memory[candidateIndex], depthOfSearch, intensityOfMutation);

		// update the objective function value for the candidate solution
		memory[candidateIndex].setObjectiveFunctionValue(cost);

		System.out.println("Final Cost: " + cost);
		return cost;
	}

	// applyHeuristic for crossover heuristics
	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {

		this.copySolution(parent1Index, candidateIndex);

		// TODO - apply heuristic and return the objective value of the candidate solution
		int cost = objectiveFunction.getObjectiveFunctionValue(this.memory[candidateIndex].getSolutionRepresentation());
		System.out.println("Initial Cost: " + cost);

		switch(hIndex){
			case 3:
				crossoverHeuristic = new PMX(this.rng);
				break;
			default:
				System.out.println("Invalid heuristic index");
				System.exit(-1);
		}

		crossoverHeuristic.setObjectiveFunction(this.objectiveFunction);

		cost = (int) crossoverHeuristic.apply(this.memory[parent1Index], this.memory[parent2Index], this.memory[candidateIndex], depthOfSearch, intensityOfMutation);

		memory[candidateIndex].setObjectiveFunctionValue(cost);

		return cost;
	}

	@Override
	public String bestSolutionToString() {

		// TODO
		return this.memory[3].toString();
	}

	@Override
	public boolean compareSolutions(int a, int b) {

		// TODO
		UZFSolution solutionA = this.memory[a];
		UZFSolution solutionB = this.memory[b];

		for (int i = 0; i < solutionA.getNumberOfLocations(); i++) {
			if (solutionA.getSolutionRepresentation().getSolutionRepresentation()[i] != solutionB.getSolutionRepresentation().getSolutionRepresentation()[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void copySolution(int a, int b) {

		// TODO - BEWARE this should copy the solution, not the reference to it!
		//			That is, that if we apply a heuristic to the solution in index 'b',
		//			then it does not modify the solution in index 'a' or vice-versa.
		this.memory[b] = (UZFSolution) this.memory[a].clone();
	}

	@Override
	public double getBestSolutionValue() {

		// TODO
		return this.memory[3].getObjectiveFunctionValue();
	}

	@Override
	public double getFunctionValue(int index) {

		// TODO

		return this.memory[index].getObjectiveFunctionValue();
	}

	// TODO
	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {

		if(type == HeuristicType.LOCAL_SEARCH) {
			return localSearchHeuristics;
		} else if(type == HeuristicType.CROSSOVER) {
			return crossoverHeuristics;
		} else {
			return mutationHeuristics;
		}
	}


	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {

		return usesDepthOfSearch;
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {

		return usesIntensityOfMutation;
	}

	@Override
	public int getNumberOfHeuristics() {

		// TODO - has to be hard-coded due to the design of the HyFlex framework

		return 5;
	}

	@Override
	public int getNumberOfInstances() {

		// TODO
		return 7;
	}

	@Override
	public void initialiseSolution(int index) {

		// TODO - make sure that you also update the best solution!
		// 0 is RANDOM and 1 is CONSTRUCTIVE
		boolean updateBest = false;

		if (this.memory[index] == null) {
			updateBest = true;
		}

		int mode = this.rng.nextInt(2);
		if (mode == 0) {
			this.memory[index] = this.loadedInstance.createSolution(InitialisationMode.RANDOM);
		} else {
			this.memory[index] = this.loadedInstance.createSolution(InitialisationMode.CONSTRUCTIVE);
		}

		if (updateBest) {
			updateBestSolution(index);
		}
	}

	@Override
	public void loadInstance(int instanceId) {

		// TODO load the instance (referenced by ID) from file
		//  here might be a good place to set the objective function within each low-level heuristic
		UAVInstanceReaderInterface instanceReader = new UAVInstanceReader();
		String relativePath = "/instances/uzf/";
		String[] instanceFiles = {
				relativePath + "square.uzf",
				relativePath + "libraries-15.uzf",
				relativePath + "carparks-40.uzf",
				relativePath + "tramstops-85.uzf",
				relativePath + "grid.uzf",
				relativePath + "clustered-enclosures.uzf",
				relativePath + "chatgpt-instance-100-enclosures.uzf"
		};

		String instanceFileName = instanceFiles[instanceId];

		try {
			URI uri = Paths.get(Objects.requireNonNull(getClass().getResource(instanceFileName)).toURI()).toUri();
			Path path = Paths.get(uri);

			// load the instance
			this.loadedInstance = (UZFInstance) instanceReader.readUZFInstance(path, this.rng);

			// set the objective function
			this.objectiveFunction = (UZFObjectiveFunction) loadedInstance.getUZFObjectiveFunction();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setMemorySize(int size) {

		// TODO
		UZFSolution[] tempMemory = new UZFSolution[size];
		if (this.memory != null) {
			int i;
			if (tempMemory.length <= this.memory.length) {
				for(i = 0; i < tempMemory.length; ++i) {
					tempMemory[i] = this.memory[i];
				}
			} else {
				for(i = 0; i < this.memory.length; ++i) {
					tempMemory[i] = this.memory[i];
				}
			}
		}

		this.memory = tempMemory;
	}

	@Override
	public String solutionToString(int index) {

		// Debug
		return this.memory[index].toString();
	}

	@Override
	public String toString() {

		return null;
	}

	private void updateBestSolution(int index) {

		// TODO - make sure we cannot modify the best solution accidentally after storing it!
		if(this.memory[3] == null || this.memory[index].getObjectiveFunctionValue() < this.memory[3].getObjectiveFunctionValue()) {
			this.memory[3] = (UZFSolution) this.memory[index].clone();
		}
	}

	@Override
	public UZFInstanceInterface getLoadedInstance() {

		return this.loadedInstance;
	}
	/**
	 * @return The integer array representing the ordering of the best solution.
	 */
	@Override
	public int[] getBestSolutionRepresentation() {

		return this.memory[3].getSolutionRepresentation().getSolutionRepresentation();
	}

	@Override
	public Location[] getRouteOrderedByLocations() {

		// TODO
		return this.loadedInstance.getSolutionAsListOfLocations(this.memory[3]).toArray(new Location[0]);
	}

	public UAVSolutionInterface getBestSolution() {

		// TODO
		return this.memory[3];
	}

	public int[] getSolution(int i) {

		// TODO
		return this.memory[i].getSolutionRepresentation().getSolutionRepresentation();
	}
}
