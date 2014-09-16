package resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class ContactDao {
	static int id = 1;
	public Map<Integer, Contact> map;

	public ContactDao() {
		map = new HashMap<Integer, Contact>();
	}
	
	public Contact find(int id){
		return map.get(id);
	}

	public List<Contact> findAll(){
		return new ArrayList<>();
	}
	
	public void delete(int id){
		map.remove(id);	
	}
	
	public void update(Contact contact){
		map.remove(contact.getId());
		map.put(contact.getId(), contact);
	}
	
	public void save(Contact contact){
		map.put(contact.getId(), contact);
	}
	
	public Contact createContact(){
		Contact contact = new Contact();
		contact.setId(id);
		map.put(id, contact);
		id++;
		return contact;
	}
}
