package contacy.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import contact.JettyMain;
import contact.entity.Contact;
import contact.service.ContactDao;
import contact.service.DaoFactory;

/**
 * Test ContactResource with ETAG.
 * GET and POST test ETAG header.
 * PUT and DELETE test If-Match , If-None-Match.
 * @author Rungroj Maipradit 5510546654
 *
 */
public class EtagTest {
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
	 * Start JETTY and set URL.
	 * @throws Exception
	 */
	public static void doFirst() throws Exception{
		url = JettyMain.startServer(8080)+"contacts/";
		client = new HttpClient();
		client.start();

		DaoFactory.getInstance().getContactDao().createContact(999, "test in put", null, null, null, 0);
		DaoFactory.getInstance().getContactDao().createContact(888, "test in delete", null, null, null, 0);
	}

	@AfterClass
	/**
	 * After finish test close client and stop server.
	 * @throws Exception
	 */
	public static void doLast() throws Exception {
		DaoFactory.getInstance().getContactDao().delete(999);
		DaoFactory.getInstance().getContactDao().delete(888);
		JettyMain.stopServer();
		client.stop();
	}

	@Test
	/**
	 * Check ETAG from get method.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public void getWithEtag() throws InterruptedException, ExecutionException, TimeoutException{
		Contact contact = DaoFactory.getInstance().getContactDao().find(1234);
		ContentResponse con = client.newRequest(url+1234).method(HttpMethod.GET).send();
		assertEquals("\""+contact.hashCode()+"\"",con.getHeaders().get(HttpHeader.ETAG));
		assertEquals(200, con.getStatus());
	}

	@Test
	/**
	 * Try to get new contact from server but contact doesn't update.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public void getWithEtagNoUpdate() throws InterruptedException, ExecutionException, TimeoutException{
		ContentResponse con = client.newRequest(url+1234).method(HttpMethod.GET).send();
		String etag = con.getHeaders().get(HttpHeader.ETAG);
		con = client.newRequest(url+1234).method(HttpMethod.GET).header(HttpHeader.IF_NONE_MATCH, etag).send();
		assertEquals(304, con.getStatus());
	}

	@Test
	/**
	 * Check ETAG from post method.
	 * @throws Exception
	 */
	public void postWithEtag() throws Exception{
		StringContentProvider content = new StringContentProvider("<contact id=\"99999\">"
				+"<title>pass</title></contact>");
		ContentResponse con = client.newRequest(url).content(content,"application/xml").method(HttpMethod.POST).send();
		assertEquals(201, con.getStatus());
		String str = client.GET(url+1234).getContentAsString();
		Pattern titlepat = Pattern.compile(".*(<title>)pass(</title>).*");
		Matcher titlemat = titlepat.matcher(str);
		assertTrue(titlemat.matches());
		Contact contact = DaoFactory.getInstance().getContactDao().find(99999);
		assertEquals("\""+contact.hashCode()+"\"", con.getHeaders().get(HttpHeader.ETAG));
		client.newRequest(url+99999).method(HttpMethod.DELETE).send();
	}


	@Test
	/**
	 * Update contact but get from old ETAG.
	 * @throws Exception
	 */
	public void putWithEtag() throws Exception{
		ContentResponse con = client.newRequest(url+999).method(HttpMethod.GET).send();
		String etag = con.getHeaders().get(HttpHeader.ETAG);

		StringContentProvider content = new StringContentProvider("<contact id=\"999\">"
				+"<title>update</title></contact>");
		con = client.newRequest(url+999).content(content,"application/xml").method(HttpMethod.PUT).send();
		String str = client.GET(url+999).getContentAsString();
		Pattern titlepat = Pattern.compile(".*(<title>)update(</title>).*");
		Matcher titlemat = titlepat.matcher(str);
		assertTrue(titlemat.matches());

		con = client.newRequest(url+999).header(HttpHeader.IF_MATCH, etag).method(HttpMethod.GET).send();
		assertEquals(412,con.getStatus());
	}
	@Test
	/**
	 * Try to delete from ETAG but contact update.
	 * @throws Exception
	 */
	public void deleteWithEtag() throws Exception{
		ContentResponse con = client.newRequest(url+888).method(HttpMethod.GET).send();
		String etag = con.getHeaders().get(HttpHeader.ETAG);
		
		StringContentProvider content = new StringContentProvider("<contact id=\"888\">"
				+"<title>UPDATE</title></contact>");
		client.newRequest(url+888).content(content,"application/xml").method(HttpMethod.PUT).send();
		
		con = client.newRequest(url+888).header(HttpHeader.IF_MATCH, etag).method(HttpMethod.DELETE).send();
		assertEquals(412, con.getStatus());
	}
}
