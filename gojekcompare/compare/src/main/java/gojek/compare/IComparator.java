package gojek.compare;

import java.io.File;
import java.util.List;

public interface IComparator {
	
	public Boolean compare(String url1, String url2) ;

	public List<List<String>> getURLs();
	
}
