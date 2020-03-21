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
			Location postalOffice = new Location(Double.parseDouble(bfr.readLine()), Double.parseDouble(bfr.readLine()));
			bfr.readLine(); //ignore "WORKER_ADDRESS"
			Location workerAddress = new Location(Double.parseDouble(bfr.readLine()), Double.parseDouble(bfr.readLine()));
			
			//getting the arraylist of locations
			ArrayList<Location> locations = new ArrayList<Location>();
			String x,y;
			while((x=bfr.readLine())!="EOF" && (y=bfr.readLine())!="EOF")
				locations.add(new Location(Double.parseDouble(x), Double.parseDouble(y)));

			return new PWPInstance(locations.size(), locations.toArray(new Location[0]), postalOffice, workerAddress, random);
			
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}
}
