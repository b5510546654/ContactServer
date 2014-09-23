package test.contact.resource;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLEngineResult.Status;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import org.apache.derby.iapi.types.XML;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.research.ws.wadl.Request;

import contact.JettyMain;
import contact.entity.Contact;
import contact.resource.ContactResource;


public class ContactResourceTest {
	
	private static String url;
	private static HttpClient client;
	
	@BeforeClass
	public static void doFirst() throws Exception{
		url = JettyMain.startServer(8080)+"contacts/";
		client = new HttpClient();
		client.start();
	}

	@AfterClass
	public static void doLast() throws Exception {
		JettyMain.stopServer();
		client.stop();
	}
	
	@Test
	public void getPass() throws InterruptedException, ExecutionException, TimeoutException{
		ContentResponse con = client.GET(url);
		assertEquals(200, con.getStatus());
	}
	
	@Test
	public void getFail() throws InterruptedException, ExecutionException, TimeoutException{
		ContentResponse con = client.GET(url+999999);
		assertEquals(204, con.getStatus());
	}
	
	@Test
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
	public void postFail() throws Exception{
		StringContentProvider content = new StringContentProvider("<contact id=\"1234\">"
				+"<title>pass</title></contact>");
		ContentResponse con = client.newRequest(url).content(content,"application/xml").method(HttpMethod.POST).send();
		assertEquals(409, con.getStatus());
	}
	@Test
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
	public void putFail() throws Exception{
		StringContentProvider content = new StringContentProvider("<contact id=\"12345678\">"
				+"<title>pass</title></contact>");
		ContentResponse con = client.newRequest(url+12345678).content(content,"application/xml").method(HttpMethod.PUT).send();
		assertEquals(400,con.getStatus());
	}
	@Test
	public void deletePass() throws Exception{
		StringContentProvider content = new StringContentProvider("<contact id=\"99999\">"
				+"<title>pass</title></contact>");
		client.newRequest(url).content(content,"application/xml").method(HttpMethod.POST).send();
		ContentResponse con = client.newRequest(url+99999).method(HttpMethod.DELETE).send();
		assertEquals(200, con.getStatus());
	}
	
	@Test
	public void deleteFail() throws Exception{
		ContentResponse con = client.newRequest(url).method(HttpMethod.DELETE).send();
		assertEquals(405,con.getStatus());
	}
	
}
