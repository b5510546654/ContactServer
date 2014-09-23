package contact.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import com.sun.research.ws.wadl.Application;

import contact.entity.Contact;
import contact.entity.ContactList;
import contact.service.ContactDao;
import contact.service.DaoFactory;
import contact.service.mem.MemContactDao;

/**
 * Use from connect between server and contactDao.
 * Contains method get post put delete.
 * @author Rungroj Maipradit 5510546654
 */
@Path("/contacts")
@Singleton
public class ContactResource {
	/**
	 * This object use for contact with ContactDao.
	 */
	private ContactDao contactDao ;
	
	/**
	 * Receive contactDao via dependency injection for contract with contactDao.
	 * @param contactDao use for contract with contactDao
	 */
	public ContactResource(ContactDao contactDao) {
		this.contactDao = contactDao;
	}
	/**
	 * Get path parameter from URI.
	 * Then return Contact + Response.
	 * @param id path parameter use for search
	 * @return response and contact type xml
	 */
	@GET
	@Path("{id}")
	public Response get(@PathParam("id") int id){
		if(!contactDao.containID(id))
			return Response.status(204).build();
		return Response.ok().type(MediaType.TEXT_XML).entity(contactDao.find(id)).build();
	}

	/**
	 * Get Query parameter from URI if parameter doesn't exist then show all list.
	 * Then return Contact + Response.
	 * @param q parameter use for query
	 * @return response and contact
	 */
	@GET
	public Response getQuery(@QueryParam("title") String q){
		System.out.println("title:"+q);
		ContactList contactList = new ContactList();
		boolean check = false;
		if(q == null){
			check = true;
			for (Contact contact : contactDao.findAll()) {
				contactList.addContact(contact);
			}			
		}
		else{
			for (Contact contact : contactDao.findByStr(q)) {
				contactList.addContact(contact);
				check = true;
			}
		}
		if(check)
			return Response.ok().type(MediaType.TEXT_XML).entity(contactList).build();
		else
			return Response.status(404).build();
	}

	/**
	 * Use for create contact.
	 * Receive content type application/x-www-form-urlencoded.
	 * @param id id of contact if not set it will generate by default
	 * @param title title of contact
	 * @param email email of contact
	 * @param name name of contact
	 * @param phoneNumber phoneNumber of contact
	 * @param photoURL URL of photo in this contact
	 * @return response with set id in header
	 * @throws URISyntaxException when URI is fail
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public Response create (@FormParam("id") @DefaultValue("0") int id,
			@FormParam("title") String title ,
			@FormParam("email") String email,
			@FormParam("name") String name,
			@FormParam("photoURL") String photoURL,
			@FormParam("phoneNumber") int phoneNumber) throws URISyntaxException{
		Contact contact = contactDao.createContact(id,title,email,name,photoURL,phoneNumber);
		URI uri = new URI(contact.getId()+"");
		return Response.created(uri).build();
	}

	/**
	 * Use for create contact.
	 * Receive from text/xml
	 * @param con use for get contact
	 * @return response with new header
	 * @throws URISyntaxException when URI is fail
	 */
	@POST
	@Consumes({"application/xml",MediaType.TEXT_XML})
	public Response create (JAXBElement<Contact> con) throws URISyntaxException{
		Contact contact = (Contact)con.getValue();
		System.out.println("ID"+contact.getId()+"   "+contactDao.find(contact.getId()));
		if(contactDao.find(contact.getId()) != null){
			return Response.status(Response.Status.CONFLICT).build();
		}
		contact = contactDao.createContact(contact.getId(),contact.getTitle(),contact.getEmail(),contact.getName(),contact.getPhotoUrl(),contact.getPhoneNumber());
		URI uri = new URI(contact.getId()+"");
		return Response.created(uri).build();
	}
	
	/**
	 * Use for update contact if not create return noContent.
	 * @param id use for check already create or not
	 * @param title title of contact
	 * @param email email of contact
	 * @param name name of contact
	 * @param phoneNumber phoneNumber of contact
	 * @param photoURL URL of photo in this contact
	 * @return response with set id in header
	 * @throws URISyntaxException when URI is fail
	 */
	@PUT
	@Path("{id}")
	@Consumes("application/x-www-form-urlencoded")
	public Response update (@PathParam("id") int id,
			@FormParam("title") String title ,
			@FormParam("email") String email,
			@FormParam("name") String name,
			@FormParam("photoURL") String photoURL,
			@FormParam("phoneNumber") int phoneNumber) throws URISyntaxException{
		if(contactDao.containID(id))
			return Response.status(404).build();
		Contact contact = new Contact();
		contact.setEmail(email);
		contact.setName(name);
		contact.setPhoneNumber(phoneNumber);
		contact.setPhotoUrl(photoURL);
		contact.setId(id);
		contactDao.update(contact);
		return Response.ok(new URI(id+"")).build();
	}

	/**
	 * Use for update contact if not create return noContent.
	 * Receive from text/xml
	 * @param id use for check already create or not
	 * @param con use for get contact
	 * @return response with new header
	 * @throws URISyntaxException when URI is fail
	 */
	@PUT
	@Path("{id}")
	@Consumes({"application/xml",MediaType.TEXT_XML})
	public Response update (@PathParam("id") int id,JAXBElement<Contact> con) throws URISyntaxException{
		if(!contactDao.containID(id))
			return Response.status(400).build();
		Contact contact = (Contact)con.getValue();
		contact.setId(id);
		contactDao.update(contact);
		return Response.ok().build();
	}

	/**
	 * delete contact from id
	 * @param id use for delete
	 * @return ok if delete complete noContent if fail
	 */
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") int id){
		contactDao.delete(id);
		return Response.ok().build();
	}
}
