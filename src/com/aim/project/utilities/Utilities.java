package com.aim.project.utilities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.aim.project.pwp.instance.Location;

public class Utilities {
	public static void shuffleArray(int[] arr, Random rand) {
		// goes through each in the array and picks a random index for it to be swapped with
	
		// for each element in the array
		for(int i=0; i<arr.length; i++) {
			// pick a random index
			int randomIndex = rand.nextInt(arr.length);
			// store this element
			int oldElement = arr[i];
			// overwrite this element with the new element at a random index
			arr[i] = arr[randomIndex];
			// set the at the randomIndex with the old element
			arr[randomIndex] = oldElement;
		}
		
	}
	
	public static Boolean arrayContainsValue(int [] arr, int value) {
		
		boolean contains = false;
		
		for(int i=0; i<arr.length; i++)
			if(arr[i]==value) {
				contains=true;
				break;
			}
		
		return contains;
	}

	public static int[] convertToArray(ArrayList<Integer> al) {
		int[] arr = new int[al.size()];
		
		for(int i=0; i<arr.length; i++) {
			arr[i] = al.get(i).intValue();
		}
		
		return arr;
	}
	
	public static ArrayList<Integer> convertToArrayList(int[] arr) {
		ArrayList<Integer> al = new ArrayList<Integer>();
		
		for(int i=0; i<arr.length; i++) {
			al.add(arr[i]);
		}
		
		return al;
	}

	public static Boolean arrayListContainsValue(ArrayList<Integer> al, int value) {
		for(int i=0; i<al.size(); i++)
			if(al.get(i)==value) {
				return true;
			}
		return false;
	}
	
	public static int arrayGetIndexOf(int [] arr, int value) {

		for(int i=0; i<arr.length; i++)
			if(arr[i]==value) {
				return i;
			}

		return -1;
	}
	
	
	public static void myPrintSolution(String strOutputFilePath, List<Location> loRouteLocations) {
			
			OutputStream os;
			try {
				os = new FileOutputStream(strOutputFilePath);
				PrintStream printStream = new PrintStream(os);
				loRouteLocations.forEach( l -> {
					printStream.print("("+l.getX() + "," + l.getY()+")");
					if(l!=loRouteLocations.get(loRouteLocations.size()-1))
						printStream.print("-> ");
				});
				printStream.close();
			} catch (FileNotFoundException e) {
	
				e.printStackTrace();
			}
		}
	
}

