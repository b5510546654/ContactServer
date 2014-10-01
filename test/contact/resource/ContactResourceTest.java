package contact.resource;


import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import contact.JettyMain;

/**
 * Use for test contactResource .
 * By start jetty and test get,put,delete,post.
 * each test contains 2 part success and fail.
 * @author Rungroj Maipradit 5510546654
 */
public class ContactResourceTest {
	
	/**
	 * URL use for test
	 */
	private static String url;
	/**
	 * Client use for sent request.
	 */
	private static HttpClient client;
	
	@BeforeClass
	/**
	 * Start jetty and set url.
	 * @throws Exception
	 */
	public static void doFirst() throws Exception{
		url = JettyMain.startServer(8080)+"contacts/";
		client = new HttpClient();
		client.start();
	}

	@AfterClass
	/**
	 * After finish test close client and stop server.
	 * @throws Exception
	 */
	public static void doLast() throws Exception {
		JettyMain.stopServer();
		client.stop();
	}
	
	@Test
	/**
	 * Test get if response ok then get work fine.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public void getPass() throws InterruptedException, ExecutionException, TimeoutException{
		ContentResponse con = client.GET(url);
		assertEquals(200, con.getStatus());
	}
	
	@Test
	/**
	 * Try to request not exist id get response 204.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public void getFail() throws InterruptedException, ExecutionException, TimeoutException{
		ContentResponse con = client.GET(url+999999);
		assertEquals(204, con.getStatus());
	}
	
	@Test
	/**
	 * Create new contact and sent to server check if sent success or not and check title.
	 * @throws Exception
	 */
	public void postPass() throws Exception{
		StringContentProvider content = new StringContentProvider("<contact id=\"99999\">"
				+"<title>pass</title></contact>");
		ContentResponse con = client.newRequest(url).content(content,"application/xml").method(HttpMethod.POST).send();
		assertEquals(201, con.getStatus());
		String str = client.GET(url+1234).getContentAsString();
		Pattern titlepat = Pattern.compile(".*(<title>)pass(</title>).*");
		Matcher titlemat = titlepat.matcher(str);
		assertTrue(titlemat.matches());
		client.newRequest(url+99999).method(HttpMethod.DELETE).send();
	}
	
	@Test
	/**
	 * Create contact and sent post to exist id it should return 409.
	 * @throws Exception
	 */
	public void postFail() throws Exception{
		StringContentProvider content = new StringContentProvider("<contact id=\"1234\">"
				+"<title>pass</title></contact>");
		ContentResponse con = client.newRequest(url).content(content,"application/xml").method(HttpMethod.POST).send();
		assertEquals(409, con.getStatus());
	}
	@Test
	/**
	 * Update contact from exist id it should update and return 200.
	 * @throws Exception
	 */
	public void putPass() throws Exception{
		StringContentProvider content = new StringContentProvider("<contact id=\"1234\">"
				+"<title>pass</title></contact>");
		ContentResponse con = client.newRequest(url+1234).content(content,"application/xml").method(HttpMethod.PUT).send();
		String str = client.GET(url+1234).getContentAsString();
		Pattern titlepat = Pattern.compile(".*(<title>)pass(</title>).*");
		Matcher titlemat = titlepat.matcher(str);
		assertTrue(titlemat.matches());
		assertEquals(200,con.getStatus());
	}
	
	@Test
	/**
	 * Update contact from non exist id it shouldn't update and return 400.
	 * @throws Exception
	 */
	public void putFail() throws Exception{
		StringContentProvider content = new StringContentProvider("<contact id=\"12345678\">"
				+"<title>pass</title></contact>");
		ContentResponse con = client.newRequest(url+12345678).content(content,"application/xml").method(HttpMethod.PUT).send();
		assertEquals(400,con.getStatus());
	}
	@Test
	/**
	 * try delete it should return 200.
	 * @throws Exception
	 */
	public void deletePass() throws Exception{
		StringContentProvider content = new StringContentProvider("<contact id=\"99999\">"
				+"<title>pass</title></contact>");
		client.newRequest(url).content(content,"application/xml").method(HttpMethod.POST).send();
		ContentResponse con = client.newRequest(url+99999).method(HttpMethod.DELETE).send();
		assertEquals(200, con.getStatus());
	}
	
	@Test
	/**
	 * try delete but wrong url it should return 405.
	 * @throws Exception
	 */
	public void deleteFail() throws Exception{
		ContentResponse con = client.newRequest(url).method(HttpMethod.DELETE).send();
		assertEquals(405,con.getStatus());
	}
	
}
