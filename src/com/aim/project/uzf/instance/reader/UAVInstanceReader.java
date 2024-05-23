package com.aim.project.uzf.instance.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

import com.aim.project.uzf.instance.Location;
import com.aim.project.uzf.instance.UZFInstance;
import com.aim.project.uzf.interfaces.UZFInstanceInterface;
import com.aim.project.uzf.interfaces.UAVInstanceReaderInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UAVInstanceReader implements UAVInstanceReaderInterface {

    @Override
    public UZFInstanceInterface readUZFInstance(Path path, Random random) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            ArrayList<Location> enclosureLocations = new ArrayList<>();
            Location preparationArea = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("NAME")) {
                    // Ignore the NAME line
                    continue;
                } else if (line.startsWith("COMMENT")) {
                    // Ignore the COMMENT line
                    continue;
                } else if (line.equals("PREPARATION_AREA")) {
                    // Read preparation area
                    String[] coords = reader.readLine().split("\\s+");
                    preparationArea = new Location(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                } else if (line.equals("ENCLOSURE_LOCATIONS")) {
                    // Read enclosure locations
                    while (!(line = reader.readLine()).equals("EOF")) {
                        String[] coords = line.split("\\s+");
                        enclosureLocations.add(new Location(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
                    }
                }
            }

            if (preparationArea != null && !enclosureLocations.isEmpty()) {
                Location[] enclosureLocationsArray = new Location[enclosureLocations.size()];
                enclosureLocations.toArray(enclosureLocationsArray);
                return new UZFInstance(enclosureLocationsArray.length, enclosureLocationsArray, preparationArea, random);
            } else {
                System.err.println("Error: Unable to read UZF instance from the file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
