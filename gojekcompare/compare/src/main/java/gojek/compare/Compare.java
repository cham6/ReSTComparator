package gojek.compare;

import java.io.File;
import java.util.List;

public class Compare {
	
	//Method call : Main method to execute the comparator code
	public static void main(String[] args) {
		
		File file1 = new File("firstFile.txt");
		File file2 = new File("secondFile.txt");
		IComparator comparator = new RESTComparator(file1, file2);
		
		List<List<String>> allURLs = comparator.getURLs();
		
		List<String> urlsFromFirstFile = allURLs.get(0);
		List<String> urlsFromSecondFile = allURLs.get(1);
		
		int firstFileLength = urlsFromFirstFile.size();
		int secondFileLength = urlsFromSecondFile.size();
		
		int maxNumberOfUrls = firstFileLength > secondFileLength?
				firstFileLength : secondFileLength;
		
		for (int i=0; i<maxNumberOfUrls; i++) {
			
			if(i>firstFileLength) {
				System.out.println("First file ran out of URLs. No comparision required");
				System.out.println(urlsFromSecondFile.get(i) + " does not have its counterpart url from file1");
				continue;
			}
			if(i>secondFileLength) {
				System.out.println("Second file ran out of URLs. No comparision required");
				System.out.println(urlsFromFirstFile.get(i) + " does not have its counterpart url from file2");
				continue;
			}
			String url1 = urlsFromFirstFile.get(i);
			String url2 = urlsFromSecondFile.get(i);
			Boolean compareResult = 
					comparator.compare(url1, url2);
			
			if (compareResult == null) {
				System.out.println("Some exception has happened during comparision");
			}
			if (compareResult) {
				System.out.println(url1 + " equals " + url2);
			}
			else{
				System.out.println(url1 + " not equals " + url2);
			}
		}		
	}

}
