package com.chamlabs.compare;

import java.io.File;
import java.util.List;

import org.apache.commons.io.LineIterator;

public class RunComparator {
	
	//Method call : Main method to execute the comparator code
	public static void main(String[] args) {
		try {
			File file1 = new File("firstFile.txt");
			File file2 = new File("secondFile.txt");
			IComparator comparator = new RESTComparator(file1, file2);
			
			List<LineIterator> allIterators = comparator.getURLs();
			
			LineIterator firstFileLineIterator = allIterators.get(0);
			LineIterator secondFileLineIterator = allIterators.get(1);
	
			
			while(firstFileLineIterator.hasNext() && secondFileLineIterator.hasNext()) {
				String url1 = firstFileLineIterator.next();
				String url2 = secondFileLineIterator.next();
				Boolean compareResult = 
						comparator.compare(url1, url2);
				
				if (compareResult == null) {
					System.out.println("Some exception has happened during comparision of " 
							+ url1 + " and " + url2);
				}
				if (compareResult) {
					System.out.println(url1 + " equals " + url2);
				}
				else{
					System.out.println(url1 + " not equals " + url2);
				}
			}
			while (firstFileLineIterator.hasNext()) {
				System.out.println(firstFileLineIterator.next() + " does not have its counterpart url from file2."
						+ " Comparision cannot be done.");
			}
			
			while (secondFileLineIterator.hasNext()) {
				System.out.println(secondFileLineIterator.next() + " does not have its counterpart url from file1"
						+ " Comparision cannot be done");
			}
			if (firstFileLineIterator != null) {
				firstFileLineIterator.close();
			}
			if (secondFileLineIterator != null) {
				secondFileLineIterator.close();
			}
			
			System.out.println("Comparator: Done with the comparisions");
			
		} catch(Exception e){
			System.out.println("Some exception has occurred in running the comparator.");
		}
		
	}

}
