package com.aim.project.uzf;

import com.aim.project.uzf.instance.Location;
import com.aim.project.uzf.interfaces.ObjectiveFunctionInterface;
import com.aim.project.uzf.interfaces.UZFInstanceInterface;
import com.aim.project.uzf.interfaces.SolutionRepresentationInterface;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFObjectiveFunction implements ObjectiveFunctionInterface {

	private final UZFInstanceInterface oInstance;

	public UZFObjectiveFunction(UZFInstanceInterface oInstance) {
		this.oInstance = oInstance;
	}

	@Override
	public int getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {
		// TODO
		// distance between first enclosure in the solution and the food preparation area
		int totalDistance = getCostBetweenFoodPreparationAreaAnd(oSolution.getSolutionRepresentation()[0]);

		for (int i = 0; i < oSolution.getNumberOfLocations() - 1; i++) {
			// distance between each pair of enclosures in the solution
			totalDistance += getCost(oSolution.getSolutionRepresentation()[i], oSolution.getSolutionRepresentation()[i + 1]);
		}

		// distnace between the last enclosure in the solution and the food preparation area
		totalDistance += getCostBetweenFoodPreparationAreaAnd(oSolution.getSolutionRepresentation()[oSolution.getNumberOfLocations() - 1]);

		return totalDistance;
	}

	@Override
	public int getCost(Location oLocationA, Location oLocationB) {
		// Get the locations of the two enclosures
		long dx = oLocationA.x() - oLocationB.x();
		long dy = oLocationA.y() - oLocationB.y();
		double distanceSquared = Math.pow(dx, 2) + Math.pow(dy, 2);
		return (int) Math.ceil(Math.sqrt(distanceSquared));
	}

	@Override
	public int getCost(int iLocationA, int iLocationB) {
		// Get the locations of the two enclosures
		Location locationA = this.oInstance.getLocationForEnclosure(iLocationA);
		Location locationB = this.oInstance.getLocationForEnclosure(iLocationB);
		return getCost(locationA, locationB);
	}

	@Override
	public int getCostBetweenFoodPreparationAreaAnd(int iLocation) {
		// Get the location of the food preparation area and the location of the enclosure
		Location preparationArea = this.oInstance.getLocationOfFoodPreparationArea();
		Location enclosureLocation = this.oInstance.getLocationForEnclosure(iLocation);
		return getCost(preparationArea, enclosureLocation);
	}
}
