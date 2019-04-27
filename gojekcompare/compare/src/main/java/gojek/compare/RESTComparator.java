package gojek.compare;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class RESTComparator implements IComparator {

	private static int linesRead = 0;
	
	
	public boolean compare(String url1, String url2) {
		
		return false;
	}

	public List<String[]> getURLs(File file1, File file2) {
		
		try {
		if(file1 == null || file2 == null) {
			System.out.println("Either or both of the given files are empty. "
					+ "Fix the configuration");
			return null;
		}
		
		if(!file1.exists() || !file2.exists()) {
			System.out.println("Either or both of the given files does not exists. "
					+ "Fix the configuration");
			return null;
		}
		
		String[] file1URLs = {};
		String[] file2URLs = {};
		
		
		for(int i=0; i<10; i++) {
			file1URLs[i] = Files.readLines(file1, Charsets.UTF_8).get(linesRead+i);
		}
		
		for(int i=0; i<10; i++) {
			file2URLs[i] = Files.readLines(file2, Charsets.UTF_8).get(linesRead+i);
		}
		
		
		List<String[]> urlList = new ArrayList();
		urlList.add(file1URLs);
		urlList.add(file2URLs);
		return urlList;
		
		} catch(Exception e) {
			System.out.println("An exception has occurred in getURLs method.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
		
	}
		

}
