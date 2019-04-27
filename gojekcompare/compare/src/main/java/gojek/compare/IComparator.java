package gojek.compare;

import java.io.File;
import java.util.List;

public interface IComparator {
	
	public boolean compare(String url1, String url2) ;

	public List<String[]> getURLs(File file1, File file2);
	
}
