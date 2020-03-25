package com.aim.project.pwp.instance.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.PWPInstance;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPInstanceReaderInterface;


public class PWPInstanceReader implements PWPInstanceReaderInterface {

	@Override
	public PWPInstanceInterface readPWPInstance(Path path, Random random) {
		BufferedReader bfr;
		try {
			bfr = Files.newBufferedReader(path);
			//getting the lines lines from the BFR used to describe the problem instance
			String name = bfr.readLine();
			String comment = bfr.readLine();
			bfr.readLine(); //ignore "POSTAL_OFFICE"
			String poLine = bfr.readLine();
			Location postalOffice = new Location(Double.parseDouble(poLine.split(" ")[0]), Double.parseDouble(poLine.split(" ")[1]));
			bfr.readLine(); //ignore "WORKER_ADDRESS"
			String waLine = bfr.readLine();
			Location workerAddress = new Location(Double.parseDouble(waLine.split(" ")[0]), Double.parseDouble(waLine.split(" ")[1]));
			
			bfr.readLine(); //ignore "POSTAL_ADDRESSES"
			
			//getting the arraylist of locations
			ArrayList<Location> locations = new ArrayList<Location>();
			String rowLine;
			while(!(rowLine=bfr.readLine()).contains("EOF")) {
				locations.add(new Location(Double.parseDouble(rowLine.split(" ")[0]), Double.parseDouble(rowLine.split(" ")[1])));
			}
				
			
			return new PWPInstance(locations.size()+2, locations.toArray(new Location[0]), postalOffice, workerAddress, random);
			
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}
}
