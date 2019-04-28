package gojek.compare;


import java.io.File;
import java.util.List;

import org.apache.commons.io.LineIterator;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.post;
import static com.xebialabs.restito.semantics.Condition.get;
import com.xebialabs.restito.server.StubServer;
import static org.glassfish.grizzly.http.util.HttpStatus.ACCEPTED_202;
import static org.glassfish.grizzly.http.util.HttpStatus.NOT_FOUND_404;
import static com.xebialabs.restito.semantics.Action.contentType;
import static com.xebialabs.restito.semantics.Action.stringContent;

public class TestRESTComparator {
	private StubServer server;
	private static int PORT = 9092;
	private RESTComparator restComparator;
	private String urlHost = "http://localhost:" + PORT;
	private String endpoint1 = "/users/1";
	private String endpoint2 = "/users/2";
	private String jsonString = 
			"{\"data\":{\"id\":3,\"first_name\":\"Emma\",\"last_name\":\"Wong\",\"avatar\""
			+ ":\"https://s3.amazonaws.com/uifaces/faces/twitter/olegpogodaev/128.jpg\"}}";
	
	
	@BeforeTest
	public void setUp() {
		// Kill the restito server
        if (server != null) {
            server.stop();
        }
       // Initialize and configure a newer instance of the server
        server = new StubServer(PORT).run();
        restComparator = new RESTComparator();
		
	}
	
	@AfterTest
	public void tearDown() {
		// Kill the restito server
        if (server != null) {
            server.stop();
        }
		
	}
	
	@DataProvider(name="nullEmptyFileProvider")
    public Object[][] getNullDataprovider(){
    return new Object[][] 
    	{
            { null, new File("firstFile") },
            { new File("firstFile"), null },
            { new File("nonExistantFile"), new File("firstFile") },
            { new File("firstFile"), new File("nonExistantFile")}
        };

    } 
	
	@Test(dataProvider="nullEmptyFileProvider")
	public void testNullFiles(File firstFile, File secondFile) {
		RESTComparator comparator = new RESTComparator(firstFile, secondFile);
		List<LineIterator> urlLists = comparator.getURLs();
		Assert.assertNull(urlLists);
	}	
	
	/**
	 * Test the compare method when the status codes are different.
	 */
	@Test
	public void testCompareWithDifferentStatusCode() {
		
		whenHttp(server).match(get(endpoint1)).then(status(ACCEPTED_202));
		whenHttp(server).match(get(endpoint2)).then(status(NOT_FOUND_404));
		Boolean result = 
				restComparator.compare(urlHost+endpoint1, urlHost+endpoint2);
		Assert.assertFalse(result);
	}
	
	@Test
	public void testCompareWithSameStatusCode() {
		
		whenHttp(server).match(get(endpoint1)).then(status(ACCEPTED_202));
		whenHttp(server).match(get(endpoint2)).then(status(ACCEPTED_202));
		Boolean result = 
				restComparator.compare(urlHost+endpoint1, urlHost+endpoint2);
		Assert.assertTrue(result);
	}
	
	@Test
	public void testCompareWithSameBody() {
		
		whenHttp(server).match(get(endpoint1)).then(status(ACCEPTED_202),
				stringContent(jsonString), contentType("application/json"));
		whenHttp(server).match(get(endpoint2)).then(status(ACCEPTED_202),
				stringContent(jsonString), contentType("application/json"));
		Boolean result = 
				restComparator.compare(urlHost+endpoint1, urlHost+endpoint2);
		Assert.assertTrue(result);
	}
	
	@Test
	public void testCompareWithDifferentBody() {
		
		whenHttp(server).match(get(endpoint1)).then(status(ACCEPTED_202),
				stringContent(jsonString), contentType("application/json"));
		whenHttp(server).match(get(endpoint2)).then(status(ACCEPTED_202),
				stringContent("testbody"), contentType("application/json"));
		Boolean result = 
				restComparator.compare(urlHost+endpoint1, urlHost+endpoint2);
		Assert.assertFalse(result);
	}
	
	@Test
	public void testCompareWithDifferentHeader() {
		
		whenHttp(server).match(get(endpoint1)).then(status(ACCEPTED_202),
				stringContent(jsonString), contentType("application/json"));
		whenHttp(server).match(get(endpoint2)).then(status(ACCEPTED_202),
				stringContent(jsonString), contentType("application/text"));
		Boolean result = 
				restComparator.compare(urlHost+endpoint1, urlHost+endpoint2);
		Assert.assertFalse(result);
	}
	
	
}
