package com.aim.project.uzf.solution;

import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFSolution implements UAVSolutionInterface {

	private SolutionRepresentationInterface representation;
	private int objectiveFunctionValue;

	public UZFSolution(SolutionRepresentationInterface representation, int objectiveFunctionValue) {
		this.representation = representation;
		this.objectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public int getObjectiveFunctionValue() {

		// TODO
		return this.objectiveFunctionValue;
	}

	@Override
	public void setObjectiveFunctionValue(int objectiveFunctionValue) {

		// TODO
		this.objectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {

		// TODO
		return this.representation;
	}

	@Override
	public UAVSolutionInterface clone() {

		// TODO
		return new UZFSolution(representation.clone(), objectiveFunctionValue);
	}

	@Override
	public int getNumberOfLocations() {

		// TODO
		return this.representation.getNumberOfLocations();
	}
}
