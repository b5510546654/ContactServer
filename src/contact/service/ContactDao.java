package contact.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import contact.entity.Contact;
/**
 * Has map that contains created contact.
 * Use similar to database.
 * @author Rungroj Maipradit 5510546654
 */
public class ContactDao {
	/** Represent a contact every contact has different id. */
	static int id = 1;
	/** Contains id in key and contact in value.	 */
	private Map<Integer, Contact> map;
	/**
	 * Initial map.
	 */
	public ContactDao() {
		map = new HashMap<Integer, Contact>();
	}
	
	/**
	 * Find contact by id
	 * @param id key match with contact
	 * @return contact that key match with.
	 */
	public Contact find(int id){
		System.out.println(id);
		return map.get(id);
	}

	/**
	 * Return all of contact in map.
	 * @return all of contact in list
	 */
	public List<Contact> findAll(){
		List<Contact> list = new ArrayList<Contact>();
		for (Integer in : map.keySet()) {
			list.add(map.get(in));
		}
		return list;
	}
	
	/**
	 * Return list of contact that title contain str.
	 * @param str substring of title
	 * @return list of contact that title contain str
	 */
	public List<Contact> findByStr(String str){
		List<Contact> list = new ArrayList<Contact>();
		for (Integer in : map.keySet()) {
			if(map.get(in).getTitle().contains(str))
				list.add(map.get(in));
		}
		return list;
	}
	
	/**
	 * Remove contact from map by id. 
	 * @param id key that use for remove
	 */
	public void delete(int id){
		map.remove(id);	
	}
	
	/**
	 * Update contact from id.
	 * @param contact that use for update
	 */
	public void update(Contact contact){
		map.remove(contact.getId());
		map.put(contact.getId(), contact);
	}
	
	/**
	 * Save new contact in map.
	 * @param contact that use to save
	 */
	public void save(Contact contact){
		map.put(contact.getId(), contact);
	}
	
	/**
	 * Create contact with generate id and other value is null.
	 * @return contact that created.
	 */
	public Contact createContact(){
		Contact contact = new Contact();
		contact.setId(id);
		map.put(id, contact);
		id++;
		return contact;
	}
	
	/**
	 * Check map has key or not.
	 * @param id that use to check
	 * @return if has return true if not return false
	 */
	public boolean containID(int id){
		return map.containsKey(id);
	}
	
	/**
	 * Create contact from parameter that given.
	 * @param id of contact if id = 0 then use id from generateID.
	 * @param title of contact.
	 * @param email of contact.
	 * @param name of contact.
	 * @param phoneNumber of contact.
	 * @return contact that created.
	 */
	public Contact createContact(int id, String title, String email, String name,
			int phoneNumber) {
		Contact contact = new Contact();
		contact.setTitle(title);
		contact.setEmail(email);
		contact.setName(name);
		contact.setPhoneNumber(phoneNumber);
		if(id != 0){
		contact.setId(id);
		map.put(id, contact);
		}
		else{
			contact.setId(this.id);
			map.put(this.id++, contact);
		}
		return contact;
	}
}
