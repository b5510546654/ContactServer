package resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;


@Path("/Test")
public class ContactResource {
	@Context
	UriInfo uriInfo;
	
	ContactDao contactDao = new ContactDao();
	public ContactResource() {
		
	}
	@GET
	@Path("contacts")
	public Response get(){
		System.out.println(contactDao.findAll().toString());
		return Response.ok().build();
	}
	
	@GET
	@Path("contacts/{id}")
	@Consumes(MediaType.APPLICATION_XML)
	public Response get(@PathParam("id") int id){
		System.out.println(contactDao.find(id).toString());
		return Response.ok().build();
	}
	
	@POST
	@Path("contacts")
	public Response create (){
		System.out.println(contact.getValue().getId());
//		Contact contact = contactDao.createContact();
//		System.out.println("CREATE "+contact.getId());
		return Response.created(null).build();
	}
	
	@PUT
	@Path("contacts/{id}")
	public String update (@PathParam("id") int id){
		Contact contact = contactDao.find(id);
		contactDao.update(contact);
		return "update";
	}
	
	@DELETE
	@Path("contacts/{id}")
	public void delete(@PathParam("id") int id){
		contactDao.delete(id);
	}
}
