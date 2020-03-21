package com.aim.project.utilities;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.instance.Location;

public class Utilities {
	public static <T> T[] shuffleArray(T[] arr, Random rand) {
		
		// for each element in the array
		for(int i=0; i<arr.length; i++) {
			// pick a random index
			int randomIndex = rand.nextInt(arr.length);
			// store this element
			T oldElement = arr[i];
			// overwrite this element with the new element at a random index
			arr[i] = arr[randomIndex];
			// set the at the randomIndex with the old element
			arr[randomIndex] = oldElement;
		}
		
		return arr;
	}
	public static int[] shuffleArray(int[] arr, Random rand) {
			
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
			
			return arr;
		}
	
	public static double calculateEuclidianDistance(Location l1, Location l2) {
		double x1, x2, y1, y2;
		x1 = l1.getX();
		x2 = l2.getX();
		y1 = l1.getY();
		y2 = l2.getY();
		
		return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
	}
	
	public static int[] reverseArray(int[] arr) {
		
		// for the first half of the array..
		for(int i=0; i<arr.length/2; i++) {
			//.. swap this element by its offset with the element from the end with teh same offset
			int oldElement = arr[i];
			arr[i] = arr[arr.length -i -1];
			arr[arr.length -i -1] = oldElement;
		}
		
		return arr;
	}
	
	public static <T> T[] reverseArray(T[] arr) {
		
		// for the first half of the array..
		for(int i=0; i<arr.length/2; i++) {
			//.. swap this element by its offset with the element from the end with teh same offset
			T oldElement = arr[i];
			arr[i] = arr[arr.length -i -1];
			arr[arr.length -i -1] = oldElement;
		}
		
		return arr;
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
	
	public static int[] getArrayOfBitIndexes(int numberOfVariables) {
		// adding variables into an array
		int array[] = new int[numberOfVariables];
		for(int i=0; i<numberOfVariables; i++) {
			array[i]=i;
		}
		
		// returning the array of variables
		return array;
	}
}

