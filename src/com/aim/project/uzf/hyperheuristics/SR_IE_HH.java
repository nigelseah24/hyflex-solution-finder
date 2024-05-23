package com.aim.project.uzf.hyperheuristics;


import com.aim.project.uzf.UZFDomain;
import com.aim.project.uzf.SolutionPrinter;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

import java.util.Arrays;

public class SR_IE_HH extends HyperHeuristic {

	private static final int SECOND_PARENT_INDEX = 2;

	private static final int BEST_ACCEPTED_INDEX = 3;

	public SR_IE_HH(long lSeed) {

		super(lSeed);
	}

	@Override
	protected void solve(ProblemDomain oProblem) {

		// Set memory size of 4.
		// The first two indices are used for the current and candidate solutions.
		// The third index is used for the second parent solution in crossover heuristics.
		// The fourth index is used for the best solution accepted so far.
		oProblem.setMemorySize(4);

		int currentIndex = 0;
		int candidateIndex = 1;

		// Initialise the current solution and copy it to the best solution accepted so far
		oProblem.initialiseSolution(currentIndex);

		oProblem.copySolution(currentIndex, BEST_ACCEPTED_INDEX);

		// Get the current objective function value and the number of heuristics (5)
		double currentCost = oProblem.getFunctionValue(currentIndex);
		int numberOfHeuristics = oProblem.getNumberOfHeuristics();
		System.out.println(numberOfHeuristics);

		// Sorting heuristics into mutation and crossover
		// cache indices of crossover heuristics
		boolean[] isCrossover = new boolean[numberOfHeuristics];
		Arrays.fill(isCrossover, false);

		for(int i : oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER)) {

			isCrossover[i] = true;
		}

		// Set a time limit for the hyper-heuristic
		// main search loop
		double candidateCost;
		while(!hasTimeExpired()) {

			int h = rng.nextInt(numberOfHeuristics);

			// Ignore the PMX crossover heuristic for now
//			if (h == 3) continue;

			// If its a crossover heuristic, we need a parent index to crossover with
			// The parent index can be either the second parent or the best solution accepted so far
			if(isCrossover[h]) {

				if(rng.nextBoolean()) {
					// randomly choose between crossover with newly initialised solution
					oProblem.initialiseSolution(SECOND_PARENT_INDEX);
					candidateCost = oProblem.applyHeuristic(h, currentIndex, SECOND_PARENT_INDEX, candidateIndex);
				} else {
					// or with best solution accepted so far
					candidateCost = oProblem.applyHeuristic(h, currentIndex, BEST_ACCEPTED_INDEX, candidateIndex);
				}

			}
			// Else if its a mutation heuristic, we dont need a parent index, only the current solution
			else {
				candidateCost = oProblem.applyHeuristic(h, currentIndex, candidateIndex);
			}

			// update best
			if(candidateCost < currentCost) {

				oProblem.copySolution(candidateIndex, BEST_ACCEPTED_INDEX);
			}

			// accept improving or equal moves
			if(candidateCost <= currentCost) {

				currentCost = candidateCost;
				currentIndex = 1 - currentIndex;
				candidateIndex = 1 - candidateIndex;

			}
			System.out.println("Best solution: " + oProblem.getFunctionValue(BEST_ACCEPTED_INDEX));
		}

		UAVSolutionInterface oSolution = ((UZFDomain) oProblem).getBestSolution();
		SolutionPrinter oSolutionPrinter = new SolutionPrinter("out.csv");
		oSolutionPrinter.printSolution( ((UZFDomain) oProblem).getLoadedInstance().getSolutionAsListOfLocations(oSolution));
	}

	@Override
	public String toString() {

		return "SR_IE_HH";
	}
}
