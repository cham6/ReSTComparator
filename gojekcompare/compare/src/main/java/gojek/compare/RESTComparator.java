package gojek.compare;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;

public class RESTComparator implements IComparator {

	private static String[] headersArray = {
			"Expires", 
			"Content-Encoding", 
			"Connection",
			"Content-Length",
			"Cache-Control"
	};
	File file1, file2;
	
	private static int linesRead = 0;
	
	public RESTComparator() {
		System.out.println("Default constructor called. Set file objects manually");
	}
	
	public RESTComparator(File file1, File file2) {
		this.file1 = file1;
		this.file2 = file2; 
	}
	
	/**
	 * Validates and returns the corresponding LineIterator objects for file1 and file2
	 */
	public List<LineIterator> getURLs() {
		
		try {
			if((file1 == null) || (file2 == null)) {
				System.out.println("Either or both of the given files are null. "
						+ "Fix the configuration");
				return null;
			}
			
			if((!file1.exists()) || (!file2.exists())) {
				System.out.println("Either or both of the given files does not exists. "
						+ "Fix the configuration");
				return null;
			}
			
			LineIterator firstFileLineIterator = FileUtils.lineIterator(file1, "UTF-8");
			LineIterator secondFileLineIterator = FileUtils.lineIterator(file2, "UTF-8");
			
			
			List<LineIterator> lineIteratorList = new ArrayList<LineIterator>();
			lineIteratorList.add(firstFileLineIterator);
			lineIteratorList.add(secondFileLineIterator);
			return lineIteratorList;
			
		} catch(Exception e) {
			System.out.println("An exception has occurred in getURLs method.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			// TO-DO
		}
		
		return null;
	}
	
	/**
	 * Invoke the HTTP GET requests 
	 * Compares the output of both the requests
	 * Returns true if the responses are same. Else false. 
	 * Returns null on any failure cases. 
	 */
	public Boolean compare(String url1, String url2) {
		System.out.println("========Compare Started===========");
		 if(url1 == null || url2 == null) {
			 System.out.println("Either or both of URLs are null. Cannot proceed with comparisions. ");
			 return null;
		 }
		try {
			//Invoke both the URLs using Rest-Assured Client and capture the responses. 
			 Response response1 = RestAssured.get(url1);
			 Response response2 = RestAssured.get(url2);
			 
			 //Compare the Response body of both the responses.
			 if(!response1.getBody().asString().equalsIgnoreCase(response2.getBody().asString())) {
				 System.out.println("The response bodies are different for given URL responses");
				 System.out.println("The response body of " + url1 + " is : " 
						 + response1.getBody().asString());
				 System.out.println("The response body of " + url2 + " is : " 
						 + response2.getBody().asString());
				 return false;
			 }
			 
			//Compare the Content type of both the responses.
			 if(!response1.getContentType().toString().equalsIgnoreCase(response2.getContentType().toString())) {
				 System.out.println("The Content Types are different for given URL responses");
				 System.out.println("The Content Type of " + url1 + " is : " 
						 + response1.getContentType().toString());
				 System.out.println("The Content Type of " + url2 + " is : " 
						 + response2.getContentType().toString());
				 return false;
			 }
			 
			 //Compare the Status code of both the responses.
			 if(response1.getStatusCode() != response2.getStatusCode()) {
				 System.out.println("The status codes are different for given URL responses");
				 System.out.println("The status code of " + url1 + " is : " 
						 + response1.getStatusCode());
				 System.out.println("The Content Type of " + url2 + " is : " 
						 + response2.getStatusCode());
				 return false;
			 }
			 
			 Headers headersList1 = response1.getHeaders();
			 Headers headersList2 = response2.getHeaders();
			 
			 //Compare the length of headers of both the responses.
			 if(headersList1.size() != headersList2.size()) {
				 System.out.println("The lenghts of headers are different. ");
				 System.out.println(url1 + " has " + headersList1.size() + " headers.");
				 System.out.println(url2 + " has " + headersList2.size() + " headers.");
				 return false;
			 }
			 
			 //Compare the important headers initialized in headersArray for both the requests
			 for(int i = 0; i<headersArray.length; i++) {
				 if((!headersList1.hasHeaderWithName(headersArray[i])) && 
						 (!headersList2.hasHeaderWithName(headersArray[i]))) {
					 continue;
				 }
				 if (!headersList1.hasHeaderWithName(headersArray[i])) {
					 System.out.println("First response does not have the header but second response has");
					 return false;
				 }
				 if (!headersList2.hasHeaderWithName(headersArray[i])) {
					 System.out.println("Second response does not have the header but first response has");
					 return false;
				 }
				 
				 if(!headersList1.get(headersArray[i]).getValue().toString().equalsIgnoreCase( 
						 headersList2.get(headersArray[i]).getValue().toString())) {
					 System.out.println("The " + headersArray[i] + " headers are different for given "
					 		+ "URL responses");
					 System.out.println("The " + headersArray[i]  + " of " + url1 + " is : " 
							 + headersList1.get(headersArray[i]).getValue().toString());
					 System.out.println("The " + headersArray[i]  + " of " + url2 + " is : " 
							 + headersList2.get(headersArray[i]).getValue().toString());
					 return false;
				 }
			 }
			 
			 //All the above validations have passed. Return true.
			 return true;
			
		} catch(Exception e) {
			System.out.println("An exception has occurred in getURLs method.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			System.out.println("========Compare Done==============");
		}
		return false;
	}
		

}
