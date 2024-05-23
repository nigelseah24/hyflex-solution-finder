package com.aim.project.uzf.solution;

import com.aim.project.uzf.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] aiRepresentation;

	public SolutionRepresentation(int[] aiRepresentation) {
		this.aiRepresentation = aiRepresentation;
	}

	@Override
	public int[] getSolutionRepresentation() {

		// TODO
		return aiRepresentation.clone();
	}

	@Override
	public void setSolutionRepresentation(int[] aiSolutionRepresentation) {

		// TODO
		aiRepresentation = aiSolutionRepresentation.clone();
	}

	@Override
	public int getNumberOfLocations() {

		// TODO
		return aiRepresentation.length;
	}

	@Override
	public SolutionRepresentationInterface clone() {

		// TODO
		return new SolutionRepresentation(aiRepresentation.clone());
	}

}
