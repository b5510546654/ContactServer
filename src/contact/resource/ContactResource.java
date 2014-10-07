package contact.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import contact.entity.Contact;
import contact.entity.ContactList;
import contact.service.ContactDao;
import contact.service.DaoFactory;
/**
 * Use from connect between server and contactDao.
 * Contains method get post put delete and support ETAG.
 * @author Rungroj Maipradit 5510546654
 */
@Path("/contacts")
@Singleton
public class ContactResource {
	/**
	 * This object use for contact with ContactDao.
	 */
	private ContactDao contactDao = DaoFactory.getInstance().getContactDao();
	/** Use for control cache.*/
	private CacheControl cc;
	/**
	 * set maximum size for cache.
	 */
	public ContactResource() {
		cc = new CacheControl();
		cc.setMaxAge(86400);
	}
		/**
	 * Get path parameter from URI.
	 * Then return Contact + Response.
	 * @param id path parameter use for search
	 * @return response and contact type xml
	 */
	@GET
	@Path("{id}")
	public Response get(@PathParam("id") int id,@Context Request req){
		if(!contactDao.containID(id))
			return Response.status(Status.NOT_FOUND).build();
		Response.ResponseBuilder rb = null;
		EntityTag etag = new EntityTag(contactDao.find(id).hashCode()+"");
		rb = req.evaluatePreconditions(etag);
		if(rb!= null){
			return rb.cacheControl(cc).build();
		}
		return Response.ok(contactDao.find(id)).cacheControl(cc).tag(etag).build();
	}

	/**
	 * Get Query parameter from URI if parameter doesn't exist then show all list.
	 * Then return Contact + Response.
	 * @param q parameter use for query
	 * @return response and contact
	 */
	@GET
	public Response getQuery(@QueryParam("title") String q){
		ContactList contactList = new ContactList();
		boolean check = false;
		if(q == null){
			check = true;
			for (Contact contact : contactDao.findAll()) {
				contactList.addContact(contact);
			}			
		}
		else{
			for (Contact contact : contactDao.findByTitle(q)) {
				contactList.addContact(contact);
				check = true;
			}
		}
		if(check)
			return Response.ok().type(MediaType.TEXT_XML).entity(contactList).build();
		else
			return Response.status(Status.NOT_FOUND).build();
	}

	/**
	 * Use for create contact.
	 * Receive from text/xml,application.xml
	 * @param con use for get contact
	 * @return response with new header
	 * @throws URISyntaxException when URI is fail
	 */
	@POST
	@Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
	public Response create (JAXBElement<Contact> con) throws URISyntaxException{
		Contact contact = (Contact)con.getValue();
		if(contactDao.find(contact.getId()) != null){
			return Response.status(Response.Status.CONFLICT).build();
		}
		contactDao.save(contact);
		URI uri = new URI(contact.getId()+"");
		EntityTag etag = new EntityTag(contact.hashCode()+"");
		return Response.created(uri).cacheControl(cc).tag(etag).build();
	}

	/**
	 * Use for update contact if not create return noContent.
	 * Receive from text/xml,application/xml
	 * @param id use for check already create or not
	 * @param con use for get contact
	 * @return response with new header
	 * @throws URISyntaxException when URI is fail
	 */
	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
	public Response update (@PathParam("id") int id,JAXBElement<Contact> con,@Context Request req) throws URISyntaxException{
		if(!contactDao.containID(id))
			return Response.status(Status.BAD_REQUEST).build();
		Response.ResponseBuilder rb = null;
		EntityTag etag = new EntityTag(contactDao.find(id).hashCode()+"");
		rb = req.evaluatePreconditions(etag);
		if(rb!= null){
			return rb.cacheControl(cc).build();
		}
		Contact contact = (Contact)con.getValue();
		contact.setId(id);
		contactDao.update(contact);
		return Response.ok().build();
	}

	/**
	 * delete contact from id
	 * @param id use for delete
	 * @return ok if delete complete not found if fail
	 */
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") int id,@Context Request req){
		if(contactDao.containID(id)){
			Response.ResponseBuilder rb = null;
			EntityTag etag = new EntityTag(contactDao.find(id).hashCode()+"");
			rb = req.evaluatePreconditions(etag);
			if(rb!= null){
				return rb.cacheControl(cc).build();
			}
			contactDao.delete(id);
			return Response.ok().build();
		}
		return Response.status(Status.NOT_FOUND).build();
	}
}
