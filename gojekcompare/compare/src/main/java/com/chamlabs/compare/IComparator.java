package com.chamlabs.compare;

import java.io.File;
import java.util.List;

import org.apache.commons.io.LineIterator;

public interface IComparator {
	
	public Boolean compare(String url1, String url2) ;

	public List<LineIterator> getURLs();
	
}
